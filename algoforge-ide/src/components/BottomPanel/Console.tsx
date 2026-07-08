import { useState } from 'react';
import Icon from '../Icon';
import type { RunResult, TestOutcome } from '../../types';

type Tab = 'console' | 'tests' | 'output';

interface BottomPanelProps {
  stdin: string;
  onStdinChange: (v: string) => void;
  practiceResult: RunResult | null;
  testOutcome: TestOutcome | null;
  status: string | null;
  running: boolean;
}

function StatusLine({ result }: { result: RunResult }) {
  const color = result.ok ? 'text-primary' : 'text-error';
  const metrics = [
    result.timeMs != null ? `${result.timeMs} ms` : null,
    result.memoryKb != null ? `${(result.memoryKb / 1024).toFixed(1)} MB` : null,
  ]
    .filter(Boolean)
    .join(' · ');
  return (
    <div className="mt-2 pt-2 border-t border-outline-variant/20 flex justify-between items-center">
      <span className={color}>
        {result.statusText}
        {metrics && <span className="text-outline-variant"> · {metrics}</span>}
      </span>
      <span className="text-outline-variant italic">Press Ctrl/Cmd+R to run again</span>
    </div>
  );
}

export default function BottomPanel({
  stdin,
  onStdinChange,
  practiceResult,
  testOutcome,
  status,
  running,
}: BottomPanelProps) {
  const [tab, setTab] = useState<Tab>('console');

  const tabBtn = (id: Tab, icon: string, label: string) => (
    <button
      onClick={() => setTab(id)}
      className={`px-2 py-1 rounded font-code-sm text-code-sm flex items-center gap-xs transition-colors ${
        tab === id
          ? 'text-secondary-fixed-dim bg-surface-variant/50'
          : 'text-on-surface-variant hover:text-on-surface hover:bg-surface-variant'
      }`}
    >
      <Icon name={icon} size={16} />
      {label}
    </button>
  );

  return (
    <footer className="h-56 bg-surface-container-high border-t border-outline-variant flex flex-col z-40 shrink-0">
      {/* Tab row */}
      <div className="flex items-center gap-md px-md pt-2 border-b border-outline-variant/30 h-9 shrink-0">
        {tabBtn('console', 'terminal', 'Console')}
        {tabBtn('tests', 'fact_check', 'Tests')}
        {tabBtn('output', 'wysiwyg', 'Output')}
        <div className="ml-auto flex items-center gap-xs text-outline font-code-sm text-code-sm">
          {running && <Icon name="progress_activity" size={16} className="animate-spin text-primary" />}
          <span className="truncate max-w-[280px]">{status ?? 'Ready'}</span>
        </div>
      </div>

      {/* Stdin input */}
      <div className="flex items-start gap-sm px-md py-1.5 border-b border-outline-variant/30 shrink-0">
        <label className="font-label-caps text-label-caps uppercase text-outline shrink-0 pt-1">Stdin</label>
        <textarea
          value={stdin}
          onChange={(e) => onStdinChange(e.target.value)}
          placeholder="Input passed to your program's standard input…"
          rows={2}
          className="flex-1 resize-none bg-surface-container border border-outline-variant rounded px-2 py-1 font-code-sm text-code-sm text-on-surface focus:border-primary focus:outline-none custom-scrollbar"
        />
      </div>

      {/* Body */}
      <div className="flex-1 p-md overflow-y-auto custom-scrollbar font-code-sm text-code-sm">
        {tab === 'console' && (
          <div>
            {!practiceResult && <span className="text-outline">Run your code to see program output.</span>}
            {practiceResult && (
              <>
                <div className="flex items-center gap-sm mb-1">
                  <span className="text-secondary">➜</span>
                  <span className="text-on-surface-variant">Executed in server sandbox…</span>
                </div>
                {practiceResult.stdout && (
                  <div className="text-on-surface whitespace-pre-wrap">{practiceResult.stdout}</div>
                )}
                {practiceResult.compileOutput && (
                  <div className="text-tertiary whitespace-pre-wrap">{practiceResult.compileOutput}</div>
                )}
                {practiceResult.stderr && (
                  <div className="text-error whitespace-pre-wrap">{practiceResult.stderr}</div>
                )}
                {!practiceResult.stdout &&
                  !practiceResult.stderr &&
                  !practiceResult.compileOutput &&
                  practiceResult.error && (
                    <div className="text-error whitespace-pre-wrap">{practiceResult.error}</div>
                  )}
                <StatusLine result={practiceResult} />
              </>
            )}
          </div>
        )}

        {tab === 'tests' && (
          <div>
            {!testOutcome && (
              <span className="text-outline">
                Run to compare your stdout against the standard solution. (Requires the standard file to be a
                complete, runnable program.)
              </span>
            )}
            {testOutcome && (
              <div className="space-y-2">
                <div className="flex items-center gap-sm py-1">
                  {testOutcome.status === 'pass' && (
                    <Icon name="check_circle" size={16} className="text-secondary" />
                  )}
                  {(testOutcome.status === 'fail' || testOutcome.status === 'error') && (
                    <Icon name="cancel" size={16} className="text-error" />
                  )}
                  {testOutcome.status === 'skipped' && <Icon name="info" size={16} className="text-tertiary" />}
                  <span
                    className={`font-bold ${
                      testOutcome.status === 'pass'
                        ? 'text-secondary'
                        : testOutcome.status === 'skipped'
                          ? 'text-tertiary'
                          : 'text-error'
                    }`}
                  >
                    {testOutcome.status.toUpperCase()}
                  </span>
                  <span className="text-on-surface-variant">{testOutcome.message}</span>
                </div>
                {testOutcome.expected !== undefined && (
                  <div className="pl-6 space-y-1">
                    <div className="text-outline">
                      expected: <span className="text-secondary-fixed-dim whitespace-pre-wrap">{testOutcome.expected}</span>
                    </div>
                    <div className="text-outline">
                      actual: <span className="text-tertiary-fixed whitespace-pre-wrap">{testOutcome.actual}</span>
                    </div>
                  </div>
                )}
              </div>
            )}
          </div>
        )}

        {tab === 'output' && (
          <div>
            {!practiceResult && <span className="text-outline">Your program's stdout appears here.</span>}
            {practiceResult && (
              <div className="text-on-surface whitespace-pre-wrap">
                {practiceResult.stdout || '(no output)'}
              </div>
            )}
          </div>
        )}
      </div>
    </footer>
  );
}
