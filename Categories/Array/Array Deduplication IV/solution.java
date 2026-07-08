/**
 * Given an unsorted integer array, remove adjacent duplicate elements repeatedly, from left to right. 
 * For each group of elements with the same value do not keep any of them.

Do this in-place, using the left side of the original array. Return the array after deduplication.

Assumptions

The given array is not null
Examples

{1, 2, 3, 3, 3, 2, 2} → {1, 2, 2, 2} → {1}, return {1}
 */

/**
(如果 array/string+stack 的题目看见让你用 in-place, 不要慌, 指针可以解决)

structure:

top: pointer top points to top of ‘virtual’ stack. top = -1 meaning empty stack.

leftside of top represents the numbers should be kept (including top).

cur: pointer points to current processing number

如果 array[cur] == stack.top, 那么 cur 往后走, 找到第一个和 top 不一样的数. pop stack.

如果 array[cur] != stack.top, 那么把当前值 push 到 stack 上, cur++;

time O(n)
space O(1)
 */

public class Solution {
    public int[] dedup(int[] array){
  
      //edge case 可以加
      if (array.length == 0|| array.length == 1) {
          return array;
      }
  
      int top = -1, cur = 0;
  
      while(cur < array.length){
        if(top == -1 || array[top] != array[cur]){
          top++;
          array[top] = array[cur];
          cur++;
        } else {
          while(cur < array.length && array[cur] == array[top]){
            cur++;
          }
          top--;
        }
      }
  
      return Arrays.copyOfRange(array, 0, top+1);
    }
  }