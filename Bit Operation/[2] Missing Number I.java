/**
 * Given an integer array of size N - 1, containing all the numbers from 1 to N except one, find the missing number.

Assumptions

The given array is not null, and N >= 1
Examples

A = {2, 1, 4}, the missing number is 3
A = {1, 2, 3}, the missing number is 4
A = {}, the missing number is 1

https://leetcode.com/problems/missing-number/solution/
 */

public class Solution {
    public int missing(int[] array) {
      // Write your solution here
      int res = array.length + 1;
      for(int i = 0; i < array.length; i++){
        res ^= array[i] ^ (i + 1);
      }
  
      return res;
    }
  }