/**
 * Implement regular expression matching with support for '.' and '*'. 
 * '.' Matches any single character. 
 * '*' Matches zero or more of the preceding element. 
 * The matching should cover the entire input string (not partial).

Example

isMatch("aa","a") → false

isMatch("aa","aa") → true

isMatch("aaa","aa") → false

isMatch("aa", "a*") → true

isMatch("aa", ".*") → true

isMatch("ab", ".*") → true

isMatch("aab", "c*a*b") → true
 */


 /**
  * 如果 p.charAt(j) == s.charAt(i) : dp[i][j] = dp[i-1][j-1]；
    如果 p.charAt(j) == '.' : dp[i][j] = dp[i-1][j-1]；
    如果 p.charAt(j) == '*'：
    如果 p.charAt(j-1) != s.charAt(i) : dp[i][j] = dp[i][j-2] //in this case, a* only counts as empty
    如果 p.charAt(i-1) == s.charAt(i) or p.charAt(i-1) == '.'：
    dp[i][j] = dp[i-1][j] //in this case, a* counts as multiple a
    or dp[i][j] = dp[i][j-1] // in this case, a* counts as single a
    or dp[i][j] = dp[i][j-2] // in this case, a* counts as empty
  */
public class Solution {
    public boolean isMatch(String input, String pattern) {
      // Write your solution here
  
      int m = input.length();
      int n = pattern.length();
  
      boolean[][] dp = new boolean[m + 1][n + 1];
      dp[0][0] = true;
  
      for(int i = 2; i <= n; i++){
        if(pattern.charAt(i - 1) == '*'){
          dp[0][i] = dp[0][i - 2];
        }
      }
      // 如果出现这种情况 下一个位置不管是什么都可以匹配
  
      for(int i = 0; i < m; i++){
        for(int j = 0; j < n; j++){
          if(pattern.charAt(j) == '.'){
            dp[i + 1][j + 1] = dp[i][j];
          }
          if(pattern.charAt(j) == input.charAt(i)){
            dp[i+1][j+1] = dp[i][j];
          }
  
          if(pattern.charAt(j) == '*'){
            if(pattern.charAt(j - 1) != input.charAt(i) && pattern.charAt(j - 1) != '.'){
              dp[i + 1][j + 1] = dp[i + 1][j - 1];      // a* only counts as empty
            } else {
              dp[i + 1][j + 1] = dp[i + 1][j] || dp[i][j + 1] || dp[i + 1][j - 1];
              // a* counts as multiple a    or     a* counts as single a      or     a* counts as empty
            }
          }
        }
      }
      return dp[m][n];
    }
  }