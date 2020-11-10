/**
 * Given an array of integers, sort the elements in the array in ascending order. The selection sort algorithm should be used to solve this problem.

Examples

{1} is sorted to {1}
{1, 2, 3} is sorted to {1, 2, 3}
{3, 2, 1} is sorted to {1, 2, 3}
{4, 2, -3, 6, 1} is sorted to {-3, 1, 2, 4, 6}
Corner Cases

What if the given array is null? In this case, we do not need to do anything.
What if the given array is of length zero? In this case, we do not need to do anything.
 */

  // {4, 2, -1, 0}
  // 思想就是 maintain 一个数组的两个部分  前一部分是一个 sort好的array （因为每次添加的都是剩下数组里面的最小值）
  // 后一部分是一个 unsorted的 array 只用来每次找最小值就好

  /**
   * 从头index开始   
   * 再来一个循环找出包含头在内的 最小值 的index， 保存下来这个index
   * 然后 这个index的值 和 头所在位置 交换
   * 循环完毕
   */

public class Solution {
    public int[] solve(int[] array) {
      // Write your solution here
  
      for(int i = 0; i < array.length; i ++){
  
        int smallIndex = i;
        for(int j = smallIndex; j < array.length; j++){
          //find smallest ele
          if(array[j] < array[smallIndex]){
            smallIndex = j;
          }
        }
        int temp = array[i];
        array[i] = array[smallIndex];
        array[smallIndex] = temp;
      }
  
      return array;
    }
  }
  
  //time complixity O(n**2)
  //space complixity O(1)