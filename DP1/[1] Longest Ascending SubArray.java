/**
 * Given an unsorted array, find the length of the longest subarray in which the numbers are in ascending order.

Assumptions

The given array is not null
Examples

{7, 2, 3, 1, 5, 8, 9, 6}, longest ascending subarray is {1, 5, 8, 9}, length is 4.

{1, 2, 3, 3, 4, 4, 5}, longest ascending subarray is {1, 2, 3}, length is 3.
 */

/*
    {7, 2, 3, 1, 5, 8, 9, 6}
     1  1  2  1  2  3  4  1    生成这样一个数组  维持一个 global Max value
*/

public class Solution {
    public int longest(int[] array) {
      // Write your solution here
      if(array == null || array.length == 0) return 0;
      int res = 1;
      int[] dp = new int[array.length];
      dp[0] = 1;
      for(int i = 1; i < array.length; i++){
        if(array[i] > array[i - 1]){
          dp[i] = dp[i - 1] + 1;
          res = Math.max(res, dp[i]);
        } else {
          dp[i] = 1;
        }
      }
      return res;
    }
  }