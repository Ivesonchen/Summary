/**
 * There is a function signFunc(x) that returns:

1 if x is positive.
-1 if x is negative.
0 if x is equal to 0.
You are given an integer array nums. Let product be the product of all values in the array nums.

Return signFunc(product).
 */


class Solution {
  public int arraySign(int[] nums) {
      if(nums == null || nums.length == 0) return 0;
      
      int res = 1;
      
      for(int num : nums) {
          if(num == 0) return 0;
          else if(num < 0) {
              res *= -1;
          } else {
              res *= 1;
          }
      }
      
      return res;
  }
}