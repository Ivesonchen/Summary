/**
 * Determine if there exist two elements in a given array, the sum of which is the given target number.

Assumptions

The given array is not null and has length of at least 2
​Examples

A = {1, 2, 3, 4}, target = 5, return true (1 + 4 = 5)

A = {2, 4, 2, 1}, target = 4, return true (2 + 2 = 4)

A = {2, 4, 1}, target = 4, return false
 */
// map key 存 complement value 存 index

public class Solution {
    public boolean existSum(int[] array, int target) {
      // Write your solution here
      Map<Integer, Integer> map = new HashMap<>();
  
      for(int i = 0; i < array.length; i++){
        int complement = target - array[i];
        if(map.containsKey(array[i])) return true;
  
        map.put(complement, 1);
      }
  
      return false;
    }
  }