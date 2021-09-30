/**
 * Given a target integer T and an integer array A, A is sorted in ascending order first, then shifted by an arbitrary number of positions.

For Example, A = {3, 4, 5, 1, 2} (shifted left by 2 positions). Find the index i such that A[i] == T or return -1 if there is no such index.

Assumptions

There could be duplicate elements in the array.
Return the smallest index if target has multiple occurrence. 
Examples

A = {3, 4, 5, 1, 2}, T = 4, return 1
A = {3, 3, 3, 1, 3}, T = 1, return 3
A = {3, 1, 3, 3, 3}, T = 1, return 1
​Corner Cases

What if A is null or A is of zero length? We should return -1 in this case.
 */

public class Solution {
  public int search(int[] array, int target) {
    // Write your solution here
    if(array == null || array.length == 0) return -1;
    int start = 0, end = array.length - 1;
    while(start <= end){
      int mid = (start + end) / 2;
      if(array[mid] == target){
        // 向左找
        while(mid >= 0 && array[mid] == target){
          mid --;
        }
        return mid + 1;
      }
      // right sorted || array[mid] < array[start]
      // 
      if(array[start] == array[end]) {
        end--;
        continue;
      }
      
      if(array[mid] <= array[end]){       //右边这条线 
        if(array[mid] < target && target <= array[end]){
          start = mid + 1;
        } else {
          end = mid - 1;
        }
      } else { // 左边这条线
        if(array[start] <= target && target < array[mid]){
          end = mid - 1;
        } else {
          start = mid + 1;
        }
      }

    }
    return -1;
  }
}