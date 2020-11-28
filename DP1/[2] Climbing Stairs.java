/**
 * There are in total n steps to climb until you can reach the top. You can climb 1 or 2 steps a time. 
 * Determine the number of ways you can do that.

Example:

Input: n = 4, Return 5.
 */

public class Solution {
    // public int stairs(int n) {
    //   // Write your solution here
    //   if(n == 1) return 1;
    //   if(n == 2) return 2;
  
    //   return stairs(n - 1) + stairs(n - 2);
    // }
  
    public int stairs(int n){
      if(n <= 2) return n;
      int[] dp = new int[n];
  
      dp[0] = 1;
      dp[1] = 2;
  
      for(int i = 2; i < n; i++){
        dp[i] = dp[i - 1] + dp[i - 2];
      }
  
      return dp[n - 1];
    }
  }