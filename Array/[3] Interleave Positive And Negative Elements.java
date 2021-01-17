/**
 * Given an array with both positive and negative numbers in random order. 
 * Shuffle the array so that positive and negative numbers are put in position with even and odd indices, respectively.

If there are more positive/negative numbers, put them at the end of the array. 
The ordering of positive/negative numbers does not matter.

Assumptions:

The given array is not null.
There is no 0 in the array.
Examples:

{1, 2, 3, 4, 5, -1, -1, -1} --> {1, -1, 2, -1, 3, -1, 4, 5}  (The ordering of positive/negative numbers do not matter)
 */

public class Solution {
    public int[] interleave(int[] array) {
      // Write your solution here
  
      if(array.length <= 1) return array;
  
      // not in place sort   用 两个ArrayList 分别存 positive 和 negative number 然后 merge 到 res array里
  
      int len = array.length;
  
      int left = 0, right = len - 1;
  
      // sort function  左边是正数部分 右边是负数部分
      while(left <= right){
        if(array[left] < 0) {
          swap(array, left, right);
          right --;
        } else {
          left++;
        }
      }   //类似于 quicksort 的 partition， 用0作为pivot  左边都是正数 右边都是负数
  
      int i = 1;
      while(i < len && left < len){
        swap(array, i, left);
        left++;
        i += 2;
      }  
      // 然后 把右边的负数给插入到左边相应的位置
      return array;
    }
  
    public void swap(int[] array, int i, int j){
      int temp = array[i];
      array[i] = array[j];
      array[j] = temp;
    }
  }
  