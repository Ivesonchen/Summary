# Java Coding Interview / Competitive Programming Cheatsheet

> Target: Java 8–17. Everything here is copy‑paste ready.

---

## 1. Boilerplate

### Interview (LeetCode style)
```java
class Solution {
    public int solve(int[] nums) {   // LeetCode gives you the class + signature —— 力扣直接给好类名和方法签名
        return 0;                    // just fill in the body —— 只需填函数体，无需自己读输入
    }
}
```

### Competitive (fast I/O — always use this, `Scanner` is ~10x slower)
### 竞赛快速输入输出（务必使用，`Scanner` 慢约 10 倍）
```java
import java.io.*;
import java.util.*;

public class Main {
    static final int MOD = 1_000_000_007;   // common prime modulus —— 常用取模质数（1e9+7）
    public static void main(String[] args) throws IOException {
        FastReader in = new FastReader();       // buffered reader —— 带缓冲的快速读入器
        StringBuilder sb = new StringBuilder();  // buffer all output —— 缓存全部输出，最后一次性打印
        int t = in.nextInt();                    // number of test cases —— 测试用例组数
        while (t-- > 0) {                         // loop each test case —— 逐组处理
            int n = in.nextInt();
            long[] a = new long[n];
            for (int i = 0; i < n; i++) a[i] = in.nextLong();  // read the array —— 读入数组
            sb.append(solve(n, a)).append('\n');               // append answer + newline —— 追加答案与换行
        }
        System.out.print(sb);              // print ONCE, never println in a loop —— 只打印一次，切勿在循环里 println
    }

    static long solve(int n, long[] a) { return 0; }   // your logic here —— 在此写核心逻辑

    static class FastReader {
        private final DataInputStream din = new DataInputStream(System.in);
        private final byte[] buf = new byte[1 << 16];   // 64KB read buffer —— 64KB 读缓冲区
        private int ptr = 0, len = 0;                   // buffer cursor & length —— 缓冲区游标与有效长度
        private int read() throws IOException {
            if (ptr == len) { len = din.read(buf, 0, buf.length); ptr = 0; if (len <= 0) return -1; }  // refill when empty —— 缓冲区读空则重新填充
            return buf[ptr++];
        }
        int nextInt() throws IOException { return (int) nextLong(); }   // reuse long parser —— 复用 long 解析
        long nextLong() throws IOException {
            int c = read(); while (c <= ' ') c = read();               // skip whitespace —— 跳过空白字符
            boolean neg = c == '-'; if (neg) c = read();              // handle sign —— 处理负号
            long x = 0; while (c > ' ') { x = x * 10 + (c - '0'); c = read(); }  // accumulate digits —— 逐位累加数字
            return neg ? -x : x;
        }
        String next() throws IOException {
            int c = read(); while (c <= ' ') c = read();               // skip whitespace —— 跳过空白
            StringBuilder s = new StringBuilder();
            while (c > ' ') { s.append((char) c); c = read(); }        // read until whitespace —— 读到下一个空白为止
            return s.toString();
        }
    }
}
```

### Deep recursion → run on a big-stack thread
### 深度递归 → 在大栈线程里运行（避免 StackOverflow）
```java
public static void main(String[] args) {
    // default stack ~512KB overflows near ~10k frames; 1<<26 = 64MB stack —— 默认栈约 512KB，约万层就溢出；这里给 64MB 大栈
    new Thread(null, () -> { /* real main */ }, "main", 1 << 26).start();
}
```

---

## 2. Core Syntax Quick Reference

```java
// numeric limits —— 数值边界

Integer.MAX_VALUE  // 2147483647   (~2.1e9)  —— int 上限，超过就溢出
Long.MAX_VALUE     // 9223372036854775807 (~9.2e18) —— long 上限
Integer.MIN_VALUE, Double.POSITIVE_INFINITY   // int 下限 / double 正无穷（常用作初始最小值）

// literals —— 字面量写法
long big = 1_000_000_007L;   // MUST suffix L —— 必须加 L，否则先按 int 算会溢出；_ 仅为可读分隔
int hex = 0xFF, bin = 0b1010;   // hex / binary literals —— 十六进制 / 二进制写法

// integer division / modulo (careful: Java % keeps sign of dividend!)
// 整除 / 取模（注意：Java 的 % 结果符号跟被除数一致，与 Python 不同！）
-7 / 2 == -3      // truncates toward zero —— 向 0 截断（Python 结果是 -4）
-7 % 2 == -1      // sign follows dividend —— 符号跟被除数（Python 结果是 1）
int m2 = ((a % m) + m) % m;   // safe non-negative modulo —— 保证非负的取模写法

// char arithmetic —— 字符运算（char 可直接当整数用）
char c = 'a'; int idx = c - 'a';        // map 'a'..'z' -> 0..25 —— 字母映射为下标
char back = (char)('a' + 3);            // 0..25 -> letter —— 下标反推回字母 == 'd'
Character.isDigit(c); Character.isLetter(c); Character.isLetterOrDigit(c);  // 字符类型判断
int d = c - '0';                        // digit char -> int —— 数字字符转整数

// var (Java 10+) —— 类型推断，少写泛型样板
var map = new HashMap<String, List<Integer>>();
for (var e : map.entrySet()) { e.getKey(); e.getValue(); }   // e 自动推断为 Map.Entry
```

