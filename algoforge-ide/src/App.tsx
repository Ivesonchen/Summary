import { useCallback, useEffect, useState } from 'react';
import TopNavBar from './components/TopNavBar';
import FileExplorer from './components/Sidebar/FileExplorer';
import SolutionPane from './components/Editor/SolutionPane';
import PracticePane from './components/Editor/PracticePane';
import BottomPanel from './components/BottomPanel/Console';
import { extToLanguage, extToMonaco, fetchFile, fetchProblem, fetchTree, runCode } from './lib/api';
import { compareRuns } from './lib/compare';
import type {
  FileNode,
  Language,
  ProblemLanguage,
  ProblemNode,
  RunResult,
  Section,
  TestOutcome,
} from './types';

// Complete, runnable starter programs (stdin -> stdout model, like an online judge).
const STARTER: Record<Language, string> = {
  java: 'import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        // Read input from stdin and print your answer.\n        Scanner sc = new Scanner(System.in);\n        System.out.println("Hello from Java");\n    }\n}\n',
  python: 'import sys\n\ndef main():\n    data = sys.stdin.read().split()\n    print("Hello from Python")\n\nmain()\n',
  javascript:
    "const lines = require('fs').readFileSync(0, 'utf8').split('\\n');\nconsole.log('Hello from JavaScript');\n",
  typescript:
    "const input: string = require('fs').readFileSync(0, 'utf8');\nconsole.log('Hello from TypeScript');\n",
  cpp: '#include <bits/stdc++.h>\nusing namespace std;\n\nint main() {\n    // Read from cin, write to cout.\n    cout << "Hello from C++" << endl;\n    return 0;\n}\n',
  c: '#include <stdio.h>\n\nint main() {\n    printf("Hello from C\\n");\n    return 0;\n}\n',
  go: 'package main\n\nimport "fmt"\n\nfunc main() {\n    fmt.Println("Hello from Go")\n}\n',
  unknown: '// This file type cannot be executed. Study the standard solution on the left.\n',
};

export default function App() {
  const [sections, setSections] = useState<Section[]>([]);
  const [rootName, setRootName] = useState('');
  const [treeLoading, setTreeLoading] = useState(true);
  const [treeError, setTreeError] = useState<string | null>(null);

  // Selection: either a standalone problem (algorithm folder) or a loose file.
  const [selectedPath, setSelectedPath] = useState<string | null>(null);
  const [title, setTitle] = useState<string | null>(null);
  const [problemLangs, setProblemLangs] = useState<ProblemLanguage[]>([]);
  const [activeExt, setActiveExt] = useState<string | null>(null);
  const [group, setGroup] = useState<number | null>(null);

  const [solution, setSolution] = useState('');
  const [fileLoading, setFileLoading] = useState(false);
  const [solutionName, setSolutionName] = useState<string | null>(null);
  const [monacoLang, setMonacoLang] = useState('plaintext');
  const [language, setLanguage] = useState<Language>('unknown');

  const [practice, setPractice] = useState(STARTER.unknown);
  const [stdin, setStdin] = useState('');

  const [running, setRunning] = useState(false);
  const [status, setStatus] = useState<string | null>(null);
  const [practiceResult, setPracticeResult] = useState<RunResult | null>(null);
  const [testOutcome, setTestOutcome] = useState<TestOutcome | null>(null);

  useEffect(() => {
    fetchTree()
      .then((t) => {
        setSections(t.sections);
        setRootName(t.root.toUpperCase());
      })
      .catch((e) => setTreeError(e.message))
      .finally(() => setTreeLoading(false));
  }, []);

  // Load a single solution.<ext> file into the editor and reset run state.
  const loadSolution = useCallback(async (filePath: string, ext: string) => {
    setFileLoading(true);
    setPracticeResult(null);
    setTestOutcome(null);
    setStatus(null);
    const lang = extToLanguage(ext);
    setLanguage(lang);
    setActiveExt(ext);
    setMonacoLang(extToMonaco(ext));
    setPractice(STARTER[lang]);
    setSolutionName(filePath.split('/').pop() ?? null);
    try {
      const res = await fetchFile(filePath);
      setSolution(res.content);
    } catch (e) {
      setSolution(`// Failed to load file: ${(e as Error).message}`);
    } finally {
      setFileLoading(false);
    }
  }, []);

  // Selecting a problem: load its default language (prefer Java) and fetch group.
  const handleSelectProblem = useCallback(
    async (problem: ProblemNode) => {
      setSelectedPath(problem.path);
      setTitle(problem.name);
      setProblemLangs(problem.languages);
      setGroup(null);
      const preferred =
        problem.languages.find((l) => l.ext === 'java') ?? problem.languages[0];
      if (preferred) await loadSolution(preferred.path, preferred.ext);
      fetchProblem(problem.path)
        .then((p) => setGroup(p.meta?.group ?? null))
        .catch(() => setGroup(null));
    },
    [loadSolution]
  );

  // Switch language within the current problem.
  const handleSwitchLanguage = useCallback(
    (ext: string) => {
      const lang = problemLangs.find((l) => l.ext === ext);
      if (lang) loadSolution(lang.path, lang.ext);
    },
    [problemLangs, loadSolution]
  );

  // Selecting a loose (non-problem) file.
  const handleSelect = useCallback(
    async (file: FileNode) => {
      setSelectedPath(file.path);
      setTitle(file.display);
      setProblemLangs([]);
      setGroup(null);
      await loadSolution(file.path, file.ext);
    },
    [loadSolution]
  );

  const handleRun = useCallback(async () => {
    if (running) return;
    if (language === 'unknown') {
      setStatus('This file type cannot be executed by the sandbox.');
      return;
    }

    setRunning(true);
    setStatus('Submitting to sandbox…');

    const pRes = await runCode(practice, language, stdin);
    setPracticeResult(pRes);

    // Auto-compare against the standard solution when it is a complete, runnable program.
    setStatus('Comparing against standard solution…');
    const refRes = await runCode(solution, language, stdin);
    setTestOutcome(compareRuns(pRes, refRes));

    setStatus(pRes.ok ? `${pRes.statusText} · ${pRes.timeMs ?? '?'}ms` : pRes.statusText || 'Error');
    setRunning(false);
  }, [running, language, practice, stdin, solution]);

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
    <div className="h-screen flex flex-col overflow-hidden">
      <TopNavBar
        fileName={title}
        language={language}
        languages={problemLangs}
        activeExt={activeExt}
        group={group}
        onSwitchLanguage={handleSwitchLanguage}
        runnable={language !== 'unknown'}
        running={running}
        onRun={handleRun}
      />
      <main className="flex-1 flex overflow-hidden min-h-0">
        <FileExplorer
          sections={sections}
          rootName={rootName}
          selectedPath={selectedPath}
          onSelect={handleSelect}
          onSelectProblem={handleSelectProblem}
          loading={treeLoading}
          error={treeError}
        />
        <div className="flex-1 flex flex-col min-w-0">
          <div className="flex-1 flex overflow-hidden gap-panel-gap bg-outline-variant/30 min-h-0">
            <SolutionPane
              content={solution}
              monacoLang={monacoLang}
              fileName={solutionName}
              loading={fileLoading}
            />
            <PracticePane
              value={practice}
              monacoLang={monacoLang}
              onChange={setPractice}
              onReset={() => setPractice(STARTER[language])}
              onRun={handleRun}
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
    </div>
  );
}
