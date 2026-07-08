/**
 * Given an array of balls, where the color of the balls can only be Red, Green, Blue or Black, 
 * sort the balls such that all balls with same color are grouped together and from left to right the order is 
 * Red->Green->Blue->Black. 
 * (Red is denoted by 0, Green is denoted by 1,  Blue is denoted by 2 and Black is denoted by 3).

Examples

{0} is sorted to {0}
{1, 0} is sorted to {0, 1}
{1, 3, 1, 2, 0} is sorted to {0, 1, 1, 2, 3}
Assumptions

The input array is not null.
 */

public class Solution {
    public int[] rainbowSortII(int[] array) {
      // Write your solution here
  
      // 一遍 for 循环   把 red 0 的数给换到最左边    然后就转化成了 rainbow sort 1
      int start = 0;
      for(int i = 0; i < array.length; i++){
        if(array[i] == 0){
          //swap(i, start)
          swap(array, i, start);

          start ++;
        }
      }// red 0 给换到了最左边
  
      // if(start >= array.length) break;
      int right = array.length - 1;
      int left = start;
      while(start <= right){
        if(array[start] == 3){
          swap(array, start, right--);
        } else if (array[start] == 2){
          start++;
        } else {
          swap(array, start++, left++);
        }
      }
      return array;
    }
  
    public void swap(int[] array, int left, int right){
      int temp = array[left];
      array[left] = array[right];
      array[right] = temp;
    }
  }
  