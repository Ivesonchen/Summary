/**
 * Given an matrix of integers, each row is sorted in ascending order from left to right, each column is also sorted in ascending order from top to bottom.

Determine how many negative numbers in the matrix.

Assumptions:

The given matrix is not null.
Examples:

{ {-5, -3, 0, 0, 1},

  {-3, -2, 1, 1, 3}

  {-2, 0,  3, 5, 6} }

The number of negative elements in the matrix is 5.
 */

 //需要一个隔板 index 所有在fence右边的 都不予考虑
 // 而由于又有 纵向也是 sorted 的  所以这个fence 也可以应用于下边的几行

public class Solution {
    public int negNumber(int[][] matrix) {
      // Write your solution here
      int counter = 0;
      int fence = matrix[0].length;
  
      for(int i = 0; i < matrix.length; i++){
        for(int j = 0; j < fence; j++){
          if(matrix[i][j] < 0){
            counter++;
          } else {
            fence = j;
          }
        }
      }
  
      return counter;
    }
  }
  