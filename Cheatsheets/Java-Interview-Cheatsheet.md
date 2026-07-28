# Java Coding Interview / Competitive Programming Cheatsheet

> Target: Java 8–17. Everything here is copy‑paste ready.

---

## 1. Boilerplate

### Interview (LeetCode style)
```java
class Solution {
    public int solve(int[] nums) {
        return 0;
    }
}
```

### Competitive (fast I/O — always use this, `Scanner` is ~10x slower)
```java
import java.io.*;
import java.util.*;

public class Main {
    static final int MOD = 1_000_000_007;
    public static void main(String[] args) throws IOException {
        FastReader in = new FastReader();
        StringBuilder sb = new StringBuilder();
        int t = in.nextInt();
        while (t-- > 0) {
            int n = in.nextInt();
            long[] a = new long[n];
            for (int i = 0; i < n; i++) a[i] = in.nextLong();
            sb.append(solve(n, a)).append('\n');
        }
        System.out.print(sb);              // print ONCE, never println in a loop
    }

    static long solve(int n, long[] a) { return 0; }

    static class FastReader {
        private final DataInputStream din = new DataInputStream(System.in);
        private final byte[] buf = new byte[1 << 16];
        private int ptr = 0, len = 0;
        private int read() throws IOException {
            if (ptr == len) { len = din.read(buf, 0, buf.length); ptr = 0; if (len <= 0) return -1; }
            return buf[ptr++];
        }
        int nextInt() throws IOException { return (int) nextLong(); }
        long nextLong() throws IOException {
            int c = read(); while (c <= ' ') c = read();
            boolean neg = c == '-'; if (neg) c = read();
            long x = 0; while (c > ' ') { x = x * 10 + (c - '0'); c = read(); }
            return neg ? -x : x;
        }
        String next() throws IOException {
            int c = read(); while (c <= ' ') c = read();
            StringBuilder s = new StringBuilder();
            while (c > ' ') { s.append((char) c); c = read(); }
            return s.toString();
        }
    }
}
```

### Deep recursion → run on a big-stack thread
```java
public static void main(String[] args) {
    new Thread(null, () -> { /* real main */ }, "main", 1 << 26).start();
}
```

---

## 2. Core Syntax Quick Reference

```java
// numeric limits
Integer.MAX_VALUE  // 2147483647   (~2.1e9)
Long.MAX_VALUE     // 9223372036854775807 (~9.2e18)
Integer.MIN_VALUE, Double.POSITIVE_INFINITY

// literals
long big = 1_000_000_007L;   // MUST suffix L
int hex = 0xFF, bin = 0b1010;

// integer division / modulo (careful: Java % keeps sign of dividend!)
-7 / 2 == -3      // truncates toward zero (Python gives -4)
-7 % 2 == -1      // Python gives 1
int m2 = ((a % m) + m) % m;   // safe non-negative modulo

// char arithmetic
char c = 'a'; int idx = c - 'a';
char back = (char)('a' + 3);
Character.isDigit(c); Character.isLetter(c); Character.isLetterOrDigit(c);
int d = c - '0';

// var (Java 10+)
var map = new HashMap<String, List<Integer>>();
for (var e : map.entrySet()) { e.getKey(); e.getValue(); }
```

### Overflow traps
```java
int mid = (lo + hi) / 2;          // BAD: can overflow
int mid = lo + (hi - lo) / 2;     // GOOD
long prod = (long) a * b;         // cast BEFORE multiply
Math.floorDiv(-7, 2) == -4;       // Python-like division
Math.floorMod(-7, 2) == 1;        // Python-like modulo
```

---

## 3. Arrays

