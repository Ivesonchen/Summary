import { useEffect, useState } from 'react';
import Icon from './Icon';
import { fetchGitHubConfig, saveGitHubConfig, syncToRepo } from '../lib/api';
import type { GitHubConfig, SyncResult } from '../types';

interface GitHubDialogProps {
  onClose: () => void;
}

/**
 * Configure GitHub sync (repo/branch/token) and trigger a Blob -> git sync.
 * The token is write-scoped to the repo and is stored server-side only; it is
 * never returned to the browser (the dialog only shows whether one is set).
 */
export default function GitHubDialog({ onClose }: GitHubDialogProps) {
  const [config, setConfig] = useState<GitHubConfig | null>(null);
  const [repo, setRepo] = useState('');
  const [branch, setBranch] = useState('master');
  const [syncBranch, setSyncBranch] = useState('algoforge-sync');
  const [token, setToken] = useState('');
  const [saving, setSaving] = useState(false);
  const [syncing, setSyncing] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [result, setResult] = useState<SyncResult | null>(null);

  useEffect(() => {
    fetchGitHubConfig()
      .then((c) => {
        setConfig(c);
        setRepo(c.repo);
        setBranch(c.branch || 'master');
        setSyncBranch(c.syncBranch || 'algoforge-sync');
      })
      .catch((e) => setError((e as Error).message));
  }, []);

  const save = async () => {
    setSaving(true);
    setError(null);
    try {
      const c = await saveGitHubConfig({ repo, branch, syncBranch, token: token || undefined });
      setConfig(c);
      setToken(''); // clear the secret from the field once stored server-side
    } catch (e) {
      setError((e as Error).message);
    } finally {
      setSaving(false);
    }
  };

  const sync = async () => {
    setSyncing(true);
    setError(null);
    setResult(null);
    try {
      // Persist any pending config edits first.
      await saveGitHubConfig({ repo, branch, syncBranch, token: token || undefined });
      setToken('');
      const r = await syncToRepo();
      setResult(r);
      setConfig((c) => (c ? { ...c, repo, branch, syncBranch, hasToken: true } : c));
    } catch (e) {
      setError((e as Error).message);
    } finally {
      setSyncing(false);
    }
  };

  const tokenConfigured = config?.hasToken || token.length > 0;

  return (
    <div className="fixed inset-0 z-[100] flex items-center justify-center bg-black/50" onClick={onClose}>
      <div
        className="w-[92vw] max-w-[460px] max-h-[90dvh] overflow-y-auto bg-surface-container border border-outline-variant rounded-xl shadow-xl p-md"
        onClick={(e) => e.stopPropagation()}
      >
        <div className="flex items-center justify-between mb-md">
          <span className="font-headline-sm text-headline-sm text-on-surface flex items-center gap-xs">
            <Icon name="cloud_sync" size={20} className="text-primary" />
            GitHub Sync
          </span>
          <button className="text-outline hover:text-on-surface" onClick={onClose}>
            <Icon name="close" size={20} />
          </button>
        </div>

        <p className="font-code-sm text-code-sm text-on-surface-variant mb-md">
          Commit problems/solutions created in the app back into the repository. New files are added
          to a dedicated{' '}
          <span className="text-primary">{syncBranch || 'algoforge-sync'}</span> branch and kept in a
          single standing{' '}
          <span className="text-primary">pull request</span> into{' '}
          <span className="text-primary">{branch || 'master'}</span> — review and merge it manually
          when ready (nothing is pushed to {branch || 'master'} directly, overwritten, or deleted).
        </p>

        <div className="space-y-sm">
          <label className="block">
            <span className="font-label-caps text-label-caps uppercase text-outline">Repository (owner/name)</span>
            <input
              value={repo}
              onChange={(e) => setRepo(e.target.value)}
              placeholder="Ivesonchen/Summary"
              className="w-full mt-1 bg-surface-container-high border border-outline-variant rounded px-2 py-1.5 font-code-sm text-code-sm text-on-surface focus:border-primary focus:outline-none"
            />
          </label>
          <label className="block">
            <span className="font-label-caps text-label-caps uppercase text-outline">Production branch (PR base)</span>
            <input
              value={branch}
              onChange={(e) => setBranch(e.target.value)}
              placeholder="master"
              className="w-full mt-1 bg-surface-container-high border border-outline-variant rounded px-2 py-1.5 font-code-sm text-code-sm text-on-surface focus:border-primary focus:outline-none"
            />
          </label>
          <label className="block">
            <span className="font-label-caps text-label-caps uppercase text-outline">Sync branch (PR head)</span>
            <input
              value={syncBranch}
              onChange={(e) => setSyncBranch(e.target.value)}
              placeholder="algoforge-sync"
              className="w-full mt-1 bg-surface-container-high border border-outline-variant rounded px-2 py-1.5 font-code-sm text-code-sm text-on-surface focus:border-primary focus:outline-none"
            />
          </label>
          <label className="block">
            <span className="font-label-caps text-label-caps uppercase text-outline">
              Personal access token {config?.hasToken && <span className="text-secondary">(configured)</span>}
            </span>
            <input
              type="password"
              value={token}
              onChange={(e) => setToken(e.target.value)}
              placeholder={config?.hasToken ? '•••••••• (leave blank to keep)' : 'ghp_… (repo write scope)'}
              autoComplete="off"
              className="w-full mt-1 bg-surface-container-high border border-outline-variant rounded px-2 py-1.5 font-code-sm text-code-sm text-on-surface focus:border-primary focus:outline-none"
            />
            <span className="block mt-1 text-[10px] text-outline">
              Needs a fine-grained token with Contents: read &amp; write. Stored on the server only —
              never shown again.
            </span>
          </label>
        </div>

        {error && <div className="mt-sm text-error font-code-sm text-code-sm">{error}</div>}
        {result && (
          <div className="mt-sm font-code-sm text-code-sm">
            <span className={result.count > 0 ? 'text-secondary' : 'text-on-surface-variant'}>{result.message}</span>
            {result.prUrl && (
              <a
                href={result.prUrl}
                target="_blank"
                rel="noreferrer"
                className="ml-1 text-primary underline"
              >
                view pull request
              </a>
            )}
          </div>
        )}

        <div className="flex justify-end gap-sm mt-md">
          <button
            onClick={save}
            disabled={saving || syncing}
            className="px-md py-1.5 rounded font-body-sm text-body-sm text-on-surface-variant hover:text-on-surface border border-outline-variant disabled:opacity-40"
          >
            {saving ? 'Saving…' : 'Save config'}
          </button>
          <button
            onClick={sync}
            disabled={syncing || saving || !repo.trim() || !tokenConfigured}
            className="px-md py-1.5 rounded font-bold font-body-sm text-body-sm bg-primary-container text-on-primary-container hover:bg-primary transition-colors flex items-center gap-xs disabled:opacity-40 disabled:cursor-not-allowed"
          >
            <Icon name={syncing ? 'progress_activity' : 'cloud_sync'} size={16} className={syncing ? 'animate-spin' : ''} />
            {syncing ? 'Syncing…' : 'Sync now'}
          </button>
        </div>
      </div>
    </div>
  );
}
