/**
 * Given a non-negative integer 2D array representing the heights of bars in a matrix. 
 * Suppose each bar has length and width of 1. Find the largest amount of water that can be trapped in the matrix. 
 * The water can flow into a neighboring bar if the neighboring bar's height is smaller than the water's height. 
 * Each bar has 4 neighboring bars to the left, right, up and down side.

Assumptions

The given matrix is not null and has size of M * N, where M > 0 and N > 0, 
all the values are non-negative integers in the matrix.
Examples

{ { 2, 3, 4, 2 },

  { 3, 1, 2, 3 },

  { 4, 3, 5, 4 } }

the amount of water can be trapped is 3, 

at position (1, 1) there is 2 units of water trapped,

at position (1, 2) there is 1 unit of water trapped.

https://leetcode.com/problems/trapping-rain-water-ii/
 */

 /**
  * 从周围一圈 然后找最低的地方 往里面 延伸 遇到更低点的时候 记录trapped water
  */

public class Solution {
    public int maxTrapped(int[][] matrix) {
      // Write your solution here
      if(matrix == null || matrix.length == 0 || matrix[0].length == 0) return 0;
  
      PriorityQueue<Point> queue = new PriorityQueue<>(new Comparator<Point>(){
        public int compare(Point a, Point b){
          return a.height - b.height;
        }
      }); // 按照每个点的height排序
  
      int m = matrix.length;
      int n = matrix[0].length;
      boolean[][] visited = new boolean[m][n];

      // Initially, add all the Cells which are on borders to the queue.    左右墙
      for(int i = 0; i < m; i++){
        visited[i][0] = true;
        visited[i][n - 1] = true;
        queue.offer(new Point(i, 0, matrix[i][0]));
        queue.offer(new Point(i, n - 1, matrix[i][n - 1]));
      }
      for(int i = 0; i < n; i++){                                           // 上下墙
        visited[0][i] = true;
        visited[m - 1][i] = true;
        queue.offer(new Point(0, i, matrix[0][i]));
        queue.offer(new Point(m - 1, i, matrix[m - 1][i]));
      }
  
      // from the borders, pick the shortest cell visited and check its neighbors:
      // if the neighbor is shorter, collect the water it can trap and update its height as its height plus the water trapped
      // add all its neighbors to the queue.
      int[][] dirs = new int[][]{{-1, 0}, {1,0}, {0,-1}, {0,1}};
      int res = 0;
      while(!queue.isEmpty()){
        Point point = queue.poll();
        for(int[] dir : dirs){
          int row = point.row + dir[0];
          int col = point.col + dir[1];
          if(row >= 0 && row < m && col >= 0 && col < n && !visited[row][col]){
            visited[row][col] = true;
            res += Math.max(0, point.height - matrix[row][col]);       // 以墙的高度 减 新的一步的高度
            queue.offer(new Point(row, col, Math.max(matrix[row][col], point.height)));     // 将新的一步的高度 设置为墙的高度
          }
        }
      }
      return res;
    }
  }
  
  class Point{
    int row;
    int col;
    int height;
  
    public Point(int row, int col, int height){
      this.row = row;
      this.col = col;
      this.height = height;
    }
  }