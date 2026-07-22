import { useCallback, useEffect, useRef, useState } from 'react';
import TopNavBar from './components/TopNavBar';
import FileExplorer from './components/Sidebar/FileExplorer';
import SolutionPane from './components/Editor/SolutionPane';
import PracticePane from './components/Editor/PracticePane';
import BottomPanel from './components/BottomPanel/Console';
import CreateDialog from './components/CreateDialog';
import GitHubDialog from './components/GitHubDialog';
import { extToLanguage, extToMonaco, fetchFile, fetchProblem, fetchTree, runCode, saveSolution, saveVariantSolution } from './lib/api';
import { compareRuns } from './lib/compare';
import type {
  FileNode,
  Language,
  ProblemNode,
  RunResult,
  Section,
  TestOutcome,
  TreeNode,
} from './types';

// Complete, runnable starter programs (stdin -> stdout model, like an online judge).
const STARTER: Record<Language, string> = {
  java: 'import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        // Read input from stdin and print your answer.\n        Scanner sc = new Scanner(System.in);\n        System.out.println("Hello from Java");\n    }\n}\n',
  python: 'import sys\n\ndef main():\n    data = sys.stdin.read().split()\n    print("Hello from Python")\n\nmain()\n',
  javascript:
    "const lines = require('fs').readFileSync(0, 'utf8').split('\\n');\nconsole.log('Hello from JavaScript');\n",
  typescript:
    "declare function require(name: string): any;\n\nconst input: string = require('fs').readFileSync(0, 'utf8');\nconsole.log('Hello from TypeScript');\n",
  cpp: '#include <bits/stdc++.h>\nusing namespace std;\n\nint main() {\n    // Read from cin, write to cout.\n    cout << "Hello from C++" << endl;\n    return 0;\n}\n',
  c: '#include <stdio.h>\n\nint main() {\n    printf("Hello from C\\n");\n    return 0;\n}\n',
  go: 'package main\n\nimport "fmt"\n\nfunc main() {\n    fmt.Println("Hello from Go")\n}\n',
  unknown: '// This file type cannot be executed. Study the standard solution on the left.\n',
};

// Languages the sandbox can run and save (extensions the playground can switch between).
const SUPPORTED_EXTS = ['java', 'py', 'js', 'ts', 'cpp', 'c', 'go'];

/** Find a problem node by its path anywhere in the sections tree. */
function findProblem(sections: Section[], targetPath: string): ProblemNode | null {
  const walk = (nodes: TreeNode[]): ProblemNode | null => {
    for (const node of nodes) {
      if (node.type === 'problem' && node.path === targetPath) return node;
      if (node.type === 'folder') {
        const hit = walk(node.children);
        if (hit) return hit;
      }
    }
    return null;
  };
  for (const s of sections) {
    const hit = walk(s.children);
    if (hit) return hit;
  }
  return null;
}

