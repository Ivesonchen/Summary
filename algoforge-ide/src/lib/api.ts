import type {
  ChatMessage,
  FileResponse,
  GitHubConfig,
  Language,
  ProblemResponse,
  RunResult,
  SyncResult,
  TreeResponse,
} from '../types';

// API origin. Empty in dev (Vite proxies /api to the local server); set to the
// Container Apps URL at build time via VITE_API_BASE for the deployed frontend.
const API_BASE = (import.meta.env.VITE_API_BASE ?? '').replace(/\/$/, '');

export async function fetchTree(): Promise<TreeResponse> {
  const res = await fetch(`${API_BASE}/api/tree`);
  if (!res.ok) throw new Error(`Failed to load tree (${res.status})`);
  return res.json();
}

/** Fetch a problem's metadata (e.g. group) from its algorithm folder path. */
export async function fetchProblem(path: string): Promise<ProblemResponse> {
  const res = await fetch(`${API_BASE}/api/problem?path=${encodeURIComponent(path)}`);
  if (!res.ok) throw new Error(`Failed to load problem (${res.status})`);
  return res.json();
}

/** Create a new problem folder (starter solution.<ext> + meta.json). */
export async function createProblem(input: {
  parentPath: string;
  name: string;
  group?: number | null;
  ext: string;
}): Promise<{ problemPath: string; solutionPath: string }> {
  const res = await fetch(`${API_BASE}/api/problem`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(input),
  });
  const data = await res.json().catch(() => ({}));
  if (!res.ok) throw new Error(data.error || `Failed to create problem (${res.status})`);
  return data;
}

/** Add a solution.<ext> (new language) to an existing problem. */
export async function createSolution(input: {
  problemPath: string;
  ext: string;
}): Promise<{ solutionPath: string }> {
  const res = await fetch(`${API_BASE}/api/solution`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(input),
  });
  const data = await res.json().catch(() => ({}));
  if (!res.ok) throw new Error(data.error || `Failed to create solution (${res.status})`);
  return data;
}

/** Overwrite an existing solution file with edited content (left pane save). */
export async function saveSolution(input: {
  path: string;
  content: string;
}): Promise<{ path: string }> {
  const res = await fetch(`${API_BASE}/api/solution`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(input),
  });
  const data = await res.json().catch(() => ({}));
  if (!res.ok) throw new Error(data.error || `Failed to save solution (${res.status})`);
  return data;
}

/** Save playground code as a new named-variant solution (right pane save). */
export async function saveVariantSolution(input: {
  problemPath: string;
  ext: string;
  variant: string;
  content: string;
}): Promise<{ solutionPath: string; variant: string }> {
  const res = await fetch(`${API_BASE}/api/solution/variant`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(input),
  });
  const data = await res.json().catch(() => ({}));
  if (!res.ok) throw new Error(data.error || `Failed to save solution (${res.status})`);
  return data;
}

export async function fetchFile(path: string): Promise<FileResponse> {
  const res = await fetch(`${API_BASE}/api/file?path=${encodeURIComponent(path)}`);
  if (!res.ok) {
    const body = await res.json().catch(() => ({}));
    throw new Error(body.error || `Failed to load file (${res.status})`);
  }
  return res.json();
}

/** Execute source in the server-side sandbox (Piston). */
export async function runCode(source: string, language: Language, stdin: string): Promise<RunResult> {
  const res = await fetch(`${API_BASE}/api/run`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ source, language, stdin }),
  });
  const data = await res.json().catch(() => ({}));
  if (!res.ok) {
    return {
      ok: false,
      statusText: 'Request failed',
      stdout: '',
      stderr: '',
      compileOutput: '',
      message: '',
      timeMs: null,
      memoryKb: null,
      error: data.error || `Run failed (${res.status})`,
    };
  }
  return data as RunResult;
}

/** Fetch the current GitHub sync config (token is never returned, only hasToken). */
export async function fetchGitHubConfig(): Promise<GitHubConfig> {
  const res = await fetch(`${API_BASE}/api/github/config`);
  if (!res.ok) throw new Error(`Failed to load GitHub config (${res.status})`);
  return res.json();
}

