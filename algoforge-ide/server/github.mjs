// GitHub sync: commits Blob-created algorithm files back into the repository via
// the GitHub Git Data API, so problems/solutions created in the deployed app
// become versioned in git (and flow through CI/CD).
//
// Security: the token is held server-side only. It is NEVER returned to the
// browser (config responses expose only `hasToken`). Defaults come from env
// (GITHUB_REPO / GITHUB_BRANCH / GITHUB_TOKEN); the web UI can override them at
// runtime (stored in memory for the process lifetime).

import { FileError } from './fileStore.mjs';

const API = 'https://api.github.com';

const config = {
  repo: process.env.GITHUB_REPO || '', // "owner/name"
  branch: process.env.GITHUB_BRANCH || 'master',
  token: process.env.GITHUB_TOKEN || '',
};

export function getGitHubConfig() {
  return { repo: config.repo, branch: config.branch, hasToken: Boolean(config.token) };
}

export function setGitHubConfig({ repo, branch, token }) {
  if (repo != null) config.repo = String(repo).trim().replace(/^https?:\/\/github\.com\//, '').replace(/\.git$/, '');
  if (branch != null && String(branch).trim()) config.branch = String(branch).trim();
  if (token) config.token = String(token); // only overwrite when a value is provided
  return getGitHubConfig();
}

function parseRepo() {
  const [owner, name] = config.repo.split('/');
  return { owner: (owner || '').trim(), name: (name || '').trim() };
}

async function gh(pathname, method = 'GET', body) {
  const res = await fetch(`${API}${pathname}`, {
    method,
    headers: {
      Accept: 'application/vnd.github+json',
      'User-Agent': 'algoforge-ide',
      'X-GitHub-Api-Version': '2022-11-28',
      ...(config.token ? { Authorization: `Bearer ${config.token}` } : {}),
      ...(body ? { 'Content-Type': 'application/json' } : {}),
    },
    body: body ? JSON.stringify(body) : undefined,
  });
  if (!res.ok) {
    const text = await res.text().catch(() => '');
    let msg = `GitHub ${res.status}`;
    try {
      msg = JSON.parse(text).message || msg;
    } catch {
      /* keep default */
    }
    throw new FileError(`GitHub API: ${msg}`, res.status === 401 || res.status === 403 ? 400 : 502);
  }
  return res.json();
}

/**
 * Sync: add any file present in the store (Blob/disk) that is missing from the
 * repo, as a single commit. Never overwrites or deletes existing repo files.
 */
export async function syncToGitHub(store) {
  const { owner, name } = parseRepo();
  if (!owner || !name) throw new FileError('GitHub repository not configured (owner/name)', 400);
  if (!config.token) throw new FileError('GitHub token not configured', 400);
  const branch = config.branch;

  // 1. Resolve branch -> latest commit -> base tree.
  const ref = await gh(`/repos/${owner}/${name}/git/ref/heads/${encodeURIComponent(branch)}`);
  const baseCommitSha = ref.object.sha;
  const baseCommit = await gh(`/repos/${owner}/${name}/git/commits/${baseCommitSha}`);
  const baseTreeSha = baseCommit.tree.sha;

  // 2. Existing repo file paths (recursive tree).
  const tree = await gh(`/repos/${owner}/${name}/git/trees/${baseTreeSha}?recursive=1`);
  const repoPaths = new Set((tree.tree || []).filter((t) => t.type === 'blob').map((t) => t.path));

  // 3. Files in the store, restricted to the algorithm sections.
  const storePaths = await store.listAllPaths();

  // 4. Diff: files present in the store but not in the repo.
  const toAdd = storePaths.filter((p) => !repoPaths.has(p));
  if (toAdd.length === 0) {
    return { committed: [], count: 0, message: 'Already in sync — nothing to commit.' };
  }

  // 5. Create git blobs for each new file.
  const treeItems = [];
  for (const p of toAdd) {
    const bytes = await store.readRawBytes(p);
    const blob = await gh(`/repos/${owner}/${name}/git/blobs`, 'POST', {
      content: Buffer.from(bytes).toString('base64'),
      encoding: 'base64',
    });
    treeItems.push({ path: p, mode: '100644', type: 'blob', sha: blob.sha });
  }

  // 6. New tree -> commit -> move the branch ref.
  const newTree = await gh(`/repos/${owner}/${name}/git/trees`, 'POST', {
    base_tree: baseTreeSha,
    tree: treeItems,
  });
  const commit = await gh(`/repos/${owner}/${name}/git/commits`, 'POST', {
    message: `Sync ${toAdd.length} file(s) created via AlgoForge IDE`,
    tree: newTree.sha,
    parents: [baseCommitSha],
  });
  await gh(`/repos/${owner}/${name}/git/refs/heads/${encodeURIComponent(branch)}`, 'PATCH', {
    sha: commit.sha,
  });

  return {
    committed: toAdd,
    count: toAdd.length,
    commitUrl: commit.html_url,
    message: `Committed ${toAdd.length} file(s) to ${owner}/${name}@${branch}.`,
  };
}
