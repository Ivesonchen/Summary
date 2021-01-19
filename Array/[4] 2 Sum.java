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

/**
 * 方法1：暴力，复杂度O(n^2) 会超时

方法2：hash。用一个哈希表，存储每个数对应的下标，复杂度O(n).

方法3：先排序，然后左右夹逼，排序O(nlogn)，左右夹逼O(n)，最终O(nlogn)。但是注意，这题需要返回的是下标，而不是数字本身，而重新排序会导致index失真，因此这个方法行不通。
 */
/*
    key         value
    互补数      互补数所在位置
*/
  class Solution {
    public int[] twoSum(int[] nums, int target) {
        HashMap<Integer, Integer> map = new HashMap<>();
        int[] res = new int[]{-1,-1};
        for(int i = 0; i < nums.length; i++) {
            if(map.containsKey(target - nums[i])){
                res[0] = map.get(target - nums[i]);
                res[1] = i;
            }
            map.put(nums[i],i);
        }
        return res;
    }
}

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

