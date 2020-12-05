/**
 * Given a rope with positive integer-length n, how to cut the rope into m integer-length parts with length p[0], p[1], ...,p[m-1], 
 * in order to get the maximal product of p[0]*p[1]* ... *p[m-1]? m is determined by you and must be greater than 0 
 * (at least one cut must be made). Return the max product you can have.

Assumptions

n >= 2
Examples

n = 12, the max product is 3 * 3 * 3 * 3 = 81(cut the rope into 4 pieces with length of each is 3).
 */

/**
 *          size = 1m               M[1] = 1 or 0       //Or to be decided
 * 
 *          size = 2m               M[1] =   ?????  M[2]
 *                                  For a rope of size = 2m, there is only 1 way to cut this rope
 *                                  Case 1: left = 1m,      right = 1m
 *                                          M[2] = max(1, M[1]) * max(1, M[1]) = 1
 * 
 *          size = 3m               M[3] =   ?????  M[1] M[2]
 *                                  For a rope of size = 3m, there are 2 ways to cut this rope
 *                                  Case 1: left = 1m       right = 1m
 *                                          product_case1 = max(1, M[1]) * max(2, M[2]) = 1 * 2 = 2
 *                                  Case 2: left = 2m       right = 1m
 *                                          product_case2 = max(2, M[2]) * max(1, M[1]) = 2 * 1 = 2
 *                                  M[3] = max(case1, case2) = max(2, 2) = 2
 * 
 *          size = 4m               M[4] =   ?????  M[1] M[2] M[3]
 *                                  For a rope of size = 4m, there are 3 ways to cut this rope
 *                                  Case 1: left = 1m       right = 3m
 *                                          product_case1 = max(1, M[1]) * max(3, M[3]) = 1 * 3 = 3
 *                                  Case 2: left = 2m       right = 2m
 *                                          product_case2 = max(2, M[2]) * max(2, M[2]) = 2 * 2 = 4
 *                                  Case 2: left = 3m       right = 1m
 *                                          product_case3 = max(3, M[3]) * max(1, M[1]) = 3 * 1 = 3
 *                                  M[3] = max(case1, case2, case3) = max(3, 4, 3) = 4
 */

 // Time O(n^2)  Space O(n)
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