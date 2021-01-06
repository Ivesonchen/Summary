/**
 * Given a sorted array of positive numbers in ascending order, 
 * find the smallest positive integer value that cannot be represented as sum of elements of any sub-sequence of the input array.

Assumptions:

The given array is not null.
Examples:

array = {1, 3, 6, 10, 11, 15}, result is 2
array = {1, 1, 1, 1}, result is 5
 */

public class Solution {
    public int firstMissing(int[] array) {
      // Write your solution here
      int n = array.length;
      int res = 1; // Initialize result   
  
      // Traverse the array and increment 'res' if arr[i] is 
      // smaller than or equal to 'res'. 
      for (int i = 0; i < n && array[i] <= res; i++) 
          res = res + array[i]; 
  
      return res; 
    }
  }
  