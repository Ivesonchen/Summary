/**
 * Given an array of integers, find out whether there are two distinct indices i and j in the array such that 
 * the absolute difference between nums[i] and nums[j] is at most t 
 * and the absolute difference between i and j is at most k.
 */

public class Solution {
    public boolean containsNearbyAlmostDuplicate(int[] nums, int k, int t) {
      // Write your solution here
  
      if (nums == null || nums.length == 0 || k <= 0 || t < 0) {
          return false;
      }
      TreeSet<Long> treeSet = new TreeSet<>();
      for (int i = 0; i < nums.length; i++) {
          Long target = treeSet.ceiling((long)nums[i] - t);
          if (target != null && target <= (long)nums[i] + t) {
              return true;
          }
          if (i >= k) {
              treeSet.remove((long)nums[i - k]);
          }
          treeSet.add((long)nums[i]);
      }
      return false;
    }
  }
  


 /**
  * #### TreeSet
- TreeSet还是一个set, 我们用来装已经visit过得item
- 如果window大小超过K, 那么把nums[i - k - 1] 去掉, 并且加上新的element
- 这里有个公式推算: (Math.abs(A-B) <= t) =>>>>> (-t <= A - B <= t) =>>>>>> A >= B - t, A <= B + t
- 也就是说, 如果对于 B = nums[i], 来说, 能找到一个target A, 满足上面的公式, 那么就可以 return true.
- Time O(nLogk), treeSet的大小不会超过k,  而 treeSet.ceiling(), treeSet.add(), treeSet.remove() 都是 O(logK)
- Space O(k)

#### Note
- 与Contains Duplicate II 类似概念. TreeSet有BST 因此可以直接用, 而不用自己构建BST
- 简化题目里面的重要条件 Math.abs(A-B) <= t 而推断出 A >= B - t, A <= B + t
- 并且需要需要用 TreeSet.ceiling(x): return number greater or equal to x. 这个用法要记住吧, 没别的捷径.

  */