```java
int[] a = new int[n];
int[] b = {1, 2, 3};
int[][] g = new int[n][m];
int[][] jag = new int[n][];        // rows allocated later

Arrays.fill(a, -1);
for (int[] row : g) Arrays.fill(row, Integer.MAX_VALUE);   // 2D fill

Arrays.sort(a);                              // primitives: dual-pivot quicksort
Arrays.sort(boxed, (x, y) -> y - x);         // objects only; use Integer[] to reverse
Arrays.sort(g, (x, y) -> x[0] != y[0] ? x[0] - y[0] : x[1] - y[1]);  // by col0 then col1
Arrays.sort(a, from, to);                    // subrange

int[] copy   = Arrays.copyOf(a, n);
int[] slice  = Arrays.copyOfRange(a, l, r);  // [l, r)
System.arraycopy(src, sp, dst, dp, len);

Arrays.toString(a);  Arrays.deepToString(g);  // debugging
Arrays.equals(a, b);
Arrays.stream(a).sum(); Arrays.stream(a).max().getAsInt();

int[] pre = new int[n + 1];
for (int i = 0; i < n; i++) pre[i + 1] = pre[i] + a[i];   // prefix sums
// sum of [l, r] = pre[r + 1] - pre[l]

// binarySearch: returns index, or (-(insertionPoint) - 1) if absent
int p = Arrays.binarySearch(a, key);
if (p < 0) p = -p - 1;   // first index > key
```

> **Trap:** `Arrays.sort(int[])` has no comparator and cannot be reversed. Box to `Integer[]`, sort ascending then reverse, or negate values.

---

## 4. Collections Cheat Table

| Need | Use | Key ops |
|---|---|---|
| dynamic array | `ArrayList<>` | `add, get, set, remove(int), size` |
| stack / queue / deque | `ArrayDeque<>` | `push/pop/peek`, `offer/poll/peek`, `addFirst/addLast` |
| hash map/set | `HashMap`, `HashSet` | O(1) avg |
| sorted map/set | `TreeMap`, `TreeSet` | `floorKey, ceilingKey, higherKey, lowerKey, firstKey, lastKey`, `subMap` |
| heap | `PriorityQueue<>` | min-heap by default |
| insertion/access order | `LinkedHashMap` | LRU via `removeEldestEntry` |
| pair | `int[]{a,b}` or `long` encode | avoid custom classes when possible |

```java
List<Integer> list = new ArrayList<>();
Collections.sort(list); Collections.sort(list, Comparator.reverseOrder());
Collections.reverse(list); Collections.swap(list, i, j);
Collections.max(list); Collections.min(list); Collections.frequency(list, x);
Collections.nCopies(n, 0);

// List <-> array
Integer[] arr = list.toArray(new Integer[0]);
int[] prim = list.stream().mapToInt(Integer::intValue).toArray();
List<Integer> back = Arrays.stream(prim).boxed().collect(Collectors.toList());
List<int[]> res = new ArrayList<>();
int[][] out = res.toArray(new int[0][]);
```

> **Trap:** `List<Integer>.remove(int)` removes by **index**, `remove(Object)` by value → `list.remove(Integer.valueOf(x))`.
> **Trap:** `Integer == Integer` only works for cached −128..127. Use `.equals()` or unbox.

### HashMap idioms
```java
Map<Integer, Integer> cnt = new HashMap<>();
cnt.merge(x, 1, Integer::sum);                          // counter (best)
cnt.put(x, cnt.getOrDefault(x, 0) + 1);                 // equivalent
cnt.computeIfAbsent(k, z -> new ArrayList<>()).add(v);  // multimap
cnt.putIfAbsent(k, v);
for (Map.Entry<Integer,Integer> e : cnt.entrySet()) {}
cnt.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
```

### Deque as stack & queue (prefer over `Stack` / `LinkedList`)
```java
Deque<Integer> st = new ArrayDeque<>();
st.push(x); st.pop(); st.peek(); st.isEmpty();       // stack (front)
Deque<Integer> q = new ArrayDeque<>();
q.offer(x); q.poll(); q.peek();                      // queue (FIFO)
Deque<Integer> dq = new ArrayDeque<>();
dq.offerLast(x); dq.pollFirst(); dq.peekLast();      // monotonic deque
```
> `ArrayDeque` forbids `null`. `Stack` is synchronized/legacy — avoid.

### PriorityQueue
```java
PriorityQueue<Integer> min = new PriorityQueue<>();
PriorityQueue<Integer> max = new PriorityQueue<>(Comparator.reverseOrder());
PriorityQueue<int[]> pq = new PriorityQueue<>((x, y) -> x[1] - y[1]);          // by 2nd field
PriorityQueue<int[]> pq2 = new PriorityQueue<>(Comparator.comparingInt(x -> x[1]));
pq.offer(v); pq.poll(); pq.peek(); pq.size();
// O(n) heapify: new PriorityQueue<>(collection)
```
> **Trap:** PQ iteration order is NOT sorted; `remove(o)` is O(n).
> **Trap:** `(x, y) -> x - y` overflows — use `Integer.compare(x, y)`.