### Overflow traps
### 溢出陷阱
```java
int mid = (lo + hi) / 2;          // BAD: lo+hi can overflow int —— 错：lo+hi 可能溢出 int
int mid = lo + (hi - lo) / 2;     // GOOD: never overflows —— 对：永不溢出的写法
long prod = (long) a * b;         // cast BEFORE multiply —— 乘之前先转 long，否则先按 int 相乘已溢出
Math.floorDiv(-7, 2) == -4;       // Python-like division —— 向下取整除法（类 Python）
Math.floorMod(-7, 2) == 1;        // Python-like modulo —— 总为非负的取模（类 Python）
```

---

## 3. Arrays

```java
int[] a = new int[n];              // fixed size, default 0 —— 定长数组，默认值 0
int[] b = {1, 2, 3};               // array literal —— 字面量初始化
int[][] g = new int[n][m];         // 2D grid, all 0 —— 二维数组，全为 0
int[][] jag = new int[n][];        // rows allocated later —— 锐齿数组，每行长度可不同，稍后分配

Arrays.fill(a, -1);                                       // fill all with -1 —— 全部填 -1
for (int[] row : g) Arrays.fill(row, Integer.MAX_VALUE);  // 2D fill row by row —— 二维需逐行填充

Arrays.sort(a);                              // primitives: dual-pivot quicksort O(n log n) —— 基本类型升序（双轴快排）
Integer[] boxed = {3, 1, 2};                 // must be Integer[], NOT int[] —— 必须是包装类 Integer[]，不能是 int[]
Arrays.sort(boxed, (x, y) -> y - x);         // objects only; use Integer[] to reverse —— 只有包装类 Integer[] 才能用比较器降序 -> [3, 2, 1]
int[][] g = {{1, 5}, {1, 2}, {0, 9}};        // e.g. intervals / points —— 例如区间或坐标点
Arrays.sort(g, (x, y) -> x[0] != y[0] ? x[0] - y[0] : x[1] - y[1]);  // by col0 then col1 —— 先按第0列、再按第1列排 -> {{0,9},{1,2},{1,5}}
Arrays.sort(a, from, to);                    // sort subrange [from, to) —— 只排子区间

int[] copy   = Arrays.copyOf(a, n);          // copy first n (pad with 0) —— 复制前 n 个，不足补 0
int[] slice  = Arrays.copyOfRange(a, l, r);  // copy [l, r) —— 复制左闭右开区间
System.arraycopy(src, sp, dst, dp, len);     // fastest bulk copy —— 最快的批量复制

Arrays.toString(a);  Arrays.deepToString(g);  // debugging print —— 调试打印（二维用 deep）
Arrays.equals(a, b);                          // element-wise equality —— 逐元素比较相等
Arrays.stream(a).sum(); Arrays.stream(a).max().getAsInt();   // stream aggregates —— 求和 / 求最大

int[] pre = new int[n + 1];                            // prefix sum array —— 前缀和数组（多一位方便）
for (int i = 0; i < n; i++) pre[i + 1] = pre[i] + a[i];   // pre[i]=前 i 个元素之和
// sum of [l, r] = pre[r + 1] - pre[l]                 // 区间和 O(1) 查询

// binarySearch: returns index, or (-(insertionPoint) - 1) if absent
// 二分查找：命中返回下标；未命中返回 (-(应插入位置)-1)
int p = Arrays.binarySearch(a, key);
if (p < 0) p = -p - 1;   // convert to first index > key —— 转为第一个 > key 的位置
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

> Each block below lists the **basic / most-used operations** with the resulting state or return value shown as a `// comment on the side`.
> 下面每个代码块列出最【基础 / 最常用】的操作，右侧 `// 注释` 标出操作后的状态或返回值。

