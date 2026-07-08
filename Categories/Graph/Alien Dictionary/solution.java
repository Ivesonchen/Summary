/**
 * There is a new alien language which uses the latin alphabet. However, the order among letters are unknown to you. 
 * You receive a list of non-empty words from the dictionary, where words are sorted lexicographically by the rules of this new language. 
 * Derive the order of letters in this language.

Example 1:
Given the following words in dictionary,

[
  "wrt",
  "wrf",
  "er",
  "ett",
  "rftt"
]
The correct order is: "wertf".

Example 2:
Given the following words in dictionary,

[
  "z",
  "x"
]
The correct order is: "zx".

Example 3:
Given the following words in dictionary,

[
  "z",
  "x",
  "z"
]
The order is invalid, so return "".

Note:

You may assume all letters are in lowercase.
You may assume that if a is a prefix of b, then a must appear before b in the given dictionary.
If the order is invalid, return an empty string.
There may be multiple valid order of letters, return any one of them is fine.

https://github.com/grandyang/leetcode/issues/269
 */

/**
 * 这道题让给了一些按“字母顺序”排列的单词，但是这个字母顺序不是我们熟知的顺序，而是另类的顺序，让根据这些“有序”的单词来找出新的字母顺序，
 * 这实际上是一道有向图遍历的问题，跟之前的那两道 Course Schedule II 和 Course Schedule 的解法很类似，
 * 我们先来看 BFS 的解法，需要一个 TreeSet 来保存可以推测出来的顺序关系，比如题目中给的例子1，可以推出的顺序关系有：
    t->f
    w->e
    r->t
    e->r
    这些就是有向图的边，对于有向图中的每个结点，计算其入度，然后从入度为0的结点开始 BFS 遍历这个有向图，然后将遍历路径保存下来返回即可。下面来看具体的做法：

    根据之前讲解，需用 TreeSet 来保存这些 pair，还需要一个 HashSet 来保存所有出现过的字母，需要一个一维数组 in 来保存每个字母的入度，
    另外还要一个 queue 来辅助拓扑遍历，我们先遍历单词集，把所有字母先存入 HashSet，然后我们每两个相邻的单词比较，找出顺序 pair，
    然后根据这些 pair 来赋度，把 HashSet 中入度为0的字母都排入 queue 中，然后开始遍历，如果字母在 TreeSet 中存在，
    则将其 pair 中对应的字母的入度减1，若此时入度减为0了，则将对应的字母排入 queue 中并且加入结果 res 中，直到遍历完成，
    看结果 res 和 ch 中的元素个数是否相同，若不相同则说明可能有环存在，返回空字符串
 */
public class Solution {
    public String alienOrder(String[] words) {
      // Write your solution here
      Map<Character, Set<Character>> map = new HashMap<Character, Set<Character>>();
      Map<Character, Integer> degree = new HashMap<Character, Integer>();
  
      String result = "";
      if(words == null || words.length == 0) return result;
  
      for(String s : words){
        for(char c : s.toCharArray()){
          degree.put(c, 0);
        }
      } // get all character int degree map
  
      for(int i = 0; i < words.length - 1; i++){
        String cur = words[i];
        String next = words[i + 1];
        int length = Math.min(cur.length(), next.length());
        for(int j = 0; j < length; j++){
          char c1 = cur.charAt(j);
          char c2 = next.charAt(j);
          if(c1 != c2){
            Set<Character> set = new HashSet<Character>();
            if(map.containsKey(c1)) set = map.get(c1);
            if(!set.contains(c2)){
              set.add(c2);
              map.put(c1, set);
              degree.put(c2, degree.get(c2) + 1);
            }
            break;
          }
        }
      }
  
      Queue<Character> q = new LinkedList<Character>();
      for(char c : degree.keySet()){
        if(degree.get(c) == 0) q.add(c);
      }
  
      while(!q.isEmpty()){
        char c = q.remove();
        result += c;
        if(map.containsKey(c)){
          for(char c2 : map.get(c)){
            degree.put(c2, degree.get(c2) - 1);
            if(degree.get(c2) == 0) q.add(c2);
          }
        }
      }
      if(result.length() != degree.size()) return "";
      return result;
    }
  }