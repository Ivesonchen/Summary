// Thin client for a self-hosted Piston instance (https://github.com/engineer-man/piston).
//
// Why Piston instead of Judge0: Piston isolates via Linux namespaces + prlimit and
// runs cleanly on cgroup v2, so it works on modern kernels (Docker Desktop/WSL2, and
// current Azure Linux VMs) without the cgroup-v1 requirement that Judge0's isolate has.
//
// Local dev: point PISTON_URL at the docker-compose stack (http://localhost:2000).
// Azure: point it at the private Piston host (reachable only from the API subnet).

const PISTON_URL = (process.env.PISTON_URL || 'http://localhost:2000').replace(/\/$/, '');

// Per-run guardrails.
const COMPILE_TIMEOUT_MS = Number(process.env.RUN_COMPILE_TIMEOUT_MS || 10000);
const RUN_TIMEOUT_MS = Number(process.env.RUN_WALL_TIME_LIMIT || 10) * 1000;
// Piston run_memory_limit is in BYTES; -1 = unlimited. Default unlimited to avoid
// JVM/compiler OOM surprises on small caps. Override with RUN_MEMORY_LIMIT_BYTES.
const RUN_MEMORY_LIMIT_BYTES = Number(process.env.RUN_MEMORY_LIMIT_BYTES || -1);

// Map our language keys -> Piston language name + the source file name Piston should use.
// File names matter for compiled languages (e.g. Java expects Main.java for `public class Main`).
const LANGUAGES = {
  java: { piston: 'java', file: 'Main.java' },
  python: { piston: 'python', file: 'main.py' },
  javascript: { piston: 'javascript', file: 'main.js' },
  typescript: { piston: 'typescript', file: 'main.ts' },
  cpp: { piston: 'c++', file: 'main.cpp' },
  c: { piston: 'c', file: 'main.c' },
  go: { piston: 'go', file: 'main.go' },
};

export function isSupportedLanguage(key) {
  return Object.prototype.hasOwnProperty.call(LANGUAGES, key);
}

// Cache of piston language name -> installed version, resolved from /runtimes.
let runtimeCache = null;

async function resolveVersion(pistonName) {
  // Allow explicit override, e.g. PISTON_VER_JAVA=15.0.2
  const key = Object.keys(LANGUAGES).find((k) => LANGUAGES[k].piston === pistonName);
  const override = key ? process.env[`PISTON_VER_${key.toUpperCase()}`] : undefined;
  if (override) return override;

  if (!runtimeCache) {
    const res = await fetch(`${PISTON_URL}/api/v2/runtimes`);
    if (!res.ok) throw new Error(`Piston /runtimes returned ${res.status}`);
    const runtimes = await res.json();
    runtimeCache = new Map();
    for (const rt of runtimes) {
      const names = [rt.language, ...(rt.aliases || [])];
      for (const n of names) {
        // Keep the first (Piston lists newest first for a given language install).
        if (!runtimeCache.has(n)) runtimeCache.set(n, rt.version);
      }
    }
  }
  return runtimeCache.get(pistonName);
}

/** Reset the cached runtimes (call if you install new languages while running). */
export function clearRuntimeCache() {
  runtimeCache = null;
}

/**
 * Execute source in the Piston sandbox and return a normalized result.
 *
 * @param {object} opts
 * @param {string} opts.source      Full program source.
 * @param {string} opts.languageKey One of the LANGUAGES keys.
 * @param {string} [opts.stdin]     Standard input fed to the program.
 */
export async function runInSandbox({ source, languageKey, stdin = '' }) {
  const lang = LANGUAGES[languageKey];
  if (!lang) {
    return {
      ok: false,
      statusText: 'Unsupported language',
      stdout: '',
      stderr: '',
      compileOutput: '',
      message: '',
      timeMs: null,
      memoryKb: null,
      error: `No Piston mapping for "${languageKey}"`,
    };
  }

  let version;
  try {
    version = await resolveVersion(lang.piston);
  } catch (err) {
    return unreachable(err);
  }
  if (!version) {
    return {
      ok: false,
      statusText: 'Runtime not installed',
      stdout: '',
      stderr: '',
      compileOutput: '',
      message: '',
      timeMs: null,
      memoryKb: null,
      error: `Piston has no runtime installed for "${lang.piston}". Install it with the Piston CLI/API.`,
    };
  }

  let data;
  try {
    const res = await fetch(`${PISTON_URL}/api/v2/execute`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        language: lang.piston,
        version,
        files: [{ name: lang.file, content: source }],
        stdin,
        compile_timeout: COMPILE_TIMEOUT_MS,
        run_timeout: RUN_TIMEOUT_MS,
        run_memory_limit: RUN_MEMORY_LIMIT_BYTES,
      }),
    });
    if (!res.ok) {
      const body = await res.text().catch(() => '');
      return {
        ok: false,
        statusText: 'Execution rejected',
        stdout: '',
        stderr: '',
        compileOutput: '',
        message: '',
        timeMs: null,
        memoryKb: null,
        error: `Piston ${res.status}: ${body.slice(0, 300)}`,
      };
    }
    data = await res.json();
  } catch (err) {
    return unreachable(err);
  }

  const compile = data.compile || {};
  const run = data.run || {};
  const compileOutput = (compile.stderr || compile.output || '').trim();
  const compileFailed = compile.code != null && compile.code !== 0;
  const runStderr = run.stderr || '';
  const signal = run.signal || null;
  const exitCode = run.code;

  let statusText;
  let ok = false;
  if (compileFailed) {
    statusText = 'Compilation Error';
  } else if (signal) {
    // SIGKILL usually means the run hit the time or memory limit.
    statusText = signal === 'SIGKILL' ? 'Time/Memory Limit Exceeded' : `Runtime Error (${signal})`;
  } else if (exitCode !== 0) {
    statusText = 'Runtime Error';
  } else {
    statusText = 'Accepted';
    ok = true;
  }

  return {
    ok,
    statusText,
    stdout: run.stdout || '',
    stderr: runStderr,
    compileOutput,
    message: signal ? `Terminated by ${signal}` : '',
    // Piston does not report wall time / peak memory in the default response.
    timeMs: null,
    memoryKb: null,
    error: ok ? '' : compileOutput || runStderr || statusText,
  };
}

function unreachable(err) {
  return {
    ok: false,
    statusText: 'Sandbox unreachable',
    stdout: '',
    stderr: '',
    compileOutput: '',
    message: '',
    timeMs: null,
    memoryKb: null,
    error: `Could not reach Piston at ${PISTON_URL}. Is the sandbox running? (${err.message})`,
  };
}
