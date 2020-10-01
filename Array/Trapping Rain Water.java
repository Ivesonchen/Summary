/**
 * Given n non-negative integers representing an elevation map where the width of each bar is 1, compute how much water it is able to trap after raining.

For example, 
Given [0,1,0,2,1,0,1,3,2,1,2,1], return 6.
 */

public class Solution {
    public int trapWater(int[] A) {
      // Write your solution here
      int left = 0;
      int right = A.length - 1;
      int leftMax = Integer.MIN_VALUE;
      int rightMax = Integer.MIN_VALUE;
      int res = 0;
  
      while(left < right){
        leftMax = Math.max(leftMax, A[left]);
        rightMax = Math.max(rightMax, A[right]);
        
        if(leftMax < rightMax){
          res += leftMax - A[left];
          left++;
        } else {
          res += rightMax - A[right];
          right--;
        }
      }
      return res;
    }
  }
  