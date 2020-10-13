/**
 * Given an integer number n, find its integer square root.

Assumption:

n is guaranteed to be >= 0.
Example:

Input: 18, Return: 4

Input: 4, Return: 2
 */

public class Solution {
    public int sqrt(int x) {
      // Write your solution here
      if(x == 0) return 0;
      if(x == 1) return 1;
  
      int left = 0, right = x;
  
      while(left <= right){
        int mid = left + (right - left) / 2;
  
        if(mid > x / mid){
          right = mid - 1;
        } else {
          if((mid + 1) > x / (mid + 1)) return mid;
          left = mid + 1;
        }
      }
  
      return 0;
    }
  }