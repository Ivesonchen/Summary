/**
 * In a 2D black image there are some disjoint white objects with arbitrary shapes, 
 * find the number of disjoint white objects in an efficient way.

By disjoint, it means there is no white pixels that can connect the two objects, 
there are four directions to move to a neighbor pixel (left, right, up, down).

Black is represented by 1’s and white is represented by 0’s.

Assumptions

The given image is represented by a integer matrix and all the values in the matrix are 0 or 1
The given matrix is not null
Examples

the given image is
    0  0  0  1
    1  0  1  1
    1  1  0  0
    0  1  0  0

there are 3 disjoint white objects.
 */
// Same as number of islands
public class Solution {
    public int whiteObjects(int[][] matrix) {
      // Write your solution here
      int rows = matrix.length;
      if(rows == 0) return 0;
      int cols = matrix[0].length;
  
      int res = 0;
  
      for(int i = 0; i < rows; i++){
        for(int j = 0; j < cols; j++){
          if(matrix[i][j] == 0){
            dfs(matrix, i, j);
            res ++;
          }
        }
      }
  
      return res;
    }
  
    public void dfs(int[][] matrix, int i, int j){
      if(i < 0 || i >= matrix.length || j < 0 || j >= matrix[0].length || matrix[i][j] == 1) return;
  
      matrix[i][j] = 1;
      dfs(matrix, i + 1, j);
      dfs(matrix, i, j + 1);
      dfs(matrix, i - 1, j);
      dfs(matrix, i, j - 1);
    }
  }