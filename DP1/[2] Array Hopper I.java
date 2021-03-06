/**
 * Given an array A of non-negative integers, you are initially positioned at index 0 of the array. A[i] means the maximum jump distance from that position (you can only jump towards the end of the array). Determine if you are able to reach the last index.

Assumptions

The given array is not null and has length of at least 1.
Examples

{1, 3, 2, 0, 3}, we are able to reach the end of array(jump to index 1 then reach the end of the array)

{2, 1, 1, 0, 2}, we are not able to reach the end of array
 */

//Option1 推动一个fastest的挡板 
// 如果可以到达 (array.length - 1) 就说明可以return true
// 如果 到达 位置i 之后 新更新的fastest 没有大于 位置i ( <= i ) 就可以停止了
public class Solution {
    public boolean canJump(int[] array) {
      // Write your solution here
      int fastest = 0;
  
      for(int i = 0; i < array.length; i++){
        fastest = Math.max(array[i] + i, fastest);
        if(fastest >= array.length - 1) return true;
        if(fastest <= i) return false;
      }
  
      return true;
    }
}

/**
 * dp[0] = true;
 * i == [0 ~ end]
 * j == [0 ~ i]   dp[i] 代表了 位置i 能否被到达
 */
public boolean canJump(int[] nums){
    if(nums == null || nums.length == 0) return true;
    int n = nums.length;
    boolean[] dp = new boolean[n];
    dp[0] = true;

    for(int i = 1; i < n; i++){
        dp[i] = false;
        for(int j = 0; j < i; j++){           
            // 任何一个位置i 之前的位置([0 ~ i] 出现过 dp[j] == True 并且此位置可以reach或超过end 点，就把dp[i] 设为true)
            if(dp[j] && j + nums[j] >= i){
                dp[i] = true;
                break;
            }
        }
    }
    return dp[n - 1];
}