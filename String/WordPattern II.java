/**
 * Given a pattern and a string str, find if str follows the same pattern.

Here follow means a full match, such that there is a bijection between a letter in pattern and a non-empty substring in str.

Examples:

pattern = "abab", str = "redblueredblue" should return true.
pattern = "aaaa", str = "asdasdasdasd" should return true.
pattern = "aabb", str = "xyzabcxzyabc" should return false.
Notes:
You may assume both pattern and str contains only lowercase letters.
https://github.com/grandyang/leetcode/issues/291 

思路讲解的好啊
 */

public class Solution {
    public boolean wordPatternMatch(String pattern, String input) {
      // Write your solution here
      HashMap<Character, String> map = new HashMap<>();
      HashSet<String> set = new HashSet<>();
  
      return isMatch(input, 0, pattern, 0, map, set);
    }
  
    private boolean isMatch(String str, int i, String pat, int j, HashMap<Character, String> map, HashSet<String> set){
      if(i == str.length() && j == pat.length()) return true;  // 两个指针同时都到达了末尾
      if(i == str.length() || j == pat.length()) return false; // 只有一个到达了末尾 (匹配完成了)
  
      char c = pat.charAt(j);
      // 模式字符串匹配过对应的
      if(map.containsKey(c)){
        String s = map.get(c);
        if(!str.startsWith(s, i)){ // 如果匹配串里对应i位置不start with 的话 说明整体的匹配失败
          return false;
        }
        return isMatch(str, i + s.length(), pat, j + 1, map, set);
      }
  
      // 模式字符串没匹配过对应的 对匹配字符串进行 backtracking
      for(int k  = i; k < str.length(); k++){
        String p = str.substring(i, k + 1);
        if(set.contains(p)){
          continue;
        }
        map.put(c, p);
        set.add(p);
  
        if(isMatch(str, k+ 1, pat, j + 1, map, set)){ // 一直往下dfs到头了 都可以匹配的话 才能返回true
          return true;
        }
  
        map.remove(c);
        set.remove(p);
      }
      return false;
    }
  }
  