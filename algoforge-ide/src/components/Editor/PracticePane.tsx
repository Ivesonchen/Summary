import Editor from '@monaco-editor/react';
import Icon from '../Icon';

// Short label per extension for the playground language switcher.
const EXT_LABEL: Record<string, string> = {
  java: 'Java',
  py: 'Python',
  js: 'JS',
  ts: 'TS',
  cpp: 'C++',
  c: 'C',
  go: 'Go',
};

interface PracticePaneProps {
  value: string;
  monacoLang: string;
  languages: string[];
  activeExt: string | null;
  onSwitchLanguage: (ext: string) => void;
  onChange: (value: string) => void;
  onReset: () => void;
  onRun: () => void;
  onSave: () => void;
  runnable: boolean;
  running: boolean;
  canSave: boolean;
  saving: boolean;
}

export default function PracticePane({
  value,
  monacoLang,
  languages,
  activeExt,
  onSwitchLanguage,
  onChange,
  onReset,
  onRun,
  onSave,
  runnable,
  running,
  canSave,
  saving,
}: PracticePaneProps) {
  return (
    <section className="flex-1 min-w-0 min-h-[60vh] md:min-h-0 bg-surface flex flex-col overflow-hidden border-t md:border-t-0 md:border-l border-outline-variant">
      <div className="h-9 px-md flex justify-between items-center bg-surface-container border-b border-outline-variant shrink-0">
        <span className="font-label-caps text-label-caps text-secondary uppercase tracking-wider flex items-center gap-xs">
          <Icon name="science" size={14} />
          Playground
        </span>
        <div className="flex items-center gap-sm">
          {/* Playground language switcher (independent of the standard solution) */}
          {languages.length > 0 && (
            <select
              value={activeExt ?? ''}
              onChange={(e) => onSwitchLanguage(e.target.value)}
              title="Playground language"
              className="bg-surface-container-high border border-outline-variant rounded px-sm py-0.5 font-body-sm text-body-sm text-on-surface-variant hover:text-secondary focus:border-secondary focus:outline-none cursor-pointer"
            >
              {languages.map((ext) => (
                <option key={ext} value={ext}>
                  {EXT_LABEL[ext] ?? ext.toUpperCase()}
                </option>
              ))}
            </select>
          )}
          <button
            onClick={() => navigator.clipboard?.writeText(value)}
            title="Copy code"
            className="text-outline hover:text-on-surface"
          >
            <Icon name="content_copy" size={16} />
          </button>
          <button onClick={onReset} title="Reset to starter" className="text-outline hover:text-on-surface">
            <Icon name="restart_alt" size={16} />
          </button>
          <button
            onClick={onSave}
            disabled={!canSave || saving}
            title={canSave ? 'Save this as a new solution to the problem' : 'Open a problem to save a new solution'}
            className="flex items-center gap-xs px-sm py-0.5 rounded font-body-sm text-body-sm border border-outline-variant text-on-surface-variant hover:text-secondary disabled:opacity-40 disabled:cursor-not-allowed"
          >
            <Icon name={saving ? 'progress_activity' : 'save_as'} size={14} className={saving ? 'animate-spin' : ''} />
            {saving ? 'Saving…' : 'Save'}
          </button>
          <button
            onClick={onRun}
            disabled={!runnable || running}
            title={runnable ? 'Run this code (Ctrl/Cmd+R)' : 'This language cannot be executed by the sandbox'}
            className="flex items-center gap-xs px-sm py-0.5 rounded font-body-sm text-body-sm bg-primary-container text-on-primary-container hover:bg-primary transition-colors disabled:opacity-40 disabled:cursor-not-allowed"
          >
            <Icon name={running ? 'progress_activity' : 'play_arrow'} size={14} className={running ? 'animate-spin' : ''} />
            Run
          </button>
        </div>
      </div>
      <div className="flex-1 min-h-0">
        <Editor
          height="100%"
          theme="vs-dark"
          language={monacoLang === 'plaintext' ? 'javascript' : monacoLang}
          value={value}
          onChange={(v) => onChange(v ?? '')}
          onMount={(editor, monaco) => {
            editor.addCommand(monaco.KeyMod.CtrlCmd | monaco.KeyCode.KeyR, () => onRun());
            editor.addCommand(monaco.KeyMod.CtrlCmd | monaco.KeyCode.KeyS, () => onSave());
          }}
          options={{
            minimap: { enabled: false },
            fontSize: 13,
            fontFamily: 'JetBrains Mono, monospace',
            lineNumbers: 'on',
            scrollBeyondLastLine: false,
            automaticLayout: true,
            tabSize: 2,
          }}
        />
      </div>
    </section>
  );
}
