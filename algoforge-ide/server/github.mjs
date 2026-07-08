// GitHub sync: commits Blob-created algorithm files back into the repository via
// the GitHub Git Data API. New files land on a dedicated integration branch
// (GITHUB_SYNC_BRANCH, default `algoforge-sync`) and a single standing pull
// request from that branch into the production branch (GITHUB_BRANCH) stays open
// for manual review + merge — the production branch is never pushed to directly.
//
// Security: the token is held server-side only. It is NEVER returned to the
// browser (config responses expose only `hasToken`). Defaults come from env
// (GITHUB_REPO / GITHUB_BRANCH / GITHUB_SYNC_BRANCH / GITHUB_TOKEN); the web UI
// can override them at runtime (stored in memory for the process lifetime).

import { FileError } from './fileStore.mjs';

const API = 'https://api.github.com';

const config = {
  repo: process.env.GITHUB_REPO || '', // "owner/name"
  branch: process.env.GITHUB_BRANCH || 'master', // production branch (PR base)
  syncBranch: process.env.GITHUB_SYNC_BRANCH || 'algoforge-sync', // dedicated integration branch
  token: process.env.GITHUB_TOKEN || '',
};

export function getGitHubConfig() {
  return {
    repo: config.repo,
    branch: config.branch,
    syncBranch: config.syncBranch,
    hasToken: Boolean(config.token),
  };
}

export function setGitHubConfig({ repo, branch, syncBranch, token }) {
  if (repo != null) config.repo = String(repo).trim().replace(/^https?:\/\/github\.com\//, '').replace(/\.git$/, '');
  if (branch != null && String(branch).trim()) config.branch = String(branch).trim();
  if (syncBranch != null && String(syncBranch).trim()) config.syncBranch = String(syncBranch).trim();
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

// Ensure the dedicated integration branch exists; create it from the production
// branch tip if missing. Returns the sync branch tip commit sha.
async function ensureSyncBranch(owner, name, baseBranch, syncBranch) {
  try {
    const ref = await gh(`/repos/${owner}/${name}/git/ref/heads/${encodeURIComponent(syncBranch)}`);
    return ref.object.sha;
  } catch {
    // Not found (or not resolvable): branch it off the production branch tip below.
  }
  const baseRef = await gh(`/repos/${owner}/${name}/git/ref/heads/${encodeURIComponent(baseBranch)}`);
  const baseSha = baseRef.object.sha;
  await gh(`/repos/${owner}/${name}/git/refs`, 'POST', {
    ref: `refs/heads/${syncBranch}`,
    sha: baseSha,
  });
  return baseSha;
}

// Find an open PR from syncBranch -> baseBranch, or open one if none exists.
async function ensureStandingPr(owner, name, baseBranch, syncBranch) {
  const existing = await gh(
    `/repos/${owner}/${name}/pulls?state=open&head=${owner}:${encodeURIComponent(syncBranch)}&base=${encodeURIComponent(baseBranch)}`
  );
  if (Array.isArray(existing) && existing.length > 0) {
    return { pr: existing[0], created: false };
  }
  const pr = await gh(`/repos/${owner}/${name}/pulls`, 'POST', {
    title: `AlgoForge sync → ${baseBranch}`,
    head: syncBranch,
    base: baseBranch,
    body: `Standing pull request for files created in AlgoForge IDE.\n\nEach sync adds new commits to \`${syncBranch}\`. Review the accumulated changes here and merge into \`${baseBranch}\` when ready.`,
  });
  return { pr, created: true };
}

/**
 * Sync: add any file present in the store (Blob/disk) that is missing from the
 * dedicated `algoforge-sync` integration branch. Commits land on that branch
 * (never on the production branch), and a single standing pull request from
 * `algoforge-sync` into the production branch stays open for manual review and
 * merge. Never overwrites or deletes files.
 */
export async function syncToGitHub(store) {
  const { owner, name } = parseRepo();
  if (!owner || !name) throw new FileError('GitHub repository not configured (owner/name)', 400);
  if (!config.token) throw new FileError('GitHub token not configured', 400);
  const baseBranch = config.branch;
  const syncBranch = config.syncBranch;

  // 1. Ensure the dedicated integration branch exists -> its tip commit + tree.
  const syncTipSha = await ensureSyncBranch(owner, name, baseBranch, syncBranch);
  const syncCommit = await gh(`/repos/${owner}/${name}/git/commits/${syncTipSha}`);
  const syncTreeSha = syncCommit.tree.sha;

  // 2. Existing file paths on the integration branch (recursive tree).
  const tree = await gh(`/repos/${owner}/${name}/git/trees/${syncTreeSha}?recursive=1`);
  const repoPaths = new Set((tree.tree || []).filter((t) => t.type === 'blob').map((t) => t.path));

  // 3. Files in the store, restricted to the algorithm sections.
  const storePaths = await store.listAllPaths();

  // 4. Diff: files present in the store but not on the integration branch.
  const toAdd = storePaths.filter((p) => !repoPaths.has(p));
  if (toAdd.length === 0) {
    // Still make sure the standing PR exists so the user always has a review entry point.
    const { pr } = await ensureStandingPr(owner, name, baseBranch, syncBranch);
    return {
      committed: [],
      count: 0,
      prNumber: pr.number,
      prUrl: pr.html_url,
      branch: syncBranch,
      message: `Already in sync — nothing to commit. Standing PR #${pr.number} into ${baseBranch}.`,
    };
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

  // 6. New tree -> commit (parented on the integration branch tip).
  const newTree = await gh(`/repos/${owner}/${name}/git/trees`, 'POST', {
    base_tree: syncTreeSha,
    tree: treeItems,
  });
  const commit = await gh(`/repos/${owner}/${name}/git/commits`, 'POST', {
    message: `Sync ${toAdd.length} file(s) created via AlgoForge IDE`,
    tree: newTree.sha,
    parents: [syncTipSha],
  });

  // 7. Fast-forward the integration branch ref (does NOT touch the production branch).
  await gh(`/repos/${owner}/${name}/git/refs/heads/${encodeURIComponent(syncBranch)}`, 'PATCH', {
    sha: commit.sha,
    force: false,
  });

  // 8. Ensure the single standing PR from the integration branch into production.
  const { pr, created } = await ensureStandingPr(owner, name, baseBranch, syncBranch);

  return {
    committed: toAdd,
    count: toAdd.length,
    prNumber: pr.number,
    prUrl: pr.html_url,
    branch: syncBranch,
    message: `Added ${toAdd.length} file(s) to ${syncBranch}. ${created ? 'Opened' : 'Updated'} standing PR #${pr.number} into ${baseBranch} — review and merge manually.`,
  };
}
