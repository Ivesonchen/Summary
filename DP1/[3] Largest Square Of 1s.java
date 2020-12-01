/**
 * Determine the largest square of 1s in a binary matrix (a binary matrix only contains 0 and 1), 
 * return the length of the largest square.

Assumptions

The given matrix is not null and guaranteed to be of size N * N, N >= 0
Examples

{ {0, 0, 0, 0},

  {1, 1, 1, 1},

  {0, 1, 1, 1},

  {1, 0, 1, 1}}

the largest square of 1s has length of 2
https://leetcode.com/problems/maximal-square/solution/
 */

public class Solution {
    public int largest(int[][] matrix) {
      // Write your solution here
  
      int row = matrix.length;
      int col = matrix[0].length;
  
      int[][] dp = new int[row + 1][col + 1];
      int result = 0;
  
      for(int i = 1; i <= row; i++){
        for(int j = 1; j <= col; j++){
          if(matrix[i - 1][j - 1] == 1){
            dp[i][j] = Math.min(dp[i - 1][j - 1], Math.min(dp[i][j - 1], dp[i - 1][j])) + 1;
            result = Math.max(dp[i][j], result);
          }
        }
      }
      return result;
    }
}