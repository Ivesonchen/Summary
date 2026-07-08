/**
 * Given an unsorted array of doubles, find the subarray that has the greatest product. Return the product.

Assumptions

The given array is not null and has length of at least 1.
Examples

{2.0, -0.1, 4, -2, -1.5}, the largest subarray product is 4 * (-2) * (-1.5) = 12
 */

// Option 1 brute force


//Option 2  DP

//细节注意  要用一个temp变量 存原始的max 给计算min用
public class Solution {
    public double largestProduct(double[] array) {
      // Write your solution here
      double min = array[0], max = array[0], res = array[0];
  
      for(int i = 1; i < array.length; i++){
  
        double temp = max;
        max = Math.max(Math.max(max * array[i], min * array[i]), array[i]);
        min = Math.min(Math.min(min * array[i], temp * array[i]), array[i]);
  
        res = Math.max(res, max);
      }
  
      return res;
    }
}