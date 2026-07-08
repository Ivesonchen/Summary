/**
 * Given a matrix that contains only 1s and 0s, find the largest cross which contains only 1s, 
 * with the same arm lengths and the four arms joining at the central point.

Return the arm length of the largest cross.

Assumptions

The given matrix is not null, has size of N * M, N >= 0 and M >= 0.
Examples

{ {0, 0, 0, 0},

  {1, 1, 1, 1},

  {0, 1, 1, 1},

  {1, 0, 1, 1} }

the largest cross of 1s has arm length 2.
 */
/**
 * 为了获得最大的十字形，我们可以预处理matrix，生成四个dp table，dp[r][c] 分别表示到点 [r, c] 为止，来自左右上下四个方向的连续 1 的长度。
 * 如此一来，为了获得最大十字，我们只需要检查每个点的左右上下四个方向连续 1 的长度，取四个长度的最小值作为当前位置十字的arm length;
时间复杂度: O(5n^2);
 */
public class Solution {
    public int largest(int[][] matrix) {
      // Write your solution here
      int R = matrix.length, C = matrix[0].length;
  
      int[][] leftRight = new int[R][C];
      int[][] rightLeft = new int[R][C];
      int[][] topDown = new int[R][C];
      int[][] downTop = new int[R][C];
  
      for(int i = 0; i < R; i++){
        for(int j = 0; j < C; j++){
          if(j == 0){
            leftRight[i][j] = matrix[i][j];
          } else {
            leftRight[i][j] = matrix[i][j] == 1 ? leftRight[i][j - 1] + 1 : 0;
          }
        }
      }
  
      for(int i = 0; i < R; i++){
        for(int j = C - 1; j >= 0; j--){
          if(j == C - 1){
            rightLeft[i][j] = matrix[i][j];
          } else {
            rightLeft[i][j] = matrix[i][j] == 1 ? rightLeft[i][j + 1] + 1 : 0;
          }
        }
      }
  
      for(int i = 0; i < R; i++){
        for(int j = 0; j < C; j++){
          if(i == 0){
            topDown[i][j] = matrix[i][j];
          } else {
            topDown[i][j] = matrix[i][j] == 1 ? topDown[i - 1][j] + 1 : 0;
          }
        }
      }
      
      for(int i = R - 1; i >= 0; i--){
        for(int j = 0; j < C; j++){
          if(i == R - 1){
            downTop[i][j] = matrix[i][j];
          } else {
            downTop[i][j] = matrix[i][j] == 1 ? downTop[i + 1][j] + 1 : 0;
          }
        }
      }
  
      int res = 0;
      for(int i = 0; i < R; i++){
        for(int j = 0; j < C; j++){
          int armLength = Math.min(Math.min(leftRight[i][j], rightLeft[i][j]), Math.min(topDown[i][j], downTop[i][j]));
          res = Math.max(res, armLength);
        }
      }
      return res;
    }
  }