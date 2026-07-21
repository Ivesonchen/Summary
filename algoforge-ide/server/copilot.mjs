// GitHub Copilot provider — drives the OFFICIAL GitHub Copilot CLI + SDK
// (@github/copilot-sdk) server-side, the same supported tooling the desktop
// clients use. This binds to a SINGLE GitHub account: an operator signs in once
// via device flow, and the app then serves chat completions from that account's
// Copilot entitlement.
//
// Security / scope:
//   - Chat-only: every tool/permission request from the agent is REJECTED, so
//     the model cannot run shell commands or touch the server filesystem.
//   - The OAuth token is managed by the Copilot CLI itself (OS keychain +
//     ~/.copilot); this process never handles the raw token.
//   - Because it is one shared seat, this is intended for a personal/self-hosted
//     deployment, not a public multi-tenant service.
//
// Adapted from the Jarvis project's electron/auth-manager.ts and
// backend/copilot-client-factory.ts.

import { spawn } from 'node:child_process';
import { createRequire } from 'node:module';
import os from 'node:os';
import path from 'node:path';

import { FileError } from './fileStore.mjs';

const require = createRequire(import.meta.url);

// Lazily import the SDK so the rest of the server still boots if it is absent.
let sdk = null;
async function loadSdk() {
  if (sdk) return sdk;
  try {
    sdk = await import('@github/copilot-sdk');
    return sdk;
  } catch (err) {
    throw new FileError(
      `Copilot SDK not available: ${err.message}. Run "npm install @github/copilot-sdk".`,
      500
    );
  }
}

// Resolve the bundled platform-specific Copilot CLI binary.
function packageNameFor(platform, arch) {
  if (platform === 'darwin' && arch === 'arm64') return '@github/copilot-darwin-arm64';
  if (platform === 'darwin' && arch === 'x64') return '@github/copilot-darwin-x64';
  if (platform === 'win32' && arch === 'x64') return '@github/copilot-win32-x64';
  if (platform === 'win32' && arch === 'arm64') return '@github/copilot-win32-arm64';
  if (platform === 'linux' && arch === 'x64') return '@github/copilot-linux-x64';
  if (platform === 'linux' && arch === 'arm64') return '@github/copilot-linux-arm64';
  throw new FileError(`Unsupported platform for Copilot CLI: ${platform}-${arch}`, 500);
}

let cliPathCache = null;
function getCliPath() {
  if (cliPathCache) return cliPathCache;
  const pkg = packageNameFor(process.platform, process.arch);
  // The package's exports field points directly at the binary.
  try {
    cliPathCache = require.resolve(pkg);
    return cliPathCache;
  } catch {
    /* fall through */
  }
  // Fallback: resolve through the SDK's own layout (pnpm/npm hoisting differences).
  try {
    const sdkEntry = require.resolve('@github/copilot-sdk/package.json');
    const sdkRequire = createRequire(sdkEntry);
    cliPathCache = sdkRequire.resolve(pkg);
    return cliPathCache;
  } catch (err) {
    throw new FileError(`Copilot CLI binary not found (${pkg}): ${err.message}`, 500);
  }
}

// A sanitized env for spawning the CLI (mirrors Jarvis: strip tokens/options
// that would leak into or confuse the CLI).
function cleanEnv() {
  const env = { ...process.env };
  delete env.ELECTRON_RUN_AS_NODE;
  delete env.NODE_OPTIONS;
  delete env.GH_TOKEN;
  delete env.GITHUB_TOKEN;
  delete env.GITHUB_MODELS_TOKEN;
  env.GIT_TERMINAL_PROMPT = '0';
  return env;
}

// The working directory the agent sees. We deny all tools anyway, but keep it
// off the repo to be safe.
const WORK_DIR = os.tmpdir();

// Device-flow login timeouts.
const DEVICE_CODE_TIMEOUT_MS = 30_000;
const PROCESS_LIFETIME_MS = 5 * 60_000;

let client = null;
let clientInit = null;
let loginProc = null;

async function createClient() {
  const { CopilotClient, RuntimeConnection } = await loadSdk();
  const cliPath = getCliPath();
  const c = new CopilotClient({
    logLevel: 'error',
    workingDirectory: WORK_DIR,
    env: cleanEnv(),
    connection: RuntimeConnection.forTcp({ port: 0, path: cliPath }),
  });
  await c.start();
  return c;
}

async function ensureClient() {
  if (client) return client;
  if (!clientInit) {
    clientInit = (async () => {
      client = await createClient();
    })();
  }
  try {
    await clientInit;
  } finally {
    clientInit = null;
  }
  if (!client) throw new FileError('Copilot client failed to start', 500);
  return client;
}

async function resetClient() {
  const c = client;
  client = null;
  clientInit = null;
  if (c && typeof c.stop === 'function') {
    try {
      await c.stop();
    } catch {
      /* ignore */
    }
  }
}

/** Whether the SDK + CLI are present (so the UI can show/hide the provider). */
export function isCopilotAvailable() {
  try {
    getCliPath();
    return true;
  } catch {
    return false;
  }
}

/** Current auth status: { authenticated, username? }. Never throws. */
export async function getCopilotStatus() {
  try {
    const c = await ensureClient();
    const status = await c.getAuthStatus();
    if (status?.isAuthenticated) return { authenticated: true, username: status.login };
    return { authenticated: false };
  } catch (err) {
    return { authenticated: false, error: err.message };
  }
}

