# Python Coding Interview / Competitive Programming Cheatsheet

> Target: Python 3.8+ (CPython / PyPy). Everything here is copy‑paste ready.

---

## 1. Boilerplate

### Interview (LeetCode style)
```python
from typing import List, Optional

class Solution:
    def solve(self, nums: List[int]) -> int:
        return 0
```

### Competitive (fast I/O — `input()` is slow, `sys.stdin` is not)
```python
import sys
from collections import defaultdict, deque, Counter
from heapq import heappush, heappop, heapify
from bisect import bisect_left, bisect_right, insort
from functools import lru_cache, cache, cmp_to_key, reduce
from itertools import accumulate, permutations, combinations, product

input = sys.stdin.readline          # NOTE: keeps trailing '\n' -> use .strip()
def ints(): return list(map(int, input().split()))

def main():
    out = []
    t = int(input())
    for _ in range(t):
        n = int(input())
        a = ints()
        out.append(str(solve(n, a)))
    sys.stdout.write('\n'.join(out) + '\n')   # print ONCE

def solve(n, a):
    return 0

main()
```

### Read everything at once (fastest)
```python
data = sys.stdin.buffer.read().split()
it = iter(data)
n = int(next(it))
a = [int(next(it)) for _ in range(n)]
```

### Recursion limit
```python
sys.setrecursionlimit(300000)     # default is only 1000!
# threading trick for very deep recursion:
import threading
threading.stack_size(1 << 26)
threading.Thread(target=main).start()
```

---

## 2. Core Syntax Quick Reference

```python
# numbers: unbounded ints, no overflow ever
float('inf'), float('-inf'), math.inf
divmod(7, 2)          # (3, 1)
-7 // 2               # -4  (floors, unlike Java/C++)
-7 % 2                # 1   (always sign of divisor)
int(-3.7)             # -3  (truncates)
round(2.5)            # 2   (banker's rounding!)  -> use math.floor(x + 0.5)
10 ** 18              # exact
pow(b, e, m)          # fast modpow, built-in
abs(x), min(a, b), max(a, b), sum(it)

# chars
ord('a'), chr(97), ord(c) - ord('a')
'a' <= c <= 'z', c.isdigit(), c.isalpha(), c.isalnum(), c.lower()

# swap / multiple assign / chained compare
a, b = b, a
x = y = 0
if 0 <= i < n: ...

# ternary, walrus
v = a if cond else b
while (line := input().strip()):
    ...

# unpacking
first, *rest = arr
a, b = pair
```

---

## 3. Lists

```python
a = [0] * n                     # 1D
g = [[0] * m for _ in range(n)] # 2D — NEVER [[0]*m]*n (shared rows!)
dp = [[[0]*k for _ in range(m)] for _ in range(n)]

a.append(x); a.pop(); a.pop(0)      # pop(0) is O(n) — use deque
a.insert(i, x)                      # O(n)
a.remove(val)                       # first occurrence, O(n)
a.extend(b); a += b
a.index(val); a.count(val)
a.sort(); a.sort(reverse=True); a.sort(key=lambda x: (x[0], -x[1]))
b = sorted(a, key=..., reverse=True)      # returns new list
a.reverse(); b = a[::-1]
len(a), sum(a), max(a), min(a), any(a), all(a)

# slicing (never raises)
a[l:r]      # [l, r)
a[::-1]     # reversed copy
a[::2]      # every 2nd
a[-3:]      # last 3
a[l:r] = [] # delete range in place

# comprehensions
sq   = [x * x for x in a if x > 0]
flat = [x for row in g for x in row]
pos  = [i for i, x in enumerate(a) if x == target]
grid = [list(input().strip()) for _ in range(n)]

# handy builtins
enumerate(a, start=1)
zip(a, b)                       # stops at shortest
list(zip(*g))                   # TRANSPOSE a 2D list
reversed(a); range(n-1, -1, -1)
list(accumulate(a))             # prefix sums
list(accumulate(a, initial=0))  # 3.8+: length n+1, cleaner
```

> **Trap:** `[[0]*m]*n` makes n references to the SAME row.
> **Trap:** `list.pop(0)` / `insert(0, x)` are O(n) → use `collections.deque`.
> **Trap:** mutating a list while iterating it.

---

## 4. Data Structures Cheat Table

