/**
 * Given an array of integers, move all the 0s to the right end of the array.

The relative order of the elements in the original array need to be maintained.

Assumptions:

The given array is not null.
Examples:

{1} --> {1}
{1, 0, 3, 0, 1} --> {1, 3, 1, 0, 0}
 */


public class Solution {
    public int[] moveZero(int[] array) {
      // Write your solution here
  
      int start = 0;
      for(int i = 0; i < array.length; i++){
        if(array[i] != 0) {
          int temp = array[i];
          array[i] = array[start];
          array[start++] = temp;
        }
      }
      return array;
    }
  }


 // 区别在于 要保持 顺序不变    所以不可以用 two pointer的方法