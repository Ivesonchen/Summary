/**
 * Given a target integer T and an integer array A sorted in ascending order, 
 * find the index of the first occurrence of T in A or return -1 if there is no such index.

Assumptions

There can be duplicate elements in the array.
Examples

A = {1, 2, 3}, T = 2, return 1
A = {1, 2, 3}, T = 4, return -1
A = {1, 2, 2, 2, 3}, T = 2, return 1
Corner Cases

What if A is null or A of zero length? We should return -1 in this case.
 */

 // why use left < right - 1  因为 我们在left right变化的时候 并没有+1, 所以要在相邻的时候就 break， 而不是交叉越过的时候break
public class Solution {
    public int firstOccur(int[] array, int target) {
      // Write your solution here
      if(array == null || array.length == 0) return -1;
  
      int left = 0, right = array.length - 1;
  
      while(left < right - 1){  // if left get right neighbors, then terminate
        int mid = (left + right) / 2;
        if(array[mid] == target){
          right = mid;   // 向左查询的倾向 就算是相等了 也要向左继续找 直到推到头
        } else if(array[mid] < target){
          left = mid;
        } else {
          right = mid;
        }
      }
  
      //post  先比较左边
      if(array[left] == target){
        return left;
      } else if(array[right] == target){
        return right;
      } else {
        return -1;
      }
    }
  }

/**
 * 同理适用于 last occurence
 * int lastPossiableResult = -1;
 * 
 * while(left <= right){
 * 
 *  if(mid == target){
 *    right = mid - 1;
 *    lastPossiableResult = mid;
 *  } else {
 *    ..........
 *  }
 * }
 */