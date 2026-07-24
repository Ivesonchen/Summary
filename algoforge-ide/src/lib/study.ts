// Study / spaced-repetition tracking for problems the user has marked "Done".
//
// Scheduling uses FSRS (Free Spaced Repetition Scheduler) — the modern
// stability/difficulty model with a power-law forgetting curve, which is more
// accurate than a fixed SM-2 multiplier. Each "Mark Done" is treated as a
// successful "Good" review: it recomputes the card's memory stability, grows the
// review interval, and puts the problem back into the review set on a later date.
//
// Data is cached per-browser in localStorage and synced to the server for
// cross-device persistence.

import { useCallback, useEffect, useRef, useState } from 'react';
import { fetchStudy, saveStudy } from './api';

/** FSRS grades: Again (forgot), Hard, Good, Easy. */
export type Grade = 1 | 2 | 3 | 4;

export interface StudyEntry {
  path: string;
  title: string;
  group?: number | null;
  ext?: string | null;
  /** First time the problem was marked done. */
  markedAt: number;
  /** Last review timestamp (resets the forgetting curve). */
  lastReviewedAt: number;
  /** Number of times reviewed (including the initial mark). */
  reviewCount: number;
  /** Current scheduled interval in days (derived from stability). */
  intervalDays: number;
  /** FSRS memory stability (days for retrievability to fall to 90%). */
  stability?: number;
  /** FSRS difficulty (1 = easiest, 10 = hardest). */
  difficulty?: number;
}

const STORAGE_KEY = 'studyEntries';
const DAY_MS = 24 * 60 * 60 * 1000;

// ── FSRS-5 core ──────────────────────────────────────────────────────────────
// Default FSRS-5 parameters (19 weights).
const W = [
  0.40255, 1.18385, 3.173, 15.69105, 7.1949, 0.5345, 1.4604, 0.0046, 1.54575,
  0.1192, 1.01925, 1.9395, 0.11, 0.29605, 2.2698, 0.2315, 2.9898, 0.51655, 0.6621,
];
const DECAY = -0.5;
const FACTOR = Math.pow(0.9, 1 / DECAY) - 1; // = 19/81 ≈ 0.2345
const REQUEST_RETENTION = 0.9; // target retrievability at the scheduled interval

const clamp = (x: number, lo: number, hi: number) => Math.min(hi, Math.max(lo, x));

/** Power-law forgetting curve: retrievability after t days at stability S. */
function forgettingCurve(t: number, S: number): number {
  return Math.pow(1 + (FACTOR * t) / Math.max(S, 0.01), DECAY);
}

/** Days until retrievability decays to REQUEST_RETENTION for stability S. */
function intervalFromStability(S: number): number {
  const days = (S / FACTOR) * (Math.pow(REQUEST_RETENTION, 1 / DECAY) - 1);
  return Math.max(1, Math.round(days));
}

const initStability = (g: Grade) => Math.max(W[g - 1], 0.1);
const initDifficulty = (g: Grade) => clamp(W[4] - Math.exp(W[5] * (g - 1)) + 1, 1, 10);

function nextDifficulty(D: number, g: Grade): number {
  const delta = -W[6] * (g - 3);
  const damped = D + delta * ((10 - D) / 9); // linear damping
  return clamp(W[7] * initDifficulty(4) + (1 - W[7]) * damped, 1, 10); // mean reversion to Easy
}

function nextRecallStability(D: number, S: number, R: number, g: Grade): number {
  const hardPenalty = g === 2 ? W[15] : 1;
  const easyBonus = g === 4 ? W[16] : 1;
  return (
    S *
    (1 +
      Math.exp(W[8]) *
        (11 - D) *
        Math.pow(S, -W[9]) *
        (Math.exp(W[10] * (1 - R)) - 1) *
        hardPenalty *
        easyBonus)
  );
}

function nextForgetStability(D: number, S: number, R: number): number {
  return W[11] * Math.pow(D, -W[12]) * (Math.pow(S + 1, W[13]) - 1) * Math.exp(W[14] * (1 - R));
}

/** Read an entry's stability, migrating older SM-2 entries by their interval. */
function stabilityOf(e: StudyEntry): number {
  return e.stability ?? Math.max(e.intervalDays ?? 1, 0.1);
}
const difficultyOf = (e: StudyEntry): number => e.difficulty ?? 5;

function load(): StudyEntry[] {
  try {
    const raw = localStorage.getItem(STORAGE_KEY);
    if (!raw) return [];
    const parsed = JSON.parse(raw);
    return Array.isArray(parsed) ? parsed : [];
  } catch {
    return [];
  }
}