### `ArrayList` — dynamic array (`List`)
### `ArrayList` —— 动态数组（`List` 接口）
```java
List<Integer> list = new ArrayList<>();     // []  空列表
list.add(5);                 // append to end            -> [5]      在末尾追加
list.add(0, 9);              // insert at index          -> [9, 5]   在指定下标插入
list.get(1);                 // read by index            == 5        按下标读取
list.set(1, 7);              // overwrite index, ret old == 5, list -> [9, 7]  覆盖并返回旧值
list.size();                 // element count            == 2        元素个数
list.isEmpty();              // size == 0 ?              == false    是否为空
list.contains(9);            // linear scan O(n)         == true     是否包含（线性扫描）
list.indexOf(7);             // first position, -1 none  == 1        首次出现下标，没有则 -1
list.remove(0);              // remove by INDEX          -> [7]  (returns 9)  按【下标】删除
list.remove(Integer.valueOf(7)); // remove by VALUE     -> []       按【值】删除
list.clear();                // empty it                 -> []       清空

// utilities (java.util.Collections) —— 工具方法
Collections.sort(list);                            // ascending in place    原地升序
Collections.sort(list, Comparator.reverseOrder()); // descending            降序
Collections.reverse(list); Collections.swap(list, i, j);   // reverse / swap 反转 / 交换两个下标
Collections.max(list); Collections.min(list); Collections.frequency(list, x);  // 最大 / 最小 / x 出现次数
Collections.nCopies(n, 0);                          // n copies of 0         生成 n 个 0 的列表

// List <-> array —— 列表与数组互转
Integer[] arr = list.toArray(new Integer[0]);      // List -> Integer[]     转包装类数组
int[] prim = list.stream().mapToInt(Integer::intValue).toArray();  // -> int[]  转基本类型数组
List<Integer> back = Arrays.stream(prim).boxed().collect(Collectors.toList());  // int[] -> List  转回列表
List<int[]> res = new ArrayList<>();
int[][] out = res.toArray(new int[0][]);           // List<int[]> -> int[][]  转二维数组
```
> **Trap:** `List<Integer>.remove(int)` removes by **index**, `remove(Object)` by value → `list.remove(Integer.valueOf(x))`.
> **陷阱：** `remove(int)` 按【下标】删，`remove(Object)` 按【值】删，删值要写 `list.remove(Integer.valueOf(x))`。
> **Trap:** `Integer == Integer` only works for cached −128..127. Use `.equals()` or unbox.
> **陷阱：** `Integer == Integer` 只对缓存的 −128..127 成立，比较值要用 `.equals()` 或先拆箱。

### `HashMap` — key→value, O(1) avg
### `HashMap` —— 键→值映射，平均 O(1)
```java
Map<String, Integer> map = new HashMap<>();   // {}  空映射
map.put("a", 1);             // insert / overwrite       -> {a=1}    插入或覆盖
map.get("a");                // value, or null if absent == 1        取值，不存在返回 null
map.getOrDefault("b", 0);    // safe read                == 0        取值，不存在返回默认值
map.containsKey("a");        // key present?             == true     是否含该键
map.remove("a");             // delete, returns old      == 1        删除并返回旧值
map.size();                  // number of entries        == 0        键值对数量
map.keySet(); map.values(); map.entrySet();   // views for iteration   键集 / 值集 / 键值对集（用于遍历）
// 例：map = {a=1, b=2}
for (String k : map.keySet())    { /* k = "a","b" */ }              // 遍历所有键
for (int v : map.values())       { /* v = 1, 2 */ }                 // 遍历所有值
for (var e : map.entrySet())     { e.getKey(); e.getValue(); }      // 同时拿键和值（最常用）

// counting / grouping idioms —— 计数 / 分组常用写法
map.merge("a", 1, Integer::sum);                        // counter (best)  计数器（最佳写法）
// 例：统计词频，从空 map 开始
Map<String,Integer> cnt = new HashMap<>();
cnt.merge("apple", 1, Integer::sum);   // "apple" 不存在 -> 直接放 1   -> {apple=1}
cnt.merge("apple", 1, Integer::sum);   // 已存在 -> 旧值 1 + 1 = 2      -> {apple=2}
cnt.merge("pear",  1, Integer::sum);   // 新键                          -> {apple=2, pear=1}
map.put("a", map.getOrDefault("a", 0) + 1);            // equivalent      等价写法
map.computeIfAbsent("k", z -> new ArrayList<>()).add(v); // multimap        一键多值（分组）
// 例：按首字母分组单词
Map<Character,List<String>> groups = new HashMap<>();
for (String w : new String[]{"apple", "avocado", "banana"}) {
    groups.computeIfAbsent(w.charAt(0), z -> new ArrayList<>()).add(w);
    // 键不存在 -> 先建空 ArrayList 再 add；键已存在 -> 直接拿到旧 List 再 add
}
// 结果 -> {a=[apple, avocado], b=[banana]}
map.putIfAbsent("k", v);                                // set only if absent  仅当键不存在时写入
for (Map.Entry<String,Integer> e : map.entrySet()) { e.getKey(); e.getValue(); }  // 遍历键值对
map.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();  // key with max value  取值最大的键
```

### `HashSet` — unique elements, O(1) avg
### `HashSet` —— 去重集合，平均 O(1)
```java
Set<Integer> set = new HashSet<>();   // {}  空集合
set.add(5);                  // returns false if already present -> {5}  已存在则返回 false
set.contains(5);             // membership test          == true     是否包含
set.remove(5);               // delete, returns boolean  -> {}       删除，返回是否存在过
set.size(); set.isEmpty();   // count / empty?           元素个数 / 是否为空
set.addAll(list);            // union with a collection   并集（加入另一集合）
set.retainAll(other);        // intersection (keep common)  交集（只保留公共元素）
set.removeAll(other);        // difference (remove shared)  差集（移除公共元素）
```

