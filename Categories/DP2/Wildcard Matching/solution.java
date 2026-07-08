/**
 * Given two strings where first string is a normal string and second string may contain wild card characters. 
 * Write a function that returns true if the two strings match. 
 * The following are allowed wildcard characters in first string. 

* --> Matches with 0 or more instances of any character or set of characters.
? --> Matches with any one character.

Assumptions:

The two strings are both not null.
Assume there is no more than one '*' adjacent to each other.
Examples:

input = "abc", pattern = "?*", return true.
 */

public class Solution {
    public boolean match(String input, String pattern) {
      // Write your solution here
      int m = input.length();
      int n = pattern.length();
  
      boolean[][] dp = new boolean[m + 1][n + 1];
      dp[0][0] = true;
  
      for(int i = 1; i <= n; i++){
        if(pattern.charAt(i - 1) == '*'){
          dp[0][i] = true;
        } else {
          break;
        }
      }
  
      for(int i = 1; i <= m; i++){
        for(int j = 1; j <= n; j++){
          //字符相等或者 等于 ?    斜角传递
          //字符等于*    上左传递
          if(pattern.charAt(j - 1) == '*'){
            dp[i][j] = dp[i - 1][j] || dp[i][j - 1];
          } else if(input.charAt(i - 1) == pattern.charAt(j - 1) || pattern.charAt(j - 1) == '?'){
            dp[i][j] = dp[i - 1][j - 1];
          }
        }
      }
  
      return dp[m][n];
    }
  }