### TreeMap / TreeSet (Java's edge over Python — no built-in sorted container there)
```java
TreeMap<Integer, Integer> tm = new TreeMap<>();
tm.floorKey(x);    // greatest key <= x
tm.ceilingKey(x);  // smallest key >= x
tm.lowerKey(x);    // strictly <
tm.higherKey(x);   // strictly >
tm.firstKey(); tm.lastKey(); tm.pollFirstEntry(); tm.pollLastEntry();
tm.headMap(x, true); tm.tailMap(x, false); tm.subMap(lo, true, hi, false);
tm.descendingMap();

TreeSet<Integer> ts = new TreeSet<>();   // floor/ceiling/higher/lower/first/last
// multiset: TreeMap<T,Integer>, merge(+1); on removal decrement and remove at 0
```

---

## 5. Strings

```java
String s = "abc";
s.charAt(i); s.length(); s.substring(l, r);          // [l, r)
s.indexOf("x"); s.lastIndexOf('x'); s.contains("ab");
s.startsWith(p); s.endsWith(p); s.isEmpty();
s.toCharArray(); new String(charArr);
s.split("\\s+"); String.join(",", list);
s.replace('a','b'); s.trim(); s.repeat(3);
s.compareTo(t);  // lexicographic
Integer.parseInt(s); Long.parseLong(s); String.valueOf(num);
Integer.toBinaryString(n); Integer.parseInt(s, 2);

// StringBuilder — ALWAYS use for building (String concat in loop = O(n^2))
StringBuilder sb = new StringBuilder();
sb.append(x).append(',');
sb.setCharAt(i, c); sb.charAt(i); sb.deleteCharAt(sb.length() - 1);
sb.insert(0, c); sb.reverse(); sb.setLength(0);   // clear
sb.toString();

// char-count array (26 letters) — faster than HashMap
int[] cnt = new int[26];
for (char c : s.toCharArray()) cnt[c - 'a']++;

// anagram key
char[] k = s.toCharArray(); Arrays.sort(k); String key = new String(k);
```

---

## 6. Math / Bit Tricks

```java
Math.abs, max, min, pow, sqrt, ceil, floor, round;
(a + b - 1) / b                 // int ceil-div for a,b > 0
Math.floorDiv(a, b); Math.floorMod(a, b);
long gcd(long a, long b) { return b == 0 ? a : gcd(b, a % b); }
long lcm = a / gcd(a, b) * b;   // divide first to avoid overflow

// bits
n & 1                    // odd?
n >> 1, n << 1           // /2, *2   (>>> = unsigned shift)
n & (n - 1)              // clear lowest set bit
n & (-n)                 // isolate lowest set bit
(n & (n - 1)) == 0       // power of two (n > 0)
Integer.bitCount(n); Long.bitCount(n);
Integer.highestOneBit(n); Integer.numberOfTrailingZeros(n);
mask ^ (1 << i)          // toggle bit i
(mask >> i) & 1          // read bit i
for (int sub = mask; sub > 0; sub = (sub - 1) & mask) {}   // enumerate submasks

// modpow / modular inverse
long power(long b, long e, long m) {
    long r = 1; b %= m;
    while (e > 0) { if ((e & 1) == 1) r = r * b % m; b = b * b % m; e >>= 1; }
    return r;
}
long inv(long a, long p) { return power(a, p - 2, p); }  // p prime

// sieve
boolean[] comp = new boolean[n + 1];
for (int i = 2; (long) i * i <= n; i++)
    if (!comp[i]) for (int j = i * i; j <= n; j += i) comp[j] = true;
```

---

## 7. Algorithm Templates

### Binary search (lower bound)
```java
int lo = 0, hi = n;                  // search space [0, n)
while (lo < hi) {
    int mid = lo + (hi - lo) / 2;
    if (ok(mid)) hi = mid; else lo = mid + 1;
}
return lo;                            // first index where ok() is true
```

