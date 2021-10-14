/**
 * Given an integer array A, A is sorted in ascending order first then shifted by an arbitrary number of positions, 
 * For Example, A = {3, 4, 5, 1, 2} (shifted left by 2 positions). Find the index of the smallest number.

Assumptions

There are no duplicate elements in the array
Examples

A = {3, 4, 5, 1, 2}, return 3
A = {1, 2, 3, 4, 5}, return 0
Corner Cases

What if A is null or A is of zero length? We should return -1 in this case.
 */

 // Tao-Lu   在一个sorted 然后 shifted的 数组中找 最小值 O(Logn)

public class Solution {
    public int shiftPosition(int[] array) {
      // Write your solution here
      if(array == null || array.length == 0) return -1;
      int len = array.length;
  
      int left = 0;
      int right = len - 1;
  
      while(left < right){
        int mid = left + (right - left) / 2;
  
        if(array[mid] > array[right]) left = mid + 1;
        else right = mid;
      }
  
      return left;
    }
  }