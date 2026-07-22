import { useEffect, useRef, useState } from 'react';
import ReactMarkdown from 'react-markdown';
import remarkGfm from 'remark-gfm';
import Icon from '../Icon';
import { fetchAIConfig, fetchCopilotStatus, sendChat, startCopilotLogin, type AIConfig } from '../../lib/api';
import type { ChatMessage } from '../../types';

// Context about what the user is currently viewing, folded into the system prompt
// so the assistant can answer about the open problem/solution.
export interface ChatContext {
  title: string | null;
  language: string;
  solution: string;
  practice: string;
}

interface ChatPanelProps {
  context: ChatContext;
}

const SUGGESTIONS = [
  'Explain the current solution',
  'What is the time & space complexity?',
  'Suggest a more efficient approach',
  'Help me debug my playground code',
];

function buildSystemPrompt(ctx: ChatContext): string {
  const parts = [
    'You are an expert coding assistant embedded in Algo Playground, an app for studying data-structures & algorithm problems.',
    'Give clear, concise explanations. Format ALL responses in GitHub-flavored Markdown: use headings, bold, bullet/numbered lists, tables, inline `code`, and fenced code blocks with a language tag. Focus on correctness, complexity, and idiomatic style.',
  ];
  if (ctx.title) parts.push(`The user is currently viewing the problem: "${ctx.title}".`);
  if (ctx.language && ctx.language !== 'unknown') parts.push(`The active language is ${ctx.language}.`);
  if (ctx.solution.trim()) {
    parts.push('The reference solution (left pane) is:\n```\n' + ctx.solution.slice(0, 6000) + '\n```');
  }
  if (ctx.practice.trim()) {
    parts.push("The user's playground code (right pane) is:\n```\n" + ctx.practice.slice(0, 6000) + '\n```');
  }
  return parts.join('\n\n');
}

// A fenced code block with a copy button in the top-right corner.
function CodeBlock({ code }: { code: string }) {
  const [copied, setCopied] = useState(false);
  const copy = async () => {
    try {
      await navigator.clipboard?.writeText(code);
      setCopied(true);
      setTimeout(() => setCopied(false), 1500);
    } catch {
      /* clipboard unavailable */
    }
  };
  return (
    <div className="relative group my-1">
      <button
        onClick={copy}
        title={copied ? 'Copied!' : 'Copy code'}
        className="absolute top-1 right-1 z-10 flex items-center gap-xs px-1.5 py-0.5 rounded bg-surface-container border border-outline-variant/60 text-outline hover:text-primary opacity-70 hover:opacity-100 transition-opacity font-code-sm text-code-sm"
      >
        <Icon name={copied ? 'check' : 'content_copy'} size={13} className={copied ? 'text-secondary' : ''} />
        {copied ? 'Copied' : 'Copy'}
      </button>
      <pre className="p-2 pt-7 rounded bg-surface-container-high border border-outline-variant/50 overflow-x-auto custom-scrollbar font-code-sm text-code-sm text-on-surface whitespace-pre">
        {code}
      </pre>
    </div>
  );
}

// Extract raw text from a React node tree (react-markdown passes parsed children).
function nodeToText(node: React.ReactNode): string {
  if (node == null || typeof node === 'boolean') return '';
  if (typeof node === 'string' || typeof node === 'number') return String(node);
  if (Array.isArray(node)) return node.map(nodeToText).join('');
  if (typeof node === 'object' && 'props' in (node as never)) {
    return nodeToText((node as { props: { children?: React.ReactNode } }).props.children);
  }
  return '';
}