### `LinkedHashMap` — insertion order (or access order for LRU)
### `LinkedHashMap` —— 保持插入顺序（或访问顺序，用于 LRU）
```java
Map<Integer,Integer> lru = new LinkedHashMap<>(16, 0.75f, true) { // accessOrder=true —— 按访问顺序排列
    protected boolean removeEldestEntry(Map.Entry<Integer,Integer> e) {
        return size() > CAPACITY;    // auto-evicts least-recently-used —— 超容量时自动淘汰最久未用的
    }
};
// same put/get/remove as HashMap, but iteration follows insertion/access order
// put/get/remove 用法与 HashMap 相同，但遍历顺序按插入 / 访问顺序
```

### `ArrayDeque` — stack & queue (prefer over `Stack` / `LinkedList`)
### `ArrayDeque` —— 栈与队列（优于 `Stack` / `LinkedList`）
```java
Deque<Integer> st = new ArrayDeque<>();          // stack (LIFO, at head) —— 栈（后进先出，在头部）
st.push(1); st.push(2);      // add to head              -> [2, 1]   压入栈顶（头部）
st.peek();                   // look at top              == 2        查看栈顶（不删）
st.pop();                    // remove top               == 2, st -> [1]  弹出栈顶

Deque<Integer> q = new ArrayDeque<>();           // queue (FIFO) —— 队列（先进先出）
q.offer(1); q.offer(2);      // add to tail              -> [1, 2]   入队（尾部）
q.peek();                    // look at head             == 1        查看队头（不删）
q.poll();                    // remove head              == 1, q -> [2]  出队（头部）

Deque<Integer> dq = new ArrayDeque<>();          // double-ended / monotonic —— 双端队列 / 单调队列
dq.offerFirst(x); dq.offerLast(y);               // add at head / tail   头部加 / 尾部加
dq.pollFirst(); dq.pollLast(); dq.peekFirst(); dq.peekLast();  // 两端删 / 两端查
st.isEmpty(); st.size();     // empty? / size            是否为空 / 大小
```
> `ArrayDeque` forbids `null`. `Stack` is synchronized/legacy — avoid.
> `ArrayDeque` 不允许 `null`；`Stack` 是同步的旧类，避免使用。
> Both `pop()` and `poll()` remove from the **head**; `push` adds to head, `offer` adds to tail.
> `pop()` 和 `poll()` 都从【头部】删；`push` 加到头部，`offer` 加到尾部。

### `PriorityQueue` — heap (min-heap by default)
### `PriorityQueue` —— 堆（默认小顶堆）
```java
PriorityQueue<Integer> min = new PriorityQueue<>();                       // min-heap —— 小顶堆（堆顶最小）
PriorityQueue<Integer> max = new PriorityQueue<>(Comparator.reverseOrder()); // max-heap —— 大顶堆
PriorityQueue<int[]> pq  = new PriorityQueue<>((a, b) -> a[1] - b[1]);    // by 2nd field —— 按第2列排
PriorityQueue<int[]> pq2 = new PriorityQueue<>(Comparator.comparingInt(x -> x[1]));  // 同上，更安全
min.offer(3); min.offer(1); min.offer(2);   // add       -> heap of {1,2,3}  入堆
min.peek();                  // smallest, no removal      == 1        查看堆顶（不删）
min.poll();                  // remove smallest           == 1        弹出堆顶（最小）
min.size();                  // element count             元素个数
// O(n) heapify: new PriorityQueue<>(collection)  —— 用集合构造可 O(n) 建堆
```
> **Trap:** PQ iteration order is NOT sorted; `remove(o)` is O(n).
> **陷阱：** 直接遍历优先队列【不是】有序的；`remove(o)` 是 O(n)。
> **Trap:** `(a, b) -> a - b` overflows — use `Integer.compare(a, b)`.
> **陷阱：** `(a, b) -> a - b` 会溢出，请用 `Integer.compare(a, b)`。

