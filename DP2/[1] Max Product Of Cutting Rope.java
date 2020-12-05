/**
 * Given a rope with positive integer-length n, how to cut the rope into m integer-length parts with length p[0], p[1], ...,p[m-1], 
 * in order to get the maximal product of p[0]*p[1]* ... *p[m-1]? m is determined by you and must be greater than 0 
 * (at least one cut must be made). Return the max product you can have.

Assumptions

n >= 2
Examples

n = 12, the max product is 3 * 3 * 3 * 3 = 81(cut the rope into 4 pieces with length of each is 3).
 */

 

public class Solution {
    public int maxProduct(int length) {
      // Write your solution here
      int[] dp = new int[length + 1];
  
      dp[0] = 0;
      dp[1] = 0;
  
      for(int i = 1; i <= length; i++){
        int curMax = 0;
        for(int j = 1; j < i; j++){
          curMax = Math.max(curMax, Math.max(j, dp[j]) * Math.max(i - j, dp[i - j]));
        }
        dp[i] = curMax;
      }
  
      return dp[length];
    }
  }