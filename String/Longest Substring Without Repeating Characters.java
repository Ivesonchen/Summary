/**
 * Given a string s, find the length of the longest substring without repeating characters.

Input: s = "abcabcbb"
Output: 3
Explanation: The answer is "abc", with the length of 3.

Constraints:

0 <= s.length <= 5 * 104
s consists of English letters, digits, symbols and spaces.
 */

 /**
  * Approach 1: Brute Force
Intuition

Check all the substring one by one to see if it has no duplicate character.
https://leetcode.com/problems/longest-substring-without-repeating-characters/solution/
  */
  public class Solution {
    public int lengthOfLongestSubstring(String s) {
        int n = s.length();

        int res = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                if (checkRepetition(s, i, j)) {
                    res = Math.max(res, j - i + 1);
                }
            }
        }
        return res;
    }
    private boolean checkRepetition(String s, int start, int end) {
        int[] chars = new int[128];

        for (int i = start; i <= end; i++) {
            char c = s.charAt(i);
            chars[c]++;
            if (chars[c] > 1) {
                return false;
            }
        }

        return true;
    }
}

// sliding window
public class Solution {
  public int lengthOfLongestSubstring(String s) {
      int[] chars = new int[128];

      int left = 0;
      int right = 0;

      int res = 0;
      while (right < s.length()) {
          char r = s.charAt(right);
          chars[r]++;

          while (chars[r] > 1) {
              char l = s.charAt(left);
              chars[l]--;
              left++;
          }

          res = Math.max(res, right - left + 1);

          right++;
      }
      return res;
  }
}
//Approach 3: Sliding Window Optimized
class Solution {
  public int lengthOfLongestSubstring(String s) {
      int[] ds = new int[128]; // 最后一次出现某个字符的位置
      Arrays.fill(ds, -1);
      int len = s.length();
      
      int left = 0, right = 0;
      int res = 0;
      
      while(right < len){
          char r = s.charAt(right);
          
          int index = ds[r];
          if(index != -1 && index >= left && index < right) left = index + 1;
          
          res = Math.max(res, right - left + 1);
          ds[r] = right;
          right++;
      }
      
      return res;
  }
}