// Render assistant/user message content as GitHub-flavored Markdown, themed to
// match the IDE. Fenced code blocks get a copy button; other elements are styled.
function MessageMarkdown({ content }: { content: string }) {
  return (
    <div className="space-y-2 leading-relaxed break-words">
      <ReactMarkdown
        remarkPlugins={[remarkGfm]}
        components={{
          p: ({ children }) => <p className="whitespace-pre-wrap">{children}</p>,
          h1: ({ children }) => <h1 className="text-body-sm font-bold text-on-surface mt-2">{children}</h1>,
          h2: ({ children }) => <h2 className="text-body-sm font-bold text-on-surface mt-2">{children}</h2>,
          h3: ({ children }) => <h3 className="font-bold text-on-surface mt-2">{children}</h3>,
          ul: ({ children }) => <ul className="list-disc pl-5 space-y-0.5">{children}</ul>,
          ol: ({ children }) => <ol className="list-decimal pl-5 space-y-0.5">{children}</ol>,
          li: ({ children }) => <li className="marker:text-outline">{children}</li>,
          a: ({ children, href }) => (
            <a href={href} target="_blank" rel="noreferrer" className="text-primary underline hover:no-underline">
              {children}
            </a>
          ),
          strong: ({ children }) => <strong className="font-bold text-on-surface">{children}</strong>,
          em: ({ children }) => <em className="italic">{children}</em>,
          blockquote: ({ children }) => (
            <blockquote className="border-l-2 border-outline-variant pl-2 text-on-surface-variant italic">
              {children}
            </blockquote>
          ),
          hr: () => <hr className="border-outline-variant/40" />,
          table: ({ children }) => (
            <div className="overflow-x-auto custom-scrollbar">
              <table className="border-collapse text-code-sm">{children}</table>
            </div>
          ),
          th: ({ children }) => (
            <th className="border border-outline-variant/50 px-2 py-0.5 text-left font-bold">{children}</th>
          ),
          td: ({ children }) => <td className="border border-outline-variant/50 px-2 py-0.5">{children}</td>,
          code: ({ className, children }) => {
            // Block code carries a language- className; inline code does not.
            const isBlock = /language-/.test(className || '');
            if (isBlock) return <CodeBlock code={nodeToText(children).replace(/\n$/, '')} />;
            return (
              <code className="px-1 py-0.5 rounded bg-surface-container-high border border-outline-variant/40 font-code-sm text-code-sm text-tertiary-fixed">
                {children}
              </code>
            );
          },
          pre: ({ children }) => <>{children}</>,
        }}
      >
        {content}
      </ReactMarkdown>
    </div>
  );
}

