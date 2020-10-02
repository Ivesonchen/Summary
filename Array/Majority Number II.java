/**
 * Given an integer array of length L, find all numbers that occur more than 1/3 * L times if any exist.

Assumptions

The given array is not null
Examples

A = {1, 2, 1, 2, 1}, return [1, 2]
A = {1, 2, 1, 2, 3, 3, 1}, return [1]
A = {1, 2, 2, 3, 1, 3}, return []
*/

public class Solution {
    public List<Integer> majority(int[] array) {
      // Write your solution here
      int times = array.length / 3;
      List<Integer> res = new ArrayList<>();
  
      Arrays.sort(array);
  
      int slow = 0;
      int fast = 0;
  
      while(slow < array.length && fast < array.length){
        while(fast < array.length && array[fast] == array[slow]){
          fast ++;
        }
        if(fast - slow > times){
          res.add(array[slow]);
        }
  
        slow = fast;
      }
      return res;
    }
  }