/**
 * Find the longest common substring of two given strings.

Assumptions

The two given strings are not null
Examples

S = “abcde”, T = “cdf”, the longest common substring of S and T is “cd”
 */

 /**
  * 二维数组 记录 字符串匹配长度 
    if(char at source != char at target) dp[i][j] = 0;
    else    dp[i][j] = dp[i - 1][j - 1] + 1
  * 注意 start 和 longest 的操作方法
  */

public class Solution {
    public String longestCommon(String source, String target) {
      // Write your solution here
      int sLen = source.length();
      int tLen = target.length();
  
      int[][] dp = new int[sLen + 1][tLen + 1];
      int start = 0, longest = 0;
  
      for(int i = 1; i <= sLen; i++){
        for(int j = 1; j <= tLen; j++){
          if(source.charAt(i - 1) == target.charAt(j - 1)){
            dp[i][j] = dp[i - 1][j - 1] + 1;
            if(dp[i][j] > longest){
              longest = dp[i][j];
              start = i - longest;
            }
          } else {
            dp[i][j] = 0;
          }
        }
      }
  
      return source.substring(start, start + longest);
    }
  }