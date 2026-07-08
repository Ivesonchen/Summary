/**
 * Given a matrix that contains only 1s and 0s, find the largest X shape which contains only 1s, 
 * with the same arm lengths and the four arms joining at the central point.

Return the arm length of the largest X shape.

Assumptions

The given matrix is not null, has size of N * M, N >= 0 and M >= 0.
Examples

{ {0, 0, 0, 0},

  {1, 1, 1, 1},

  {0, 1, 1, 1},

  {1, 0, 1, 1} }

the largest X of 1s has arm length 2.
 */

 // similar to Longest Cross Of 1s
public class Solution {
    public int largest(int[][] matrix) {
      // Write your solution here	
      if(matrix==null || matrix.length==0) return 0;
  
      int rows=matrix.length, cols=matrix[0].length;
      int[][] leftdown2rightup = new int[rows][cols];
      int[][] rightup2leftdown = new int[rows][cols];
      int[][] leftup2rightdown = new int[rows][cols];
      int[][] rightdown2leftup = new int[rows][cols];
  
      int result=0;
      for(int i=0; i<rows; i++) {
        Arrays.fill(leftdown2rightup[i], 0);
        Arrays.fill(rightup2leftdown[i], 0);
        Arrays.fill(leftup2rightdown[i], 0);
        Arrays.fill(rightdown2leftup[i], 0);
      }
      for(int i=rows-1; i>=0; i--) {
        for(int j=cols-1; j>=0; j--) {
          if(j==0 || i==rows-1) {
            leftdown2rightup[i][j] = matrix[i][j];
          }
          else if(matrix[i][j]==1){
            leftdown2rightup[i][j] = leftdown2rightup[i+1][j-1]+1;
          }
        }
      }
      for(int i=0; i<rows; i++) {
        for(int j=cols-1; j>=0; j--) {
          if(i==0 || j==cols-1) {
            rightup2leftdown[i][j] = matrix[i][j];
          }
          else if(matrix[i][j]==1) {
            rightup2leftdown[i][j] = rightup2leftdown[i-1][j+1]+1;
          }
        }
      }
      for(int i=0; i<rows; i++) {
        for(int j=0; j<cols; j++) {
          if(i==0 || j==0) {
            leftup2rightdown[i][j] = matrix[i][j];
          }
          else if(matrix[i][j]==1) {
            leftup2rightdown[i][j] = leftup2rightdown[i-1][j-1]+1;
          }
        }
      }
      for(int i=rows-1; i>=0; i--) {
        for(int j=cols-1; j>=0; j--) {
          if(j==cols-1 || i==rows-1) {
            rightdown2leftup[i][j]=matrix[i][j];
          }
          else if(matrix[i][j]==1) {
            rightdown2leftup[i][j]=rightdown2leftup[i+1][j+1]+1;
          }
        }
      }
      for(int i=0; i<rows; i++) {
        for(int j=0; j<cols; j++) {
          result=Math.max(result, Math.min(Math.min(leftdown2rightup[i][j], rightup2leftdown[i][j]), Math.min(leftup2rightdown[i][j], rightdown2leftup[i][j])));
        }
      }
      return result;
    }
  }