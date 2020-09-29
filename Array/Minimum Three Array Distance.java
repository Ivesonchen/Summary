/**
 * Given three sorted integer arrays, pick one element from each of them, what is the min value of |x - y| + |y - z| + |z - x|.

Assumptions:

The given three arrays are not null or empty.
Examples:

a = {1, 2, 3}

b = {4, 5}

c = {3, 4}

The minimum value is |3 - 4| + |4 - 4| + |4 - 3| = 2
 */
// 吐了 这是eazy ？？

// 使用 Find the closest number using binary search
// 然后进行“合理“的 循环     O(nlogn)

public class Solution {
    public int minDistance(int[] a, int[] b, int[] c) {
      // Write your solution here
      int min = Integer.MAX_VALUE;
  
      for(int i = 0; i < a.length; i++){
        int idxb = binarySearchGetMinIndex(b, a[i]);
        int idxc = binarySearchGetMinIndex(c, a[i]);
  
        min = Math.min(min, (Math.abs(a[i] - b[idxb]) + Math.abs(a[i] - c[idxc]) + Math.abs(b[idxb] - c[idxc])));
      }
  
      for(int j = 0; j < b.length; j++){
        int idxa = binarySearchGetMinIndex(a, b[j]);
        int idxc = binarySearchGetMinIndex(c, b[j]);
  
        min = Math.min(min, (Math.abs(b[j] - a[idxa]) + Math.abs(b[j] - c[idxc]) + Math.abs(a[idxa] - c[idxc])));
      }
  
      for(int k = 0; k < c.length; k++){
        int idxa = binarySearchGetMinIndex(a, c[k]);
        int idxb = binarySearchGetMinIndex(b, c[k]);
  
        min = Math.min(min, (Math.abs(c[k] - a[idxa]) + Math.abs(c[k] - b[idxb]) + Math.abs(a[idxa] - b[idxb])));
      }
  
      return min;
    }
  
    public int binarySearchGetMinIndex(int[] array, int target){
      int left = 0;
      int right = array.length - 1;
      while(left < right - 1){
        int mid = (left + right) / 2;
        if(array[mid] == target){
          return mid;
        } else if (array[mid] < target){
          left = mid;
        } else {
          right = mid;
        }
      }
      int leftAbs = Math.abs(array[left] - target);
      int rightAbs = Math.abs(array[right] - target);
  
      if(leftAbs <= rightAbs){
        return left;
      } else {
        return right;
      }
    }
  }
  