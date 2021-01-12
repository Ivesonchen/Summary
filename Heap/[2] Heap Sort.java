/**
 * Heap Sort is a comparison based sorting algorithm with O(nlogn) time and O(1) space.

Requirements:

You have to do it in place, extra space used is no more than O(1).
Time complexity is O(nlogn).
 */

public class Solution {
    public int[] heapsort(int[] array) {
      // Write your solution here
      int n = array.length;
  
      for(int i = n/2 - 1; i >= 0; i--){
        heapify(array, n, i);
      }
  
      for(int i = n - 1; i >= 0; i--){
        int temp = array[0];
        array[0] = array[i];
        array[i] = temp;
  
        heapify(array, i, 0);
      }
  
      return array;
    }
  
    public void heapify(int arr[], int n, int i){
      int largest = i;
      int l = 2 * i + 1;
      int r = 2 * i + 2;
  
      if(l < n && arr[l] > arr[largest]) largest = l;
  
      if(r < n && arr[r] > arr[largest]) largest = r;
  
      if(largest != i){
        int swap = arr[i];
        arr[i] = arr[largest];
        arr[largest] = swap;
  
        heapify(arr, n, largest);
      }
    }
  }