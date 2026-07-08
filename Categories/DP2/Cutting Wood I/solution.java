/**
 * There is a wooden stick with length L >= 1, we need to cut it into pieces, where the cutting positions are defined in an int array A. 
 * The positions are guaranteed to be in ascending order in the range of [1, L - 1]. 
 * The cost of each cut is the length of the stick segment being cut. 
 * Determine the minimum total cost to cut the stick into the defined pieces.

Examples

L = 10, A = {2, 4, 7}, the minimum total cost is 10 + 4 + 6 = 20 (cut at 4 first then cut at 2 and cut at 7)
 */

 // 类似于 burst balloons
 public class Solution {
    public int minCost(int[] cuts, int length) {
      // Write your solution here
      List<Integer> A = new ArrayList<>();
      for(int a : cuts) A.add(a);
  
      A.add(0);
      A.add(length);
      Collections.sort(A);
      int n = A.size();
      int[][] dp = new int[n][n];
        
      for(int d = 2; d < n; d++){                   //计算粒度
        for(int i = 0; i < n - d; i++){             //i为左边界         j为右边界
          int j = i + d;
          dp[i][j] = 1000000000;
          for(int k = i + 1; k < j; k++){           //左右之间走一遍
            dp[i][j] = Math.min(dp[i][j], dp[i][k] + dp[k][j] + A.get(j) - A.get(i));
          }
        }
      }
  
      return dp[0][n - 1];
    }
  }