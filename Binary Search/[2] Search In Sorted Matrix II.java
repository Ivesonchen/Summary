/**
 * Given a 2D matrix that contains integers only, 
 * which each row is sorted in ascending order and each column is also sorted in ascending order.

Given a target number, returning the position that the target locates within the matrix. 
If the target number does not exist in the matrix, return {-1, -1}.

Assumptions:

The given matrix is not null.
Examples:

matrix = { {1, 2, 3}, {2, 4, 5}, {6, 8, 10} }

target = 5, return {1, 2}

target = 7, return {-1, -1}
 */

 /*
We start search the matrix from top right corner, initialize the current position to top right corner, if the target is greater than the value in current position, then the target can not be in entire row of current position because the row is sorted, if the target is less than the value in current position, then the target can not in the entire column because the column is sorted too. We can rule out one row or one column each time, so the time complexity is O(m+n).
*/

//Time O(m + n)
//Space O(1)

public class Solution {
    public int[] search(int[][] matrix, int target) {
      // Write your solution here
      if(matrix.length == 0 && matrix[0].length == 0) return new int[]{-1, -1};
  
      int x = 0;
      int y = matrix[0].length - 1;
  
      while(x < matrix.length && y >= 0){
        if(matrix[x][y] == target){
          return new int[]{x, y};
        } else if(matrix[x][y] > target){
          y --;
        } else {
          x ++;
        }
      }
      return new int[]{-1, -1};
    }
  }
  