/**
 * Generate an N * N 2D array in spiral order clock-wise starting from the top left corner, using the numbers of 1, 2, 3, …, N * N in increasing order.

Assumptions

N >= 0
Examples

N = 3, the generated matrix is

{ {1,  2,  3}

  {8,  9,  4},

  {7,  6,  5} }
 */

public class Solution {
    public int[][] spiralGenerate(int n) {
      // Write your solution here
      int[] dx = {0,1,0,-1};
      int[] dy = {1,0,-1,0};
      if(n == 0) return new int[0][0];
  
      int[][] res = new int[n][n];
      int step = 1;
      int direction = 0;
      int i = 0;
      int j = 0;
      res[i][j] = step++;
  
      while(step <= n * n) {
          int x = dx[direction % 4];
          int y = dy[direction % 4];
          i += x;
          j += y;
  
          while(i >= 0 && i < n && j >= 0 && j < n && res[i][j] == 0) {
            res[i][j] = step ++;
            i += x;
            j += y;
          }
  
          i -= x;
          j -= y;
          direction ++;
      }
  
      return res;
    }
  } // really hard to understand the edge

  public class Solution {
    int[] dx = {0,1,0,-1};
    int[] dy = {1,0,-1,0};
  public int[][] spiralGenerate(int n) {
    // Write your solution here

    int[][] res = new int[n][n];

    if(n == 0) return res;
    int step = 1;
    int direction = 0;
    int i = 0;
    int j = 0;

    while(step <= n * n){
      res[i][j] = step ++;
      
      direction = computeDirection(res, direction, i, j);

      i += dx[direction];
      j += dy[direction];
    }

    return res;
  }

  public int computeDirection(int[][] graph, int curDirection, int x, int y){
    int nextX = x + dx[curDirection];
    int nextY = y + dy[curDirection];

    if(nextX >= 0 && nextX < graph.length && nextY >= 0 && nextY < graph[0].length && graph[nextX][nextY] == 0){
      return curDirection;
    }

    return (curDirection + 1) % 4;
  }
}
// niu pi o    it's extenable to m * n;

  public class Solution {
    public int[][] generateMatrix(int n) {
        // Declaration
        int[][] matrix = new int[n][n];
        
        // Edge Case
        if (n == 0) {
            return matrix;
        }
        
        // Normal Case
        int rowStart = 0;
        int rowEnd = n-1;
        int colStart = 0;
        int colEnd = n-1;
        int num = 1; //change
        
        while (rowStart <= rowEnd && colStart <= colEnd) {
            for (int i = colStart; i <= colEnd; i ++) {
                matrix[rowStart][i] = num ++; //change
            }
            rowStart ++;
            
            for (int i = rowStart; i <= rowEnd; i ++) {
                matrix[i][colEnd] = num ++; //change
            }
            colEnd --;
            
            for (int i = colEnd; i >= colStart; i --) {
                if (rowStart <= rowEnd)
                    matrix[rowEnd][i] = num ++; //change
            }
            rowEnd --;
            
            for (int i = rowEnd; i >= rowStart; i --) {
                if (colStart <= colEnd)
                    matrix[i][colStart] = num ++; //change
            }
            colStart ++;
        }
        
        return matrix;
    }
}


/**
 * 
 * #### Move forward till end
- Similar concept as `The Maze`: keep walking until hit wall, turn back
- fix direction `dx[direction % 4]`
 */