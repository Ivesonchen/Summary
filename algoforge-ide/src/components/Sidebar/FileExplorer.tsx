import { useState } from 'react';
import Icon from '../Icon';
import type { FileNode, FolderNode, ProblemNode, Section, TreeNode } from '../../types';

interface FileExplorerProps {
  sections: Section[];
  rootName: string;
  selectedPath: string | null;
  onSelect: (file: FileNode) => void;
  onSelectProblem: (problem: ProblemNode) => void;
  onCreate: () => void;
  onSync: () => void;
  loading: boolean;
  error: string | null;
  open?: boolean;
  onClose?: () => void;
}

// Icon per known top-level section; falls back to a folder icon.
const SECTION_ICON: Record<string, string> = {
  Categories: 'category',
  Companies: 'business',
  Design: 'dashboard',
  Random: 'shuffle',
};

// Accent color per language extension (for the problem's language dots).
const EXT_COLOR: Record<string, string> = {
  java: 'text-error',
  py: 'text-secondary',
  js: 'text-tertiary',
  ts: 'text-primary',
  cpp: 'text-primary',
  c: 'text-outline',
  go: 'text-secondary-fixed-dim',
};

const DIFFICULTY_COLOR: Record<number, string> = {
  1: 'text-secondary',
  2: 'text-primary',
  3: 'text-tertiary',
  4: 'text-error',
};

function ProblemRow({
  node,
  depth,
  selectedPath,
  onSelectProblem,
}: {
  node: ProblemNode;
  depth: number;
  selectedPath: string | null;
  onSelectProblem: (p: ProblemNode) => void;
}) {
  const selected = node.path === selectedPath;
  return (
    <div
      onClick={() => onSelectProblem(node)}
      style={{ paddingLeft: `${depth * 12 + 8}px` }}
      className={`flex items-center gap-xs py-1 pr-sm cursor-pointer rounded font-code-sm text-code-sm truncate ${
        selected
          ? 'bg-surface-variant/40 text-secondary-fixed-dim border-l-2 border-secondary-fixed-dim'
          : 'text-on-surface-variant hover:text-on-surface hover:bg-surface-variant/40'
      }`}
      title={`${node.name} — ${node.languages.map((l) => l.ext).join(', ')}`}
    >
      <Icon name="code" size={15} className="text-secondary shrink-0" />
      <span className="truncate">{node.name}</span>
      <span className="ml-auto flex items-center gap-[3px] shrink-0">
        {[...new Set(node.languages.map((l) => l.ext))].map((ext) => (
          <span key={ext} className={`text-[8px] font-bold uppercase ${EXT_COLOR[ext] ?? 'text-outline'}`}>
            {ext}
          </span>
        ))}
      </span>
    </div>
  );
}

function FileRow({
  node,
  depth,
  selectedPath,
  onSelect,
}: {
  node: FileNode;
  depth: number;
  selectedPath: string | null;
  onSelect: (f: FileNode) => void;
}) {
  const selected = node.path === selectedPath;
  return (
    <div
      onClick={() => onSelect(node)}
      style={{ paddingLeft: `${depth * 12 + 8}px` }}
      className={`flex items-center gap-xs py-1 pr-sm cursor-pointer rounded font-code-sm text-code-sm truncate ${
        selected
          ? 'bg-surface-variant/40 text-secondary-fixed-dim border-l-2 border-secondary-fixed-dim'
          : 'text-on-surface-variant hover:text-on-surface hover:bg-surface-variant/40'
      }`}
      title={node.name}
    >
      <Icon name="description" size={15} className="text-outline shrink-0" />
      {node.difficulty != null && (
        <span className={`text-[10px] font-bold shrink-0 ${DIFFICULTY_COLOR[node.difficulty] ?? 'text-outline'}`}>
          [{node.difficulty}]
        </span>
      )}
      <span className="truncate">{node.display}</span>
      <span className="ml-auto text-[9px] uppercase text-outline shrink-0">{node.ext}</span>
    </div>
  );
}


function FolderRow({
  node,
  depth,
  selectedPath,
  onSelect,
  onSelectProblem,
  defaultOpen,
}: {
  node: FolderNode;
  depth: number;
  selectedPath: string | null;
  onSelect: (f: FileNode) => void;
  onSelectProblem: (p: ProblemNode) => void;
  defaultOpen?: boolean;
}) {
  const [open, setOpen] = useState(!!defaultOpen);
  return (
    <div>
      <div
        onClick={() => setOpen((o) => !o)}
        style={{ paddingLeft: `${depth * 12 + 4}px` }}
        className="flex items-center gap-xs py-1 pr-sm text-on-surface hover:bg-surface-variant/40 cursor-pointer rounded font-code-sm text-code-sm"
      >
        <Icon name={open ? 'expand_more' : 'chevron_right'} size={16} className="text-outline shrink-0" />
        <Icon name={open ? 'folder_open' : 'folder'} size={15} className="text-outline shrink-0" />
        <span className="truncate">{node.name}</span>
      </div>
      {open && <div>{renderChildren(node.children, depth + 1, selectedPath, onSelect, onSelectProblem)}</div>}
    </div>
  );
}

