/**
 * Given an binary matrix of N * N (the only elements in the matrix are 0s and 1s), each of the indices represents one person.

matrix[i][j] = 1 if and only if person i knows person j (this is single direction, only if matrix[j][i] = 1 
such that person j knows person i).

Determine if there is one celebrity among all N persons, a celebrity is defined as

He does not know any other person.
All the other persons know him.
Return the celebrity's index if there exist one (there could be at most one celebrity, why?), return -1 otherwise.

Assumptions:

The given matrix is not null and N >= 2.
Examples:

matrix = {{ 0, 1, 1 },
          { 0, 0, 0 },
          { 1, 1, 0 }}

The celebrity is person 1, since person 0 and person 2 all know him, but person 1 does not know person 0 or person 2.
 */

 // 注意edge case     解法按照直觉走   先找到除了自己和自己认不认识的情况 每一行中 哪一行是除了自己 都不认识别人的 
                                //  然后去相应的那一列查找   看是不是别人都认识这个人
public class Solution {
    public int celebrity(int[][] matrix) {
      // Write your solution here
      int rows = matrix.length;
      int cols = matrix[0].length;
      if(rows == 1 && cols == 1) return matrix[0][0] == 1 ? 0 : -1; //注意
  
      int row = -1;
  
      for(int i = 0; i < rows; i++){
        boolean flag = true;
        for(int j = 0; j < cols; j++){
          if(i == j) continue;   // 注意
          if(matrix[i][j] != 0) {
            flag = false;
            break;
          }
        }
        if(flag){
          row = i;
        }
      }
      if(row == -1) return -1;
  
      for(int i = 0; i < rows; i++){
        if(i == row) continue;
        if(matrix[i][row] != 1) return -1;
      }
  
      return row;
    }
  }