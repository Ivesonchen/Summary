import Editor from '@monaco-editor/react';
import Icon from '../Icon';

interface PracticePaneProps {
  value: string;
  monacoLang: string;
  onChange: (value: string) => void;
  onReset: () => void;
  onRun: () => void;
}

export default function PracticePane({ value, monacoLang, onChange, onReset, onRun }: PracticePaneProps) {
  return (
    <section className="flex-1 min-w-0 bg-surface flex flex-col overflow-hidden border-l border-outline-variant">
      <div className="h-9 px-md flex justify-between items-center bg-surface-container border-b border-outline-variant shrink-0">
        <span className="font-label-caps text-label-caps text-secondary uppercase tracking-wider flex items-center gap-xs">
          <Icon name="edit_note" size={14} />
          Your Workspace
        </span>
        <div className="flex gap-sm">
          <button
            onClick={() => navigator.clipboard?.writeText(value)}
            title="Copy"
            className="text-outline hover:text-on-surface"
          >
            <Icon name="content_copy" size={16} />
          </button>
          <button onClick={onReset} title="Reset" className="text-outline hover:text-on-surface">
            <Icon name="restart_alt" size={16} />
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
