/**
 * A robot is located at the top-left corner of a  m  x  n  grid (marked 'Start' in the diagram below).

The robot can only move either down or right at any point in time. 
The robot is trying to reach the bottom-right corner of the grid (marked 'Finish' in the diagram below).

How many possible unique paths are there?
https://github.com/grandyang/leetcode/issues/62
 */

 /**
    这跟之前那道 Climbing Stairs 很类似，那道题是说可以每次能爬一格或两格，问到达顶部的所有不同爬法的个数。
    而这道题是每次可以向下走或者向右走，求到达最右下角的所有不同走法的个数。那么跟爬梯子问题一样，需要用动态规划 Dynamic Programming 来解，
    可以维护一个二维数组 dp，其中 dp[i][j] 表示到当前位置不同的走法的个数，
    然后可以得到状态转移方程为:  dp[i][j] = dp[i - 1][j] + dp[i][j - 1]，这里为了节省空间，使用一维数组 dp，一行一行的刷新也可以，
  */

class Solutiin{
    public int uniquePath(int m, int n){
        int[] dp = new int[n];

        Arrays.fill(dp, 1);

        for(int i = 1; i < m; i++){
            for(int j = 1; j < n; i++){
                dp[j] += dp[j - 1];
            }
        }
        return dp[n - 1];
    }
}

  class Solution {
    public:
        int uniquePaths(int m, int n) {
            vector<int> dp(n, 1);
            for (int i = 1; i < m; ++i) {
                for (int j = 1; j < n; ++j) {
                    dp[j] += dp[j - 1]; 
                }
            }
            return dp[n - 1];
        }
    };