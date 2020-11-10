/**
 * Given an array of integers, sort the elements in the array in ascending order. The insertion sort algorithm should be used to solve this problem.

Examples

{1, 2, 3} is sorted to {1, 2, 3}
{4, 2, -3, 6, 1} is sorted to {-3, 1, 2, 4, 6}
Corner Cases

What if the given array is null? In this case, we do not need to do anything.
What if the given array is of length zero? In this case, we do not need to do anything.
 */

 // {4, 2, -1, 0}

 /**
    从 第二位置的 数开始    取出此位置上的数 存为 key，
    然后 向前比较 条件为 index >= 0 && key还比 pre位置的 值 小
    循环中 把 pre位置的值 顺便的 往后 复制移动 pre--
    蹦出循环的时候 把key放到该放的位置上 (注意 该放的位置是 pre + 1)
  */

public class Solution {
    public int[] sort(int[] array) {
      // Write your solution here
  
      for(int i = 1; i < array.length; i++){
        int key = array[i];
        int pre = i - 1;
  
        while(pre >= 0 && key < array[pre]){
          array[pre+1] = array[pre];
          pre--;
        }
        array[pre+1] = key;
      }
  
      return array;
    }
  }
  
/**
 * Worst complexity: n^2
   Average complexity: n^2
   Best complexity: n
   Space complexity: 1
 */