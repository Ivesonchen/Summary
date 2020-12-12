/**
 * Given an array of integers and an integer k, 
 * find out whether there are two distinct indices i and j in the array 
 * such that nums[i] = nums[j] and the absolute difference between i and j is at most k.
 */

 //[[1,2,3,4,5,6,7,8,8],1]

 /**
  * #### HashSet
- 很巧妙地根据k range地条件, 把HashSet里面的值控制在[i - k, i]
- 每次不断地往set里面加新元素, 从set里减去末尾index的元素
- 而set.add(x)如果遇到重复, 会return false.
- 一旦在这个length k 的 range里面, 有重复, 就符合条件. 
- Time O(n)

  */

public class Solution {
    public boolean containsNearbyDuplicate(int[] nums, int k) {
      // Write your solution here
      if(nums == null || nums.length == 0 || k <= 0) return false;
      Set<Integer> set = new HashSet<>();
  
      for(int i = 0; i < nums.length; i++){
  
        if(i > k){
          set.remove(nums[i - k - 1]);
        }
  
        if(!set.add(nums[i])) return true;
      }
  
      return false;
    }
  }

  /**
   * /*
Thoughts:
Store in hashmap<value, index>. When there is a duplicate, check against k.
Though, quite slow: O(n * h), where h is the possible duplicates.
 In the extreme case when n = h, it becomes O(n^2)
*/

class Solution {
    public boolean containsNearbyDuplicate(int[] nums, int k) {
        if (nums == null || nums.length == 0 || k <= 0) {
            return false;
        }
        Map<Integer, List<Integer>> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (map.containsKey(nums[i])) {
                for (int index : map.get(nums[i])) {
                    if (Math.abs(index - i) <= k) {
                        return true;
                    }
                }
            } else {
                map.put(nums[i], new ArrayList<>());
            }
            map.get(nums[i]).add(i);
        }
        return false;
    }
}