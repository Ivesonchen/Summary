/**
 * Given an array A of non-negative integers, you are initially positioned at index 0 of the array. 
 * A[i] means the maximum jump distance from index i (you can only jump towards the end of the array). 
 * Determine the minimum number of jumps you need to reach the end of array. 
 * If you can not reach the end of the array, return -1.

Assumptions

The given array is not null and has length of at least 1.
Examples

{3, 3, 1, 0, 4}, the minimum jumps needed is 2 (jump to index 1 then to the end of array)

{2, 1, 1, 0, 2}, you are not able to reach the end of array, return -1 in this case.
 */

// dp O(n ^ 2)
/**
 * dp[i] 代表 可以到达位置i的 最小步骤
 * 注意edge case的处理
 */
public class Solution {
    public int minJump(int[] array) {
      // Write your solution here
      int len = array.length;
      int[] dp = new int[len];
      dp[0] = 0;
  
      for(int i = 1; i < len; i++){
        dp[i] = Integer.MAX_VALUE;
  
        for(int j = 0; j < i; j++){
          if(array[j] + j >= i) dp[i] = Math.min(dp[i], dp[j] + 1);
          if(j == i - 1 && dp[i] == Integer.MAX_VALUE) return -1;  
          // 如果检测到最后一个位置依然没有可以从前一个点到达i的时候   就直接return -1 表示此时已经无法到达
        }
      }
  
      return dp[len - 1];
    }
}


// Greedy
/**
 * 每个位置i 都在更新 当前的maxrange  固定值赋给farest 相当于手里的路费 （懒惰的）
 * 当 当前手里的（懒惰的）路费花光时再去更新 counter (代表了 numbers of jumps)
 */
public int minJump(int[] nums){
    if(nums == null || nums.length <= 1) return 0;

    int counter = 0, farest = 0, maxRange = 0, n = nums.length;
    for(int i = 0; i < n - 1; i++){
        maxRange = Math.max(maxRange, nums[i] + i);
        if(i == farest){
            counter++;
            farest = maxRange;
        }
    }
    return counter;
}