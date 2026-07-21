import 'dotenv/config';
import express from 'express';
import fs from 'node:fs';
import path from 'node:path';
import { fileURLToPath } from 'node:url';
import { createFileStore, FileError } from './fileStore.mjs';
import { getGitHubConfig, setGitHubConfig, syncToGitHub } from './github.mjs';
import { isSupportedLanguage, runInSandbox } from './piston.mjs';

const __dirname = path.dirname(fileURLToPath(import.meta.url));

const PORT = process.env.PORT || 3001;

// Cap submitted source size to protect the sandbox (64 KB is generous for a solution).
const MAX_SOURCE_BYTES = 64 * 1024;
const MAX_STDIN_BYTES = 64 * 1024;

const app = express();
app.use(express.json({ limit: '256kb' }));

// CORS: the frontend (Static Web App) is a different origin from this API
// (Container Apps), so browsers require CORS headers. Configure allowed origins
// via ALLOWED_ORIGINS (comma-separated). Use "*" to allow any origin.
const ALLOWED_ORIGINS = (process.env.ALLOWED_ORIGINS || '')
  .split(',')
  .map((s) => s.trim())
  .filter(Boolean);

app.use((req, res, next) => {
  const origin = req.headers.origin;
  const allowAny = ALLOWED_ORIGINS.includes('*');
  if (origin && (allowAny || ALLOWED_ORIGINS.includes(origin))) {
    res.setHeader('Access-Control-Allow-Origin', allowAny ? '*' : origin);
    res.setHeader('Vary', 'Origin');
    res.setHeader('Access-Control-Allow-Methods', 'GET,POST,PUT,DELETE,OPTIONS');
    res.setHeader('Access-Control-Allow-Headers', 'Content-Type');
    res.setHeader('Access-Control-Max-Age', '600');
  }
  if (req.method === 'OPTIONS') return res.sendStatus(204);
  next();
});

// The file store is disk-backed locally and Blob-backed in the cloud (FILE_SOURCE).
const store = await createFileStore();

// GET /api/tree -> { categories, companies, root }
app.get('/api/tree', async (_req, res) => {
  try {
    res.json(await store.getTree());
  } catch (err) {
    console.error('tree error:', err);
    res.status(500).json({ error: 'Failed to load file tree' });
  }
});

// GET /api/file?path=Array/[4] 2 Sum.java -> { path, content, ext }
app.get('/api/file', async (req, res) => {
  const relPath = String(req.query.path || '');
  if (!relPath) return res.status(400).json({ error: 'Missing path' });
  try {
    res.json(await store.getFile(relPath));
  } catch (err) {
    if (err instanceof FileError) return res.status(err.status).json({ error: err.message });
    console.error('file error:', err);
    res.status(500).json({ error: 'Failed to read file' });
  }
});

// GET /api/problem?path=<algorithm folder> -> { path, meta } (meta.json contents, e.g. { group }).
app.get('/api/problem', async (req, res) => {
  const relPath = String(req.query.path || '');
  if (!relPath) return res.status(400).json({ error: 'Missing path' });
  try {
    const meta = await store.getMeta(relPath);
    res.json({ path: relPath, meta });
  } catch (err) {
    if (err instanceof FileError) return res.status(err.status).json({ error: err.message });
    console.error('problem error:', err);
    res.status(500).json({ error: 'Failed to read problem metadata' });
  }
});

// POST /api/problem { parentPath, name, group?, ext } -> create a new problem folder.
app.post('/api/problem', async (req, res) => {
  const { parentPath, name, group, ext } = req.body || {};
  try {
    const result = await store.createProblem({ parentPath, name, group, ext });
    res.status(201).json(result);
  } catch (err) {
    if (err instanceof FileError) return res.status(err.status).json({ error: err.message });
    console.error('create problem error:', err);
    res.status(500).json({ error: 'Failed to create problem' });
  }
});

