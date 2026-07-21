import { useEffect, useRef, useState } from 'react';
import Icon from '../Icon';
import { sendChat } from '../../lib/api';
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
    'You are an expert coding assistant embedded in AlgoForge IDE, an app for studying data-structures & algorithm problems.',
    'Give clear, concise explanations. Prefer short code snippets in fenced blocks. Focus on correctness, complexity, and idiomatic style.',
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

// Very small Markdown-ish renderer: fenced code blocks + inline code, everything
// else as plain wrapped text. Avoids pulling in a Markdown dependency.
function renderContent(content: string) {
  const blocks = content.split(/```/);
  return blocks.map((block, i) => {
    const isCode = i % 2 === 1;
    if (isCode) {
      // Drop an optional language hint on the first line.
      const body = block.replace(/^[a-zA-Z0-9+#-]*\n/, '');
      return (
        <pre
          key={i}
          className="my-1 p-2 rounded bg-surface-container-high border border-outline-variant/50 overflow-x-auto custom-scrollbar font-code-sm text-code-sm text-on-surface whitespace-pre"
        >
          {body}
        </pre>
      );
    }
    return (
      <span key={i} className="whitespace-pre-wrap">
        {block}
      </span>
    );
  });
}

export default function ChatPanel({ context }: ChatPanelProps) {
  const [messages, setMessages] = useState<ChatMessage[]>([]);
  const [input, setInput] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const scrollRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    scrollRef.current?.scrollTo({ top: scrollRef.current.scrollHeight, behavior: 'smooth' });
  }, [messages, loading]);

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
      {/* Message list */}
      <div ref={scrollRef} className="flex-1 overflow-y-auto custom-scrollbar p-sm space-y-3">
        {messages.length === 0 && !loading && (
          <div className="text-outline font-code-sm text-code-sm space-y-3 pt-2">
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
              {renderContent(m.content)}
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
    </div>
  );
}
