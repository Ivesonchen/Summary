/**
 A group of two or more people wants to meet and minimize the total travel distance. You are given a 2D grid of values 0 or 1, where each 1 marks the home of someone in the group. The distance is calculated using Manhattan Distance, where distance(p1, p2) = |p2.x - p1.x| + |p2.y - p1.y|.

For example, given three people living at (0,0), (0,4), and (2,2):

1 - 0 - 0 - 0 - 1
|   |   |   |   |
0 - 0 - 0 - 0 - 0
|   |   |   |   |
0 - 0 - 1 - 0 - 0
The point (0,2) is an ideal meeting point, as the total travel distance of 2+2+2=6 is minimal. So return 6.
 */

 // Breadth First Search
 /**
  *  对于每一个点 进行四个方向的BFS扩展
     这个BFS 没有必要进行 ‘层’ size() 的BFS 
  */

public class Solution {
    public int minTotalDistance(int[][] grid) {
      // Write your solution here
      if(grid.length == 0 || grid[0].length == 0) return 0;
      int minDistance = Integer.MAX_VALUE;
      for(int row = 0; row < grid.length; row++){
        for(int col = 0; col < grid[0].length; col++){
          int distance = search(grid, row, col);
          minDistance = Math.min(distance, minDistance);
        }
      }
      return minDistance;
    }
  
    private int search(int[][] grid, int row, int col){
      Queue<Point> q = new LinkedList<>();
      int m = grid.length;
      int n = grid[0].length;
      boolean[][] visited = new boolean[m][n];
      q.add(new Point(row, col, 0));
      int totalDistance = 0;
  
      while(!q.isEmpty()){
        Point point = q.poll();
        int r = point.row;
        int c = point.col;
        int d = point.distance;
        if(r < 0 || c < 0 || r >= m || c >= n || visited[r][c]) continue;
  
        if(grid[r][c] == 1){
          totalDistance += d;
        }
        visited[r][c] = true;
        q.add(new Point(r + 1, c, d + 1));
        q.add(new Point(r, c + 1, d + 1));
        q.add(new Point(r - 1, c, d + 1));
        q.add(new Point(r, c - 1, d + 1));
      }
      return totalDistance;
    }
  }
  
  class Point {
    int row;
    int col;
    int distance;
    public Point(int row, int col, int distance){
      this.row = row;
      this.col = col;
      this.distance = distance;
    }
  }