function save(entries: StudyEntry[]): void {
  try {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(entries));
  } catch {
    /* storage full / unavailable — ignore */
  }
}

/** FSRS retrievability (0..1): probability of recall right now. */
export function retention(entry: StudyEntry, now: number = Date.now()): number {
  const elapsedDays = Math.max(0, (now - entry.lastReviewedAt) / DAY_MS);
  return forgettingCurve(elapsedDays, stabilityOf(entry));
}

/** A problem is "due" once retrievability drops to the target retention. */
export function isDue(entry: StudyEntry, now: number = Date.now()): boolean {
  return retention(entry, now) <= REQUEST_RETENTION;
}

/** Sort a copy of entries by most-due first (lowest retrievability at the top). */
export function sortByDue(entries: StudyEntry[], now: number = Date.now()): StudyEntry[] {
  return [...entries].sort((a, b) => retention(a, now) - retention(b, now));
}

/**
 * Pick the next problem to review: favor the most-due problems (top of the
 * FSRS-sorted list) but add light randomness among the top few so it is
 * "not purely random". Returns null when there is nothing to review.
 */
export function pickReviewNext(entries: StudyEntry[], now: number = Date.now()): StudyEntry | null {
  if (entries.length === 0) return null;
  const sorted = sortByDue(entries, now);
  const poolSize = Math.min(3, sorted.length);
  return sorted[Math.floor(Math.random() * poolSize)];
}

export interface StudyProblemRef {
  path: string;
  title: string;
  group?: number | null;
  ext?: string | null;
}

/** React hook exposing the study set + mutators (localStorage cache + server sync). */
export function useStudy() {
  const [entries, setEntries] = useState<StudyEntry[]>(() => load());
  const hydratedRef = useRef(false);

  // On mount, hydrate from the server (cross-device source of truth) and cache
  // it locally. Falls back to the localStorage copy if the server is empty or
  // unavailable.
  useEffect(() => {
    let cancelled = false;
    (async () => {
      const remote = await fetchStudy<StudyEntry>();
      if (cancelled) return;
      hydratedRef.current = true;
      if (Array.isArray(remote) && remote.length > 0) {
        save(remote);
        setEntries(remote);
      }
    })();
    return () => {
      cancelled = true;
    };
  }, []);

  // Persist to localStorage immediately and push to the server (best-effort).
  const persist = useCallback((next: StudyEntry[]) => {
    save(next);
    // Avoid clobbering the server with an empty set before hydration completes.
    if (hydratedRef.current || next.length > 0) {
      void saveStudy(next);
    }
  }, []);

  // Mark a problem done, or — if already tracked — record a fresh review that
  // updates its FSRS memory state and reschedules it. `grade` defaults to Good.
  const markDone = useCallback(
    (p: StudyProblemRef, grade: Grade = 3) => {
      const now = Date.now();
      setEntries((prev) => {
        const existing = prev.find((e) => e.path === p.path);
        let next: StudyEntry[];
        if (existing) {
          const elapsedDays = Math.max(0, (now - existing.lastReviewedAt) / DAY_MS);
          const S = stabilityOf(existing);
          const D = difficultyOf(existing);
          const R = forgettingCurve(elapsedDays, S);
          const newS = grade === 1 ? nextForgetStability(D, S, R) : nextRecallStability(D, S, R, grade);
          const newD = nextDifficulty(D, grade);
          next = prev.map((e) =>
            e.path === p.path
              ? {
                  ...e,
                  title: p.title,
                  group: p.group ?? e.group ?? null,
                  ext: p.ext ?? e.ext ?? null,
                  lastReviewedAt: now,
                  reviewCount: e.reviewCount + 1,
                  stability: newS,
                  difficulty: newD,
                  intervalDays: intervalFromStability(newS),
                }
              : e
          );
        } else {
          const S = initStability(grade);
          const D = initDifficulty(grade);
          next = [
            ...prev,
            {
              path: p.path,
              title: p.title,
              group: p.group ?? null,
              ext: p.ext ?? null,
              markedAt: now,
              lastReviewedAt: now,
              reviewCount: 1,
              stability: S,
              difficulty: D,
              intervalDays: intervalFromStability(S),
            },
          ];
        }
        persist(next);
        return next;
      });
    },
    [persist]
  );

  const remove = useCallback(
    (path: string) => {
      setEntries((prev) => {
        const next = prev.filter((e) => e.path !== path);
        persist(next);
        return next;
      });
    },
    [persist]
  );

  const isDone = useCallback((path: string) => entries.some((e) => e.path === path), [entries]);

  return { entries, markDone, remove, isDone };
}
