/**
 * Determine if the given integer array has three indices such that i < j < k and a[i] < a[j] < a[k].

Assumptions:

The given array is not null.
Examples:

{1, 5, 2, 4}, return true since i = 0, j = 2, k = 3

{4, 3, 2, 1}, return false
https://leetcode.com/problems/longest-increasing-subsequence/solution/
 */

// 对于每个位置i 往前找位置j上  先比较 如果满足ascending order 在 dp[j]基础上+1   在j循环过程中取最大，  并且最终res 要取dp[i] 中的最大

public class Solution {
    public boolean existIJK(int[] array) {
      // Write your solution here
      if(array.length < 3) return false;
      int[] dp = new int[array.length];
      dp[0] = 1;
      int res = 0;
      for(int i = 1; i < array.length; i++){
        int maxLen = 0;
        for(int j = 0; j < i; j++){
          if(array[i] > array[j]){
            maxLen = Math.max(maxLen, dp[j]);
          }
        }
        dp[i] = maxLen + 1;
        res = Math.max(res, dp[i]);
      }
      return res >= 3;
    }
  }