import Editor from '@monaco-editor/react';
import Icon from '../Icon';

interface SolutionPaneProps {
  content: string;
  monacoLang: string;
  fileName: string | null;
  loading: boolean;
  onChange: (value: string) => void;
  onReset: () => void;
  onSave: () => void;
  canSave: boolean;
  dirty: boolean;
  saving: boolean;
}

export default function SolutionPane({
  content,
  monacoLang,
  fileName,
  loading,
  onChange,
  onReset,
  onSave,
  canSave,
  dirty,
  saving,
}: SolutionPaneProps) {
  return (
    <section className="flex-1 min-w-0 min-h-[60vh] md:min-h-0 bg-surface-container-low flex flex-col overflow-hidden">
      <div className="h-9 px-md flex items-center gap-sm bg-surface-container border-b border-outline-variant shrink-0">
        <span className="font-label-caps text-label-caps text-on-surface-variant uppercase tracking-wider flex items-center gap-xs">
          <Icon name="edit_document" size={14} className="text-primary" />
          Solution
          {dirty && <span className="text-tertiary" title="Unsaved changes">•</span>}
        </span>
        {fileName && <span className="ml-auto font-code-sm text-code-sm text-outline truncate">{fileName}</span>}
        <div className={`flex items-center gap-sm ${fileName ? '' : 'ml-auto'}`}>
          <button
            onClick={onReset}
            disabled={!dirty || saving}
            title="Revert unsaved changes to the saved file"
            className="text-outline hover:text-on-surface disabled:opacity-40 disabled:cursor-not-allowed"
          >
            <Icon name="restart_alt" size={16} />
          </button>
          <button
            onClick={onSave}
            disabled={!canSave || !dirty || saving}
            title={canSave ? 'Save changes to this solution' : 'Open a problem solution to save'}
            className="flex items-center gap-xs px-sm py-0.5 rounded font-body-sm text-body-sm border border-outline-variant text-on-surface-variant hover:text-primary disabled:opacity-40 disabled:cursor-not-allowed"
          >
            <Icon name={saving ? 'progress_activity' : 'save'} size={14} className={saving ? 'animate-spin' : ''} />
            {saving ? 'Saving…' : 'Save'}
          </button>
        </div>
      </div>
      <div className="flex-1 min-h-0 relative">
        {loading ? (
          <div className="p-md text-outline font-code-sm text-code-sm">Loading…</div>
        ) : content || fileName ? (
          <Editor
            height="100%"
            theme="vs-dark"
            language={monacoLang}
            value={content}
            onChange={(v) => onChange(v ?? '')}
            onMount={(editor, monaco) => {
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
        ) : (
          <div className="h-full flex flex-col items-center justify-center text-outline gap-sm">
            <Icon name="code_blocks" size={40} />
            <span className="font-code-sm text-code-sm">Select a file from the explorer</span>
          </div>
        )}
      </div>
    </section>
  );
}
