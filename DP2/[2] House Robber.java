/**
 * You are a skilled robber planing to rob houses along a street. Each house has a certain amount of gold in it, 
 * but if you rob two adjacent houses the security system will automatically contact the police.

Given a list of non-negative integers representing the amount of gold in each house, 
return the maximum amount of gold you can rob without being caught.



Example: 

Input: [1,5,6,7]

Output: 12

Explanation: You can either rob house 0 and house 2 (1 + 6 = 7) or you can rob house 1 and 3 (5 + 7 = 12)

https://leetcode.com/problems/house-robber/discuss/156523/From-good-to-great.-How-to-approach-most-of-DP-problems.
 */

 /**
  * 这是一个多阶段最优化问题，且要走到最底部才能知道答案，因此广搜排除，只剩下贪心和动规。贪心明显要排除，只剩下动规。

设状态 f[i] 为到位置i时能抢到的金钱最大和，那么状态转移方程如下：

f[i]=max(f[i-1], f[i-2] + nums[i])

其含义是，如果不选择i，则抢到的钱是f[i-1]，如果选择i，则能抢到的钱是f[i-2] + nums[i]。
  */

public class Solution {
    public int rob(int[] num) {
      // Write your solution here
      int len = num.length;
      if(num == null || len == 0) return 0;
      if(len == 1) return num[0];
      int[] dp = new int[len];
  
      dp[0] = num[0];
      dp[1] = Math.max(num[0], num[1]);
  
      for(int i = 2; i < len; i++){
        dp[i] = Math.max(dp[i - 1], dp[i - 2] + num[i]);
      }
  
      return dp[len - 1];
    }
  }

  /**
   * 在状态转移方程中，我们可以发现 f[i]仅仅依赖前两项，因此用两个整数变量即可代替一位数组，将空间复杂度降为O(1)。
   */

   // House Robber
// Time Complexity: O(n), Space Complexity: O(1)
public class Solution {
    public int rob(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        if (nums.length == 1) return nums[0];

        int even = nums[0];
        int odd = Math.max(nums[0], nums[1]);

        for (int i = 2; i < nums.length; ++i) {
            if (i % 2 == 0) {
                even = Math.max(even + nums[i], odd);
            } else {
                odd = Math.max(odd + nums[i], even);
            }
        }
        return Math.max(even, odd);
    }
}

public int rob(int[] num) {
    int prevMax = 0;
    int currMax = 0;
    for (int x : num) {
        int temp = currMax;
        currMax = Math.max(prevMax + x, currMax);
        prevMax = temp;
    }
    return currMax;
}