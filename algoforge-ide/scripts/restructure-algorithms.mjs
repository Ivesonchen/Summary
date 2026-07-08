// Restructure a category folder so each algorithm file becomes a FOLDER
// containing a canonical `solution.<ext>` plus a `meta.json`.
//
//   Before:  Array/[4] 2 Sum.java
//   After:   Array/2 Sum/solution.java
//            Array/2 Sum/meta.json   -> { "group": 4 }
//
// The leading "[n] " is a grouping of similar problems (NOT difficulty); it is
// stripped from the folder name and preserved in meta.json as "group".
// Files without a "[n] " prefix get a folder and a meta.json with no group.
//
// Usage:
//   node scripts/restructure-algorithms.mjs "<absolute path to category folder>" [--dry-run]
//
// Idempotent-ish: skips entries that are already folders. Aborts on name
// collisions so nothing is silently overwritten.

import fs from 'node:fs';
import path from 'node:path';

const SOURCE_EXT = new Set(['.java', '.py', '.ts', '.js', '.tsx', '.jsx', '.cpp', '.c', '.go']);

const target = process.argv[2];
const dryRun = process.argv.includes('--dry-run');

if (!target) {
  console.error('Usage: node scripts/restructure-algorithms.mjs "<category folder>" [--dry-run]');
  process.exit(1);
}
const dir = path.resolve(target);
if (!fs.existsSync(dir) || !fs.statSync(dir).isDirectory()) {
  console.error(`Not a directory: ${dir}`);
  process.exit(1);
}

/** Parse "[3] Foo.java" -> { group: 3, base: "Foo", ext: ".java" }. */
function parse(fileName) {
  const ext = path.extname(fileName);
  const stem = fileName.slice(0, -ext.length);
  const m = stem.match(/^\[(\d+)\]\s*(.+)$/);
  if (m) return { group: Number(m[1]), base: m[2].trim(), ext };
  return { group: null, base: stem.trim(), ext };
}

const entries = fs.readdirSync(dir, { withFileTypes: true });
const files = entries.filter((e) => e.isFile() && SOURCE_EXT.has(path.extname(e.name).toLowerCase()));

// Collision check: two files mapping to the same folder name is fatal.
const byFolder = new Map();
for (const f of files) {
  const { base } = parse(f.name);
  if (!byFolder.has(base)) byFolder.set(base, []);
  byFolder.get(base).push(f.name);
}
const collisions = [...byFolder.entries()].filter(([, list]) => list.length > 1);
if (collisions.length) {
  console.error('Aborting - folder-name collisions detected:');
  for (const [folder, list] of collisions) console.error(`  "${folder}" <- ${list.join(', ')}`);
  process.exit(1);
}

let moved = 0;
for (const f of files) {
  const { group, base, ext } = parse(f.name);
  const destFolder = path.join(dir, base);
  const destFile = path.join(destFolder, `solution${ext}`);
  const metaFile = path.join(destFolder, 'meta.json');

  if (fs.existsSync(destFolder)) {
    console.warn(`  skip (folder exists): ${base}`);
    continue;
  }

  console.log(`  ${f.name}  ->  ${base}/solution${ext}${group != null ? `  (group ${group})` : ''}`);
  if (dryRun) continue;

  fs.mkdirSync(destFolder, { recursive: true });
  fs.renameSync(path.join(dir, f.name), destFile);
  const meta = group != null ? { group } : {};
  fs.writeFileSync(metaFile, JSON.stringify(meta, null, 2) + '\n', 'utf8');
  moved++;
}

console.log(`\n${dryRun ? '[dry-run] would move' : 'Moved'} ${dryRun ? files.length : moved} file(s) in ${path.basename(dir)}.`);
