/**
 * Given an integer array of length L, find all numbers that occur more than 1/K * L times if any exist.

Assumptions

The given array is not null or empty
K >= 2
Examples

A = {1, 2, 1, 2, 1}, K = 3, return [1, 2]
A = {1, 2, 1, 2, 3, 3, 1}, K = 4, return [1, 2, 3]
A = {2, 1}, K = 2, return []
*/
// Tao-Lu 找 deduplication 的方法
public class Solution {
    public List<Integer> majority(int[] array, int k) {
      // Write your solution here
      Arrays.sort(array);
      List<Integer> res = new ArrayList<>();
      int times = array.length / k;
  
      int slow = 0;
      int fast = slow;
  
      while(slow < array.length && fast < array.length){
        while(fast < array.length && array[fast] == array[slow]){
          fast++;
        }
  
        if(fast - slow > times){
          res.add(array[slow]);
        }
  
        slow = fast;
      }
      return res;
    }
}
  