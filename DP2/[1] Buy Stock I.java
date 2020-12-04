/**
 * Given an array of positive integers representing a stock’s price on each day. 
 * On each day you can only make one operation: either buy or sell one unit of stock and you can make at most 1 transaction. 
 * Determine the maximum profit you can make.

Assumptions

The given array is not null and is length of at least 2.
Examples

{2, 3, 2, 1, 4, 5}, the maximum profit you can make is 5 - 1 = 4
 */

 // min stands for pre min value;
 // caculate profit every time and update pre min value;
 
public class Solution {
    public int maxProfit(int[] array) {
      // Write your solution here
      int len = array.length;
      if(len == 0) return 0;
  
      int min = array[0];
      int res = 0;
      for(int i = 1; i < len; i++){
        min = Math.min(min, array[i]);
        res = Math.max(array[i] - min, res);
      }
      return res;
    }
  }
  