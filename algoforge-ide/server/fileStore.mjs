// File source abstraction: serves the algorithm file tree and file contents from
// either the local disk (dev) or Azure Blob Storage (cloud), chosen via FILE_SOURCE.
//
//   FILE_SOURCE=disk  (default) -> reads from LOCAL_REPO_ROOT / the repo checkout
//   FILE_SOURCE=blob            -> reads from an Azure Blob container
//
// Both backends expose the same interface: getTree() and getFile(relPath).

import fs from 'node:fs';
import path from 'node:path';
import { fileURLToPath } from 'node:url';

const __dirname = path.dirname(fileURLToPath(import.meta.url));

// File extensions we consider "algorithm source" files.
export const SOURCE_EXT = new Set(['.java', '.py', '.ts', '.js', '.tsx', '.jsx', '.cpp', '.c', '.go']);

// Folders never surfaced in the explorer (disk walk only).
const IGNORED = new Set([
  'node_modules',
  '.git',
  'venv',
  '.venv',
  'dist',
  '__pycache__',
  'algoforge-ide', // don't list the IDE itself
]);

// Top-level sections shown in the explorer, in display order. Each section is a
// top-level folder in the repo (Categories, Companies, Design, Random). Sections
// that currently hold no source files (e.g. an empty Random) are still displayed.
const SECTIONS = (process.env.SECTIONS || 'Categories,Companies,Design,Random')
  .split(',')
  .map((s) => s.trim())
  .filter(Boolean);

/** Error carrying an HTTP status for the route layer to surface. */
export class FileError extends Error {
  constructor(message, status) {
    super(message);
    this.status = status;
  }
}

// Allowed solution extensions and minimal starter content per language.
const EXT_STARTERS = {
  java: 'import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        // your solution here\n    }\n}\n',
  py: 'import sys\n\ndef main():\n    data = sys.stdin.read().split()\n    # your solution here\n\nmain()\n',
  js: "const input = require('fs').readFileSync(0, 'utf8').trim();\n// your solution here\n",
  ts: "const input: string = require('fs').readFileSync(0, 'utf8').trim();\n// your solution here\n",
  cpp: '#include <bits/stdc++.h>\nusing namespace std;\n\nint main() {\n    // your solution here\n    return 0;\n}\n',
  c: '#include <stdio.h>\n\nint main() {\n    // your solution here\n    return 0;\n}\n',
  go: 'package main\n\nimport (\n    "bufio"\n    "fmt"\n    "os"\n)\n\nfunc main() {\n    reader := bufio.NewReader(os.Stdin)\n    _ = reader\n    _ = fmt.Sprint\n}\n',
};

/** Validate a single path segment (folder or problem name). */
function safeSegment(name) {
  const s = String(name || '').trim();
  if (!s || s === '.' || s === '..') return null;
  if (/[\\/]/.test(s) || s.startsWith('.')) return null;
  if (s.length > 120) return null;
  return s;
}

/** Validate that a relative folder path stays within the allowed section tree. */
function safeParentPath(parentPath) {
  const rel = String(parentPath || '').replace(/\\/g, '/').replace(/\/+$/,'').trim();
  if (!rel) return null;
  const segs = rel.split('/');
  if (segs.some((s) => !safeSegment(s))) return null;
  return segs.join('/');
}

/** Parse a "[3] Foo.java" prefix -> { difficulty: 3, display: "Foo.java" }. */
function parseName(fileName) {
  const m = fileName.match(/^\[(\d+)\]\s*(.+)$/);
  if (m) return { difficulty: Number(m[1]), display: m[2] };
  return { difficulty: null, display: fileName };
}

const byName = (a, b) => a.name.localeCompare(b.name, undefined, { numeric: true });

/** Convert a Map of tree nodes into sorted (folders first) arrays. */
function toNodes(map) {
  const dirs = [];
  const files = [];
  for (const node of map.values()) {
    if (node.type === 'folder') {
      dirs.push({ type: 'folder', name: node.name, path: node.path, children: toNodes(node.children) });
    } else {
      files.push(node);
    }
  }
  dirs.sort(byName);
  files.sort(byName);
  return [...dirs, ...files];
}

