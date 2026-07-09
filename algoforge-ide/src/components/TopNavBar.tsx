import Icon from './Icon';

interface TopNavBarProps {
  fileName: string | null;
  group: number | null;
  runnable: boolean;
  running: boolean;
  onRun: () => void;
  onToggleNav: () => void;
}

export default function TopNavBar({
  fileName,
  group,
  runnable,
  running,
  onRun,
  onToggleNav,
}: TopNavBarProps) {
  return (
    <header className="flex justify-between items-center w-full px-md h-12 z-50 bg-background border-b border-outline-variant shrink-0 safe-top safe-left safe-right">
      <div className="flex items-center gap-sm md:gap-lg min-w-0">
        <button
          onClick={onToggleNav}
          aria-label="Toggle file explorer"
          className="md:hidden text-on-surface-variant hover:text-primary shrink-0 -ml-1 p-1"
        >
          <Icon name="menu" size={22} />
        </button>
        <span className="font-headline-md text-headline-md font-bold text-primary tracking-tight shrink-0">
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
