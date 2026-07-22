import { useState } from 'react';
import Editor from '@monaco-editor/react';
import Icon from '../Icon';
import ChatPanel from './ChatPanel';
import type { ChatContext } from './ChatPanel';

interface SolutionPaneProps {
  content: string;
  monacoLang: string;
  fileName: string | null;
  loading: boolean;
  onChange: (value: string) => void;
  onReset: () => void;
  onSave: () => void;
  onRun: () => void;
  runnable: boolean;
  running: boolean;
  canSave: boolean;
  dirty: boolean;
  saving: boolean;
  chatContext: ChatContext;
}

export default function SolutionPane({
  content,
  monacoLang,
  fileName,
  loading,
  onChange,
  onReset,
  onSave,
  onRun,
  runnable,
  running,
  canSave,
  dirty,
  saving,
  chatContext,
}: SolutionPaneProps) {
  const [tab, setTab] = useState<'solution' | 'chat'>('solution');
  return (
    <section className="flex-1 min-w-0 min-h-[60vh] md:min-h-0 bg-surface-container-low flex flex-col overflow-hidden">
      <div className="h-9 px-md flex items-center gap-sm bg-surface-container border-b border-outline-variant shrink-0">
        <div className="flex items-center gap-md h-full">
          <button
            onClick={() => setTab('solution')}
            className={`h-full flex items-center gap-xs font-label-caps text-label-caps uppercase tracking-wider border-b-2 -mb-px transition-colors ${
              tab === 'solution'
                ? 'border-primary text-on-surface'
                : 'border-transparent text-outline hover:text-on-surface-variant'
            }`}
          >
            <Icon name="edit_document" size={14} className="text-primary" />
            Solution
            {dirty && <span className="text-tertiary" title="Unsaved changes">•</span>}
          </button>
          <button
            onClick={() => setTab('chat')}
            className={`h-full flex items-center gap-xs font-label-caps text-label-caps uppercase tracking-wider border-b-2 -mb-px transition-colors ${
              tab === 'chat'
                ? 'border-secondary text-on-surface'
                : 'border-transparent text-outline hover:text-on-surface-variant'
            }`}
          >
            <Icon name="smart_toy" size={14} className="text-secondary" />
            AI Chat
          </button>
        </div>
        {tab === 'solution' && (
          <>
            {fileName && <span className="ml-auto font-code-sm text-code-sm text-outline truncate">{fileName}</span>}
            <div className={`flex items-center gap-sm ${fileName ? '' : 'ml-auto'}`}>
              <button
                onClick={() => navigator.clipboard?.writeText(content)}
                title="Copy code"
                className="text-outline hover:text-on-surface"
              >
                <Icon name="content_copy" size={16} />
              </button>
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
              <button
                onClick={onRun}
                disabled={!runnable || running}
                title={runnable ? 'Run this solution (Ctrl/Cmd+R)' : 'This language cannot be executed by the sandbox'}
                className="flex items-center gap-xs px-sm py-0.5 rounded font-body-sm text-body-sm bg-primary-container text-on-primary-container hover:bg-primary transition-colors disabled:opacity-40 disabled:cursor-not-allowed"
              >
                <Icon name={running ? 'progress_activity' : 'play_arrow'} size={14} className={running ? 'animate-spin' : ''} />
                Run
              </button>
            </div>
          </>
        )}
      </div>
      {/* Both panes stay mounted; we toggle visibility so the AI chat keeps its
          state (and doesn't re-summarize) when switching back and forth. */}
      <div className={`flex-1 min-h-0 ${tab === 'chat' ? 'flex flex-col' : 'hidden'}`}>
        <ChatPanel context={chatContext} />
      </div>
      <div className={`flex-1 min-h-0 relative ${tab === 'solution' ? '' : 'hidden'}`}>
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
              editor.addCommand(monaco.KeyMod.CtrlCmd | monaco.KeyCode.KeyR, () => onRun());
            }}
            options={{
              minimap: { enabled: false },
              fontSize: 13,
              fontFamily: 'JetBrains Mono, monospace',
              lineNumbers: 'on',
              scrollBeyondLastLine: false,
              automaticLayout: true,
              tabSize: 4,
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
