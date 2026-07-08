import Icon from './Icon';
import type { Language } from '../types';

interface TopNavBarProps {
  fileName: string | null;
  language: Language;
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

export default function TopNavBar({ fileName, language, runnable, running, onRun }: TopNavBarProps) {
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
              {fileName ?? 'Select a file'}
            </span>
          </div>
        </nav>
      </div>
      <div className="flex items-center gap-md">
        <div className="flex items-center bg-surface-container px-sm py-1 rounded border border-outline-variant">
          <span className="font-body-sm text-body-sm text-on-surface-variant">
            Language: {LANG_LABEL[language]}
          </span>
        </div>
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
