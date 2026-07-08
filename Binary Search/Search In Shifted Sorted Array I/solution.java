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
https://leetcode.com/problems/search-in-rotated-sorted-array/
*/


public int search(int[] nums, int target) {
  if (nums == null || nums.length == 0) {
      return -1;
  }
  
  /*.*/
  int left = 0, right = nums.length - 1;
  //when we use the condition "left <= right", we do not need to determine if nums[left] == target
  //in outside of loop, because the jumping condition is left > right, we will have the determination
  //condition if(target == nums[mid]) inside of loop
  while (left <= right) {
      //left bias
      int mid = left + (right - left) / 2;
      if (target == nums[mid]) {
          return mid;
      }
      //if left part is monotonically increasing, or the pivot point is on the right part
      if (nums[left] <= nums[mid]) {
          //must use "<=" at here since we need to make sure target is in the left part,
          //then safely drop the right part
          if (nums[left] <= target && target < nums[mid]) {
              right = mid - 1;
          }
          else {
              //right bias
              left = mid + 1;
          }
      }

      //if right part is monotonically increasing, or the pivot point is on the left part
      else {
          //must use "<=" at here since we need to make sure target is in the right part,
          //then safely drop the left part
          if (nums[mid] < target && target <= nums[right]) {
              left = mid + 1;
          }
          else {
              right = mid - 1;
          }
      }
  }
  return -1;
}

/**
 * 1. 先找到rotation 的位置  rot
 * 2. 然后在再行一次binary search 用公式 realmid = (fakeMid + rot) % len
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
  