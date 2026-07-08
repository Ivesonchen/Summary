/**
 * Given an array of n positive integers and a positive integer s, 
 * find the minimal length of a contiguous subarray of which the sum ≥ s. If there isn't one, return 0 instead.

For example, given the array [2,3,1,2,4,3] and s = 7,
the subarray [4,3] has the minimal length under the problem constraint.
 */

 /**
  * 时刻紧缩window 紧缩后面的start的时候
  */

public class Solution {
    public int minSubArrayLen(int num, int[] nums) {
      // Write your solution here
      if(nums == null || nums.length == 0) return 0;
      int len = nums.length;
      int start = 0, sum = 0;
      int res = Integer.MAX_VALUE;
  
      for(int i = 0; i < len; i++){
        sum += nums[i];
        if(sum >= num){
          res = Math.min(res, i - start + 1);
        }
  
        while(sum >= num){
          res = Math.min(res, i - start + 1);
          sum -= nums[start];
          start++;
        }
      }
  
      
      return res == Integer.MAX_VALUE ? 0 : res;
    }
  }
  