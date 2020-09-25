/**
 * Given an array of balls with k different colors denoted by numbers 1- k, sort the balls.

Examples

k=1, {1} is sorted to {1}
k=3, {1, 3, 2, 1, 2} is sorted to {1, 1, 2, 2, 3}
k=5, {3, 1, 5, 5, 1, 4, 2} is sorted to {1, 1, 2, 3, 4, 5, 5}
Assumptions

The input array is not null.
k is guaranteed to be >= 1.
k << logn.
*/
/**
 * Two pass,  count sort.
 * 用一个数组来计算 array 中出现的所有次数
 * 然后利用这些计数 重新生成一个 所要求的的 array
 * O(n) O(k)
 */

public class Solution {
    public int[] rainbowSortIII(int[] array, int k) {
      // Write your solution here
      int[] count = new int[k];
  
      for(int i = 0; i < array.length; i++){
        count[array[i]-1] ++;
      }
  
      int start = 0;
      for(int i = 0; i < k; i++){
        for(int j = 0; j < count[i]; j++){
          array[start++] = i + 1;
        }
      }
  
      return array;
    }
}
  