### `TreeMap` / `TreeSet` — sorted (Java's edge over Python — no built-in there)
### `TreeMap` / `TreeSet` —— 有序容器（Java 相对 Python 的优势，Python 无内置）
```java
TreeMap<Integer, Integer> tm = new TreeMap<>();
tm.put(5, 50); tm.put(1, 10); tm.put(9, 90);   // sorted by key -> {1,5,9}  按键自动排序
tm.firstKey(); tm.lastKey();     // == 1 / == 9   最小键 / 最大键
tm.floorKey(6);    // greatest key <= 6         == 5   ≤6 的最大键
tm.ceilingKey(6);  // smallest key >= 6         == 9   ≥6 的最小键
tm.lowerKey(5);    // strictly <  5             == 1   严格小于 5 的最大键
tm.higherKey(5);   // strictly >  5             == 9   严格大于 5 的最小键
tm.pollFirstEntry(); tm.pollLastEntry();        // remove ends   弹出最小 / 最大项
tm.headMap(5, true); tm.tailMap(5, false); tm.subMap(1, true, 9, false);  // 子区间视图
tm.descendingMap();   // reversed view   逆序视图

TreeSet<Integer> ts = new TreeSet<>();
ts.add(5); ts.add(1); ts.add(9);                // sorted -> {1, 5, 9}  自动排序去重
ts.first(); ts.last(); ts.floor(6); ts.ceiling(6); ts.lower(5); ts.higher(5);  // 同 TreeMap 导航
// multiset: TreeMap<T,Integer>, merge(+1); on removal decrement and remove at 0
// 多重集：用 TreeMap<T,Integer> 计数，merge(+1)；删除时计数减 1，减到 0 就移除该键
```

---

## 5. Strings

```java
String s = "abc";
s.charAt(i); s.length(); s.substring(l, r);          // [l, r)  取字符 / 长度 / 子串（左闭右开）
s.indexOf("x"); s.lastIndexOf('x'); s.contains("ab"); // 首次 / 末次下标 / 是否包含
s.startsWith(p); s.endsWith(p); s.isEmpty();         // 前缀 / 后缀 / 是否空串
s.toCharArray(); new String(charArr);                // 字符串↔字符数组互转
s.split("\\s+"); String.join(",", list);             // 按空白拆分 / 用逗号拼接
s.replace('a','b'); s.trim(); s.repeat(3);           // 替换字符 / 去首尾空白 / 重复 3 次
s.compareTo(t);  // lexicographic —— 字典序比较（<0 / 0 / >0）
Integer.parseInt(s); Long.parseLong(s); String.valueOf(num);   // 字符串↔数字互转
Integer.toBinaryString(n); Integer.parseInt(s, 2);   // 十进制→二进制串 / 二进制串→整数

// StringBuilder — ALWAYS use for building (String concat in loop = O(n^2))
// StringBuilder —— 拼接字符串务必用它（循环里用 + 拼接是 O(n^2)）
StringBuilder sb = new StringBuilder();
sb.append(x).append(',');                            // append —— 链式追加
sb.setCharAt(i, c); sb.charAt(i); sb.deleteCharAt(sb.length() - 1);  // 改 / 取 / 删最后一个字符
sb.insert(0, c); sb.reverse(); sb.setLength(0);   // insert / reverse / clear —— 插入 / 反转 / 清空
sb.toString();                                       // build final string —— 生成最终字符串

// char-count array (26 letters) — faster than HashMap
// 字符计数数组（26 个字母）—— 比 HashMap 更快
int[] cnt = new int[26];
for (char c : s.toCharArray()) cnt[c - 'a']++;       // 统计每个字母出现次数

// anagram key —— 字母异位词的归一化键（排序后相同即为一组）
char[] k = s.toCharArray(); Arrays.sort(k); String key = new String(k);
// 例："eat" -> 排序后 "aet"；"tea" -> 也是 "aet"；"tan" -> "ant"
//     用该 key 分组，"eat"、"tea" 归入同一组，"tan" 单独一组
Map<String, List<String>> groups = new HashMap<>();
for (String w : new String[]{"eat", "tea", "tan", "ate", "nat", "bat"}) {
    char[] ck = w.toCharArray(); Arrays.sort(ck);
    groups.computeIfAbsent(new String(ck), z -> new ArrayList<>()).add(w);
}
// 结果 -> {aet=[eat, tea, ate], ant=[tan, nat], abt=[bat]}
```

---

## 6. Math / Bit Tricks

```java
Math.abs, max, min, pow, sqrt, ceil, floor, round;   // 绝对值 / 最大 / 最小 / 幂 / 开方 / 上下取整 / 四舍五入
(a + b - 1) / b                 // int ceil-div for a,b > 0 —— 正整数向上取整除法
Math.floorDiv(a, b); Math.floorMod(a, b);   // floor 除法 / 非负取模（类 Python）
long gcd(long a, long b) { return b == 0 ? a : gcd(b, a % b); }   // 最大公约数 —— 辗转相除求 GCD
long lcm = a / gcd(a, b) * b;   // divide first to avoid overflow —— 先除再乘防溢出（最小公倍数）

// bits —— 位运算
n & 1                    // odd? —— 是否为奇数
n >> 1, n << 1           // /2, *2   (>>> = unsigned shift) —— 除 2 / 乘 2（>>> 为无符号右移）
n & (n - 1)              // clear lowest set bit —— 消除最低位的 1
n & (-n)                 // isolate lowest set bit —— 取出最低位的 1（lowbit）
(n & (n - 1)) == 0       // power of two (n > 0) —— 判断是否为 2 的幂
Integer.bitCount(n); Long.bitCount(n);   // count set bits —— 统计 1 的个数
Integer.highestOneBit(n); Integer.numberOfTrailingZeros(n);   // 最高位的 1 / 末尾 0 的个数
mask ^ (1 << i)          // toggle bit i —— 翻转第 i 位
(mask >> i) & 1          // read bit i —— 读取第 i 位
for (int sub = mask; sub > 0; sub = (sub - 1) & mask) {}   // enumerate submasks —— 枚举 mask 的所有子集

// modpow / modular inverse —— 快速幂取模 / 模逆元
long power(long b, long e, long m) {
    long r = 1; b %= m;
    while (e > 0) { if ((e & 1) == 1) r = r * b % m; b = b * b % m; e >>= 1; }  // 二进制快速幂
    return r;
}
long inv(long a, long p) { return power(a, p - 2, p); }  // p prime —— 费马小定理求逆元（p 为质数）

// sieve —— 埃氏筛选质数
boolean[] comp = new boolean[n + 1];   // comp[i]=true 表示 i 是合数
for (int i = 2; (long) i * i <= n; i++)
    if (!comp[i]) for (int j = i * i; j <= n; j += i) comp[j] = true;   // 标记 i 的倍数为合数
```

