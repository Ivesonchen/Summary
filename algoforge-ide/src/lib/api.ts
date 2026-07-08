import type { FileResponse, Language, ProblemResponse, RunResult, TreeResponse } from '../types';

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
