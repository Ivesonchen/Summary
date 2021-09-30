/** 
Given a target integer T and an integer array A, A is sorted in ascending order first, 
then shifted by an arbitrary number of positions.

For Example, A = {3, 4, 5, 1, 2} (shifted left by 2 positions). 
Find the index i such that A[i] == T or return -1 if there is no such index.

Assumptions

There are no duplicate elements in the array.
Examples

A = {3, 4, 5, 1, 2}, T = 4, return 1
A = {1, 2, 3, 4, 5}, T = 4, return 3
A = {3, 5, 6, 1, 2}, T = 4, return -1
Corner Cases

What if A is null or A is of zero length? We should return -1 in this case.
*/

public class Solution {
    public int search(int[] array, int target) {
      // Write your solution here
  
      int len = array.length;
      int left = 0;
      int right = len - 1;
        // find the index of the smallest value using binary search.
        // Loop will terminate since mid < hi, and lo or hi will shrink by at least 1.
        // Proof by contradiction that mid < hi: if mid==hi, then lo==hi and loop would have been terminated.
      while(left < right){
        int mid = left + (right - left) / 2;
  
        if(array[mid] > array[right]){
          left = mid + 1;
        } else {
          right = mid;
        }
      }
    // lo==hi is the index of the smallest value and also the number of places rotated.

      int rot = left;
      left = 0; right = len - 1;

    // The usual binary search and accounting for rotation.
      while(left <= right){
        int mid = left + (right - left) / 2;
        int realMid = (mid + rot) % len;
        if(array[realMid] == target) return realMid;
        if(array[realMid] < target) left = mid + 1;
        else right = mid - 1;
      }
      return -1;
    }
  }
  