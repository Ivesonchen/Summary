/**
 * We have a list of piles of stones, each pile of stones has a certain weight, represented by an array of integers. 
 * In each move, we can merge two adjacent piles into one larger pile, the cost is the sum of the weights of the two piles. 
 * We merge the piles of stones until we have only one pile left. 
 * Determine the minimum total cost.

Assumptions

stones is not null and is length of at least 1
Examples

{4, 3, 3, 4}, the minimum cost is 28

merge first 4 and first 3, cost 7

merge second 3 and second 4, cost 7

merge two 7s, cost 14

total cost = 7 + 7 + 14 = 28

https://leetcode-cn.com/problems/minimum-cost-to-merge-stones/solution/
 */

//To-Do

public class Solution {
    public int minCost(int[] stones) {
      // Write your solution here
      int n = stones.length;
      int K = 2;
      if ((n - 1) % (K - 1) > 0) return -1;
  
      int[] prefix = new int[n+1];
      for (int i = 0; i <  n; i++)
          prefix[i + 1] = prefix[i] + stones[i];
  
      int[][] dp = new int[n][n];
      for (int m = K; m <= n; ++m)
          for (int i = 0; i + m <= n; ++i) {
              int j = i + m - 1;
              dp[i][j] = Integer.MAX_VALUE;
              for (int mid = i; mid < j; mid += K - 1)
                  dp[i][j] = Math.min(dp[i][j], dp[i][mid] + dp[mid + 1][j]);
              if ((j - i) % (K - 1) == 0)
                  dp[i][j] += prefix[j + 1] - prefix[i];
          }
      return dp[0][n - 1];
    }
  }