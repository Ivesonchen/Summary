/**
 * Generate an M * N 2D array in spiral order clock-wise starting from the top left corner, using the numbers of 1, 2, 3, …, M * N in increasing order.

Assumptions

M >= 0, N >= 0
Examples

M = 3, N = 4, the generated matrix is

{ {1,  2,  3,  4}

  {10, 11, 12, 5},

  {9,  8,  7,  6} }
 */

/**
 * 依然使用 dx 和 dy这样的方向数组  模拟 螺旋状走路
 * 注意这样生成的题目  可以使用  res本身数组 作为判定 visited的 数组    因为没走过的地方 初始化就是0
 */

public class Solution {
    int[] dx = {0, 1, 0, -1};
    int[] dy = {1, 0, -1, 0};
    public int[][] spiralGenerate(int m, int n) {
      // Write your solution here
      int[][] res = new int[m][n];
  
      if(m == 0 || n == 0) return res;
  
      int[][] visited = new int[m][n];
      int counter = 1, x = 0, y = 0, direction = 0;
  
      while(counter <= m*n){
        res[x][y] = counter;
        visited[x][y] = 1;
        counter ++;
  
        direction = computeDirection(visited, x, y, direction, m, n);
        x += dx[direction];
        y += dy[direction];
      }
  
      return res;
    }
  
    public int computeDirection(int[][] visited, int x, int y, int direction, int m, int n){
      int nextX = x + dx[direction];
      int nextY = y + dy[direction];
  
      if(nextX >= 0 && nextX < m && nextY >= 0 && nextY < n && visited[nextX][nextY] == 0) {
        return direction;
      }
  
      return (direction + 1) % 4;
    }
  }
  