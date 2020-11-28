/**
 * Given an array of non-negative integers, you are initially positioned at index 0 of the array. 
 * A[i] means the maximum jump distance from that position (you can only jump towards the end of the array). 
 * Determine the minimum number of jumps you need to jump out of the array.

By jump out, it means you can not stay at the end of the array. Return -1 if you can not do so.

Assumptions

The given array is not null and has length of at least 1.
Examples

{1, 3, 2, 0, 2}, the minimum number of jumps needed is 3 (jump to index 1 then to the end of array, then jump out)

{3, 2, 1, 1, 0}, you are not able to jump out of array, return -1 in this case.
 */

 // similar to Array Hopper II   set DP length to len + 1   

public class Solution {
    public int minJump(int[] array) {
      // Write your solution here
      int len = array.length;
      int[] dp = new int[len + 1];
      dp[0] = 0;
  
      for(int i = 1; i < len + 1; i ++){
        dp[i] = Integer.MAX_VALUE;
        for(int j = 0; j < i; j++){
          if(array[j] + j >= i) dp[i] = Math.min(dp[i], dp[j] + 1);
          if(j == i - 1 && dp[i] == Integer.MAX_VALUE) return -1; 
        }
      }
  
      return dp[len];
    }
}