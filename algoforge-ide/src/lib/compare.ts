import type { RunResult, TestOutcome } from '../types';

/** Normalize output text for comparison (trim + strip trailing whitespace per line). */
function normalize(text: string): string {
  return text
    .replace(/\r\n/g, '\n')
    .split('\n')
    .map((l) => l.replace(/\s+$/, ''))
    .join('\n')
    .trim();
}

/**
 * Compare a practice run against a reference (standard-solution) run by stdout.
 * The reference only counts if it actually ran and produced output — many repo
 * files are bare `Solution` classes with no `main`, so comparison is skipped there.
 */
export function compareRuns(practice: RunResult, reference: RunResult): TestOutcome {
  if (!reference.ok || !reference.stdout.trim()) {
    return {
      status: 'skipped',
      message:
        'No runnable reference output. The standard file may not be a complete program (e.g. a bare Solution class).',
    };
  }
  if (!practice.ok) {
    return { status: 'error', message: `Your code did not run cleanly: ${practice.statusText}` };
  }

  const expected = normalize(reference.stdout);
  const actual = normalize(practice.stdout);
  if (expected === actual) {
    return { status: 'pass', message: 'Output matches the standard solution.', expected, actual };
  }
  return { status: 'fail', message: 'Output differs from the standard solution.', expected, actual };
}
