/**
 * Find all pairs of elements in a given array that sum to the given target number. Return all the pairs of indices.

Assumptions

The given array is not null and has length of at least 2.

Examples

A = {1, 3, 2, 4}, target = 5, return [[0, 3], [1, 2]]

A = {1, 2, 2, 4}, target = 6, return [[1, 3], [2, 3]]
 */

 // hashmap key 是 complement value是一个list 存 下标

public class Solution {
    public List<List<Integer>> allPairs(int[] array, int target) {
      // Write your solution here
      List<List<Integer>> res = new ArrayList<>();

      Map<Integer, List<Integer>> map = new HashMap<>();

      for(int i = 0; i < array.length; i++){
        int complement = target - array[i];
        if(map.containsKey(array[i])){
          List<Integer> value = map.get(array[i]);
          for(int j = 0; j < value.size(); j++){
            List<Integer> temp = new ArrayList<>();
            temp.add(value.get(j));
            temp.add(i);
            res.add(temp);
          }
        }
        List<Integer> newValue = map.getOrDefault(complement, new ArrayList<Integer>());
        newValue.add(i);
        map.put(complement, newValue);
      } 

      return res;
    }
  }