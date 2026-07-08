/**
 * Given an array A[0]...A[n-1] of integers, find out the length of the longest ascending subsequence.

Assumptions

A is not null
Examples
Input: A = {5, 2, 6, 3, 4, 7, 5}
Output: 4
Because [2, 3, 4, 5] is the longest ascending subsequence.
 */
// 对于每个i， 往前查找 对于 dp[j] < dp[i]   dp[i] = Math.max(dp[i], dp[j] + 1);

public class Solution {
    public int longest(int[] array) {
      // Write your solution here
      if(array == null || array.length == 0) return 0;
  
      int[] dp = new int[array.length];
      int res = 1;
  
      for(int i = 0; i < array.length; i++){
        dp[i] = 1;
        for(int j = i; j >= 0; j--){
          if(array[j] < array[i]){
            dp[i] = Math.max(dp[i], dp[j] + 1);
          }
        }
        res = Math.max(res, dp[i]);
      }
      return res;
    }
}

public int longest(int[] array) {
    if (array.length == 0) {
      return 0;
    }
    int[] dp = new int[array.length];
    int max = 1;
    for (int i = 0; i < dp.length; i++) {
      dp[i] = 1;
      for (int j = 0; j < i; j++) {
        if (array[j] < array[i]) {
          dp[i] = Math.max(dp[i], dp[j] + 1);
        }
      }
      max = Math.max(max, dp[i]);
    }
    return max;
  }