/**
 * Given an unsorted integer array, return the local minimum's index.

An element at index i is defined as local minimum when it is smaller than all its possible two neighbors a[i - 1] and a[i + 1]

(you can think a[-1] = +infinite, and a[a.length] = +infinite)

Assumptions:

The given array is not null or empty.
There are no duplicate elements in the array.
There is always one and only one result for each case.
 */

 /**
  * Lets say you have a mid number at index x, nums[x]
if (num[x+1] > nums[x]), that means a peak element HAS to exist on the right half of that array, because (since every number is unique) 1. the numbers keep increasing on the right side, and the peak will be the last element. 2. the numbers stop increasing and there is a 'dip', or there exists somewhere a number such that nums[y] < nums[y-1], which means number[x] is a peak element.

This same logic can be applied to the left hand side (nums[x] < nums[x-1]).
*/

public class Solution {
    public int localMinimum(int[] array) {
      // Write your solution here
      int len = array.length;
      int left = 0;
      int right = len - 1;
  
      while(left < right){
        int mid1 = left + (right - left) / 2;
        int mid2 = mid1 + 1;
        if(array[mid1] < array[mid2]) 
          right = mid1;
        else
          left = mid2;
      }
  
      return left;
    }
  }
  