/** Build a nested folder/file tree from a flat list of POSIX relative paths. */
function buildTreeFromPaths(paths) {
  const root = new Map();
  for (const rel of paths) {
    const parts = rel.split('/');
    let level = root;
    for (let i = 0; i < parts.length; i++) {
      const part = parts[i];
      const isFile = i === parts.length - 1;
      if (isFile) {
        const ext = path.extname(part).toLowerCase();
        const { difficulty, display } = parseName(part);
        level.set(part, {
          type: 'file',
          name: part,
          display,
          path: rel,
          ext: ext.slice(1),
          difficulty,
        });
      } else {
        if (!level.has(part)) {
          level.set(part, {
            type: 'folder',
            name: part,
            path: parts.slice(0, i + 1).join('/'),
            children: new Map(),
          });
        }
        level = level.get(part).children;
      }
    }
  }
  return collapseProblems(toNodes(root));
}

/**
 * Collapse each "problem folder" (a folder directly containing one or more
 * `solution.<ext>` files) into a single `problem` node carrying the available
 * languages. This makes each algorithm a single explorer entry with a language
 * switcher instead of a folder of loose files.
 */
function collapseProblems(nodes) {
  const out = [];
  for (const node of nodes) {
    if (node.type !== 'folder') {
      out.push(node);
      continue;
    }
    const solutionFiles = node.children.filter(
      (c) => c.type === 'file' && path.basename(c.name, path.extname(c.name)).toLowerCase() === 'solution'
    );
    if (solutionFiles.length > 0) {
      const languages = solutionFiles
        .map((f) => ({ ext: f.ext, path: f.path }))
        .sort((a, b) => a.ext.localeCompare(b.ext));
      out.push({ type: 'problem', name: node.name, path: node.path, languages });
    } else {
      out.push({ ...node, children: collapseProblems(node.children) });
    }
  }
  return out;
}


/** Base class: shares tree assembly; subclasses provide the raw data. */
class FileStore {
  constructor() {
    this._treeCache = null;
    this._treeCacheAt = 0;
    this._treeTtlMs = 30000;
  }

  async listSourcePaths() {
    throw new Error('not implemented');
  }
  async getFile() {
    throw new Error('not implemented');
  }
  rootName() {
    return 'Algorithms';
  }

  /** Invalidate the cached tree (call after any write). */
  invalidateTree() {
    this._treeCache = null;
  }

  // --- writes (implemented by subclasses) ---
  async writeFile() {
    throw new Error('not implemented');
  }
  async exists() {
    throw new Error('not implemented');
  }

  /**
   * Create a new problem folder under `parentPath` with a starter solution and
   * meta.json. Returns { problemPath, solutionPath }.
   */
  async createProblem({ parentPath, name, group, ext }) {
    const parent = safeParentPath(parentPath);
    if (!parent) throw new FileError('Invalid parent folder', 400);
    if (!SECTIONS.includes(parent.split('/')[0])) throw new FileError('Parent must be within a known section', 400);
    const problemName = safeSegment(name);
    if (!problemName) throw new FileError('Invalid problem name', 400);
    if (!EXT_STARTERS[ext]) throw new FileError(`Unsupported language: ${ext}`, 400);

    const problemPath = `${parent}/${problemName}`;
    const solutionPath = `${problemPath}/solution.${ext}`;
    if (await this.exists(solutionPath)) throw new FileError('A problem with that name already exists', 409);

    await this.writeFile(solutionPath, EXT_STARTERS[ext]);
    const meta = Number.isFinite(Number(group)) ? { group: Number(group) } : {};
    await this.writeFile(`${problemPath}/meta.json`, JSON.stringify(meta, null, 2) + '\n');
    this.invalidateTree();
    return { problemPath, solutionPath };
  }

  /** Add a `solution.<ext>` to an existing problem folder. Returns { solutionPath }. */
  async createSolution({ problemPath, ext }) {
    const prob = safeParentPath(problemPath);
    if (!prob) throw new FileError('Invalid problem path', 400);
    if (!SECTIONS.includes(prob.split('/')[0])) throw new FileError('Path must be within a known section', 400);
    if (!EXT_STARTERS[ext]) throw new FileError(`Unsupported language: ${ext}`, 400);

    const solutionPath = `${prob}/solution.${ext}`;
    if (await this.exists(solutionPath)) throw new FileError('That language already exists for this problem', 409);
    await this.writeFile(solutionPath, EXT_STARTERS[ext]);
    this.invalidateTree();
    return { solutionPath };
  }

  /** Read the numeric `group` for each problem folder path (batched). */
  async getGroupsForProblems(problemPaths) {
    const groups = new Map();
    const batchSize = 25;
    for (let i = 0; i < problemPaths.length; i += batchSize) {
      const batch = problemPaths.slice(i, i + batchSize);
      const metas = await Promise.all(batch.map((p) => this.getMeta(p).catch(() => ({}))));
      batch.forEach((p, idx) => {
        const g = metas[idx] ? metas[idx].group : undefined;
        groups.set(p, typeof g === 'number' ? g : null);
      });
    }
    return groups;
  }

