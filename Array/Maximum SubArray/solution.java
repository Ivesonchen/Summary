/**
LeetCode:
Given an integer array nums, find the contiguous subarray (containing at least one number) 
which has the largest sum and return its sum.
Example:
Input: [-2,1,-3,4,-1,2,1,-5,4],
Output: 6
Explanation: [4,-1,2,1] has the largest sum = 6.
Follow up:
If you have figured out the O(n) solution, 
try coding another solution using the divide and conquer approach, 
which is more subtle.
 */

 /*
Thoughts:
sequence dp
continous subarray: cannot skip element
dp[i]: for first i items, what's the largest sum that containts nums[i]?
dp[i] = Math.max(dp[i - 1] + nums[i - 1], nums[i - 1])
record max globally
dp[i]: 0 items, max = 0
*/
class Solution {
    public int maxSubArray(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        int n = nums.length;
        int[] dp = new int[n + 1];
        dp[0] = 0;
        int max = Integer.MIN_VALUE;
        for (int i = 1; i <= n; i++) {
            dp[i] = Math.max(dp[i - 1] + nums[i - 1], nums[i - 1]); // contious, or start from nums[i-1]
            max = Math.max(max, dp[i]);
        }
        
        return max;
    }

    maxSubArray() {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        int n = nums.length;
        // int[] dp = new int[n + 1];
        // dp[0] = 0;
        int temp = nums[0];

        int max = Integer.MIN_VALUE;
        for (int i = 1; i <= n; i++) {
            
            temp = Math.max(temp + nums[i], nums[i]); // contious, or start from nums[i-1]
            max = Math.max(max, temp);
        }
        
        return max;

    }
}

 /**
  * #### Sequence DP
- dp[i]: 前i个element,包括 last element (i-1), 可能组成的 subarray 的最大sum.
- init: dp = int[n + 1], dp[0]: first 0 items, does not have any sum
- 因为continous sequence, 所以不满足条件的时候, 会断. That is: need to take curr num, regardless => can drop prev max in dp[i]
- track overall max 
- init dp[0] = 0; max = MIN_VALUE 因为有负数
- Time, space O(n)
- Rolling array, space O(1)
  */