### Binary search on answer
```java
long lo = 1, hi = 1_000_000_000L, ans = -1;
while (lo <= hi) {
    long mid = lo + (hi - lo) / 2;
    if (feasible(mid)) { ans = mid; hi = mid - 1; } else lo = mid + 1;
}
```

### Two pointers / sliding window (longest valid)
```java
int l = 0, best = 0;
for (int r = 0; r < n; r++) {
    add(a[r]);
    while (invalid()) remove(a[l++]);
    best = Math.max(best, r - l + 1);
}
```

### Monotonic stack (next greater element)
```java
int[] nge = new int[n];
Arrays.fill(nge, -1);
Deque<Integer> st = new ArrayDeque<>();
for (int i = 0; i < n; i++) {
    while (!st.isEmpty() && a[st.peek()] < a[i]) nge[st.pop()] = i;
    st.push(i);
}
```

### BFS on grid
```java
int[][] DIR = {{1,0},{-1,0},{0,1},{0,-1}};
Deque<int[]> q = new ArrayDeque<>();
boolean[][] vis = new boolean[n][m];
q.offer(new int[]{sr, sc}); vis[sr][sc] = true;
int dist = 0;
while (!q.isEmpty()) {
    for (int sz = q.size(); sz > 0; sz--) {          // level order
        int[] cur = q.poll();
        for (int[] d : DIR) {
            int nr = cur[0] + d[0], nc = cur[1] + d[1];
            if (nr < 0 || nr >= n || nc < 0 || nc >= m || vis[nr][nc] || grid[nr][nc] == '#') continue;
            vis[nr][nc] = true;
            q.offer(new int[]{nr, nc});
        }
    }
    dist++;
}
```

### DFS + backtracking (subsets, dedup)
```java
void dfs(int start, List<Integer> path, List<List<Integer>> res) {
    res.add(new ArrayList<>(path));           // copy!
    for (int i = start; i < n; i++) {
        if (i > start && a[i] == a[i - 1]) continue;   // skip dups (sorted input)
        path.add(a[i]);
        dfs(i + 1, path, res);
        path.remove(path.size() - 1);
    }
}
```

### Dijkstra
```java
long[] dist = new long[n];
Arrays.fill(dist, Long.MAX_VALUE); dist[s] = 0;
PriorityQueue<long[]> pq = new PriorityQueue<>((x, y) -> Long.compare(x[1], y[1]));
pq.offer(new long[]{s, 0});
while (!pq.isEmpty()) {
    long[] cur = pq.poll();
    int u = (int) cur[0];
    if (cur[1] > dist[u]) continue;                 // stale entry
    for (int[] e : adj.get(u)) {                    // e = {v, w}
        if (dist[u] + e[1] < dist[e[0]]) {
            dist[e[0]] = dist[u] + e[1];
            pq.offer(new long[]{e[0], dist[e[0]]});
        }
    }
}
```

### Union-Find (path compression + union by size)
```java
static int[] p, sz;
static void init(int n) { p = new int[n]; sz = new int[n];
    for (int i = 0; i < n; i++) { p[i] = i; sz[i] = 1; } }
static int find(int x) { while (p[x] != x) { p[x] = p[p[x]]; x = p[x]; } return x; }
static boolean union(int a, int b) {
    a = find(a); b = find(b);
    if (a == b) return false;
    if (sz[a] < sz[b]) { int t = a; a = b; b = t; }
    p[b] = a; sz[a] += sz[b];
    return true;
}
```

### Topological sort (Kahn)
```java
int[] indeg = new int[n];
for (int u = 0; u < n; u++) for (int v : adj.get(u)) indeg[v]++;
Deque<Integer> q = new ArrayDeque<>();
for (int i = 0; i < n; i++) if (indeg[i] == 0) q.offer(i);
List<Integer> order = new ArrayList<>();
while (!q.isEmpty()) {
    int u = q.poll(); order.add(u);
    for (int v : adj.get(u)) if (--indeg[v] == 0) q.offer(v);
}
if (order.size() != n) { /* cycle exists */ }
```

