/**
 * Given two arrays A and B, determine whether or not there exists a pair of elements, one drawn from each array, 
 * that sums to the given target number.

Assumptions

The two given arrays are not null and have length of at least 1
Examples

A = {3, 1, 5}, B = {2, 8}, target = 7, return true(pick 5 from A and pick 2 from B)

A = {1, 3, 5}, B = {2, 8}, target = 6, return false
 */

// hmmmmm   size m n      O(m + n)  O(m)

public class Solution {
    public boolean existSum(int[] a, int[] b, int target) {
      // Write your solution here
  
      Map<Integer, Integer> map = new HashMap<>();
  
      for(int i = 0; i < a.length; i++){
        map.put(a[i], 1);
      }
  
      for(int i = 0; i < b.length; i++){
        int complement = target - b[i];
        if(map.containsKey(complement)) return true;
      }
  
      return false;
    }
}