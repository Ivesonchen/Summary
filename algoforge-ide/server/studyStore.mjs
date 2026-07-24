// Cross-device persistence for the Study / spaced-repetition set.
//
// Stores a single JSON document (the array of study entries) in the same
// backing store as the algorithm files: an Azure Blob when FILE_SOURCE=blob,
// or a local file on disk otherwise. Because this app runs as a single account,
// one shared document is sufficient and syncs across the user's devices.
//
// The blob name (`.study/progress.json`) is deliberately outside the source
// extensions and section folders, so it is never surfaced in the file tree nor
// picked up by the GitHub sync.

import fs from 'node:fs';
import os from 'node:os';
import path from 'node:path';

const BLOB_NAME = '.study/progress.json';

/** Disk-backed study store (local dev). */
class DiskStudyStore {
  constructor(filePath) {
    this.filePath = filePath;
  }

  async get() {
    try {
      const raw = fs.readFileSync(this.filePath, 'utf8');
      const parsed = JSON.parse(raw);
      return Array.isArray(parsed) ? parsed : [];
    } catch {
      return [];
    }
  }

  async set(entries) {
    fs.mkdirSync(path.dirname(this.filePath), { recursive: true });
    fs.writeFileSync(this.filePath, JSON.stringify(entries), 'utf8');
    return entries;
  }
}

/** Blob-backed study store (cloud). */
class BlobStudyStore {
  constructor(containerClient) {
    this.container = containerClient;
  }

  async get() {
    try {
      const buffer = await this.container.getBlobClient(BLOB_NAME).downloadToBuffer();
      const parsed = JSON.parse(buffer.toString('utf8'));
      return Array.isArray(parsed) ? parsed : [];
    } catch (err) {
      if (err.statusCode === 404) return [];
      // Treat unexpected read errors as empty rather than failing the request.
      return [];
    }
  }

  async set(entries) {
    const client = this.container.getBlockBlobClient(BLOB_NAME);
    const data = Buffer.from(JSON.stringify(entries), 'utf8');
    await client.uploadData(data, {
      blobHTTPHeaders: { blobContentType: 'application/json' },
    });
    return entries;
  }
}

async function createBlobContainerClient() {
  const { BlobServiceClient } = await import('@azure/storage-blob');
  const container = process.env.AZURE_STORAGE_CONTAINER || 'algorithms';
  const connectionString = process.env.AZURE_STORAGE_CONNECTION_STRING;
  const accountUrl = process.env.AZURE_STORAGE_ACCOUNT_URL;
  let service;
  if (connectionString) {
    service = BlobServiceClient.fromConnectionString(connectionString);
  } else if (accountUrl) {
    const { DefaultAzureCredential } = await import('@azure/identity');
    service = new BlobServiceClient(accountUrl, new DefaultAzureCredential());
  } else {
    throw new Error('FILE_SOURCE=blob requires AZURE_STORAGE_CONNECTION_STRING or AZURE_STORAGE_ACCOUNT_URL');
  }
  return service.getContainerClient(container);
}

/** Factory: returns the configured study store (matches FILE_SOURCE). */
export async function createStudyStore() {
  const source = (process.env.FILE_SOURCE || 'disk').toLowerCase();
  if (source === 'blob') {
    const containerClient = await createBlobContainerClient();
    return new BlobStudyStore(containerClient);
  }
  // Disk: a JSON file under STUDY_DATA_DIR (defaults to the OS temp dir so it
  // never lands in the repo/git).
  const dir = process.env.STUDY_DATA_DIR || path.join(os.tmpdir(), 'algo-playground');
  return new DiskStudyStore(path.join(dir, 'study.json'));
}
