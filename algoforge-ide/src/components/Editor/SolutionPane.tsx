import Editor from '@monaco-editor/react';
import Icon from '../Icon';

interface SolutionPaneProps {
  content: string;
  monacoLang: string;
  fileName: string | null;
  loading: boolean;
}

export default function SolutionPane({ content, monacoLang, fileName, loading }: SolutionPaneProps) {
  return (
    <section className="flex-1 min-w-0 bg-surface-container-low flex flex-col overflow-hidden">
      <div className="h-9 px-md flex items-center bg-surface-container border-b border-outline-variant shrink-0">
        <span className="font-label-caps text-label-caps text-on-surface-variant uppercase tracking-wider flex items-center gap-xs">
          <Icon name="visibility" size={14} className="text-primary" />
          Standard Solution
        </span>
        {fileName && <span className="ml-auto font-code-sm text-code-sm text-outline truncate">{fileName}</span>}
      </div>
      <div className="flex-1 min-h-0 relative">
        {loading ? (
          <div className="p-md text-outline font-code-sm text-code-sm">Loading…</div>
        ) : content ? (
          <Editor
            height="100%"
            theme="vs-dark"
            language={monacoLang}
            value={content}
            options={{
              readOnly: true,
              minimap: { enabled: false },
              fontSize: 13,
              fontFamily: 'JetBrains Mono, monospace',
              lineNumbers: 'on',
              scrollBeyondLastLine: false,
              renderLineHighlight: 'none',
              automaticLayout: true,
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