export default function App() {
  const [sections, setSections] = useState<Section[]>([]);
  const [rootName, setRootName] = useState('');
  const [treeLoading, setTreeLoading] = useState(true);
  const [treeError, setTreeError] = useState<string | null>(null);

  // Selection: either a standalone problem (algorithm folder) or a loose file.
  const [selectedPath, setSelectedPath] = useState<string | null>(null);
  const [title, setTitle] = useState<string | null>(null);
  const [group, setGroup] = useState<number | null>(null);

  const [solution, setSolution] = useState('');
  const [solutionOriginal, setSolutionOriginal] = useState('');
  const [fileLoading, setFileLoading] = useState(false);
  const [solutionName, setSolutionName] = useState<string | null>(null);
  const [activeSolutionPath, setActiveSolutionPath] = useState<string | null>(null);
  const [solutionDirty, setSolutionDirty] = useState(false);
  const [savingSolution, setSavingSolution] = useState(false);
  const [savingVariant, setSavingVariant] = useState(false);
  const [monacoLang, setMonacoLang] = useState('plaintext');
  const [language, setLanguage] = useState<Language>('unknown');

  // Right (playground) pane has its own language, independent of the left solution.
  const [practice, setPractice] = useState(STARTER.unknown);
  const [practiceExt, setPracticeExt] = useState<string | null>(null);
  const [practiceLang, setPracticeLang] = useState<Language>('unknown');
  const [practiceMonaco, setPracticeMonaco] = useState('plaintext');
  const [stdin, setStdin] = useState('');

  const [running, setRunning] = useState(false);
  const [status, setStatus] = useState<string | null>(null);
  const [practiceResult, setPracticeResult] = useState<RunResult | null>(null);
  const [testOutcome, setTestOutcome] = useState<TestOutcome | null>(null);

  const [activeProblem, setActiveProblem] = useState<ProblemNode | null>(null);
  const [createOpen, setCreateOpen] = useState(false);
  const [githubOpen, setGithubOpen] = useState(false);
  const [navOpen, setNavOpen] = useState(false); // mobile sidebar drawer

  // Adjustable split between the two editor panes (left width %, desktop only).
  const editorRowRef = useRef<HTMLDivElement>(null);
  const splitResizingRef = useRef(false);
  const [leftPct, setLeftPct] = useState<number>(() => {
    const saved = Number(localStorage.getItem('editorSplit'));
    return saved >= 20 && saved <= 80 ? saved : 50;
  });

  const onSplitMove = useCallback((e: MouseEvent) => {
    if (!splitResizingRef.current || !editorRowRef.current) return;
    const rect = editorRowRef.current.getBoundingClientRect();
    const pct = ((e.clientX - rect.left) / rect.width) * 100;
    setLeftPct(Math.min(80, Math.max(20, pct)));
  }, []);

  const stopSplit = useCallback(() => {
    if (!splitResizingRef.current) return;
    splitResizingRef.current = false;
    document.body.style.cursor = '';
    document.body.style.userSelect = '';
    setLeftPct((p) => {
      localStorage.setItem('editorSplit', String(Math.round(p)));
      return p;
    });
  }, []);

  useEffect(() => {
    window.addEventListener('mousemove', onSplitMove);
    window.addEventListener('mouseup', stopSplit);
    return () => {
      window.removeEventListener('mousemove', onSplitMove);
      window.removeEventListener('mouseup', stopSplit);
    };
  }, [onSplitMove, stopSplit]);

  const startSplit = () => {
    splitResizingRef.current = true;
    document.body.style.cursor = 'col-resize';
    document.body.style.userSelect = 'none';
  };

  const refreshTree = useCallback(async (): Promise<Section[]> => {
    const t = await fetchTree();
    setSections(t.sections);
    setRootName(t.root.toUpperCase());
    return t.sections;
  }, []);

  useEffect(() => {
    refreshTree()
      .catch((e) => setTreeError(e.message))
      .finally(() => setTreeLoading(false));
  }, [refreshTree]);

  // Load a single solution.<ext> file into the LEFT (solution) pane and reset run state.
  const loadSolution = useCallback(async (filePath: string, ext: string) => {
    setFileLoading(true);
    setPracticeResult(null);
    setTestOutcome(null);
    setStatus(null);
    const lang = extToLanguage(ext);
    setLanguage(lang);
    setMonacoLang(extToMonaco(ext));
    setSolutionName(filePath.split('/').pop() ?? null);
    setActiveSolutionPath(filePath);
    setSolutionDirty(false);
    try {
      const res = await fetchFile(filePath);
      setSolution(res.content);
      setSolutionOriginal(res.content);
    } catch (e) {
      const msg = `// Failed to load file: ${(e as Error).message}`;
      setSolution(msg);
      setSolutionOriginal(msg);
    } finally {
      setFileLoading(false);
    }
  }, []);

  // Set the RIGHT (playground) pane language and load a fresh starter for it.
  const applyPracticeLanguage = useCallback((ext: string) => {
    const lang = extToLanguage(ext);
    setPracticeExt(ext);
    setPracticeLang(lang);
    setPracticeMonaco(extToMonaco(ext));
    setPractice(STARTER[lang]);
  }, []);

  // Selecting a problem: load its default language (prefer Java) and fetch group.
  const handleSelectProblem = useCallback(
    async (problem: ProblemNode) => {
      setSelectedPath(problem.path);
      setTitle(problem.name);
      setActiveProblem(problem);
      setGroup(problem.group ?? null);
      const preferred =
        problem.languages.find((l) => l.ext === 'java') ?? problem.languages[0];
      if (preferred) {
        await loadSolution(preferred.path, preferred.ext);
        applyPracticeLanguage(preferred.ext);
      }
      fetchProblem(problem.path)
        .then((p) => setGroup(p.meta?.group ?? null))
        .catch(() => {});
    },
    [loadSolution, applyPracticeLanguage]
  );

  // Revert unsaved edits in the LEFT (solution) pane back to the loaded file.
  const handleResetSolution = useCallback(() => {
    setSolution(solutionOriginal);
    setSolutionDirty(false);
  }, [solutionOriginal]);

  // Save edits to the currently-open solution file (left pane), overwriting it.
  const handleSaveSolution = useCallback(async () => {
    if (!activeSolutionPath || !activeProblem || savingSolution) return;
    setSavingSolution(true);
    setStatus('Saving solution…');
    try {
      await saveSolution({ path: activeSolutionPath, content: solution });
      setSolutionDirty(false);
      setSolutionOriginal(solution);
      setStatus(`Saved ${activeSolutionPath.split('/').pop()}`);
    } catch (e) {
      setStatus(`Save failed: ${(e as Error).message}`);
    } finally {
      setSavingSolution(false);
    }
  }, [activeSolutionPath, activeProblem, solution, savingSolution]);

  // Switch the RIGHT (playground) pane language (resets to that language's starter).
  const handleSwitchPracticeLanguage = useCallback(
    (ext: string) => {
      applyPracticeLanguage(ext);
    },
    [applyPracticeLanguage]
  );

  // Save the playground code as a new named-variant solution to the problem.
  const handleSavePractice = useCallback(async () => {
    if (!activeProblem || !practiceExt || savingVariant) return;
    const name = window.prompt(
      'Save playground as a new solution. Enter a short name (letters, numbers, dashes):',
      'v2'
    );
    if (name == null) return; // cancelled
    setSavingVariant(true);
    setStatus('Saving new solution…');
    try {
      const { solutionPath } = await saveVariantSolution({
        problemPath: activeProblem.path,
        ext: practiceExt,
        variant: name,
        content: practice,
      });
      const next = await refreshTree();
      const found = findProblem(next, activeProblem.path);
      if (found) {
        setActiveProblem(found);
      }
      await loadSolution(solutionPath, practiceExt);
      setStatus(`Saved ${solutionPath.split('/').pop()}`);
    } catch (e) {
      setStatus(`Save failed: ${(e as Error).message}`);
    } finally {
      setSavingVariant(false);
    }
  }, [activeProblem, practiceExt, practice, savingVariant, refreshTree, loadSolution]);

  // Selecting a loose (non-problem) file.
  const handleSelect = useCallback(
    async (file: FileNode) => {
      setSelectedPath(file.path);
      setTitle(file.display);
      setActiveProblem(null);
      setGroup(null);
      await loadSolution(file.path, file.ext);
      applyPracticeLanguage(file.ext);
    },
    [loadSolution, applyPracticeLanguage]
  );

  // After creating a problem or solution: refresh the tree and select the result.
  const handleCreated = useCallback(
    async (createdPath: string, kind: 'problem' | 'solution') => {
      const problemPath = kind === 'problem' ? createdPath : createdPath;
      const next = await refreshTree();
      const found = findProblem(next, problemPath);
      if (found) await handleSelectProblem(found);
    },
    [refreshTree, handleSelectProblem]
  );

  // Run the code from either pane: 'practice' (right playground) or 'solution' (left).
  const handleRun = useCallback(
    async (source: 'practice' | 'solution' = 'practice') => {
      if (running) return;

      const code = source === 'solution' ? solution : practice;
      const lang = source === 'solution' ? language : practiceLang;
      if (lang === 'unknown') {
        setStatus('This language cannot be executed by the sandbox.');
        return;
      }

      setRunning(true);
      setStatus('Submitting to sandbox…');

      const res = await runCode(code, lang, stdin);
      setPracticeResult(res);

      // Auto-compare only when running the playground against a same-language solution.
      if (source === 'practice' && language !== 'unknown' && language === practiceLang) {
        setStatus('Comparing against standard solution…');
        const refRes = await runCode(solution, language, stdin);
        setTestOutcome(compareRuns(res, refRes));
      } else {
        setTestOutcome(null);
      }

      setStatus(res.ok ? `${res.statusText} · ${res.timeMs ?? '?'}ms` : res.statusText || 'Error');
      setRunning(false);
    },
    [running, practiceLang, language, practice, stdin, solution]
  );

  // Global Ctrl/Cmd+R shortcut.
  useEffect(() => {
    const onKey = (e: KeyboardEvent) => {
      if ((e.metaKey || e.ctrlKey) && e.key.toLowerCase() === 'r') {
        e.preventDefault();
        handleRun();
      }
    };
    window.addEventListener('keydown', onKey);
    return () => window.removeEventListener('keydown', onKey);
  }, [handleRun]);

  return (
    <div className="h-[100dvh] flex flex-col overflow-hidden">
      <TopNavBar
        fileName={title}
        group={group}
        onToggleNav={() => setNavOpen((v) => !v)}
      />
      <main className="flex-1 flex overflow-hidden min-h-0 relative">
        <FileExplorer
          sections={sections}
          rootName={rootName}
          selectedPath={selectedPath}
          onSelect={handleSelect}
          onSelectProblem={handleSelectProblem}
          onCreate={() => setCreateOpen(true)}
          onSync={() => setGithubOpen(true)}
          loading={treeLoading}
          error={treeError}
          open={navOpen}
          onClose={() => setNavOpen(false)}
        />
        <div className="flex-1 flex flex-col min-w-0">
          <div
            ref={editorRowRef}
            className="flex-1 flex flex-col md:flex-row overflow-y-auto md:overflow-hidden gap-panel-gap bg-outline-variant/30 min-h-0"
          >
            <div
              className="flex w-full md:w-[var(--editor-split)] md:min-w-0 min-h-[60vh] md:min-h-0"
              style={{ ['--editor-split' as string]: `${leftPct}%` }}
            >
              <SolutionPane
                content={solution}
                monacoLang={monacoLang}
                fileName={solutionName}
                loading={fileLoading}
                onChange={(v) => {
                  setSolution(v);
                  setSolutionDirty(true);
                }}
                onReset={handleResetSolution}
                onSave={handleSaveSolution}
                onRun={() => handleRun('solution')}
                runnable={language !== 'unknown'}
                running={running}
                canSave={activeProblem != null}
                dirty={solutionDirty}
                saving={savingSolution}
                chatContext={{
                  title,
                  language,
                  solution,
                  practice,
                }}
              />
            </div>
            {/* Draggable divider between the two editor panes (desktop only). */}
            <div
              onMouseDown={startSplit}
              onDoubleClick={() => {
                setLeftPct(50);
                localStorage.setItem('editorSplit', '50');
              }}
              role="separator"
              aria-orientation="vertical"
              title="Drag to resize · double-click to reset"
              className="hidden md:block w-1 shrink-0 cursor-col-resize hover:bg-primary/40 active:bg-primary/60 transition-colors"
            />
            <PracticePane
              value={practice}
              monacoLang={practiceMonaco}
              languages={SUPPORTED_EXTS}
              activeExt={practiceExt}
              onSwitchLanguage={handleSwitchPracticeLanguage}
              onChange={setPractice}
              onReset={() => setPractice(STARTER[practiceLang])}
              onRun={() => handleRun('practice')}
              onSave={handleSavePractice}
              runnable={practiceLang !== 'unknown'}
              running={running}
              canSave={activeProblem != null && practiceLang !== 'unknown'}
              saving={savingVariant}
            />
          </div>
          <BottomPanel
            stdin={stdin}
            onStdinChange={setStdin}
            practiceResult={practiceResult}
            testOutcome={testOutcome}
            status={status}
            running={running}
          />
        </div>
      </main>
      {createOpen && (
        <CreateDialog
          sections={sections}
          activeProblem={activeProblem}
          onClose={() => setCreateOpen(false)}
          onCreated={handleCreated}
        />
      )}
      {githubOpen && <GitHubDialog onClose={() => setGithubOpen(false)} />}
    </div>
  );
}