/** Update GitHub sync config. Only include token when the user provides one. */
export async function saveGitHubConfig(input: {
  repo?: string;
  branch?: string;
  syncBranch?: string;
  token?: string;
}): Promise<GitHubConfig> {
  const res = await fetch(`${API_BASE}/api/github/config`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(input),
  });
  const data = await res.json().catch(() => ({}));
  if (!res.ok) throw new Error(data.error || `Failed to save config (${res.status})`);
  return data;
}

/** Commit store files that are missing from the repo (Blob -> git). */
export async function syncToRepo(): Promise<SyncResult> {
  const res = await fetch(`${API_BASE}/api/sync`, { method: 'POST' });
  const data = await res.json().catch(() => ({}));
  if (!res.ok) throw new Error(data.error || `Sync failed (${res.status})`);
  return data as SyncResult;
}

/** Send the conversation to the AI assistant (GitHub Models, proxied server-side). */
export async function sendChat(messages: ChatMessage[]): Promise<{ content: string; model: string }> {
  const res = await fetch(`${API_BASE}/api/chat`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ messages }),
  });
  const data = await res.json().catch(() => ({}));
  if (!res.ok) throw new Error(data.error || `AI request failed (${res.status})`);
  return data as { content: string; model: string };
}

export interface AIConfig {
  provider: 'copilot' | 'github-models';
  model: string;
  available: boolean;
  authenticated: boolean;
  username?: string;
  hasToken: boolean;
}

/** Fetch AI provider/config + auth state. */
export async function fetchAIConfig(): Promise<AIConfig> {
  const res = await fetch(`${API_BASE}/api/ai/config`);
  if (!res.ok) throw new Error(`Failed to load AI config (${res.status})`);
  return res.json();
}

/** Start GitHub Copilot device-flow sign-in. */
export async function startCopilotLogin(): Promise<{
  userCode?: string;
  verificationUri?: string;
  alreadySignedIn?: boolean;
}> {
  const res = await fetch(`${API_BASE}/api/ai/login`, { method: 'POST' });
  const data = await res.json().catch(() => ({}));
  if (!res.ok) throw new Error(data.error || `Sign-in failed (${res.status})`);
  return data;
}

/** Poll Copilot auth status. */
export async function fetchCopilotStatus(): Promise<{ authenticated: boolean; username?: string }> {
  const res = await fetch(`${API_BASE}/api/ai/status`);
  if (!res.ok) throw new Error(`Failed to load status (${res.status})`);
  return res.json();
}

/** Sign out of GitHub Copilot. */
export async function copilotLogout(): Promise<void> {
  await fetch(`${API_BASE}/api/ai/logout`, { method: 'POST' });
}

/** Fetch the persisted study set (cross-device). Returns [] on any failure. */
export async function fetchStudy<T = unknown>(): Promise<T[]> {
  try {
    const res = await fetch(`${API_BASE}/api/study`);
    if (!res.ok) return [];
    const data = await res.json();
    return Array.isArray(data) ? (data as T[]) : [];
  } catch {
    return [];
  }
}

/** Persist the study set (overwrites the stored document). */
export async function saveStudy<T = unknown>(entries: T[]): Promise<void> {
  await fetch(`${API_BASE}/api/study`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(entries),
  });
}

/** Map a file extension to the language executed by the sandbox. */
export function extToLanguage(ext: string): Language {
  switch (ext) {
    case 'java':
      return 'java';
    case 'py':
      return 'python';
    case 'js':
    case 'jsx':
      return 'javascript';
    case 'ts':
    case 'tsx':
      return 'typescript';
    case 'cpp':
      return 'cpp';
    case 'c':
      return 'c';
    case 'go':
      return 'go';
    default:
      return 'unknown';
  }
}

/** Map an extension to a Monaco language id (broader than runnable languages). */
export function extToMonaco(ext: string): string {
  switch (ext) {
    case 'js':
    case 'jsx':
      return 'javascript';
    case 'ts':
    case 'tsx':
      return 'typescript';
    case 'py':
      return 'python';
    case 'java':
      return 'java';
    case 'cpp':
    case 'c':
      return 'cpp';
    case 'go':
      return 'go';
    default:
      return 'plaintext';
  }
}
