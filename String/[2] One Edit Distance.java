/**
 * Determine if two given Strings are one edit distance.

One edit distance means you can only insert one character/delete one character/replace one character to another character in one of the two given Strings and they will become identical.

Assumptions:

The two given Strings are not null
Examples:

s = "abc", t = "ab" are one edit distance since you can remove the trailing 'c' from s so that s and t are identical

s = "abc", t = "bcd" are not one edit distance

https://leetcode.com/problems/one-edit-distance/solution/
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

class Solution {
  public boolean isOneEditDistance(String s, String t) {
    int ns = s.length();
    int nt = t.length();

    // Ensure that s is shorter than t.
    if (ns > nt)
      return isOneEditDistance(t, s);

    // The strings are NOT one edit away distance  
    // if the length diff is more than 1.
    if (nt - ns > 1)
      return false;

    for (int i = 0; i < ns; i++)
      if (s.charAt(i) != t.charAt(i))
        // if strings have the same length
        if (ns == nt)
          return s.substring(i + 1).equals(t.substring(i + 1));
        // if strings have different lengths
        else
          return s.substring(i).equals(t.substring(i + 1));

    // If there is no diffs on ns distance
    // the strings are one edit away only if
    // t has one more character. 
    return (ns + 1 == nt);
  }
}