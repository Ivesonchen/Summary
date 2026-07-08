/**
 * There is a fence with n posts, each post can be painted with one of the k colors.

You have to paint all the posts such that no more than two adjacent fence posts have the same color.

Return the total number of ways you can paint the fence.

Note:
n and k are non-negative integers.
https://leetcode.com/problems/paint-fence/solution/

http://yuanhsh.iteye.com/blog/2219891
 */

 /**
  * 因为题目要求是不超过两个相邻的栅栏有同样颜色，所以可以把题目分解一下：
设T(n)为符合要求的染色可能总数，S(n)为最后两个相邻元素为相同颜色的染色可能数，D(n)为最后两个相邻元素为不同颜色的染色可能数。
显然D(n) = (k - 1) * (S(n-1) + D(n-1))
S(n) = D(n-1)
T(n) = S(n) + D(n)
带入化简一下得出：
T(n) = (k - 1) * (T(n-1) + T(n-2)), n > 2

其实可以有更简单的方法，涂到第 n 个的可能总数为 ① 与 n-1 时的不同可能数，即(k-1)*T(n-1)；加上 ② 与 n-1 时的相同可能数，
因为连续相同不能超过两个，第 n 和 n-1 个的颜色和第 n-2 个肯定不同，所以与 n-1 时的相同数必定等于 n-2 时的不同可能数，即(k-1)*T(n-2) ，于是有：
T(n) = (k - 1) * (T(n-1) + T(n-2)), n > 2 
  */

public class Solution {
    public int numWays(int n, int k) {
      // Write your solution here
      
      // (k - 1) * numWays(n - 1) + (k - 1) * numWays(n - 2)
  
      if(n == 0) return 0;
      if(n == 1) return k;
  
      int[] dp = new int[n + 1];
  
      dp[1] = k;
      dp[2] = k * k;
  
      for(int i = 3; i <= n; i++){
        dp[i] = (k - 1) * (dp[i - 1] + dp[i - 2]);
      }
  
      return dp[n];
    }
  }