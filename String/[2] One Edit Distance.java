/**
 * Determine if two given Strings are one edit distance.

One edit distance means you can only insert one character/delete one character/replace one character to another character in one of the two given Strings and they will become identical.

Assumptions:

The two given Strings are not null
Examples:

s = "abc", t = "ab" are one edit distance since you can remove the trailing 'c' from s so that s and t are identical

s = "abc", t = "bcd" are not one edit distance
 */

public class Solution {
    public boolean oneEditDistance(String source, String target) {
      // Write your solution here
      int lenM = source.length();
      int lenN = target.length();
  
      int[][] dp = new int[lenM + 1][lenN + 1];
  
      for(int i = 1 ; i < lenM + 1; i++){
        dp[i][0] = i;
      }
  
      for(int i = 1; i < lenN + 1; i++){
        dp[0][i] = i;
      }
  
      for(int i = 1; i < lenM + 1; i++){
        for(int j = 1; j < lenN + 1; j++){
          
          if(source.charAt(i - 1) == target.charAt(j - 1)){
            dp[i][j] = dp[i - 1][j - 1];
          } else {
            int mn = Math.min(dp[i-1][j], dp[i][j-1]);
  
            dp[i][j] = Math.min(mn, dp[i-1][j-1]) + 1;
          }
        }
      }
      return dp[lenM][lenN] == 1;
    }
}