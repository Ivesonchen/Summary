/**
 * Determine if the given integer array is min heap.
 */

 // value at i < value at childrens index

public class Solution {
    public boolean isMinHeap(int[] array) {
      // Write your solution here
      if(array == null || array.length == 0) return true;
  
      int len = array.length;
  
      for(int i = 0; i < len; i++){
        int po1 = 2 * i + 1;
        int po2 = 2 * i + 2;
  
        if(po1 < len && array[i] > array[po1]) return false;
        if(po2 < len && array[i] > array[po2]) return false;
      }
  
      return true;
    }
  }
  