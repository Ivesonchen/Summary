export interface FileNode {
  type: 'file';
  name: string;
  display: string;
  path: string;
  ext: string;
  difficulty: number | null;
}

export interface FolderNode {
  type: 'folder';
  name: string;
  path: string;
  children: TreeNode[];
}

/** One solution file of a problem (`solution.<ext>` or `solution.<variant>.<ext>`). */
export interface ProblemLanguage {
  ext: string;
  path: string;
  variant?: string | null;
  name?: string;
}

/** An algorithm folder collapsed into a single problem with language variants. */
export interface ProblemNode {
  type: 'problem';
  name: string;
  path: string;
  languages: ProblemLanguage[];
  group?: number | null;
}

export type TreeNode = FolderNode | FileNode | ProblemNode;

/** A top-level explorer section (Categories, Companies, Design, Random, …). */
export interface Section {
  name: string;
  children: TreeNode[];
}

export interface TreeResponse {
  sections: Section[];
  root: string;
}

export interface FileResponse {
  path: string;
  content: string;
  ext: string;
}

export interface ProblemResponse {
  path: string;
  meta: { group?: number };
}

export interface GitHubConfig {
  repo: string;
  branch: string;
  syncBranch: string;
  hasToken: boolean;
}

export interface SyncResult {
  committed: string[];
  count: number;
  prNumber?: number;
  prUrl?: string;
  branch?: string;
  message: string;
}

export type Language = 'java' | 'python' | 'javascript' | 'typescript' | 'cpp' | 'c' | 'go' | 'unknown';

/** Normalized result returned by the /api/run endpoint (Judge0-backed). */
export interface RunResult {
  ok: boolean;
  /** Human-readable Judge0 status, e.g. "Accepted", "Runtime Error (NZEC)". */
  statusText: string;
  stdout: string;
  stderr: string;
  compileOutput: string;
  message: string;
  /** Execution time in ms (null if unavailable). */
  timeMs: number | null;
  /** Peak memory in KB (null if unavailable). */
  memoryKb: number | null;
  /** Best-effort error string when the run did not complete cleanly. */
  error: string;
}

export interface TestOutcome {
  status: 'pass' | 'fail' | 'error' | 'skipped';
  message: string;
  expected?: string;
  actual?: string;
}

/** One turn in the AI assistant conversation. */
export interface ChatMessage {
  role: 'system' | 'user' | 'assistant';
  content: string;
}