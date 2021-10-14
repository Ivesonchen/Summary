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

/**
 * 
 */

public boolean search(int[] nums, int target) {
  int start  = 0, end = nums.length - 1;
  
  //check each num so we will check start == end
  //We always get a sorted part and a half part
  //we can check sorted part to decide where to go next
  while(start <= end){
      int mid = start + (end - start)/2;
      if(nums[mid] == target) return true;
      
      //if left part is sorted
      if(nums[start] < nums[mid]){
          if(target < nums[start] || target > nums[mid]){
              //target is in rotated part
              start = mid + 1;
          }else{
              end = mid - 1;
          }
      }else if(nums[start] > nums[mid]){
          //right part is rotated
          
          //target is in rotated part
          if(target < nums[mid] || target > nums[end]){
              end = mid -1;
          }else{
              start = mid + 1;
          }
      }else{
          //duplicates, we know nums[mid] != target, so nums[start] != target
          //based on current information, we can only move left pointer to skip one cell
          //thus in the worest case, we would have target: 2, and array like 11111111, then
          //the running time would be O(n)
          start ++;
      }
  }
  
  return false;
}

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