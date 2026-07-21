// AI chat proxy backed by the GitHub Models inference API
// (https://docs.github.com/en/github-models). This lets the web app offer a
// "GitHub Copilot-style" assistant using GitHub's hosted models.
//
// Security: the access token is held server-side only and is NEVER returned to
// the browser (config responses expose only `hasToken`). The token comes from
// GITHUB_MODELS_TOKEN, falling back to GITHUB_TOKEN. It must be a PAT that has
// the "Models" permission enabled.
//
// The endpoint and default model are configurable via env:
//   GITHUB_MODELS_URL   (default https://models.github.ai/inference/chat/completions)
//   GITHUB_MODELS_MODEL (default openai/gpt-4o-mini)

import { FileError } from './fileStore.mjs';

const DEFAULT_URL = 'https://models.github.ai/inference/chat/completions';
const DEFAULT_MODEL = 'openai/gpt-4o-mini';

// Roles the API accepts; anything else is rejected before we call upstream.
const VALID_ROLES = new Set(['system', 'user', 'assistant']);

// Cap conversation size to protect the upstream call and control cost.
const MAX_MESSAGES = 40;
const MAX_CONTENT_CHARS = 24 * 1024;

function getToken() {
  return process.env.GITHUB_MODELS_TOKEN || process.env.GITHUB_TOKEN || '';
}

export function getAIConfig() {
  return {
    model: process.env.GITHUB_MODELS_MODEL || DEFAULT_MODEL,
    hasToken: Boolean(getToken()),
  };
}

/**
 * Send a chat conversation to the GitHub Models API and return the assistant's
 * reply text. `messages` is an array of { role, content } items.
 */
export async function chatCompletion(messages) {
  const token = getToken();
  if (!token) {
    throw new FileError(
      'AI is not configured. Set GITHUB_MODELS_TOKEN (a GitHub PAT with the Models permission) on the server.',
      400
    );
  }
  if (!Array.isArray(messages) || messages.length === 0) {
    throw new FileError('No messages provided', 400);
  }
  if (messages.length > MAX_MESSAGES) {
    throw new FileError('Conversation too long', 413);
  }

  const clean = [];
  for (const m of messages) {
    const role = m && typeof m.role === 'string' ? m.role : '';
    const content = m && typeof m.content === 'string' ? m.content : '';
    if (!VALID_ROLES.has(role)) throw new FileError(`Invalid message role: ${role}`, 400);
    if (content.length > MAX_CONTENT_CHARS) throw new FileError('Message content too large', 413);
    clean.push({ role, content });
  }

  const url = process.env.GITHUB_MODELS_URL || DEFAULT_URL;
  const model = process.env.GITHUB_MODELS_MODEL || DEFAULT_MODEL;

  let res;
  try {
    res = await fetch(url, {
      method: 'POST',
      headers: {
        Accept: 'application/json',
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
        'X-GitHub-Api-Version': '2022-11-28',
      },
      body: JSON.stringify({
        model,
        messages: clean,
        temperature: 0.3,
        top_p: 1,
      }),
    });
  } catch (err) {
    throw new FileError(`Failed to reach AI service: ${err.message}`, 502);
  }

  if (!res.ok) {
    const text = await res.text().catch(() => '');
    let msg = `AI service ${res.status}`;
    try {
      const parsed = JSON.parse(text);
      msg = parsed.error?.message || parsed.message || msg;
    } catch {
      /* keep default */
    }
    // Surface auth/permission problems as 400 so the UI shows the real reason.
    const status = res.status === 401 || res.status === 403 ? 400 : 502;
    throw new FileError(`AI service: ${msg}`, status);
  }

  const data = await res.json().catch(() => ({}));
  const content = data.choices?.[0]?.message?.content;
  if (typeof content !== 'string') {
    throw new FileError('AI service returned an unexpected response', 502);
  }
  return { content, model };
}
