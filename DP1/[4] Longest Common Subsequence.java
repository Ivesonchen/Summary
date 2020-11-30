/**
 * Find the length of longest common subsequence of two given strings.

Assumptions

The two given strings are not null
Examples

S = “abcde”, T = “cbabdfe”, the longest common subsequence of s and t is {‘a’, ‘b’, ‘d’, ‘e’}, the length is 4.
 */


/**
 * 利用 order 来判定 是否
 */
public class Solution {
    public int longest(String source, String target) {
      // Write your solution here
      int sLen = source.length();
      int tLen = target.length();
  
      int[][] dp = new int[sLen + 1][tLen + 1];
      int order = 0;
      int res = 0;
      for(int i = 1; i <= sLen; i++){
        for(int j = 1; j <= tLen; j++){
          if(source.charAt(i - 1) == target.charAt(j - 1)){
            if(j > order){
              order = j;
              res++;
              break;
            }
          }
        }
      }
      return res;
    }
}

/**
 * Solution: match[i][j]: s前i个字母和t前j个字母的longest common subsequence (maybe not including i and j)

s.charAt(i - 1) == t.charAt(j - 1)时，match[i][j] = match[i - 1][j - 1] + 1，否则match[i][j] = Math.max(match[i - 1][j], match[i][j - 1]);
 */
public int longest(String s, String t) {
    int[][] match = new int[s.length() + 1][t.length() + 1];
    for (int i = 1; i <= s.length(); i++) {
      for (int j = 1; j <= t.length(); j++) {
        if (s.charAt(i - 1) == t.charAt(j - 1)) {
          match[i][j] = match[i - 1][j - 1] + 1;
        } else {
          match[i][j] = Math.max(match[i - 1][j], match[i][j - 1]);
        }
      }
    }
    return match[s.length()][t.length()];
  }