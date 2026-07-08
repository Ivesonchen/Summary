import { useMemo, useState } from 'react';
import Icon from './Icon';
import { createProblem, createSolution } from '../lib/api';
import type { ProblemNode, Section, TreeNode } from '../types';

const LANGS: { ext: string; label: string }[] = [
  { ext: 'java', label: 'Java' },
  { ext: 'py', label: 'Python' },
  { ext: 'cpp', label: 'C++' },
  { ext: 'c', label: 'C' },
  { ext: 'js', label: 'JavaScript' },
  { ext: 'ts', label: 'TypeScript' },
  { ext: 'go', label: 'Go' },
];

interface CreateDialogProps {
  sections: Section[];
  activeProblem: ProblemNode | null;
  onClose: () => void;
  onCreated: (path: string, kind: 'problem' | 'solution') => void;
}

/** Flatten the tree into selectable parent folders (sections + sub-folders). */
function collectParents(sections: Section[]): { label: string; path: string }[] {
  const out: { label: string; path: string }[] = [];
  const walk = (nodes: TreeNode[], depth: number) => {
    for (const node of nodes) {
      if (node.type === 'folder') {
        out.push({ label: `${'\u00A0'.repeat(depth * 2)}${node.name}`, path: node.path });
        walk(node.children, depth + 1);
      }
    }
  };
  for (const s of sections) {
    out.push({ label: s.name, path: s.name });
    walk(s.children, 1);
  }
  return out;
}

