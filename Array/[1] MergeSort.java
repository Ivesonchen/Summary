/**
 * Given an array of integers, sort the elements in the array in ascending order. The merge sort algorithm should be used to solve this problem.

Examples

{1} is sorted to {1}
{1, 2, 3} is sorted to {1, 2, 3}
{3, 2, 1} is sorted to {1, 2, 3}
{4, 2, -3, 6, 1} is sorted to {-3, 1, 2, 4, 6}
Corner Cases

What if the given array is null? In this case, we do not need to do anything.
What if the given array is of length zero? In this case, we do not need to do anything.
 */

 // 思路  递归思想   先分叉    然后 再merge
 // 因为是原地 merge 所以要 使用 l, r, mid 这样的指针来标记subArray 的边界

 // Tao-Lu
 // 两个Array merge 的时候   注意 while循环的 套路 很有用

public class Solution {
    public int[] mergeSort(int[] array) {
      // Write your solution here
      sort(array, 0, array.length - 1);
      return array;
    }
  
    public void sort(int[] array, int l, int r){
      if(l < r){
        int mid = (l + r) / 2;
  
        sort(array, l, mid);
        sort(array, mid+1, r);
  
        merge(array, l, mid, r);
      }
    }
  
    public void merge(int[] array, int l, int mid, int r){
          // Find sizes of two subarrays to be merged 
          int n1 = mid - l + 1; 
          int n2 = r - mid; 
    
          /* Create temp arrays */
          int L[] = new int[n1]; 
          int R[] = new int[n2]; 
    
          /*Copy data to temp arrays*/
          for (int i = 0; i < n1; ++i) 
              L[i] = array[l + i]; 
          for (int j = 0; j < n2; ++j) 
              R[j] = array[mid + 1 + j]; 
    
          /* Merge the temp arrays */
    
          // Initial indexes of first and second subarrays 
          int i = 0, j = 0; 
    
          // Initial index of merged subarry array 
          int k = l; 
          while (i < n1 && j < n2) { 
              if (L[i] <= R[j]) { 
                  array[k] = L[i]; 
                  i++; 
              } else { 
                  array[k] = R[j]; 
                  j++; 
              } 
              k++; 
          } 
    
          /* Copy remaining elements of L[] if any */
          while (i < n1) { 
              array[k] = L[i]; 
              i++; 
              k++; 
          } 
    
          /* Copy remaining elements of R[] if any */
          while (j < n2) { 
              array[k] = R[j]; 
              j++; 
              k++; 
          }
    }
  }
  
  /**
    Worst complexity: n*log(n)
    Average complexity: n*log(n)
    Best complexity: n*log(n)
    Space complexity: n
  */