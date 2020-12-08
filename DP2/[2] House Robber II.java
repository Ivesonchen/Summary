/**
 * Note: This is an extension of House Robber.

After robbing those houses on that street, the thief has found himself a new place for his thievery so that he will not get too much attention. 
'This time, all houses at this place are arranged in a circle. 
That means the first house is the neighbor of the last one. 
Meanwhile, the security system for these houses remain the same as for those in the previous street.

Given a list of non-negative integers representing the amount of money of each house, 
determine the maximum amount of money you can rob tonight without alerting the police.
 */


 /**
  * 如果抢劫第一家，则不可以抢最后一家；否则，可以抢最后一家。因此，这个问题就转化成为了两趟动规，可以复用 "House Robber" 的代码。
  */

// O(n) O(1)
public class Solution {
    public int rob(int[] nums) {
      // Write your solution here
      if(nums.length == 1) return nums[0];
  
      return Math.max(helper(nums, 0, nums.length - 2), helper(nums, 1, nums.length - 1));
    }
  
    public int helper(int[] nums, int start, int end){
        int preMax = 0;
        int curMax = 0;
  
        for(int i = start; i <= end; i++){
          int temp = curMax;
          curMax = Math.max(curMax, preMax + nums[i]);
          preMax = temp;
        }
        return curMax;
    }
  }