---

## 7. Algorithm Templates

### Binary search (lower bound)
### 二分查找（下界 / 第一个满足条件的位置）
```java
int lo = 0, hi = n;                  // search space [0, n) —— 搜索区间左闭右开
while (lo < hi) {
    int mid = lo + (hi - lo) / 2;    // avoid overflow —— 防溢出取中点
    if (ok(mid)) hi = mid; else lo = mid + 1;   // shrink toward first true —— 向第一个 true 收缩
}
return lo;                            // first index where ok() is true —— 第一个使 ok() 为真的下标
```

### Binary search on answer
### 对答案二分（最大化 / 最小化可行值）
```java
long lo = 1, hi = 1_000_000_000L, ans = -1;   // answer range —— 答案的取值范围
while (lo <= hi) {
    long mid = lo + (hi - lo) / 2;
    if (feasible(mid)) { ans = mid; hi = mid - 1; } else lo = mid + 1;  // 可行则记录并继续缩小
}
```

### Two pointers / sliding window (longest valid)
### 双指针 / 滑动窗口（求最长合法区间）
```java
int l = 0, best = 0;
for (int r = 0; r < n; r++) {
    add(a[r]);                        // expand right —— 右指针扩展，加入新元素
    while (invalid()) remove(a[l++]); // shrink left until valid —— 非法时收缩左指针
    best = Math.max(best, r - l + 1); // update answer —— 更新最优窗口长度
}
```

### Monotonic stack (next greater element)
### 单调栈（求下一个更大元素）
```java
int[] nge = new int[n];
Arrays.fill(nge, -1);                 // default: none —— 默认无更大元素
Deque<Integer> st = new ArrayDeque<>();   // stack of indices —— 栈里存下标
for (int i = 0; i < n; i++) {
    while (!st.isEmpty() && a[st.peek()] < a[i]) nge[st.pop()] = i;  // i 是栈顶元素的下一更大
    st.push(i);
}
```

### BFS on grid
### 网格上的广度优先搜索（求最短路径）
```java
int[][] DIR = {{1,0},{-1,0},{0,1},{0,-1}};   // 4 directions —— 上下左右四个方向
Deque<int[]> q = new ArrayDeque<>();
boolean[][] vis = new boolean[n][m];         // visited —— 访问标记
q.offer(new int[]{sr, sc}); vis[sr][sc] = true;   // start cell —— 起点入队并标记
int dist = 0;
while (!q.isEmpty()) {
    for (int sz = q.size(); sz > 0; sz--) {          // level order —— 按层遍历（同一层距离相同）
        int[] cur = q.poll();
        for (int[] d : DIR) {
            int nr = cur[0] + d[0], nc = cur[1] + d[1];   // neighbor —— 相邻格子
            if (nr < 0 || nr >= n || nc < 0 || nc >= m || vis[nr][nc] || grid[nr][nc] == '#') continue;  // 越界/已访/障碍则跳过
            vis[nr][nc] = true;
            q.offer(new int[]{nr, nc});
        }
    }
    dist++;                                          // one layer done —— 一层完成，距离 +1
}
```

### DFS + backtracking (subsets, dedup)
### DFS + 回溯（子集枚举，含去重）
```java
void dfs(int start, List<Integer> path, List<List<Integer>> res) {
    res.add(new ArrayList<>(path));           // copy! —— 必须拷贝！直接加 path 会被后续修改
    for (int i = start; i < n; i++) {
        if (i > start && a[i] == a[i - 1]) continue;   // skip dups (sorted input) —— 同层去重（输入需已排序）
        path.add(a[i]);                       // choose —— 选择
        dfs(i + 1, path, res);                // recurse —— 递归到下一层
        path.remove(path.size() - 1);         // undo —— 回溯，撤销选择
    }
}
```

