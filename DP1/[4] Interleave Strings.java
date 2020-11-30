/**
 * Given three strings A, B and C. Determine if C can be created by merging A and B in a way that maintains the relative order of the characters in A and B.

Assumptions

none of A, B, C is null
Examples

C = "abcde", A = "acd", B = "be", return true
C = "abcde", A = "adc", B = "be", return false
 */

public class Solution {
    public boolean canMerge(String a, String b, String c) {
      // Write your solution here
      int aLen = a.length();
      int bLen = b.length();
  
      if(aLen + bLen != c.length()) return false;
  
      boolean[][] dp = new boolean[aLen + 1][bLen + 1];
  
      // int cIdx = 0;
      // dp[0][0] = true;
  
      for(int i = 0; i <= aLen; i++){
        for(int j = 0; j <= bLen; j++){
          if(i == 0 && j == 0) dp[i][j] = true;
  
          if(i > 0 && a.charAt(i - 1) == c.charAt(i + j - 1)) {
            dp[i][j] = dp[i][j] || dp[i - 1][j];
          }
          if(j > 0 && b.charAt(j - 1) == c.charAt(i + j - 1)) {
            dp[i][j] = dp[i][j] || dp[i][j - 1];
          }
        }
      }
  
      return dp[aLen][bLen];
    }
  }