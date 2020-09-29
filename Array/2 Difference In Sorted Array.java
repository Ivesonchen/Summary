/**
 * Given a sorted array A, find a pair (i, j) such that A[j] - A[i] is identical to a target number(i != j).

If there does not exist such pair, return a zero length array.

Assumptions:

The given array is not null and has length of at least 2.
If more than one pair of index exits, return the one with ascending order.
Examples:

A = {1, 2, 3, 6, 9}, target = 2, return {0, 2} since A[2] - A[0] == 2.
A = {1, 2, 3, 6, 9}, target = -2, return {2, 0} since A[0] - A[2] == 2.
 */

/**
 * 题目上说了是 sorted array 要使用有序这个性质  worst case O(n^2)
 * 然后使用双指针i j 不停地 找遍所有组合
 * 
 * 使用 取绝对值  然后 拉大i 和 j 之间的距离    由于这个数组是个 sorted  那么实际上就是逐渐使 差值 增加到 target  结束与差值大于target
 */
//Tao-Lu 双指针 实现排列组合  + 条件筛选
public class Solution {
    public int[] twoDiff(int[] array, int target) {
      // Write your solution here
      for(int i = 0; i < array.length - 1; i++){
        int j = i;
        while(Math.abs(array[j] - array[i]) <= Math.abs(target)){
          j++;
          if(array[j] - array[i] == target){
            return new int[]{i, j};
          }
          if(array[i] - array[j] == target){
            return new int[]{j, i};
          }
        }
      }
      return new int[0];
    }
  }