### Dijkstra
### 迪束斯特拉最短路（非负权图）
```java
long[] dist = new long[n];
Arrays.fill(dist, Long.MAX_VALUE); dist[s] = 0;   // init distances, source=0 —— 初始化距离，源点为 0
PriorityQueue<long[]> pq = new PriorityQueue<>((x, y) -> Long.compare(x[1], y[1]));  // min-heap by dist —— 按距离的小顶堆
pq.offer(new long[]{s, 0});
while (!pq.isEmpty()) {
    long[] cur = pq.poll();
    int u = (int) cur[0];
    if (cur[1] > dist[u]) continue;                 // stale entry —— 过期旧条目，跳过
    for (int[] e : adj.get(u)) {                    // e = {v, w} —— 邻边：目标点 v，权重 w
        if (dist[u] + e[1] < dist[e[0]]) {          // relax —— 松弛：发现更短路径
            dist[e[0]] = dist[u] + e[1];
            pq.offer(new long[]{e[0], dist[e[0]]});
        }
    }
}
```

### Union-Find (path compression + union by size)
### 并查集（路径压缩 + 按秩合并）
```java
static int[] p, sz;   // p=parent, sz=set size —— p 存父节点，sz 存集合大小
static void init(int n) { p = new int[n]; sz = new int[n];
    for (int i = 0; i < n; i++) { p[i] = i; sz[i] = 1; } }   // each node its own set —— 初始每个点自成一集
static int find(int x) { while (p[x] != x) { p[x] = p[p[x]]; x = p[x]; } return x; }  // find root + compress —— 找根并压缩路径
static boolean union(int a, int b) {
    a = find(a); b = find(b);
    if (a == b) return false;                       // already connected —— 已连通，无需合并
    if (sz[a] < sz[b]) { int t = a; a = b; b = t; } // attach smaller to larger —— 小集挂到大集下
    p[b] = a; sz[a] += sz[b];
    return true;
}
```

### Topological sort (Kahn)
### 拓扑排序（Kahn 入度法）
```java
int[] indeg = new int[n];   // in-degree —— 每个点的入度
for (int u = 0; u < n; u++) for (int v : adj.get(u)) indeg[v]++;   // count in-degrees —— 统计入度
Deque<Integer> q = new ArrayDeque<>();
for (int i = 0; i < n; i++) if (indeg[i] == 0) q.offer(i);   // start from 0 in-degree —— 入度为 0 先入队
List<Integer> order = new ArrayList<>();
while (!q.isEmpty()) {
    int u = q.poll(); order.add(u);
    for (int v : adj.get(u)) if (--indeg[v] == 0) q.offer(v);   // remove edge, enqueue new 0 —— 删边后入度归 0 则入队
}
if (order.size() != n) { /* cycle exists */ }   // not all visited → has cycle —— 未全部输出说明有环
```

### Memoized DP
### 记忆化搜索（自顶向下 DP）
```java
Integer[][] memo = new Integer[n][k + 1];     // null = uncomputed —— null 表示尚未计算
int f(int i, int j) {
    if (base) return val;                     // base case —— 边界情况
    if (memo[i][j] != null) return memo[i][j]; // cache hit —— 命中缓存直接返回
    return memo[i][j] = Math.min(f(i - 1, j), f(i, j - 1) + cost);  // compute & store —— 计算并存入缓存
}
```

### Fenwick (BIT), 0-indexed API
### 树状数组（BIT），对外 0 下标
```java
static long[] bit; static int N;   // bit[] 1-indexed internally —— 内部下标从 1 开始
static void upd(int i, long v) { for (i++; i <= N; i += i & -i) bit[i] += v; }   // point add —— 单点增加 v
static long qry(int i) { long s = 0; for (i++; i > 0; i -= i & -i) s += bit[i]; return s; }  // prefix sum [0,i] —— 前缀和
// range sum [l, r] = qry(r) - qry(l - 1)   —— 区间和 = 前缀和相减
```

### Trie
### 字典树（前缀树）
```java
class Trie {
    Trie[] ch = new Trie[26];   // 26 children (a-z) —— 26 个子节点（a-z）
    boolean end;                // marks end of a word —— 标记是否为单词结尾
    void insert(String s) {
        Trie cur = this;
        for (char c : s.toCharArray()) {
            int i = c - 'a';
            if (cur.ch[i] == null) cur.ch[i] = new Trie();   // create path —— 不存在则新建节点
            cur = cur.ch[i];
        }
        cur.end = true;         // word ends here —— 单词在此结束
    }
}
```

