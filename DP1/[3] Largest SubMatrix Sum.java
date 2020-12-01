/**
 * Given a matrix that contains integers, find the submatrix with the largest sum.

Return the sum of the submatrix.

Assumptions

The given matrix is not null and has size of M * N, where M >= 1 and N >= 1
Examples

{ {1, -2, -1, 4},

  {1, -1,  1, 1},

  {0, -1, -1, 1},

  {0,  0,  1, 1} }

the largest submatrix sum is (-1) + 4 + 1 + 1 + (-1) + 1 + 1 + 1 = 7.
 */

/**
 * 对于每一行   generate一个 数组 (长度为matrix的宽)  存储这一行以下 相同 index 的和
 * 
 * for i in (0 ~ m){
 *    for j in(i ~ m){
 *      这里面的每组操作 实际上 代表了 0 ~ m 的 范围(i, j) 的排列组合
 *    }
 * }
 */

public class Solution {
    public int largest(int[][] matrix) {
      // Write your solution here
      int m = matrix.length, n = matrix[0].length, result = matrix[0][0];
  
      for(int i = 0; i < m; i++){
        int[] cur = new int[n];
        for(int j = i; j < m; j++){
          for(int k = 0; k < n; k++){
            cur[k] += matrix[j][k];
          }

          //largest subarray sum
          int min = 0, max = cur[0], preSum = 0;
          for(int num : cur){
            preSum += num;
            max = Math.max(max, preSum - min);
            min = Math.min(min, preSum);
          }
          result = Math.max(result, max);
        }
      }
      return result;
    }
  }