import 'dotenv/config';
import express from 'express';
import fs from 'node:fs';
import path from 'node:path';
import { fileURLToPath } from 'node:url';
import { createFileStore, FileError } from './fileStore.mjs';
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
    res.setHeader('Access-Control-Allow-Methods', 'GET,POST,OPTIONS');
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