| Need | Use | Notes |
|---|---|---|
| dynamic array / stack | `list` | `append` / `pop` are O(1) |
| queue / deque | `collections.deque` | `appendleft/popleft` O(1) |
| hash map / set | `dict`, `set` | dicts keep insertion order (3.7+) |
| default map | `defaultdict(int/list/set)` | |
| counter | `collections.Counter` | `most_common(k)` |
| min-heap | `heapq` | max-heap → push negatives |
| sorted array ops | `bisect` | `insort` is O(n) insert |
| sorted container | `sortedcontainers.SortedList` | not in stdlib; LeetCode has it, most judges don't |
| immutable key | `tuple`, `frozenset` | hashable |

### dict / defaultdict / Counter
```python
d = {}
d[k] = d.get(k, 0) + 1
d.setdefault(k, []).append(v)
d.pop(k, None); k in d; d.keys(); d.values(); d.items()
{v: k for k, v in d.items()}                     # invert
max(d, key=d.get)                                # key with max value
sorted(d.items(), key=lambda kv: (-kv[1], kv[0]))

from collections import defaultdict
cnt   = defaultdict(int);  cnt[x] += 1
adj   = defaultdict(list); adj[u].append(v)
seen  = defaultdict(set)

from collections import Counter
c = Counter(a)                 # or Counter(s) for chars
c.most_common(3)               # [(val, freq), ...]
c1 - c2, c1 + c2, c1 & c2      # multiset arithmetic
list(c.elements())
c == Counter(t)                # anagram check
```

### deque
```python
from collections import deque
q = deque()
q.append(x); q.appendleft(x)
q.pop(); q.popleft()
q[0]; q[-1]; len(q)
q = deque(maxlen=k)            # auto-evicting sliding window
q.rotate(1)
```

### heapq (min-heap only)
```python
import heapq
h = []
heapq.heappush(h, x)
x = heapq.heappop(h)
h[0]                                  # peek
heapq.heapify(a)                      # O(n) in place
heapq.heappushpop(h, x); heapq.heapreplace(h, x)
heapq.nlargest(k, a); heapq.nsmallest(k, a, key=...)

# max-heap: negate
heapq.heappush(h, -x); -heapq.heappop(h)
# tuples: compared elementwise — add a tiebreak counter for unorderable payloads
heapq.heappush(h, (dist, node))
heapq.heappush(h, (priority, next(counter), obj))
```

### bisect (sorted list / binary search)
```python
from bisect import bisect_left, bisect_right, insort
i = bisect_left(a, x)      # first index with a[i] >= x   (lower_bound)
j = bisect_right(a, x)     # first index with a[j] >  x   (upper_bound)
count_of_x = j - i
insort(a, x)               # keeps sorted, O(n) memmove
# search with key (3.10+): bisect_left(a, x, key=lambda e: e[0])
```

### set
```python
s = set(); s.add(x); s.discard(x)   # discard = no KeyError
s | t, s & t, s - t, s ^ t
s.issubset(t); s.issuperset(t)
frozenset(...)                       # hashable, usable as dict key
```

---

## 5. Strings

```python
s = "abc"
s[i], s[l:r], s[::-1], len(s)
s.split(), s.split(','), s.rsplit(',', 1), s.splitlines()
','.join(list_of_str)
s.strip(), s.lstrip('0'), s.rstrip()
s.replace('a', 'b'), s.count('a'), s.find('x')   # -1 if absent
s.index('x')                                     # raises if absent
s.startswith(p), s.endswith(p), 'ab' in s
s.upper(), s.lower(), s.isdigit(), s.isalpha(), s.isupper()
s.zfill(5), s.ljust(5, '*'), s.rjust(5)
sorted(s)                        # -> list of chars; ''.join(sorted(s)) = anagram key
f"{x:.6f}", f"{x:05d}", f"{x:b}", f"{x:x}"
bin(n)[2:], int('1011', 2), hex(n), int('ff', 16)

# strings are IMMUTABLE — build with a list
parts = []
parts.append(ch)
res = ''.join(parts)             # NEVER s += ch in a loop (O(n^2))
```

---

## 6. Math / Bit Tricks

```python
import math
math.gcd(a, b), math.lcm(a, b)          # lcm is 3.9+
math.isqrt(n)                           # exact integer sqrt (no float error)
math.ceil(a / b)  -> prefer  -(-a // b) # exact int ceil-div
math.comb(n, k), math.perm(n, k)        # 3.8+
math.factorial(n), math.log2(n), math.inf

# bits
n & 1                    # odd?
n >> 1, n << 1
n & (n - 1)              # clear lowest set bit
n & -n                   # isolate lowest set bit
n & (n - 1) == 0         # power of two (n > 0)
n.bit_length()           # floor(log2(n)) + 1
bin(n).count('1')        # popcount   (3.10+: n.bit_count())
mask ^ (1 << i)          # toggle bit i
(mask >> i) & 1          # read bit i
sub = mask
while sub:               # enumerate submasks
    ...
    sub = (sub - 1) & mask

# modular
MOD = 10**9 + 7
pow(a, b, MOD)           # fast modpow (built-in!)
pow(a, MOD - 2, MOD)     # modular inverse, MOD prime
pow(a, -1, MOD)          # 3.8+: direct modular inverse
```

