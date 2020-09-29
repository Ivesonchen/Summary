/**
 * Given an integer array, determine how many pairs of elements, the absolute value of the difference between the two elements is the given target value.

Assumptions:

There could be elements with duplicate value in the array, and each of the elements is considered different.
The given array is not null and has length >= 2.
Examples:

array = {3, 1, 2, 1}, target = 2, there are 2 pairs { (3, 1), (3, 1) }
 */

//Tao-Lu 双指针 实现排列组合  + 条件筛选

public class Solution {
    public int pairs(int[] array, int target) {
      // Write your solution here
      Arrays.sort(array);
      int counter = 0;
  
      for(int i = 0; i < array.length - 1; i ++){
        int j = i + 1;
        while(j < array.length && array[j] - array[i] <= target){
          if(array[j] - array[i] == target){
            counter++;
          }
          j++;
        }
      }
      return counter;
    }
  }