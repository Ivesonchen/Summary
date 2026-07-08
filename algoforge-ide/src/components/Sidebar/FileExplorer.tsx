import { useState } from 'react';
import Icon from '../Icon';
import type { FileNode, FolderNode, Section, TreeNode } from '../../types';

interface FileExplorerProps {
  sections: Section[];
  rootName: string;
  selectedPath: string | null;
  onSelect: (file: FileNode) => void;
  loading: boolean;
  error: string | null;
}

// Icon per known top-level section; falls back to a folder icon.
const SECTION_ICON: Record<string, string> = {
  Categories: 'category',
  Companies: 'business',
  Design: 'dashboard',
  Random: 'shuffle',
};

const DIFFICULTY_COLOR: Record<number, string> = {
  1: 'text-secondary',
  2: 'text-primary',
  3: 'text-tertiary',
  4: 'text-error',
};

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
  defaultOpen,
}: {
  node: FolderNode;
  depth: number;
  selectedPath: string | null;
  onSelect: (f: FileNode) => void;
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
      {open && (
        <div>
          {node.children.map((child: TreeNode) =>
            child.type === 'folder' ? (
              <FolderRow
                key={child.path}
                node={child}
                depth={depth + 1}
                selectedPath={selectedPath}
                onSelect={onSelect}
              />
            ) : (
              <FileRow key={child.path} node={child} depth={depth + 1} selectedPath={selectedPath} onSelect={onSelect} />
            )
          )}
        </div>
      )}
    </div>
  );
}

function SectionGroup({
  section,
  selectedPath,
  onSelect,
  defaultOpen,
}: {
  section: Section;
  selectedPath: string | null;
  onSelect: (f: FileNode) => void;
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
          {section.children.map((child: TreeNode) =>
            child.type === 'folder' ? (
              <FolderRow key={child.path} node={child} depth={1} selectedPath={selectedPath} onSelect={onSelect} />
            ) : (
              <FileRow key={child.path} node={child} depth={1} selectedPath={selectedPath} onSelect={onSelect} />
            )
          )}
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
  loading,
  error,
}: FileExplorerProps) {
  return (
    <aside className="flex flex-col h-full overflow-hidden bg-surface-container-low border-r border-outline-variant w-64 shrink-0">
      <div className="p-md border-b border-outline-variant">
        <div className="flex justify-between items-center mb-sm">
          <span className="font-headline-sm text-headline-sm text-on-surface truncate">{rootName || 'ALGORITHMS'}</span>
          <Icon name="unfold_more" size={18} className="text-outline" />
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
              onSelect={onSelect}
              defaultOpen={i === 0}
            />
          ))}
      </div>
    </aside>
  );
}
