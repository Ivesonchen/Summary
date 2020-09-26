/**
 * Find all pairs of elements in a given array that sum to the pair the given target number. Return all the distinct pairs of values.

Assumptions

The given array is not null and has length of at least 2
The order of the values in the pair does not matter
Examples

A = {2, 1, 3, 2, 4, 3, 4, 2}, target = 6, return [[2, 4], [3, 3]]
 */
// 用 map 和 set 结合 去除可能使用的 重复数字， 从而去除可能重复使用的排列组合
// 问题是在于 要单独处理 当array[i] * 2 = target 的情况
// 在这个使用的是 用 halfCheck一个 flag 当出现两次 array[i] * 2 = target 时 halfCheck == 2， 然后 需要在进入条件之后， 把halfCheck 这个 flag 设为永久 false （这里把它++变成3）
public class Solution {
    public List<List<Integer>> allPairs(int[] array, int target) {
      // Write your solution here
  
      List<List<Integer>> res = new ArrayList<>();
  
      Map<Integer, Integer> map = new HashMap<>();
      Set<Integer> set = new HashSet<>();
      int halfCheck = 0;
  
      for(int i = 0; i < array.length; i++){
        int complment = target - array[i];
        if(array[i] * 2 == target) halfCheck ++;
  
        if(map.containsKey(array[i]) && (!set.contains(array[i]) || halfCheck == 2)){
  
          res.add(Arrays.asList(array[i], complment));
          if(array[i] * 2 == target) halfCheck++;
        }
        set.add(array[i]);
        map.put(complment, 1);
      }
  
      return res;
    }
  }