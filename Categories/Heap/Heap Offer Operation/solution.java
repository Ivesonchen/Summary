/**
 * Given a binary min heap in array format. The last cell of the array is not used.

Now offer a new element into the heap.

Assumptions:

The given array is not null and has length >= 1
Examples:

array = {2, 3, 4, 0}, offer(1) --> {1, 2, 4, 3}
 */

public class Solution {
    public int[] offerHeap(int[] array, int ele) {
      // Write your solution here
      int len = array.length;
  
      array[len - 1] = ele;
      // swap(array, 0, len - 1);
  
      for(int i = len; i >= 0; i--){
        int leftIndex = i * 2 + 1;
        int rightIndex = i * 2 + 2;
  
        if(leftIndex < len && array[i] > array[leftIndex]){
          swap(array, i, leftIndex);
        }
  
        if(rightIndex < len && array[i] > array[rightIndex]){
          swap(array, i, rightIndex);
        }
      }
      return array;
    }
  
    public void swap(int[] array, int i, int j){
      int temp = array[i];
      array[i] = array[j];
      array[j] = temp;
    }
  }