export default function CreateDialog({ sections, activeProblem, onClose, onCreated }: CreateDialogProps) {
  const [mode, setMode] = useState<'problem' | 'solution'>('problem');
  const parents = useMemo(() => collectParents(sections), [sections]);

  const [parentPath, setParentPath] = useState(parents[0]?.path ?? 'Random');
  const [name, setName] = useState('');
  const [group, setGroup] = useState('');
  const [ext, setExt] = useState('java');
  const [busy, setBusy] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const existing = new Set(activeProblem?.languages.map((l) => l.ext) ?? []);
  const availableForSolution = LANGS.filter((l) => !existing.has(l.ext));
  const [solExt, setSolExt] = useState(availableForSolution[0]?.ext ?? '');

  const submit = async () => {
    setBusy(true);
    setError(null);
    try {
      if (mode === 'problem') {
        if (!name.trim()) throw new Error('Enter a problem name');
        const g = group.trim() === '' ? null : Number(group);
        const r = await createProblem({ parentPath, name: name.trim(), group: g, ext });
        onCreated(r.problemPath, 'problem');
      } else {
        if (!activeProblem) throw new Error('Select a problem first');
        if (!solExt) throw new Error('No language available to add');
        const r = await createSolution({ problemPath: activeProblem.path, ext: solExt });
        onCreated(activeProblem.path, 'solution');
        void r;
      }
      onClose();
    } catch (e) {
      setError((e as Error).message);
    } finally {
      setBusy(false);
    }
  };

  return (
    <div className="fixed inset-0 z-[100] flex items-center justify-center bg-black/50" onClick={onClose}>
      <div
        className="w-[420px] bg-surface-container border border-outline-variant rounded-xl shadow-xl p-md"
        onClick={(e) => e.stopPropagation()}
      >
        <div className="flex items-center justify-between mb-md">
          <span className="font-headline-sm text-headline-sm text-on-surface">Create</span>
          <button className="text-outline hover:text-on-surface" onClick={onClose}>
            <Icon name="close" size={20} />
          </button>
        </div>

        {/* Mode tabs */}
        <div className="flex gap-px bg-surface-container-high rounded border border-outline-variant overflow-hidden mb-md">
          {(['problem', 'solution'] as const).map((m) => (
            <button
              key={m}
              onClick={() => setMode(m)}
              className={`flex-1 py-1.5 font-body-sm text-body-sm capitalize transition-colors ${
                mode === m
                  ? 'bg-primary-container text-on-primary-container font-bold'
                  : 'text-on-surface-variant hover:text-primary'
              }`}
            >
              {m}
            </button>
          ))}
        </div>

        {mode === 'problem' ? (
          <div className="space-y-sm">
            <label className="block">
              <span className="font-label-caps text-label-caps uppercase text-outline">Parent folder</span>
              <select
                value={parentPath}
                onChange={(e) => setParentPath(e.target.value)}
                className="w-full mt-1 bg-surface-container-high border border-outline-variant rounded px-2 py-1.5 font-code-sm text-code-sm text-on-surface focus:border-primary focus:outline-none"
              >
                {parents.map((p) => (
                  <option key={p.path} value={p.path}>
                    {p.label}
                  </option>
                ))}
              </select>
            </label>
            <label className="block">
              <span className="font-label-caps text-label-caps uppercase text-outline">Problem name</span>
              <input
                value={name}
                onChange={(e) => setName(e.target.value)}
                placeholder="e.g. Two Sum"
                className="w-full mt-1 bg-surface-container-high border border-outline-variant rounded px-2 py-1.5 font-code-sm text-code-sm text-on-surface focus:border-primary focus:outline-none"
              />
            </label>
            <div className="flex gap-sm">
              <label className="block flex-1">
                <span className="font-label-caps text-label-caps uppercase text-outline">Group (optional)</span>
                <input
                  value={group}
                  onChange={(e) => setGroup(e.target.value.replace(/[^0-9]/g, ''))}
                  placeholder="e.g. 1"
                  className="w-full mt-1 bg-surface-container-high border border-outline-variant rounded px-2 py-1.5 font-code-sm text-code-sm text-on-surface focus:border-primary focus:outline-none"
                />
              </label>
              <label className="block flex-1">
                <span className="font-label-caps text-label-caps uppercase text-outline">Language</span>
                <select
                  value={ext}
                  onChange={(e) => setExt(e.target.value)}
                  className="w-full mt-1 bg-surface-container-high border border-outline-variant rounded px-2 py-1.5 font-code-sm text-code-sm text-on-surface focus:border-primary focus:outline-none"
                >
                  {LANGS.map((l) => (
                    <option key={l.ext} value={l.ext}>
                      {l.label}
                    </option>
                  ))}
                </select>
              </label>
            </div>
          </div>
        ) : (
          <div className="space-y-sm">
            {!activeProblem ? (
              <div className="text-on-surface-variant font-code-sm text-code-sm">
                Select a problem in the explorer first, then add a language to it.
              </div>
            ) : (
              <>
                <div className="font-code-sm text-code-sm text-on-surface-variant">
                  Adding a language to <span className="text-primary font-bold">{activeProblem.name}</span>
                </div>
                <label className="block">
                  <span className="font-label-caps text-label-caps uppercase text-outline">Language</span>
                  {availableForSolution.length === 0 ? (
                    <div className="mt-1 text-outline font-code-sm text-code-sm italic">
                      All supported languages already exist.
                    </div>
                  ) : (
                    <select
                      value={solExt}
                      onChange={(e) => setSolExt(e.target.value)}
                      className="w-full mt-1 bg-surface-container-high border border-outline-variant rounded px-2 py-1.5 font-code-sm text-code-sm text-on-surface focus:border-primary focus:outline-none"
                    >
                      {availableForSolution.map((l) => (
                        <option key={l.ext} value={l.ext}>
                          {l.label}
                        </option>
                      ))}
                    </select>
                  )}
                </label>
              </>
            )}
          </div>
        )}

        {error && <div className="mt-sm text-error font-code-sm text-code-sm">{error}</div>}

        <div className="flex justify-end gap-sm mt-md">
          <button
            onClick={onClose}
            className="px-md py-1.5 rounded font-body-sm text-body-sm text-on-surface-variant hover:text-on-surface"
          >
            Cancel
          </button>
          <button
            onClick={submit}
            disabled={busy || (mode === 'solution' && (!activeProblem || availableForSolution.length === 0))}
            className="px-md py-1.5 rounded font-bold font-body-sm text-body-sm bg-primary-container text-on-primary-container hover:bg-primary transition-colors disabled:opacity-40 disabled:cursor-not-allowed"
          >
            {busy ? 'Creating…' : 'Create'}
          </button>
        </div>
      </div>
    </div>
  );
}
