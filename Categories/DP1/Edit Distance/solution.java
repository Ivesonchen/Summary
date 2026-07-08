/**
 * Given two strings of alphanumeric characters, 
 * determine the minimum number of Replace, Delete, and Insert operations needed to transform one string into the other.

Assumptions

Both strings are not null
Examples

string one: “sigh”, string two : “asith”

the edit distance between one and two is 2 (one insert “a” at front then replace “g” with “t”).
 */

/**
 * if    one.charAt(i - 1) == two.charAt(j - 1)  dp[i][j] = dp[i - 1][j - 1];
 * else     dp[i][j] =  左上, 上, 左， 三个数字中取最小数字  + 1
 * 
 */

public class Solution {
    public int editDistance(String one, String two) {
      // Write your solution here
  
      int oneLen = one.length();
      int twoLen = two.length();
  
      int[][] dp = new int[oneLen + 1][twoLen + 1];
  
      for(int i = 0; i <= oneLen; i++){
        dp[i][0] = i;
      }
  
      for(int i = 0; i <= twoLen; i++){
        dp[0][i] = i;
      }
  
      for(int i = 1; i <= oneLen; i++){
        for(int j = 1; j <= twoLen; j++){
          if(one.charAt(i - 1) == two.charAt(j - 1)){
            dp[i][j] = dp[i - 1][j - 1];
          } else {
            dp[i][j] = Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1])) + 1;
          }
        }
      }
  
      return dp[oneLen][twoLen];
    }
}