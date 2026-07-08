/**
 * Given a string S and a string T, find the minimum window in S which will contain all the characters in T

Input: S = “ADOBECODEBANC”

          T = “ABC”

Output: “BANC”

https://github.com/grandyang/leetcode/issues/76
 */

public class Solution {
    public String minWindow(String source, String target) {
      // Write your solution here
      String res = "";
      if(source.length() == 0 || target.length() == 0) return res;
      HashMap<Character, Integer> map = new HashMap<>();
  
      int start = 0, count = 0, minLen = Integer.MAX_VALUE;
  
      for(int i = 0; i < source.length(); i++){
        map.put(source.charAt(i), 0);
      }
  
      for(int i = 0; i < target.length(); i++){
        map.put(target.charAt(i), map.getOrDefault(target.charAt(i), 0) + 1);
      }
  
  
      for(int i = 0; i < source.length(); i++){
        char c = source.charAt(i);
  
        if(map.get(c) > 0){
          count ++;
        }
        map.put(c, map.get(c) - 1);
        while(count == target.length()){
          if(i - start + 1 < minLen){
            minLen = i - start + 1;
            res = source.substring(start, i + 1);
          }
          char temp = source.charAt(start);
          if(map.get(temp) >= 0){
            count --;
          }
          map.put(temp, map.get(temp) + 1);
          start ++;
        }
      }
      return res;
    }
}

// for reference    hashmap in java  is much more complicate
class Solution {
    public:
        string minWindow(string s, string t) {
            string res = "";
            unordered_map<char, int> letterCnt;
            int left = 0, cnt = 0, minLen = INT_MAX;
            for (char c : t) ++letterCnt[c];
            for (int i = 0; i < s.size(); ++i) {
                if (--letterCnt[s[i]] >= 0) ++cnt;
                while (cnt == t.size()) {
                    if (minLen > i - left + 1) {
                        minLen = i - left + 1;
                        res = s.substr(left, minLen);
                    }
                    if (++letterCnt[s[left]] > 0) --cnt;
                    ++left;
                }
            }
            return res;
        }
    };