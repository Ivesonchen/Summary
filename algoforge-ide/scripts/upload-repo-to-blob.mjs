import 'dotenv/config';
import fs from 'node:fs';
import path from 'node:path';
import { fileURLToPath } from 'node:url';
import { BlobServiceClient } from '@azure/storage-blob';
import { DefaultAzureCredential } from '@azure/identity';

// Uploads the repository's algorithm source files to an Azure Blob container,
// preserving their relative paths as blob names (POSIX "/"). Run once (and again
// whenever files change) to populate the store the API reads in FILE_SOURCE=blob mode.
//
// Usage:
//   # auth via connection string:
//   AZURE_STORAGE_CONNECTION_STRING=... node scripts/upload-repo-to-blob.mjs
//   # or passwordless (az login / managed identity):
//   AZURE_STORAGE_ACCOUNT_URL=https://<acct>.blob.core.windows.net node scripts/upload-repo-to-blob.mjs
//
// Config (env): AZURE_STORAGE_CONTAINER (default "algorithms"),
//               LOCAL_REPO_ROOT (default: repo checkout two levels up).

const __dirname = path.dirname(fileURLToPath(import.meta.url));
const REPO_ROOT = process.env.LOCAL_REPO_ROOT || path.resolve(__dirname, '..', '..');
const CONTAINER = process.env.AZURE_STORAGE_CONTAINER || 'algorithms';

const SOURCE_EXT = new Set(['.java', '.py', '.ts', '.js', '.tsx', '.jsx', '.cpp', '.c', '.go']);
const IGNORED = new Set(['node_modules', '.git', 'venv', '.venv', 'dist', '__pycache__', 'algoforge-ide']);

const CONTENT_TYPES = {
  '.java': 'text/x-java-source',
  '.py': 'text/x-python',
  '.ts': 'text/plain',
  '.tsx': 'text/plain',
  '.js': 'text/javascript',
  '.jsx': 'text/plain',
  '.cpp': 'text/x-c',
  '.c': 'text/x-c',
  '.go': 'text/plain',
  '.json': 'application/json',
};

// Extra non-source files to include (e.g. per-problem metadata).
const EXTRA_FILES = new Set(['meta.json']);

function isIncluded(name) {
  return SOURCE_EXT.has(path.extname(name).toLowerCase()) || EXTRA_FILES.has(name);
}

function collect(absDir, relDir, out) {
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
      collect(path.join(absDir, entry.name), rel, out);
    } else if (entry.isFile() && isIncluded(entry.name)) {
      out.push(rel);
    }
  }
}

function getServiceClient() {
  const cs = process.env.AZURE_STORAGE_CONNECTION_STRING;
  const url = process.env.AZURE_STORAGE_ACCOUNT_URL;
  if (cs) return BlobServiceClient.fromConnectionString(cs);
  if (url) return new BlobServiceClient(url, new DefaultAzureCredential());
  throw new Error('Set AZURE_STORAGE_CONNECTION_STRING or AZURE_STORAGE_ACCOUNT_URL');
}

async function main() {
  const files = [];
  collect(REPO_ROOT, '', files);
  console.log(`Found ${files.length} source files under ${REPO_ROOT}`);

  const container = getServiceClient().getContainerClient(CONTAINER);
  await container.createIfNotExists();

  const localSet = new Set(files);

  let uploaded = 0;
  for (const rel of files) {
    const absPath = path.join(REPO_ROOT, rel);
    const content = fs.readFileSync(absPath);
    const ext = path.extname(rel).toLowerCase();
    const blob = container.getBlockBlobClient(rel);
    await blob.uploadData(content, {
      blobHTTPHeaders: { blobContentType: CONTENT_TYPES[ext] || 'text/plain' },
    });
    uploaded++;
    if (uploaded % 50 === 0) console.log(`  uploaded ${uploaded}/${files.length}…`);
  }
  console.log(`Uploaded ${uploaded} files.`);

  // Sync: delete blobs that no longer exist locally (handles renames/removals,
  // e.g. the flat "Array/[4] 2 Sum.java" after restructuring to "Array/2 Sum/solution.java").
  let deleted = 0;
  for await (const blob of container.listBlobsFlat()) {
    if (!localSet.has(blob.name)) {
      await container.deleteBlob(blob.name);
      deleted++;
    }
  }
  console.log(`Deleted ${deleted} stale blob(s).`);
  console.log(`Done. Container "${CONTAINER}" now mirrors ${files.length} local files.`);
}

main().catch((err) => {
  console.error(err.message);
  process.exit(1);
});
