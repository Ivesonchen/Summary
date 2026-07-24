import { useEffect, useState } from 'react';
import Icon from '../Icon';
import { isDue, retention, sortByDue, type StudyEntry } from '../../lib/study';

interface StudySectionProps {
  entries: StudyEntry[];
  onOpen: (path: string) => void;
  onReviewNext: () => void;
  onRemove: (path: string) => void;
  selectedPath: string | null;
}

// Accent color for the retention badge based on how well the item is retained.
function retentionColor(r: number): string {
  if (r >= 0.7) return 'text-secondary';
  if (r >= 0.4) return 'text-tertiary-fixed-dim';
  return 'text-error';
}

export default function StudySection({
  entries,
  onOpen,
  onReviewNext,
  onRemove,
  selectedPath,
}: StudySectionProps) {
  const [open, setOpen] = useState(true);
  // Re-render periodically so retention values and "Due now" badges stay fresh.
  const [now, setNow] = useState(() => Date.now());
  useEffect(() => {
    const id = setInterval(() => setNow(Date.now()), 60_000);
    return () => clearInterval(id);
  }, []);

  const sorted = sortByDue(entries, now);
  const dueCount = sorted.filter((e) => isDue(e, now)).length;

  return (
    <div className="pt-2 border-b border-outline-variant pb-2 mb-1">
      <div
        onClick={() => setOpen((o) => !o)}
        className="flex items-center gap-xs px-sm py-1 cursor-pointer rounded hover:bg-surface-variant/40 text-on-surface"
      >
        <Icon name={open ? 'expand_more' : 'chevron_right'} size={16} className="text-outline shrink-0" />
        <Icon name="school" size={15} className="text-secondary shrink-0" />
        <span className="font-label-caps text-label-caps uppercase tracking-wider">Study</span>
        {dueCount > 0 && (
          <span className="ml-auto px-1.5 py-0.5 rounded-full bg-error/20 text-error text-[10px] font-bold">
            {dueCount} due
          </span>
        )}
      </div>

      {open && (
        <div className="mt-1 px-sm space-y-1">
          <button
            onClick={onReviewNext}
            disabled={entries.length === 0}
            title="Open the problem most due for review (spaced repetition)"
            className="w-full flex items-center justify-center gap-xs px-md py-2 rounded bg-secondary-container text-on-primary-container hover:opacity-90 transition-opacity font-body-sm text-body-sm disabled:opacity-40 disabled:cursor-not-allowed"
          >
            <Icon name="bolt" size={16} />
            Review Next
          </button>

          {entries.length === 0 ? (
            <p className="px-sm py-1 text-outline font-code-sm text-code-sm italic">
              Mark problems as done to build your review set.
            </p>
          ) : (
            <div className="space-y-px">
              <div className="px-1 pt-1 pb-0.5 font-label-caps text-[9px] uppercase tracking-wider text-outline">
                Due for review
              </div>
              {sorted.map((e) => {
                const r = retention(e, now);
                const due = isDue(e, now);
                const selected = e.path === selectedPath;
                return (
                  <div
                    key={e.path}
                    onClick={() => onOpen(e.path)}
                    className={`group flex items-center gap-xs py-1 px-1.5 cursor-pointer rounded font-code-sm text-code-sm ${
                      selected
                        ? 'bg-surface-variant/40 text-secondary-fixed-dim'
                        : 'text-on-surface-variant hover:text-on-surface hover:bg-surface-variant/40'
                    }`}
                    title={`${e.title} — reviewed ${e.reviewCount}×`}
                  >
                    <Icon name="menu_book" size={14} className="text-outline shrink-0" />
                    <span className="truncate flex-1">{e.title}</span>
                    {due ? (
                      <span className="shrink-0 px-1.5 py-0.5 rounded-full bg-error/20 text-error text-[9px] font-bold uppercase tracking-wider">
                        Due now
                      </span>
                    ) : (
                      <span className={`shrink-0 text-[10px] font-bold ${retentionColor(r)}`}>
                        {Math.round(r * 100)}%
                      </span>
                    )}
                    <button
                      onClick={(ev) => {
                        ev.stopPropagation();
                        onRemove(e.path);
                      }}
                      title="Remove from study set"
                      className="shrink-0 text-outline hover:text-error opacity-0 group-hover:opacity-100 transition-opacity"
                    >
                      <Icon name="close" size={13} />
                    </button>
                  </div>
                );
              })}
            </div>
          )}
        </div>
      )}
    </div>
  );
}
