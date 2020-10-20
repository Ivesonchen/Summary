/**
 * Given an integer array of size N - 1 sorted by ascending order, containing all the numbers from 1 to N except one, find the missing number.

Assumptions

The given array is not null, and N >= 1
Examples

A = {1, 2, 4}, the missing number is 3
A = {1, 2, 3}, the missing number is 4
A = {}, the missing number is 1
 */

public class Solution {
    public int missing(int[] array) {
      // Write your solution here
      int left = 0, right = array.length - 1;
  
      while(left <= right){
        int mid = left + (right - left) / 2;
  
        if(array[mid] - 1 == mid){
          left = mid + 1;
        } else {
          right = mid - 1;
        }
      }
  
      return left + 1;
    }
  }