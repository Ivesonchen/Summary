/**
 * There is a ball in a maze with empty spaces (represented as 0) and walls (represented as 1). 
 * The ball can go through the empty spaces by rolling up, down, left or right, but it won't stop rolling until hitting a wall. 
 * When the ball stops, it could choose the next direction.

Given the m x n maze, the ball's start position and the destination, 
where start = [startrow, startcol] and destination = [destinationrow, destinationcol], 
return true if the ball can stop at the destination, otherwise return false.

You may assume that the borders of the maze are all walls (see examples).


 */
public class Solution {
  int[][] dirs = new int[][] {{1,0}, {0,1}, {-1,0}, {0,-1}};

  public boolean hasPath(int[][] maze, int[] start, int[] destination) {
      int m = maze.length, n = maze[0].length;
      boolean[][] visited = new boolean[m][n];
      // int[] dx = new int[]{0, -1, 0, 1};
      // int[] dy = new int[]{1, 0, -1, 0};
      
      Queue<int[]> queue = new LinkedList<>();
      queue.offer(start);
      visited[start[0]][start[1]] = true;
      
      while (!queue.isEmpty()) {
          int[] curPos = queue.poll();
          if (curPos[0] == destination[0] && curPos[1] == destination[1]) {
              return true;
          }
          // try four direction until it hits the wall
          for (int[] dir : dirs) {
              int nx = curPos[0], ny = curPos[1];

              while (nx >= 0 && nx < m && ny >= 0 && ny < n && maze[nx][ny] == 0) {
                  nx += dir[0];
                  ny += dir[1];
              }
              
              //back one step
              nx -= dir[0];
              ny -= dir[1];
              
              if (!visited[nx][ny]) {
                  visited[nx][ny] = true;
                  queue.offer(new int[]{nx, ny});
              }
          }
      }
      return false;
  }
}

/**
 * public class Solution {
    int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

    private boolean dfs(int[][] maze, int[] current, int[] destination, boolean[][] visited) {
        if (current[0] == destination[0] && current[1] == destination[1]) return true;
        int x = current[0], y = current[1];
        if (x < 0 || y < 0 || x > maze.length || y > maze[0].length || visited[x][y]) return false;
        visited[x][y] = true;
        for (int i = 0; i < directions.length; i++) {
            int xx = x, yy = y;
            while (xx >= 0 && xx < maze.length && yy >= 0 && yy < maze[0].length && maze[xx][yy] == 0) {
                xx += directions[i][0]; yy += directions[i][1];
            }
            if (dfs(maze, new int[]{xx-directions[i][0], yy-directions[i][1]}, destination, visited)) return true;
        }
        return false;
    }

    public boolean hasPath(int[][] maze, int[] start, int[] destination) {
        if (maze.length == 0 || maze[0].length == 0) return false;
        return dfs(maze, start, destination, new boolean[maze.length][maze[0].length]);
    }
}
 */