export default function ChatPanel({ context }: ChatPanelProps) {
  const [messages, setMessages] = useState<ChatMessage[]>([]);
  const [input, setInput] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [summary, setSummary] = useState<string | null>(null);
  const [summaryLoading, setSummaryLoading] = useState(false);
  const [summaryOpen, setSummaryOpen] = useState(true);
  const scrollRef = useRef<HTMLDivElement>(null);

  // AI provider config + Copilot sign-in state.
  const [config, setConfig] = useState<AIConfig | null>(null);
  const [signInInfo, setSignInInfo] = useState<{ userCode: string; verificationUri: string } | null>(null);
  const [signingIn, setSigningIn] = useState(false);
  const [authError, setAuthError] = useState<string | null>(null);

  const loadConfig = () => {
    fetchAIConfig()
      .then(setConfig)
      .catch(() => setConfig(null));
  };
  useEffect(loadConfig, []);

  const [checking, setChecking] = useState(false);

  // Check auth once; returns true if signed in. Used by the poll loop and the
  // manual "Check now" button.
  const checkAuthOnce = async (): Promise<boolean> => {
    try {
      const s = await fetchCopilotStatus();
      if (s.authenticated) {
        setSigningIn(false);
        setSignInInfo(null);
        loadConfig();
        return true;
      }
    } catch {
      /* keep polling */
    }
    return false;
  };

  // While a Copilot sign-in is in progress, poll status until authenticated.
  // Uses a self-scheduling timeout (not setInterval) so each check — which on
  // the server restarts the CLI client — fully completes before the next.
  useEffect(() => {
    if (!signingIn) return;
    let cancelled = false;
    let handle: ReturnType<typeof setTimeout>;
    const tick = async () => {
      const done = await checkAuthOnce();
      if (!cancelled && !done) handle = setTimeout(tick, 3000);
    };
    handle = setTimeout(tick, 3000);
    return () => {
      cancelled = true;
      clearTimeout(handle);
    };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [signingIn]);

  const manualCheck = async () => {
    setChecking(true);
    await checkAuthOnce();
    setChecking(false);
  };

  const beginSignIn = async () => {
    setAuthError(null);
    try {
      const r = await startCopilotLogin();
      if (r.alreadySignedIn) {
        loadConfig();
        return;
      }
      if (r.userCode && r.verificationUri) {
        setSignInInfo({ userCode: r.userCode, verificationUri: r.verificationUri });
        setSigningIn(true);
        window.open(r.verificationUri, '_blank', 'noopener');
      }
    } catch (e) {
      setAuthError((e as Error).message);
    }
  };

  // The Copilot provider requires an operator sign-in before chat works.
  const needsSignIn = config?.provider === 'copilot' && config.available && !config.authenticated;

  useEffect(() => {
    scrollRef.current?.scrollTo({ top: scrollRef.current.scrollHeight, behavior: 'smooth' });
  }, [messages, loading]);

  // Auto-present the opened solution as a structured problem write-up so the
  // empty-state gives a full overview. Keyed by title+language so it runs once
  // per problem and refreshes when a different problem/file is opened. The
  // request key guards against React StrictMode's double-invoke and re-renders.
  const requestedKeyRef = useRef<string | null>(null);
  useEffect(() => {
    if (needsSignIn) return; // don't summarize until the provider is ready
    if (!context.solution.trim()) {
      requestedKeyRef.current = null;
      setSummary(null);
      setSummaryLoading(false);
      return;
    }
    const key = `${context.title ?? ''}::${context.language}`;
    if (requestedKeyRef.current === key) return; // already summarized/in-flight for this problem
    requestedKeyRef.current = key;

    setSummary(null);
    setSummaryLoading(true);
    sendChat([
      {
        role: 'system',
        content:
          'You are given a coding problem\'s solution code. Reverse-engineer the original problem and present it in GitHub-flavored Markdown with EXACTLY these three sections:\n' +
          '## Problem\nDescribe the full problem in natural language (what is asked, constraints, and any relevant rules).\n' +
          '## Input / Output\nDescribe the expected input and output, with a short example.\n' +
          '## Complexity\nState the target time and space complexity (Big-O) that the shown solution achieves.\n' +
          'Do not include the solution code itself. Be accurate and concise.',
      },
      {
        role: 'user',
        content: `Problem title: ${context.title ?? 'Unknown'}\nLanguage: ${context.language}\nSolution code:\n\`\`\`\n${context.solution.slice(0, 6000)}\n\`\`\``,
      },
    ])
      .then(({ content }) => {
        // Only apply if this is still the most recently requested problem.
        if (requestedKeyRef.current === key) setSummary(content.trim());
      })
      .catch(() => {
        if (requestedKeyRef.current === key) {
          setSummary(null);
          requestedKeyRef.current = null; // allow a retry on next open
        }
      })
      .finally(() => {
        if (requestedKeyRef.current === key) setSummaryLoading(false);
      });
  }, [context.title, context.solution, context.language, needsSignIn]);

  const submit = async (text: string) => {
    const trimmed = text.trim();
    if (!trimmed || loading) return;
    setError(null);
    const nextHistory: ChatMessage[] = [...messages, { role: 'user', content: trimmed }];
    setMessages(nextHistory);
    setInput('');
    setLoading(true);
    try {
      const payload: ChatMessage[] = [
        { role: 'system', content: buildSystemPrompt(context) },
        ...nextHistory,
      ];
      const { content } = await sendChat(payload);
      setMessages((m) => [...m, { role: 'assistant', content }]);
    } catch (e) {
      setError((e as Error).message);
    } finally {
      setLoading(false);
    }
  };

  const onKeyDown = (e: React.KeyboardEvent<HTMLTextAreaElement>) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      submit(input);
    }
  };

  return (
    <div className="flex flex-col h-full min-h-0">
      {needsSignIn ? (
        <div className="flex-1 overflow-y-auto custom-scrollbar p-md flex flex-col items-center justify-center text-center gap-3">
          <Icon name="lock" size={32} className="text-outline" />
          <div className="space-y-1">
            <div className="font-label-caps uppercase tracking-wider text-label-caps text-on-surface-variant">
              GitHub Copilot sign-in required
            </div>
            <p className="font-code-sm text-code-sm text-outline max-w-xs">
              This assistant uses GitHub Copilot. Sign in once with your GitHub account to enable it.
            </p>
          </div>

          {signInInfo ? (
            <div className="w-full max-w-xs space-y-2 rounded-lg border border-outline-variant/50 bg-surface-container/60 p-3">
              <p className="font-code-sm text-code-sm text-on-surface-variant">
                1. Open{' '}
                <a
                  href={signInInfo.verificationUri}
                  target="_blank"
                  rel="noreferrer"
                  className="text-primary underline"
                >
                  {signInInfo.verificationUri.replace('https://', '')}
                </a>
              </p>
              <p className="font-code-sm text-code-sm text-on-surface-variant">2. Enter this code:</p>
              <div className="flex items-center justify-center gap-xs">
                <code className="px-2 py-1 rounded bg-surface-container-high border border-outline-variant text-tertiary-fixed font-bold tracking-widest">
                  {signInInfo.userCode}
                </code>
                <button
                  onClick={() => navigator.clipboard?.writeText(signInInfo.userCode)}
                  title="Copy code"
                  className="text-outline hover:text-primary"
                >
                  <Icon name="content_copy" size={14} />
                </button>
              </div>
              <div className="flex items-center justify-center gap-xs text-outline font-code-sm text-code-sm pt-1">
                <Icon name="progress_activity" size={14} className="animate-spin text-primary" />
                Waiting for authorization…
              </div>
              <button
                onClick={manualCheck}
                disabled={checking}
                className="w-full flex items-center justify-center gap-xs px-sm py-1.5 rounded border border-outline-variant/60 text-on-surface-variant hover:text-primary hover:border-primary/50 transition-colors disabled:opacity-40 font-code-sm text-code-sm"
              >
                <Icon name={checking ? 'progress_activity' : 'refresh'} size={14} className={checking ? 'animate-spin' : ''} />
                {checking ? 'Checking…' : "I've authorized — check now"}
              </button>
            </div>
          ) : (
            <button
              onClick={beginSignIn}
              className="flex items-center gap-xs px-md py-2 rounded bg-primary-container text-on-primary-container hover:bg-primary transition-colors font-body-sm text-body-sm"
            >
              <Icon name="login" size={16} />
              Sign in with GitHub
            </button>
          )}

          {authError && (
            <div className="rounded-lg px-2.5 py-1.5 bg-error-container/40 border border-error/40 text-error font-code-sm text-code-sm max-w-xs">
              {authError}
            </div>
          )}
        </div>
      ) : (
        <>
      {/* Message list */}
      <div ref={scrollRef} className="flex-1 overflow-y-auto custom-scrollbar p-sm space-y-3">
        {/* Problem summary — pinned at the top so it stays visible during chat. */}
        {context.title && (
          <div className="rounded-lg border border-outline-variant/50 bg-surface-container/60 p-2.5 space-y-1">
            <button
              onClick={() => setSummaryOpen((o) => !o)}
              className="w-full flex items-center gap-xs text-on-surface-variant hover:text-on-surface"
              title={summaryOpen ? 'Collapse problem summary' : 'Expand problem summary'}
            >
              <Icon name="description" size={14} className="text-primary shrink-0" />
              <span className="font-label-caps uppercase tracking-wider text-label-caps truncate">
                {context.title}
              </span>
              <Icon
                name={summaryOpen ? 'expand_less' : 'expand_more'}
                size={16}
                className="ml-auto shrink-0 text-outline"
              />
            </button>
            {summaryOpen &&
              (summaryLoading ? (
                <div className="flex items-center gap-xs text-outline font-code-sm text-code-sm">
                  <Icon name="progress_activity" size={13} className="animate-spin text-primary" />
                  Summarizing the current problem…
                </div>
              ) : summary ? (
                <div className="text-on-surface-variant font-code-sm text-code-sm">
                  <MessageMarkdown content={summary} />
                </div>
              ) : (
                <p className="italic text-outline font-code-sm text-code-sm">
                  No summary available for this problem.
                </p>
              ))}
          </div>
        )}

        {messages.length === 0 && !loading && (
          <div className="text-outline font-code-sm text-code-sm space-y-3 pt-1">
            <div className="flex items-center gap-xs text-secondary">
              <Icon name="smart_toy" size={16} />
              <span className="font-label-caps uppercase tracking-wider text-label-caps">AI Assistant</span>
            </div>
            <p>Ask about the open problem, complexity, or your playground code.</p>
            <div className="space-y-1">
              {SUGGESTIONS.map((s) => (
                <button
                  key={s}
                  onClick={() => submit(s)}
                  className="w-full text-left px-sm py-1.5 rounded border border-outline-variant/60 text-on-surface-variant hover:text-primary hover:border-primary/50 transition-colors"
                >
                  {s}
                </button>
              ))}
            </div>
          </div>
        )}

        {messages.map((m, i) => (
          <div key={i} className={m.role === 'user' ? 'flex justify-end' : 'flex justify-start'}>
            <div
              className={`max-w-[92%] rounded-lg px-2.5 py-1.5 font-code-sm text-code-sm ${
                m.role === 'user'
                  ? 'bg-primary-container text-on-primary-container'
                  : 'bg-surface-container text-on-surface border border-outline-variant/40'
              }`}
            >
              {m.role === 'user' ? (
                <span className="whitespace-pre-wrap">{m.content}</span>
              ) : (
                <MessageMarkdown content={m.content} />
              )}
            </div>
          </div>
        ))}

        {loading && (
          <div className="flex justify-start">
            <div className="rounded-lg px-2.5 py-1.5 bg-surface-container border border-outline-variant/40 text-outline font-code-sm text-code-sm flex items-center gap-xs">
              <Icon name="progress_activity" size={14} className="animate-spin text-primary" />
              Thinking…
            </div>
          </div>
        )}

        {error && (
          <div className="rounded-lg px-2.5 py-1.5 bg-error-container/40 border border-error/40 text-error font-code-sm text-code-sm">
            {error}
          </div>
        )}
      </div>

      {/* Composer */}
      <div className="p-sm border-t border-outline-variant safe-bottom">
        <div className="flex items-end gap-xs">
          <textarea
            value={input}
            onChange={(e) => setInput(e.target.value)}
            onKeyDown={onKeyDown}
            placeholder="Ask the AI… (Enter to send, Shift+Enter for newline)"
            rows={2}
            className="flex-1 resize-none bg-surface-container border border-outline-variant rounded px-2 py-1.5 font-code-sm text-code-sm text-on-surface focus:border-primary focus:outline-none custom-scrollbar"
          />
          <button
            onClick={() => submit(input)}
            disabled={loading || !input.trim()}
            title="Send"
            className="shrink-0 h-9 w-9 flex items-center justify-center rounded bg-primary-container text-on-primary-container hover:bg-primary transition-colors disabled:opacity-40 disabled:cursor-not-allowed"
          >
            <Icon name="send" size={18} />
          </button>
        </div>
        {messages.length > 0 && (
          <button
            onClick={() => {
              setMessages([]);
              setError(null);
            }}
            className="mt-1 text-outline hover:text-on-surface font-code-sm text-code-sm flex items-center gap-xs"
          >
            <Icon name="delete_sweep" size={14} />
            Clear conversation
          </button>
        )}
      </div>
        </>
      )}
    </div>
  );
}
