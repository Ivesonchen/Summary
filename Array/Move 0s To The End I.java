/**
 * Given an array of integers, move all the 0s to the right end of the array.

The relative order of the elements in the original array does not need to be maintained.

Assumptions:

The given array is not null.
Examples:

{1} --> {1}
{1, 0, 3, 0, 1} --> {1, 3, 1, 0, 0} or {1, 1, 3, 0, 0} or {3, 1, 1, 0, 0}
 */

 // 这里用的是 找到0然后start 跟 index 交换
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

// 这种解法 只负责 把不是0 的 赋值 在前面       所以 并没有间接的 把 0 移动到 最后 所以要 再赋值 0
// Move Zeroes
// Time Complexity: O(n), Space Complexity: O(1)
public class Solution {
  public void moveZeroes(int[] nums) {
      int index = 0;
      for (int i = 0; i < nums.length; ++i) {
          if (nums[i] != 0) {
              nums[index++] = nums[i];
          }
      }
      for (int i = index; i < nums.length; ++i) {
          nums[i] = 0;
      }
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

//Tao-Lu   
/**
    当使用双指针的时候 while(left < right)

    里面   
    第一个 while(left < array.length && condition) 循环 left ++
    第二个 while(right >= 0 && condition) 循环 right --

 * 
 */

// 有点丑陋 
  public int[] moveZero(int[] array) {
    int i = 0, j = array.length - 1;

    while(i < j) {
      while(i < array.length && array[i] != 0){
        i++;
      }

      while(j >= 0 && array[j] == 0) {
        j--;
      }
      if(i < array.length && j >= 0 && i < j){
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
        i++;
        j--;
      }
    }
    return array;
  }