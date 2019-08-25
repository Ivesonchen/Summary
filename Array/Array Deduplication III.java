/**
 * Given a sorted integer array, remove duplicate elements. 
 * For each group of elements with the same value do not keep any of them. 
 * Do this in-place, using the left side of the original array and and maintain the relative order of the elements of the array. 
 * Return the array after deduplication.

Assumptions

The given array is not null
Examples

{1, 2, 2, 3, 3, 3} → {1}
 */

public class Solution {
    public int[] dedup(int[] array) {
      // Write your solution here
  
      int fast = 0; 
      int slow = 0;
  
      while(fast < array.length){
  
        int begin = slow;
        while(fast < array.length && array[begin] == array[fast]){
          fast++;
        }
  
        if(fast - begin == 1){
          array[slow++] = array[begin]; 
        }
      }
  
      if(slow == 0){
        return new int[0];
      }
  
      return array;
    }
  }

  /**
   * 三指针  相当于  
   * 
   * slow 是基础指针
   * 
   * begin 和 fast 来测量重复数字的长度（有几个）
   */