// POST /api/solution { problemPath, ext } -> add a language to an existing problem.
app.post('/api/solution', async (req, res) => {
  const { problemPath, ext } = req.body || {};
  try {
    const result = await store.createSolution({ problemPath, ext });
    res.status(201).json(result);
  } catch (err) {
    if (err instanceof FileError) return res.status(err.status).json({ error: err.message });
    console.error('create solution error:', err);
    res.status(500).json({ error: 'Failed to create solution' });
  }
});

// PUT /api/solution { path, content } -> overwrite an existing solution file (editable left pane).
app.put('/api/solution', async (req, res) => {
  const { path: filePath, content } = req.body || {};
  try {
    const result = await store.saveSolution({ path: filePath, content });
    res.json(result);
  } catch (err) {
    if (err instanceof FileError) return res.status(err.status).json({ error: err.message });
    console.error('save solution error:', err);
    res.status(500).json({ error: 'Failed to save solution' });
  }
});

// POST /api/solution/variant { problemPath, ext, variant, content } -> save playground as a named variant.
app.post('/api/solution/variant', async (req, res) => {
  const { problemPath, ext, variant, content } = req.body || {};
  try {
    const result = await store.saveVariantSolution({ problemPath, ext, variant, content });
    res.status(201).json(result);
  } catch (err) {
    if (err instanceof FileError) return res.status(err.status).json({ error: err.message });
    console.error('save variant error:', err);
    res.status(500).json({ error: 'Failed to save solution variant' });
  }
});

// GET /api/github/config -> { repo, branch, syncBranch, hasToken } (never returns the token).
app.get('/api/github/config', (_req, res) => {
  res.json(getGitHubConfig());
});

// POST /api/github/config { repo?, branch?, syncBranch?, token? } -> update GitHub sync config.
app.post('/api/github/config', (req, res) => {
  const { repo, branch, syncBranch, token } = req.body || {};
  res.json(setGitHubConfig({ repo, branch, syncBranch, token }));
});

// POST /api/sync -> commit store files missing from the repo, as one commit.
app.post('/api/sync', async (_req, res) => {
  try {
    const result = await syncToGitHub(store);
    res.json(result);
  } catch (err) {
    if (err instanceof FileError) return res.status(err.status).json({ error: err.message });
    console.error('sync error:', err);
    res.status(502).json({ error: 'Sync failed' });
  }
});

// POST /api/run { source, language, stdin } -> executes in the Piston sandbox.
app.post('/api/run', async (req, res) => {
  const { source, language, stdin } = req.body || {};

  if (typeof source !== 'string' || !source.trim()) {
    return res.status(400).json({ error: 'Missing source code' });
  }
  if (typeof language !== 'string' || !isSupportedLanguage(language)) {
    return res.status(400).json({ error: `Unsupported or missing language: ${language}` });
  }
  if (Buffer.byteLength(source, 'utf8') > MAX_SOURCE_BYTES) {
    return res.status(413).json({ error: 'Source code too large' });
  }
  if (stdin != null && Buffer.byteLength(String(stdin), 'utf8') > MAX_STDIN_BYTES) {
    return res.status(413).json({ error: 'Stdin too large' });
  }

  try {
    const result = await runInSandbox({
      source,
      languageKey: language,
      stdin: stdin != null ? String(stdin) : '',
    });
    res.json(result);
  } catch (err) {
    res.status(502).json({ ok: false, statusText: 'Execution error', error: err.message });
  }
});

// Health probe for container orchestrators (Azure Container Apps ingress).
app.get('/healthz', (_req, res) => res.json({ ok: true }));

// Optionally serve the built frontend so a single container can host the whole app.
// Harmless in dev (Vite serves the UI on :5173); used for single-image deployments.
const distDir = path.resolve(__dirname, '..', 'dist');
if (fs.existsSync(distDir)) {
  app.use(express.static(distDir));
  app.get('*', (req, res, next) => {
    if (req.path.startsWith('/api/') || req.path === '/healthz') return next();
    res.sendFile(path.join(distDir, 'index.html'));
  });
}

app.listen(PORT, () => {
  console.log(`AlgoForge API listening on http://localhost:${PORT}`);
  console.log(`File source: ${process.env.FILE_SOURCE || 'disk'} · root: ${store.rootName()}`);
});
