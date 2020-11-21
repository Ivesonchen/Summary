/**
 * Given a non-negative integer array representing the heights of a list of adjacent bars. Suppose each bar has a width of 1. 
 * Find the largest amount of water that can be trapped in the histogram.

Assumptions

The given array is not null
Examples

{ 2, 1, 3, 2, 4 }, the amount of water can be trapped is 1 + 1 = 2 (at index 1, 1 unit of water can be trapped and index 3, 1 unit of water can be trapped)
 */
/**
 *  每次只移动 低的那个墙
 */

public class Solution {
    public int maxTrapped(int[] array) {
      // Write your solution here
      int left = 0;
      int right = array.length - 1;
      int leftMax = Integer.MIN_VALUE;
      int rightMax = Integer.MIN_VALUE;
      int res = 0;
  
      while(left < right){
        leftMax = Math.max(leftMax, array[left]);
        rightMax = Math.max(rightMax, array[right]);
        
        if(leftMax < rightMax){
          res += leftMax - array[left];
          left++;
        } else {
          res += rightMax - array[right];
          right--;
        }
      }
      return res;
    }
}
  