/**
 * Start GitHub device-flow sign-in. Resolves once the CLI prints a device code
 * (the subprocess keeps running while the operator authorizes in the browser).
 * Returns { userCode, verificationUri } — or { alreadySignedIn: true } if the
 * CLI reports an existing session.
 */
export function startCopilotLogin() {
  cancelLogin();
  const cliPath = getCliPath();
  return new Promise((resolve, reject) => {
    const proc = spawn(cliPath, ['login'], { stdio: ['pipe', 'pipe', 'pipe'], windowsHide: true, env: cleanEnv() });
    loginProc = proc;

    let output = '';
    let resolved = false;
    let timer = setTimeout(() => fail(`No device code within ${DEVICE_CODE_TIMEOUT_MS / 1000}s`), DEVICE_CODE_TIMEOUT_MS);

    function fail(reason) {
      try {
        proc.kill();
      } catch {
        /* gone */
      }
      if (loginProc === proc) loginProc = null;
      if (timer) clearTimeout(timer);
      if (!resolved) reject(new FileError(`Copilot login: ${reason}`, 502));
    }

    function onData(buf) {
      const text = buf.toString();
      output += text;
      if (resolved) return;
      const codeMatch = output.match(/code[:\s]+([A-Z0-9]{4}-[A-Z0-9]{4})/i);
      const uriMatch = output.match(/(https:\/\/github\.com\/login\/device)/i);
      if (codeMatch) {
        resolved = true;
        if (timer) clearTimeout(timer);
        timer = setTimeout(() => {
          try {
            proc.kill();
          } catch {
            /* gone */
          }
        }, PROCESS_LIFETIME_MS);
        resolve({
          userCode: codeMatch[1],
          verificationUri: uriMatch?.[1] ?? 'https://github.com/login/device',
        });
      }
    }

    proc.stdout?.on('data', onData);
    proc.stderr?.on('data', onData);
    proc.on('error', (err) => fail(err.message));
    proc.on('close', async (code) => {
      if (timer) clearTimeout(timer);
      if (loginProc === proc) loginProc = null;
      // Reset the SDK client so it re-reads the new credentials.
      await resetClient();
      if (!resolved) {
        if (code === 0) {
          resolved = true;
          resolve({ alreadySignedIn: true });
        } else {
          fail(`login exited ${code}: ${output.slice(0, 300)}`);
        }
      }
    });
  });
}

function cancelLogin() {
  if (loginProc) {
    try {
      loginProc.kill();
    } catch {
      /* gone */
    }
    loginProc = null;
  }
}

/** Sign out: clear the CLI session and reset the client. */
export async function copilotLogout() {
  cancelLogin();
  const cliPath = getCliPath();
  await new Promise((resolve) => {
    const proc = spawn(cliPath, ['logout'], { stdio: 'ignore', windowsHide: true, env: cleanEnv() });
    proc.on('close', () => resolve());
    proc.on('error', () => resolve());
  });
  await resetClient();
}

// Reject every tool/permission request → the model can only chat.
function denyAllPermissions() {
  return async () => ({ kind: 'reject', feedback: 'Tool use is disabled in this app; answer directly.' });
}

const CHAT_TIMEOUT_MS = Number(process.env.COPILOT_CHAT_TIMEOUT_MS || 120000);

/**
 * Send a conversation to Copilot and return the assistant reply.
 * `messages` is [{ role, content }]. Creates a fresh, tool-less session, sends a
 * single flattened prompt, waits for the assistant message, then disposes it.
 */
export async function copilotChat(messages) {
  const c = await ensureClient();
  const status = await c.getAuthStatus();
  if (!status?.isAuthenticated) {
    throw new FileError('Not signed in to GitHub Copilot. Complete sign-in first.', 401);
  }

  // Flatten the messages into one prompt (system context + prior turns + latest).
  const system = messages.filter((m) => m.role === 'system').map((m) => m.content).join('\n\n');
  const convo = messages.filter((m) => m.role !== 'system');
  const promptParts = [];
  if (system) promptParts.push(system);
  for (const m of convo) {
    promptParts.push(`${m.role === 'assistant' ? 'Assistant' : 'User'}: ${m.content}`);
  }
  promptParts.push('Assistant:');
  const prompt = promptParts.join('\n\n');

  const preferredModel = process.env.COPILOT_MODEL || '';

  const session = await c.createSession({
    workingDirectory: WORK_DIR,
    streaming: true,
    onPermissionRequest: denyAllPermissions(),
    ...(preferredModel ? { model: preferredModel } : {}),
  });

  try {
    const content = await new Promise((resolve, reject) => {
      let settled = false;
      const finish = (fn, val) => {
        if (settled) return;
        settled = true;
        clearTimeout(timeout);
        fn(val);
      };
      const timeout = setTimeout(() => finish(reject, new FileError('Copilot response timed out', 504)), CHAT_TIMEOUT_MS);

      session.on('assistant.message', (e) => finish(resolve, e.data.content));
      session.on('session.error', (e) =>
        finish(reject, new FileError(`Copilot error: ${e?.data?.message || 'unknown'}`, 502))
      );

      Promise.resolve(session.send(prompt)).catch((err) =>
        finish(reject, new FileError(`Copilot send failed: ${err.message}`, 502))
      );
    });

    return { content: String(content ?? ''), model: preferredModel || 'copilot' };
  } finally {
    try {
      await c.deleteSession(session.sessionId);
    } catch {
      /* best effort */
    }
  }
}

/** List models available to the signed-in Copilot account. */
export async function listCopilotModels() {
  const c = await ensureClient();
  const models = await c.listModels();
  return models.map((m) => ({ id: m.id, name: m.name }));
}
