/**
 * You are given a m x n 2D grid initialized with these three possible values.

-1 - A wall or an obstacle.
0 - A gate.
INF - Infinity means an empty room. We use the value 231 - 1 = 2147483647 to represent INF as you may assume that the distance to a gate is less than 2147483647.
Fill each empty room with the distance to its nearest gate. If it is impossible to reach a gate, it should be filled with INF.

For example, given the 2D grid:

INF  -1  0  INF
INF INF INF  -1
INF  -1 INF  -1
  0  -1 INF INF
After running your function, the 2D grid should be:

  3  -1   0   1
  2   2   1  -1
  1  -1   2  -1
  0  -1   3   4
 */

public class Solution {
    public int[][] wallsAndGates(int[][] rooms) {
      // Write your solution here
      Queue<Point> queue = new LinkedList<>();
      if(rooms.length == 0 || rooms[0].length == 0) return rooms;
      int m = rooms.length;
      int n = rooms[0].length;
  
      for(int row = 0; row < rooms.length; row++){
        for(int col = 0; col < rooms[row].length; col++){
          if(rooms[row][col] == 0){
            queue.offer(new Point(row, col, 0));
          }
        }
      }
      while(!queue.isEmpty()){
        Point point = queue.poll();
        int r = point.row;
        int c = point.col;
        int d = point.distance;
        if(r < 0 || r >= m || c < 0 || c >= n || (rooms[r][c] != Integer.MAX_VALUE && d != 0)) continue;
        // 超越边界 或者 在扩展过程中间(非 入口) 遇到不是空位的格子
  
        rooms[r][c] = Math.min(rooms[r][c], d);
  
        queue.add(new Point(r + 1, c, d + 1));
        queue.add(new Point(r, c + 1, d + 1));
        queue.add(new Point(r - 1, c, d + 1));
        queue.add(new Point(r, c - 1, d + 1));
      }
      return rooms;
    }
  }
  
  class Point{
    int row;
    int col;
    int distance;
  
    public Point(int row, int col, int distance){
      this.row = row;
      this.col = col;
      this.distance = distance;
    }
  }