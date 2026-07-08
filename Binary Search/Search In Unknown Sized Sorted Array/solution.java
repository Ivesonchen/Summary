/**
 * Given a integer dictionary A of unknown size, where the numbers in the dictionary are sorted in ascending order, determine if a given target integer T is in the dictionary. Return the index of T in A, return -1 if T is not in A.

Assumptions

dictionary A is not null
dictionary.get(i) will return null(Java)/INT_MIN(C++)/None(Python) if index i is out of bounds
Examples

A = {1, 2, 5, 9, ......}, T = 5, return 2
A = {1, 2, 5, 9, 12, ......}, T = 7, return -1
 */

 // 注意 out of bound 的处理

public class Solution {
    public int search(Dictionary dict, int target) {
      // Write your solution here
      int slow = 0, fast = 1;
      while(dict.get(fast) != null && dict.get(fast) < target){
        slow = fast;
        fast = 2 * fast;
      }
      // if(dict.get(fast) == null) return -1;
      return bs(dict, slow, fast, target);
    }
  
    private int bs(Dictionary dict, int left, int right, int target){
      while(left <= right){
        int mid = left + (right - left) / 2;
  
        if(dict.get(mid) != null && dict.get(mid) == target){
          return mid;
        } else if (dict.get(mid) != null && dict.get(mid) < target){
          left = mid + 1;
        } else {
          right = mid - 1;
        }
      }
      return -1;
    }
  }