/**
Given a m x n matrix, if an element is 0, set its entire row and column to 0.

E.g.    
Input: Matrix =  [[1, 1, 1, 1, 0],
                  [0, 1, 1, 0, 1],
                  [1, 1, 1, 0, 1],
                  [1, 1, 1, 1, 1]]

Output: Matrix = [[0, 0, 0, 0, 0],
                  [0, 0, 0, 0, 0],
                  [0, 0, 0, 0, 0],
                  [0, 1, 1, 0, 0]]
 */

 /**
  * 循环一遍 maintain 两个 flag 数组 但凡遇到一个0 标记 对应的行和列 flag 数组
    然后 横竖循环 把对应 flag位置 为1的 行和列 set 为 0
    time : O(m*n) space : O(m + n)
  */


/**
 * 我们可以利用 第一行和 第一列 作为我们的 flag 数组 但是问题是 同时会影响 这两个位置的 0 的 处理  
 * 所以要 声明两个 boolean flag        
 * boolean row_has_zero = false; // 第一行是否存在 0
   boolean col_has_zero = false; // 第一列是否存在 0
   最后再处理这 两个位置
 */

public class Solution {
    public void setZero(int[][] matrix) {
      // Write your solution here
  
      if(matrix.length == 0) return;
      
      int[] flagForRow = new int[matrix.length];
      int[] flagForCol = new int[matrix[0].length];
  
      for(int i = 0; i < matrix.length; i ++){
        for(int j = 0; j < matrix[i].length; j++){
          if(matrix[i][j] == 0){
            flagForRow[i] = 1;
            flagForCol[j] = 1;
          }
        }
      }
      for(int i = 0; i < matrix.length; i ++){
        if(flagForRow[i] == 1) {
          for(int j = 0; j < matrix[i].length; j++){
            matrix[i][j] = 0;
          }
        }
      }
  
      for(int j = 0; j < matrix[0].length; j ++){
        if(flagForCol[j] == 1) {
          for(int i = 0; i < matrix.length; i++){
            matrix[i][j] = 0;
          }
        }
      }
    }
  }
  
// Set Matrix Zeroes
// 时间复杂度O(m*n)，空间复杂度O(1)
public class Solution {
  public void setZeroes(int[][] matrix) {
      final int m = matrix.length;
      final int n = matrix[0].length;
      boolean row_has_zero = false; // 第一行是否存在 0
      boolean col_has_zero = false; // 第一列是否存在 0

      for (int i = 0; i < n; i++)
          if (matrix[0][i] == 0) {
              row_has_zero = true;
              break;
          }

      for (int i = 0; i < m; i++)
          if (matrix[i][0] == 0) {
              col_has_zero = true;
              break;
          }

      for (int i = 1; i < m; i++)
          for (int j = 1; j < n; j++)
              if (matrix[i][j] == 0) {
                  matrix[0][j] = 0;
                  matrix[i][0] = 0;
              }
      for (int i = 1; i < m; i++)
          for (int j = 1; j < n; j++)
              if (matrix[i][0] == 0 || matrix[0][j] == 0)
                  matrix[i][j] = 0;
      if (row_has_zero)
          for (int i = 0; i < n; i++)
              matrix[0][i] = 0;
      if (col_has_zero)
          for (int i = 0; i < m; i++)
              matrix[i][0] = 0;
  }
};