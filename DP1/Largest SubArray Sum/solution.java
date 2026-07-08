/**
 * Given an unsorted integer array, find the subarray that has the greatest sum. Return the sum.

Assumptions

The given array is not null and has length of at least 1.
Examples

{2, -1, 4, -2, 1}, the largest subarray sum is 2 + (-1) + 4 = 5

{-2, -1, -3}, the largest subarray sum is -1
 */

/**
 *  {2, -1, 4, -2, 1}
 *   2   1  5   3  4    res = 每个位置最大的sum - preSum 里最小的
 * 
 * 注意初始值 和 相减的顺序
 * 当min值为负的时候 才有更新的意义
 * max初始值 要是 array第一个值 不能初始为0
 * 计算max 要在 更新 min值之前 才能体现出 算的是 pre
 */

public class Solution {
    public int largestSum(int[] array) {
      // Write your solution here
      int min = 0;
      int preSum = 0;
      int max = array[0];
  
      for(int num : array){
        preSum += num;
        max = Math.max(max, preSum - min);
        min = Math.min(min, preSum);
      }
  
      return max;
    }
  }

// Option 2     Greedy
  class Solution {
    public int maxSubArray(int[] nums) {
        int res = nums[0];
        int sum = nums[0];
        
        for(int i = 1; i < nums.length; i++){
            sum = Math.max(nums[i], sum + nums[i]); //比较 是加上之前数组的值 大  还是  不加上之前的值  另起炉灶比较大
            res = Math.max(res, sum);
        }
        return res;
    }
}

// Option 3 dp 的思想

public int maxSubArray(int[] nums){
    int[] dp = new int[nums.length];
    dp[0] = nums[0];
    int res = dp[0];
    for(int i = 1; i < nums.length; i++){
        dp[i] = (dp[i-1] < 0 ? 0 : dp[i - 1]) + nums[i];
        res = Math.max(res, dp[i]);
    }
    return res;
}