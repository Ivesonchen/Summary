/**
 * Given an integer array of length L, find the number that occurs more than 0.5 * L times.

Assumptions

The given array is not null or empty
It is guaranteed there exists such a majority number
Examples

A = {1, 2, 1, 2, 1}, return 1
 */

 // 应该不能用 O(n) O(1) 的解决方案吧

public class Solution {
    public int majority(int[] array) {
      // Write your solution here
      Arrays.sort(array);
  
      return array[array.length / 2];
    }
  }
  