---

## 7. Algorithm Templates

### Binary search (lower bound)
```python
lo, hi = 0, n                   # search space [0, n)
while lo < hi:
    mid = (lo + hi) // 2
    if ok(mid):
        hi = mid
    else:
        lo = mid + 1
return lo                       # first index where ok() is True
```

### Binary search on answer
```python
lo, hi, ans = 1, 10**9, -1
while lo <= hi:
    mid = (lo + hi) // 2
    if feasible(mid):
        ans, hi = mid, mid - 1
    else:
        lo = mid + 1
```

### Sliding window (longest valid)
```python
from collections import defaultdict
cnt = defaultdict(int)
l = best = 0
for r, ch in enumerate(s):
    cnt[ch] += 1
    while len(cnt) > k:                 # invalid
        cnt[s[l]] -= 1
        if cnt[s[l]] == 0:
            del cnt[s[l]]
        l += 1
    best = max(best, r - l + 1)
```

### Monotonic stack (next greater element)
```python
nge = [-1] * n
st = []
for i, x in enumerate(a):
    while st and a[st[-1]] < x:
        nge[st.pop()] = i
    st.append(i)
```

### BFS on grid
```python
from collections import deque
DIR = ((1,0), (-1,0), (0,1), (0,-1))
q = deque([(sr, sc)])
dist = [[-1] * m for _ in range(n)]
dist[sr][sc] = 0
while q:
    r, c = q.popleft()
    for dr, dc in DIR:
        nr, nc = r + dr, c + dc
        if 0 <= nr < n and 0 <= nc < m and dist[nr][nc] == -1 and grid[nr][nc] != '#':
            dist[nr][nc] = dist[r][c] + 1
            q.append((nr, nc))
```

### DFS + backtracking (subsets with dedup)
```python
res, path = [], []
a.sort()
def dfs(start):
    res.append(path[:])                       # copy!
    for i in range(start, len(a)):
        if i > start and a[i] == a[i - 1]:
            continue
        path.append(a[i])
        dfs(i + 1)
        path.pop()
dfs(0)
```

### Dijkstra
```python
import heapq
dist = [float('inf')] * n
dist[s] = 0
pq = [(0, s)]
while pq:
    d, u = heapq.heappop(pq)
    if d > dist[u]:
        continue                              # stale entry
    for v, w in adj[u]:
        if d + w < dist[v]:
            dist[v] = d + w
            heapq.heappush(pq, (dist[v], v))
```

### Union-Find
```python
parent = list(range(n))
size = [1] * n

def find(x):
    while parent[x] != x:
        parent[x] = parent[parent[x]]        # path halving
        x = parent[x]
    return x

def union(a, b):
    a, b = find(a), find(b)
    if a == b:
        return False
    if size[a] < size[b]:
        a, b = b, a
    parent[b] = a
    size[a] += size[b]
    return True
```

### Topological sort (Kahn)
```python
from collections import deque
indeg = [0] * n
for u in range(n):
    for v in adj[u]:
        indeg[v] += 1
q = deque(i for i in range(n) if indeg[i] == 0)
order = []
while q:
    u = q.popleft()
    order.append(u)
    for v in adj[u]:
        indeg[v] -= 1
        if indeg[v] == 0:
            q.append(v)
if len(order) != n:
    pass  # cycle
```

### Memoized DP
```python
from functools import lru_cache

@lru_cache(maxsize=None)          # or @cache (3.9+)
def f(i, j):
    if i == 0:
        return 0
    return min(f(i - 1, j), f(i, j - 1) + cost[i][j])
f.cache_clear()                   # IMPORTANT between test cases / instances
```
> **Trap:** `@lru_cache` on a method keeps `self` alive and hashes it; prefer a module-level function or a plain `dict` memo in hot code (lru_cache has notable overhead).

### Fenwick (BIT)
```python
class BIT:
    def __init__(self, n):
        self.n = n
        self.t = [0] * (n + 1)
    def add(self, i, v):          # 0-indexed
        i += 1
        while i <= self.n:
            self.t[i] += v
            i += i & -i
    def sum(self, i):             # prefix sum of [0, i]
        i += 1
        s = 0
        while i > 0:
            s += self.t[i]
            i -= i & -i
        return s
    def range_sum(self, l, r):
        return self.sum(r) - (self.sum(l - 1) if l else 0)
```

