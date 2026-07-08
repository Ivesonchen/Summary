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
};

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
    } else if (entry.isFile() && SOURCE_EXT.has(path.extname(entry.name).toLowerCase())) {
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
  console.log(`Done. Uploaded ${uploaded} files to container "${CONTAINER}".`);
}

main().catch((err) => {
  console.error(err.message);
  process.exit(1);
});
