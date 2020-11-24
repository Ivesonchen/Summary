/**
 * Given an array containing only 0s and 1s, find the length of the longest subarray of consecutive 1s.

Assumptions

The given array is not null
Examples

{0, 1, 0, 1, 1, 1, 0}, the longest consecutive 1s is 3.
 */

public class Solution {
    public int longest(int[] array) {
      // Write your solution here
      int res = 0;
      int count = 0;
  
      for(int num : array){
        if(num == 1) count += 1;
        else {
          count = 0;
        }
  
        res = Math.max(res, count);
      }
  
      return res;
    }
}