### LinkedList / Tree nodes
### 链表 / 树 节点常用操作
```java
class ListNode { int val; ListNode next; ListNode(int v) { val = v; } }   // singly linked list —— 单链表节点
class TreeNode { int val; TreeNode left, right; TreeNode(int v) { val = v; } }  // binary tree —— 二叉树节点

// reverse list —— 反转链表
ListNode prev = null, cur = head;
while (cur != null) { ListNode nx = cur.next; cur.next = prev; prev = cur; cur = nx; }   // 逐个掉头指向前驱

// dummy head for deletions —— 哑头节点，简化删除边界
ListNode dummy = new ListNode(0); dummy.next = head;

// fast/slow pointers (middle & cycle detection) —— 快慢指针（找中点 / 判环）
ListNode slow = head, fast = head;
while (fast != null && fast.next != null) { slow = slow.next; fast = fast.next.next; }   // fast 走两步，slow 走一步

// iterative inorder —— 中序遍历（迭代版，用栈代替递归）
Deque<TreeNode> st = new ArrayDeque<>();
TreeNode cur = root;
while (cur != null || !st.isEmpty()) {
    while (cur != null) { st.push(cur); cur = cur.left; }   // go left to bottom —— 一路压入左子树
    cur = st.pop();
    visit(cur);                                            // process node —— 访问节点
    cur = cur.right;                                       // then right —— 转向右子树
}
```

---

## 8. Comparators (top source of bugs)

```java
Comparator.comparingInt((int[] x) -> x[0])                              // sort by field 0 asc —— 按第0位升序
Comparator.comparingInt((int[] x) -> x[0]).thenComparingInt(x -> x[1])  // tie-break by field 1 —— 相等时再比第1位
Comparator.comparingInt((int[] x) -> x[0]).reversed()                   // descending —— 反转为降序
Comparator.comparing(Person::getName)                                  // sort by property —— 按对象属性排
(a, b) -> Integer.compare(a, b)      // safe (no overflow) —— 安全（不溢出），推荐
(a, b) -> Long.compare(a, b)         // for long —— long 的安全比较
(a, b) -> b[1] - a[1]                // only for small non-negative ints —— 仅适用于小的非负整数
```
> A comparator must be transitive & consistent or you get
> `IllegalArgumentException: Comparison method violates its general contract!`
> 比较器必须满足传递性与一致性，否则会报错：
> `IllegalArgumentException: Comparison method violates its general contract!`（常因相减溢出引起）

---

## 9. Encoding Tricks

```java
// pair (r, c) -> single int, for HashSet —— 坐标 (r,c) 编码为单个 int（方便放入 HashSet）
int key = r * m + c;              int r2 = key / m, c2 = key % m;   // encode / decode —— 编码 / 解码
// two ints -> long —— 两个 int 拼成一个 long
long k = ((long) a << 32) | (b & 0xFFFFFFFFL);   // high 32 = a, low 32 = b —— 高32位放 a，低32位放 b
int a2 = (int)(k >> 32), b2 = (int) k;           // decode back —— 拆回两个 int
// string key (slower but easy) —— 字符串键（慢但简单）
String sk = r + "," + c;
```

---

## 10. Complexity Budget (~1s)
## 10. 复杂度预算（约 1 秒内）

| n | Acceptable 可接受复杂度 |
|---|---|
| ≤ 10 | O(n!) |
| ≤ 20 | O(2ⁿ · n) bitmask DP —— 状压 DP |
| ≤ 500 | O(n³) |
| ≤ 5 000 | O(n²) |
| ≤ 10⁵ | O(n log n) |
| ≤ 10⁷ | O(n) |

Java ≈ 10⁸ simple ops/s (roughly 1–2x slower than C++).
Java 约每秒 10⁸ 次简单运算（大致比 C++ 慢 1～2 倍）。

---

## 11. Java Gotchas Checklist
## 11. Java 常见坑清单

- [ ] `int` overflow → use `long` (`(long) a * b`)  —— int 溢出改用 long，乘前先转
- [ ] `mid = lo + (hi - lo) / 2`  —— 二分取中点防溢出
- [ ] `Integer` boxing equality → `.equals()` / unbox  —— 包装类比较用 .equals() 或拆箱
- [ ] `list.remove(int)` removes by index  —— remove(int) 按下标删，非按值
- [ ] `ArrayDeque` rejects `null`  —— ArrayDeque 不能存 null
- [ ] Comparator subtraction overflow → `Integer.compare`  —— 比较器相减会溢出，用 Integer.compare
- [ ] String concat in loop → `StringBuilder`  —— 循环拼接字符串用 StringBuilder
- [ ] `%` can be negative → `Math.floorMod`  —— % 可能为负，需非负取模用 floorMod
- [ ] `Arrays.asList(intArray)` gives `List<int[]>` of size 1  —— 传 int[] 会得到长度 1 的列表
- [ ] `split` takes a **regex**: `split("\\.")`, `split("\\|")`  —— split 参数是正则，特殊字符需转义
- [ ] Forgetting to copy `path` before adding to results  —— 回溯时忘拷贝 path
- [ ] Recursion depth ~10 000 frames → iterative or big-stack thread  —— 递归约万层就爆栈，改迭代或大栈线程
- [ ] `Scanner` on 10⁵+ lines → TLE  —— 大量输入用 Scanner 会超时
