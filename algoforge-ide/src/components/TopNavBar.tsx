import Icon from './Icon';
import type { Language, ProblemLanguage } from '../types';

interface TopNavBarProps {
  fileName: string | null;
  language: Language;
  languages: ProblemLanguage[];
  activeExt: string | null;
  group: number | null;
  onSwitchLanguage: (ext: string) => void;
  runnable: boolean;
  running: boolean;
  onRun: () => void;
}

const LANG_LABEL: Record<Language, string> = {
  java: 'Java',
  python: 'Python 3',
  javascript: 'JavaScript',
  typescript: 'TypeScript',
  cpp: 'C++',
  c: 'C',
  go: 'Go',
  unknown: 'View only',
};

// Short label per extension for the language switcher tabs.
const EXT_LABEL: Record<string, string> = {
  java: 'Java',
  py: 'Python',
  js: 'JS',
  ts: 'TS',
  cpp: 'C++',
  c: 'C',
  go: 'Go',
};

export default function TopNavBar({
  fileName,
  language,
  languages,
  activeExt,
  group,
  onSwitchLanguage,
  runnable,
  running,
  onRun,
}: TopNavBarProps) {
  return (
    <header className="flex justify-between items-center w-full px-md h-12 z-50 bg-background border-b border-outline-variant shrink-0">
      <div className="flex items-center gap-lg">
        <span className="font-headline-md text-headline-md font-bold text-primary tracking-tight">
          AlgoForge IDE
        </span>
        <nav className="hidden md:flex gap-md items-center">
          <div className="flex items-center gap-xs text-on-surface-variant font-body-sm text-body-sm">
            <span>Algorithms</span>
            <Icon name="chevron_right" size={16} />
            <span className="text-primary font-bold truncate max-w-[240px]">
              {fileName ?? 'Select a problem'}
            </span>
            {group != null && (
              <span
                className="ml-1 px-1.5 py-0.5 rounded-full bg-surface-container-high border border-outline-variant text-[10px] font-label-caps text-tertiary"
                title="Problem group"
              >
                group {group}
              </span>
            )}
          </div>
        </nav>
      </div>
      <div className="flex items-center gap-md">
        {/* Language switcher across the problem's available solution.<ext> files */}
        {languages.length > 0 ? (
          <div className="flex items-center gap-px bg-surface-container rounded border border-outline-variant overflow-hidden">
            {languages.map((l) => {
              const active = l.ext === activeExt;
              return (
                <button
                  key={l.ext}
                  onClick={() => onSwitchLanguage(l.ext)}
                  className={`px-sm py-1 font-body-sm text-body-sm transition-colors ${
                    active
                      ? 'bg-primary-container text-on-primary-container font-bold'
                      : 'text-on-surface-variant hover:text-primary'
                  }`}
                >
                  {EXT_LABEL[l.ext] ?? l.ext.toUpperCase()}
                </button>
              );
            })}
          </div>
        ) : (
          <div className="flex items-center bg-surface-container px-sm py-1 rounded border border-outline-variant">
            <span className="font-body-sm text-body-sm text-on-surface-variant">
              Language: {LANG_LABEL[language]}
            </span>
          </div>
        )}
        <button
          onClick={onRun}
          disabled={!runnable || running}
          className="bg-primary-container text-on-primary-container px-md py-1.5 rounded font-bold font-body-sm text-body-sm hover:bg-primary transition-colors active:scale-95 duration-100 flex items-center gap-xs disabled:opacity-40 disabled:cursor-not-allowed"
        >
          <Icon name={running ? 'progress_activity' : 'play_arrow'} size={18} className={running ? 'animate-spin' : ''} />
          {running ? 'Running…' : 'Run Code'}
        </button>
      </div>
    </header>
  );
}
