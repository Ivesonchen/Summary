/**
 * Given an array of integers, move all the 0s to the right end of the array.

The relative order of the elements in the original array does not need to be maintained.

Assumptions:

The given array is not null.
Examples:

{1} --> {1}
{1, 0, 3, 0, 1} --> {1, 3, 1, 0, 0} or {1, 1, 3, 0, 0} or {3, 1, 1, 0, 0}
 */

public class Solution {
    public int[] moveZero(int[] array) {
      // Write your solution here
  
        int start = 0;
  
        for(int i = 0; i < array.length; i++){
          if(array[i] != 0) {
            int temp = array[i];
            array[i] = array[start];
            array[start++] = temp;
          }
        }
  
        return array;
    }
}

/*
  while(right < length){
    if(arr[right] == 0){
      arr[left++] = arr[right++];
    }else {
      right++;
    }
  }
*/

 /**
  * #### Two Pointers
- Outside pointer that moves in certain condition. 
- Save appropirate elements
  */


  //left index 
  //right index

  while(left <= right){
    if(arr[left] != 0){
      left ++;
    }else if(arr[right] == 0){
      right --;
    }else {
      swap(arr[left], arr[right]);
      left ++;
      right --;
    }
  }

  return left;