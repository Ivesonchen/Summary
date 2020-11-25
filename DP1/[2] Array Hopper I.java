/**
 * Given an array A of non-negative integers, you are initially positioned at index 0 of the array. A[i] means the maximum jump distance from that position (you can only jump towards the end of the array). Determine if you are able to reach the last index.

Assumptions

The given array is not null and has length of at least 1.
Examples

{1, 3, 2, 0, 3}, we are able to reach the end of array(jump to index 1 then reach the end of the array)

{2, 1, 1, 0, 2}, we are not able to reach the end of array
 */

//Option1 推动一个fastest的挡板 
// 如果可以到达 (array.length - 1) 就说明可以return true
// 如果 到达 位置i 之后 新更新的fastest 没有大于 位置i ( <= i ) 就可以停止了
public class Solution {
    public boolean canJump(int[] array) {
      // Write your solution here
      int fastest = 0;
  
      for(int i = 0; i < array.length; i++){
        fastest = Math.max(array[i] + i, fastest);
        if(fastest >= array.length - 1) return true;
        if(fastest <= i) return false;
      }
  
      return true;
    }
}