/** Render a list of tree children (folders, problems, and loose files). */
function renderChildren(
  children: TreeNode[],
  depth: number,
  selectedPath: string | null,
  onSelect: (f: FileNode) => void,
  onSelectProblem: (p: ProblemNode) => void
) {
  const hasProblems = children.some((c) => c.type === 'problem');
  let lastGroup: number | null | undefined = undefined;

  return children.map((child) => {
    if (child.type === 'folder') {
      return (
        <FolderRow
          key={child.path}
          node={child}
          depth={depth}
          selectedPath={selectedPath}
          onSelect={onSelect}
          onSelectProblem={onSelectProblem}
        />
      );
    }
    if (child.type === 'problem') {
      // Insert a small group divider whenever the group changes within this list.
      const g = child.group ?? null;
      const showDivider = hasProblems && g !== lastGroup;
      lastGroup = g;
      return (
        <div key={child.path}>
          {showDivider && (
            <div
              style={{ paddingLeft: `${depth * 12 + 8}px` }}
              className="flex items-center gap-xs pt-1.5 pb-0.5 text-outline"
            >
              <span className="font-label-caps text-[9px] uppercase tracking-wider">
                {g == null ? 'Ungrouped' : `Group ${g}`}
              </span>
              <span className="flex-1 h-px bg-outline-variant/40" />
            </div>
          )}
          <ProblemRow
            node={child}
            depth={depth}
            selectedPath={selectedPath}
            onSelectProblem={onSelectProblem}
          />
        </div>
      );
    }
    return <FileRow key={child.path} node={child} depth={depth} selectedPath={selectedPath} onSelect={onSelect} />;
  });
}

function SectionGroup({
  section,
  selectedPath,
  onSelect,
  onSelectProblem,
  defaultOpen,
}: {
  section: Section;
  selectedPath: string | null;
  onSelect: (f: FileNode) => void;
  onSelectProblem: (p: ProblemNode) => void;
  defaultOpen?: boolean;
}) {
  const [open, setOpen] = useState(!!defaultOpen);
  const icon = SECTION_ICON[section.name] ?? 'folder';
  return (
    <div className="pt-2">
      <div
        onClick={() => setOpen((o) => !o)}
        className="flex items-center gap-xs px-sm py-1 cursor-pointer rounded hover:bg-surface-variant/40 text-on-surface"
      >
        <Icon name={open ? 'expand_more' : 'chevron_right'} size={16} className="text-outline shrink-0" />
        <Icon name={icon} size={15} className="text-secondary shrink-0" />
        <span className="font-label-caps text-label-caps uppercase tracking-wider">{section.name}</span>
      </div>
      {open && (
        <div className="space-y-px mt-1">
          {section.children.length === 0 && (
            <div className="pl-8 py-1 text-outline font-code-sm text-code-sm italic">empty</div>
          )}
          {renderChildren(section.children, 1, selectedPath, onSelect, onSelectProblem)}
        </div>
      )}
    </div>
  );
}

export default function FileExplorer({
  sections,
  rootName,
  selectedPath,
  onSelect,
  onSelectProblem,
  onCreate,
  onSync,
  loading,
  error,
  open = false,
  onClose,
}: FileExplorerProps) {
  // On mobile, selecting an item should also dismiss the drawer.
  const selectAndClose = (file: FileNode) => {
    onSelect(file);
    onClose?.();
  };
  const selectProblemAndClose = (problem: ProblemNode) => {
    onSelectProblem(problem);
    onClose?.();
  };
  return (
    <>
      {/* Mobile backdrop */}
      <div
        onClick={onClose}
        aria-hidden="true"
        className={`fixed inset-0 bg-black/50 z-40 md:hidden transition-opacity duration-200 ${
          open ? 'opacity-100' : 'opacity-0 pointer-events-none'
        }`}
      />
      <aside
        className={`fixed inset-y-0 left-0 z-50 w-72 max-w-[85vw] md:static md:z-auto md:w-64 md:max-w-none flex flex-col h-full overflow-hidden bg-surface-container-low border-r border-outline-variant shrink-0 safe-top safe-left transition-transform duration-200 ${
          open ? 'translate-x-0' : '-translate-x-full'
        } md:translate-x-0`}
      >
        <div className="p-md border-b border-outline-variant">
          <div className="flex justify-between items-center mb-sm">
            <span className="font-headline-sm text-headline-sm text-on-surface truncate">{rootName || 'ALGORITHMS'}</span>
            <button
              onClick={onClose}
              aria-label="Close file explorer"
              className="md:hidden text-outline hover:text-on-surface"
            >
              <Icon name="close" size={20} />
            </button>
            <Icon name="unfold_more" size={18} className="text-outline hidden md:inline-block" />
          </div>
          <div className="flex items-center gap-xs text-on-surface-variant font-code-sm text-code-sm">
            <Icon name="account_tree" size={14} />
            <span>main branch</span>
          </div>
        </div>

        <div className="flex-1 overflow-y-auto custom-scrollbar p-sm">
          {loading && <div className="p-md text-outline font-code-sm text-code-sm">Loading files…</div>}
          {error && <div className="p-md text-error font-code-sm text-code-sm">{error}</div>}

          {!loading &&
            !error &&
            sections.map((section, i) => (
              <SectionGroup
                key={section.name}
                section={section}
                selectedPath={selectedPath}
                onSelect={selectAndClose}
                onSelectProblem={selectProblemAndClose}
                defaultOpen={i === 0}
              />
            ))}
        </div>

        <div className="p-sm border-t border-outline-variant space-y-1 safe-bottom">
          <button
            onClick={onCreate}
            className="w-full bg-surface-variant text-on-surface-variant px-md py-2 rounded font-code-sm text-code-sm hover:text-primary transition-colors flex items-center justify-center gap-xs"
          >
            <Icon name="add_circle" size={18} />
            Create
          </button>
          <button
            onClick={onSync}
            title="Commit app-created problems back to the repository"
            className="w-full bg-surface-variant text-on-surface-variant px-md py-2 rounded font-code-sm text-code-sm hover:text-primary transition-colors flex items-center justify-center gap-xs"
          >
            <Icon name="cloud_sync" size={18} />
            Sync to GitHub
          </button>
        </div>
      </aside>
    </>
  );
}