  async getTree() {
    if (this._treeCache && Date.now() - this._treeCacheAt < this._treeTtlMs) {
      return this._treeCache;
    }

    const paths = await this.listSourcePaths();
    const top = buildTreeFromPaths(paths);

    // Attach the group from meta.json to each problem, then sort by (group, name).
    const problemPaths = [];
    collectProblemPaths(top, problemPaths);
    const groups = await this.getGroupsForProblems(problemPaths);
    finalizeOrder(top, groups);

    // Index the top-level section folders that actually contain source files.
    const byName = new Map();
    for (const node of top) {
      if (node.type === 'folder') byName.set(node.name, node);
    }

    const sections = [];
    const seen = new Set();
    // Emit the known sections first, in configured order (empty ones included).
    for (const name of SECTIONS) {
      const node = byName.get(name);
      sections.push({ name, children: node ? node.children : [] });
      seen.add(name);
    }
    // Append any other top-level folders not covered by the known sections.
    for (const node of top) {
      if (node.type === 'folder' && !seen.has(node.name)) {
        sections.push({ name: node.name, children: node.children });
      }
    }

    const result = { sections, root: this.rootName() };
    this._treeCache = result;
    this._treeCacheAt = Date.now();
    return result;
  }
}

/** Collect the paths of all problem nodes in a (possibly nested) node list. */
function collectProblemPaths(nodes, out) {
  for (const node of nodes) {
    if (node.type === 'problem') out.push(node.path);
    else if (node.type === 'folder') collectProblemPaths(node.children, out);
  }
}

/** Attach group to problems and sort each sibling list by (folders first, group, name). */
function finalizeOrder(nodes, groups) {
  for (const node of nodes) {
    if (node.type === 'problem') {
      node.group = groups.get(node.path) ?? null;
    } else if (node.type === 'folder') {
      finalizeOrder(node.children, groups);
    }
  }
  nodes.sort(compareTreeNodes);
  return nodes;
}

function compareTreeNodes(a, b) {
  const rank = (n) => (n.type === 'folder' ? 0 : 1); // folders before problems/files
  if (rank(a) !== rank(b)) return rank(a) - rank(b);
  if (a.type === 'problem' && b.type === 'problem') {
    const ga = a.group == null ? Infinity : a.group;
    const gb = b.group == null ? Infinity : b.group;
    if (ga !== gb) return ga - gb;
  }
  return a.name.localeCompare(b.name, undefined, { numeric: true });
}

/** Reads the algorithm files from the local filesystem. */
class DiskFileStore extends FileStore {
  constructor(root) {
    super();
    this.root = root;
  }

  async listSourcePaths() {
    const out = [];
    const walk = (absDir, relDir) => {
      let entries;
      try {
        entries = fs.readdirSync(absDir, { withFileTypes: true });
      } catch {
        return;
      }
      for (const entry of entries) {
        if (entry.name.startsWith('.')) continue;
        if (IGNORED.has(entry.name)) continue;
        const rel = relDir ? `${relDir}/${entry.name}` : entry.name;
        if (entry.isDirectory()) {
          walk(path.join(absDir, entry.name), rel);
        } else if (entry.isFile()) {
          const ext = path.extname(entry.name).toLowerCase();
          if (SOURCE_EXT.has(ext)) out.push(rel);
        }
      }
    };
    walk(this.root, '');
    return out;
  }

  async getFile(relPath) {
    // Resolve and ensure the result stays inside root (path-traversal guard).
    const absPath = path.resolve(this.root, relPath);
    const rootWithSep = this.root.endsWith(path.sep) ? this.root : this.root + path.sep;
    if (absPath !== this.root && !absPath.startsWith(rootWithSep)) {
      throw new FileError('Invalid path', 400);
    }
    let stat;
    try {
      stat = fs.statSync(absPath);
    } catch {
      throw new FileError('Not found', 404);
    }
    if (!stat.isFile()) throw new FileError('Not a file', 400);
    const ext = path.extname(absPath).toLowerCase();
    if (!SOURCE_EXT.has(ext)) throw new FileError('Unsupported file type', 400);
    try {
      const content = fs.readFileSync(absPath, 'utf8');
      return { path: relPath, content, ext: ext.slice(1) };
    } catch {
      throw new FileError('Failed to read file', 500);
    }
  }

  async getMeta(folderPath) {
    const absPath = path.resolve(this.root, folderPath, 'meta.json');
    const rootWithSep = this.root.endsWith(path.sep) ? this.root : this.root + path.sep;
    if (!absPath.startsWith(rootWithSep)) throw new FileError('Invalid path', 400);
    try {
      return JSON.parse(fs.readFileSync(absPath, 'utf8'));
    } catch {
      return {}; // missing/invalid meta -> no metadata
    }
  }