### Memoized DP
```java
Integer[][] memo = new Integer[n][k + 1];     // null = uncomputed
int f(int i, int j) {
    if (base) return val;
    if (memo[i][j] != null) return memo[i][j];
    return memo[i][j] = Math.min(f(i - 1, j), f(i, j - 1) + cost);
}
```

### Fenwick (BIT), 0-indexed API
```java
static long[] bit; static int N;
static void upd(int i, long v) { for (i++; i <= N; i += i & -i) bit[i] += v; }
static long qry(int i) { long s = 0; for (i++; i > 0; i -= i & -i) s += bit[i]; return s; }
// range sum [l, r] = qry(r) - qry(l - 1)
```

### Trie
```java
class Trie {
    Trie[] ch = new Trie[26];
    boolean end;
    void insert(String s) {
        Trie cur = this;
        for (char c : s.toCharArray()) {
            int i = c - 'a';
            if (cur.ch[i] == null) cur.ch[i] = new Trie();
            cur = cur.ch[i];
        }
        cur.end = true;
    }
}
```

### LinkedList / Tree nodes
```java
class ListNode { int val; ListNode next; ListNode(int v) { val = v; } }
class TreeNode { int val; TreeNode left, right; TreeNode(int v) { val = v; } }

// reverse list
ListNode prev = null, cur = head;
while (cur != null) { ListNode nx = cur.next; cur.next = prev; prev = cur; cur = nx; }

// dummy head for deletions
ListNode dummy = new ListNode(0); dummy.next = head;

// fast/slow pointers (middle & cycle detection)
ListNode slow = head, fast = head;
while (fast != null && fast.next != null) { slow = slow.next; fast = fast.next.next; }

// iterative inorder
Deque<TreeNode> st = new ArrayDeque<>();
TreeNode cur = root;
while (cur != null || !st.isEmpty()) {
    while (cur != null) { st.push(cur); cur = cur.left; }
    cur = st.pop();
    visit(cur);
    cur = cur.right;
}
```

---

## 8. Comparators (top source of bugs)

```java
Comparator.comparingInt((int[] x) -> x[0])
Comparator.comparingInt((int[] x) -> x[0]).thenComparingInt(x -> x[1])
Comparator.comparingInt((int[] x) -> x[0]).reversed()
Comparator.comparing(Person::getName)
(a, b) -> Integer.compare(a, b)      // safe (no overflow)
(a, b) -> Long.compare(a, b)
(a, b) -> b[1] - a[1]                // only for small non-negative ints
```
> A comparator must be transitive & consistent or you get
> `IllegalArgumentException: Comparison method violates its general contract!`

---

## 9. Encoding Tricks

```java
// pair (r, c) -> single int, for HashSet
int key = r * m + c;              int r2 = key / m, c2 = key % m;
// two ints -> long
long k = ((long) a << 32) | (b & 0xFFFFFFFFL);
int a2 = (int)(k >> 32), b2 = (int) k;
// string key (slower but easy)
String sk = r + "," + c;
```

---

## 10. Complexity Budget (~1s)

| n | Acceptable |
|---|---|
| ≤ 10 | O(n!) |
| ≤ 20 | O(2ⁿ · n) bitmask DP |
| ≤ 500 | O(n³) |
| ≤ 5 000 | O(n²) |
| ≤ 10⁵ | O(n log n) |
| ≤ 10⁷ | O(n) |

Java ≈ 10⁸ simple ops/s (roughly 1–2x slower than C++).

---

## 11. Java Gotchas Checklist

- [ ] `int` overflow → use `long` (`(long) a * b`)
- [ ] `mid = lo + (hi - lo) / 2`
- [ ] `Integer` boxing equality → `.equals()` / unbox
- [ ] `list.remove(int)` removes by index
- [ ] `ArrayDeque` rejects `null`
- [ ] Comparator subtraction overflow → `Integer.compare`
- [ ] String concat in loop → `StringBuilder`
- [ ] `%` can be negative → `Math.floorMod`
- [ ] `Arrays.asList(intArray)` gives `List<int[]>` of size 1
- [ ] `split` takes a **regex**: `split("\\.")`, `split("\\|")`
- [ ] Forgetting to copy `path` before adding to results
- [ ] Recursion depth ~10 000 frames → iterative or big-stack thread
- [ ] `Scanner` on 10⁵+ lines → TLE
