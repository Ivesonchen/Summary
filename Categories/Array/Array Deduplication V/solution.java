/**
 * Given an integer array(not guaranteed to be sorted), remove adjacent repeated elements. 
 * For each group of elements with the same value keep at most two of them. 
 * Do this in-place, using the left side of the original array and maintain the relative order of the elements of the array. 
 * Return the final array.

Assumptions

The given array is not null
Examples

{1, 2, 2, 3, 3, 3} --> {1, 2, 2, 3, 3}

{2, 1, 2, 2, 2, 3} --> {2, 1, 2, 2, 3}  
 */

// 还是三指针的做法     注意最外层的while循环条件 永远是以 最 fast的那个指针 为条件
//Tao-Lu
public class Solution {
    public int[] dedup(int[] array) {
      // Write your solution here
      int start = 0, slow = 0, fast = 0;
      while(fast < array.length){
        while(fast < array.length && array[slow] == array[fast]){
          fast++;
        }
        
        if(fast - slow <= 2){
          for(int i = slow; i < fast; i++){
            array[start] = array[i];
            start++; 
          }
        } else {
          for(int i = slow; i < slow + 2; i++){
            array[start] = array[i];
            start++; 
          }
        }
        slow = fast;
      }
  
      return Arrays.copyOfRange(array, 0, start);
    }
}