### Trie (dict-based)
```python
trie = {}
for w in words:
    node = trie
    for ch in w:
        node = node.setdefault(ch, {})
    node['#'] = True             # end marker
```

### LinkedList / Tree
```python
class ListNode:
    def __init__(self, val=0, next=None):
        self.val, self.next = val, next

class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val, self.left, self.right = val, left, right

# reverse list
prev, cur = None, head
while cur:
    cur.next, prev, cur = prev, cur, cur.next

# dummy head
dummy = ListNode(0, head)

# fast/slow
slow = fast = head
while fast and fast.next:
    slow, fast = slow.next, fast.next.next

# iterative inorder
st, cur, out = [], root, []
while cur or st:
    while cur:
        st.append(cur)
        cur = cur.left
    cur = st.pop()
    out.append(cur.val)
    cur = cur.right
```

---

## 8. Sorting Keys & Custom Order

```python
a.sort(key=lambda x: (x[0], -x[1]))          # asc by 0, desc by 1
a.sort(key=lambda w: (len(w), w))
words.sort(key=str.lower)
sorted(d.items(), key=lambda kv: kv[1], reverse=True)

from operator import itemgetter, attrgetter
a.sort(key=itemgetter(1, 0))

# when a key function isn't expressible (e.g. "3" + "30" vs "30" + "3")
from functools import cmp_to_key
nums.sort(key=cmp_to_key(lambda x, y: (1 if x + y < y + x else -1)))
```
> Python's sort is **stable** — sort by secondary key first, then primary, to get multi-key ordering.

---

## 9. Handy Idioms

```python
# frequency compare / anagram
Counter(s) == Counter(t)

# grouping
groups = defaultdict(list)
for w in words:
    groups[''.join(sorted(w))].append(w)

# 2D transpose / rotate 90° clockwise
rot = [list(r) for r in zip(*g[::-1])]

# matrix print
print('\n'.join(' '.join(map(str, row)) for row in g))

# fast output of a list
print(*a)                                    # space-separated
sys.stdout.write('\n'.join(map(str, a)))

# pairwise iteration
for x, y in zip(a, a[1:]): ...

# tuple as dict key / memo key
memo[(i, j, mask)] = v

# infinite defaultdict tree
tree = lambda: defaultdict(tree)

# all coordinates
for r, c in product(range(n), range(m)): ...
```

---

## 10. Performance Notes (Python is slow — matters a lot)

- Assume ~10⁶–10⁷ simple ops/sec in CPython. Use **PyPy** if the judge allows.
- Prefer built-ins in C: `sum`, `min`, `max`, `sorted`, `map`, `any`, `all`, slicing, `''.join`.
- Avoid attribute lookups in hot loops: `push = heap.append` then `push(x)`.
- Avoid `s += ch` (O(n²)); use a list + `''.join`.
- Avoid `list.pop(0)` / `insert(0, ...)`; use `deque`.
- Local variables are faster than globals — wrap work in `def main()`.
- For huge int arrays, `array('i', ...)` or `bytearray` reduce memory.
- `dict`/`set` lookups are O(1); `x in list` is O(n) — a classic TLE cause.

### Complexity Budget (~1s, CPython)
| n | Acceptable |
|---|---|
| ≤ 10 | O(n!) |
| ≤ 18 | O(2ⁿ · n) |
| ≤ 200 | O(n³) |
| ≤ 2 000 | O(n²) |
| ≤ 2·10⁵ | O(n log n) |
| ≤ 10⁶ | O(n) |

---

## 11. Python Gotchas Checklist

- [ ] `[[0]*m]*n` shares rows → use a comprehension
- [ ] Default recursion limit 1000 → `sys.setrecursionlimit`
- [ ] `input()` keeps `'\n'` when aliased to `sys.stdin.readline` → `.strip()`
- [ ] Mutable default arg `def f(x, acc=[])` persists between calls
- [ ] Appending `path` instead of `path[:]` in backtracking
- [ ] Closures capture variables late (`lambda: i` in a loop)
- [ ] `sort()` returns `None`; `sorted()` returns the list
- [ ] `round()` uses banker's rounding
- [ ] Float precision → use `math.isqrt`, integer math, or `Decimal/Fraction`
- [ ] `@lru_cache` not cleared between test cases
- [ ] `x in list` inside a loop → O(n²) TLE; use a `set`
- [ ] `dict` mutated while iterating → RuntimeError
- [ ] `-7 // 2 == -4` (differs from Java/C++ truncation)