  _resolveInside(relPath) {
    const absPath = path.resolve(this.root, relPath);
    const rootWithSep = this.root.endsWith(path.sep) ? this.root : this.root + path.sep;
    if (!absPath.startsWith(rootWithSep)) throw new FileError('Invalid path', 400);
    return absPath;
  }

  async exists(relPath) {
    return fs.existsSync(this._resolveInside(relPath));
  }

  async writeFile(relPath, content) {
    const absPath = this._resolveInside(relPath);
    fs.mkdirSync(path.dirname(absPath), { recursive: true });
    fs.writeFileSync(absPath, content, 'utf8');
  }

  rootName() {
    return path.basename(this.root);
  }
}

/** Reads the algorithm files from an Azure Blob Storage container. */
class BlobFileStore extends FileStore {
  constructor(containerClient, rootName) {
    super();
    this.container = containerClient;
    this._rootName = rootName;
  }

  async listSourcePaths() {
    const out = [];
    for await (const blob of this.container.listBlobsFlat()) {
      const name = blob.name; // stored as a POSIX relative path
      const ext = path.extname(name).toLowerCase();
      if (!SOURCE_EXT.has(ext)) continue;
      const top = name.split('/')[0];
      if (IGNORED.has(top)) continue;
      out.push(name);
    }
    return out;
  }

  async getFile(relPath) {
    const ext = path.extname(relPath).toLowerCase();
    if (!SOURCE_EXT.has(ext)) throw new FileError('Unsupported file type', 400);
    const client = this.container.getBlobClient(relPath);
    try {
      const buffer = await client.downloadToBuffer();
      return { path: relPath, content: buffer.toString('utf8'), ext: ext.slice(1) };
    } catch (err) {
      if (err.statusCode === 404) throw new FileError('Not found', 404);
      throw new FileError('Failed to read file', 500);
    }
  }

  async getMeta(folderPath) {
    const blobName = `${folderPath.replace(/\/$/, '')}/meta.json`;
    try {
      const buffer = await this.container.getBlobClient(blobName).downloadToBuffer();
      return JSON.parse(buffer.toString('utf8'));
    } catch {
      return {}; // missing/invalid meta -> no metadata
    }
  }

  async exists(relPath) {
    try {
      return await this.container.getBlobClient(relPath).exists();
    } catch {
      return false;
    }
  }

  async writeFile(relPath, content) {
    const ext = path.extname(relPath).toLowerCase();
    const contentType = ext === '.json' ? 'application/json' : 'text/plain';
    const client = this.container.getBlockBlobClient(relPath);
    const data = Buffer.from(content, 'utf8');
    await client.uploadData(data, { blobHTTPHeaders: { blobContentType: contentType } });
  }

  rootName() {
    return this._rootName;
  }
}

/** Build the configured Azure Blob container client (connection string or managed identity). */
async function createBlobContainerClient() {
  const { BlobServiceClient } = await import('@azure/storage-blob');
  const container = process.env.AZURE_STORAGE_CONTAINER || 'algorithms';
  const connectionString = process.env.AZURE_STORAGE_CONNECTION_STRING;
  const accountUrl = process.env.AZURE_STORAGE_ACCOUNT_URL;

  let service;
  if (connectionString) {
    service = BlobServiceClient.fromConnectionString(connectionString);
  } else if (accountUrl) {
    // Passwordless auth via managed identity / az login (recommended for Azure).
    const { DefaultAzureCredential } = await import('@azure/identity');
    service = new BlobServiceClient(accountUrl, new DefaultAzureCredential());
  } else {
    throw new Error(
      'FILE_SOURCE=blob requires AZURE_STORAGE_CONNECTION_STRING or AZURE_STORAGE_ACCOUNT_URL'
    );
  }
  return service.getContainerClient(container);
}

/** Factory: returns the configured file store (disk by default). */
export async function createFileStore() {
  const source = (process.env.FILE_SOURCE || 'disk').toLowerCase();
  if (source === 'blob') {
    const containerClient = await createBlobContainerClient();
    const rootName = process.env.FILE_ROOT_NAME || 'Algorithms';
    return new BlobFileStore(containerClient, rootName);
  }
  // Disk: default to the repo checkout (two levels up from server/), overridable.
  const root = process.env.LOCAL_REPO_ROOT || path.resolve(__dirname, '..', '..');
  return new DiskFileStore(root);
}
