/**
 * Given an array of integers, sort the elements in the array in ascending order. The quick sort algorithm should be used to solve this problem.

Examples

{1} is sorted to {1}
{1, 2, 3} is sorted to {1, 2, 3}
{3, 2, 1} is sorted to {1, 2, 3}
{4, 2, -3, 6, 1} is sorted to {-3, 1, 2, 4, 6}
Corner Cases

What if the given array is null? In this case, we do not need to do anything.
What if the given array is of length zero? In this case, we do not need to do anything.
 */

 /**
  * The key process in quickSort is partition(). 
  Target of partitions is, given an array and an element x of array as pivot, 
  put x at its correct position in sorted array and put all smaller elements (smaller than x) before x, 
  and put all greater elements (greater than x) after x. All this should be done in linear time.
  注意 i 的变化  
  */

public class Solution {
    public int[] quickSort(int[] array) {
      // Write your solution here
      doQuickSort(array, 0, array.length-1);
      return array;
    }
  
    public void doQuickSort(int[] array, int l, int r){
      if(l < r){
        int pivodIndex = partition(array, l, r);
  
        doQuickSort(array, l, pivodIndex - 1);
        doQuickSort(array, pivodIndex + 1, r);
      }
    }
  
    public int partition(int[] array, int l, int r){
  
      int pivod = array[r];
      int i = l - 1;
  
      for(int j = l; j <= r; j ++){
        if(array[j] < pivod){
          i ++;
          int temp = array[i];
          array[i] = array[j];
          array[j] = temp;
        }
      }
  
      // int temp = array[r];
      array[r] = array[i + 1];
      array[i + 1] = pivod;
  
      return i + 1;
    }
  }
  
/**
 * Worst complexity: n^2
   Average complexity: n*log(n)
   Best complexity: n*log(n)
 */