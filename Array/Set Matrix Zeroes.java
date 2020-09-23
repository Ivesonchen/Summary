/**
Given a m x n matrix, if an element is 0, set its entire row and column to 0.

E.g.    Input: Matrix =  [

                                     [1, 1, 1, 1, 0],

                                     [0, 1, 1, 0, 1],

                                     [1, 1, 1, 0, 1],

                                     [1, 1, 1, 1, 1]

                                 ]

Output: Matrix = [

                             [0, 0, 0, 0, 0],

                             [0, 0, 0, 0, 0],

                             [0, 0, 0, 0, 0],

                             [0, 1, 1, 0, 0],

                          ]
 */

 /**
  * 循环一遍 maintain 两个 flag 数组 但凡遇到一个0 标记 对应的行和列 flag 数组
    然后 横竖循环 把对应 flag位置 为1的 行和列 set 为 0
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
  