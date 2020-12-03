/**
 * Determine the largest rectangle of 1s in a binary matrix (a binary matrix only contains 0 and 1), return the area.

Assumptions

The given matrix is not null and has size of M * N, M >= 0 and N >= 0
Examples

{ {0, 0, 0, 0},

  {1, 1, 1, 1},

  {0, 1, 1, 1},

  {1, 0, 1, 1} }

the largest rectangle of 1s has area of 2 * 3 = 6
 */

public class Solution {
    public int largest(int[][] matrix) {
      // Write your solution here
      if(matrix == null || matrix.length == 0 || matrix[0] == null || matrix[0].length == 0) return 0;
  
      int m = matrix.length;
      int n = matrix[0].length;
      int[][] heightMap = new int[m][n];
  
      for(int i = 0; i < m; i++){
        for(int j = 0; j < n; j++){
          if(matrix[i][j] == 0){
            heightMap[i][j] = 0;
          } else {
            heightMap[i][j] = i == 0 ? 1 : heightMap[i - 1][j] + 1;
          }
        }
      }
  
      int maxArea = 0;
      for(int i = 0; i < m; i++){
        maxArea = Math.max(maxArea, findLargestRectInHistogram(heightMap[i]));
      }
  
      return maxArea;
    }
  
    private int findLargestRectInHistogram(int[] height){
      Stack<Integer> stack = new Stack<>();
      int m = height.length;
      int max = 0;
      for(int i = 0; i <= m; i++){
        int currHeight = i == m ? -1 : height[i];
        while(!stack.isEmpty() && currHeight <= height[stack.peek()]){
          int peekHeight = height[stack.pop()];
          int width = stack.isEmpty() ? i : i - stack.peek() - 1;
          max = Math.max(max, peekHeight * width);
        }
        stack.push(i);
      }
      return max;
    }
}