/**
 * Search for a target number in a bitonic array, return the index of the target number if found in the array, or return -1.

A bitonic array is a combination of two sequence: the first sequence is a monotonically increasing one and the second sequence is a monotonically decreasing one.

Assumptions:

The array is not null.
Examples:

array = {1, 4, 7, 11, 6, 2, -3, -8}, target = 2, return 5.
 */

 //先找出 max 值的位置  然后 分两边 来进行binary search

public class Solution {
    public int search(int[] array, int target) {
      // Write your solution here
      if(array.length == 0) return -1;
      int max = findMax(array);
      int left = 0;
      int right = array.length - 1;
      if(target == array[max]) return max;
  
      int resLeft = bsa(array, target, left, max);
      int resRight = bsd(array, target, max, right);
  
      if(resLeft == -1) return resRight;
      else return resLeft;
    }
  
    private int bsa(int[] array, int target, int left, int right){
      while(left <= right){
        int mid = left + (right - left) / 2;
  
        if(array[mid] == target){
          return mid;
        } else if(array[mid] < target){
          left = mid + 1;
        } else {
          right = mid - 1;
        }
      }
      return -1;
    }
    private int bsd(int[] array, int target, int left, int right){
      while(left <= right){
        int mid = left + (right - left) / 2;
  
        if(array[mid] == target){
          return mid;
        } else if(array[mid] < target){
          right = mid - 1;
        } else {
          left = mid + 1;
        }
      }
      return -1;
    }
  
    private int findMax(int[] array){
      int left = 0;
      int right = array.length - 1;
  
      while(left <= right){
        int mid = left + (right - left) / 2;
  
        if(mid == 0 || mid == array.length - 1) return mid;
  
        if(array[mid - 1] < array[mid] && array[mid] < array[mid + 1]){
          left = mid + 1;
        } else if(array[mid - 1] > array[mid] && array[mid] > array[mid + 1]){
          right = mid - 1;
        } else {
          return mid;
        }
      }
      return Math.min(left, right);
    }
  }
  