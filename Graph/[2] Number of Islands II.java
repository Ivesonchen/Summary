/**
 * A 2d grid map of m rows and n columns is initially filled with water. 
 * We may perform an addLand operation which turns the water at position (row, col) into a land. 
 * Given a list of positions to operate, count the number of islands after each addLand operation. 
 * An island is surrounded by water and is formed by connecting adjacent lands horizontally or vertically. 
 * You may assume all four edges of the grid are all surrounded by water and the given list of positions do not duplicate.

Example:

Given m = 3, n = 3, positions = [[0,0], [0,1], [1,2], [2,1]].
Initially, the 2d grid grid is filled with water. (Assume 0 represents water and 1 represents land).

0 0 0
0 0 0
0 0 0
Operation #1: addLand(0, 0) turns the water at grid[0][0] into a land.

1 0 0
0 0 0   Number of islands = 1
0 0 0
Operation #2: addLand(0, 1) turns the water at grid[0][1] into a land.

1 1 0
0 0 0   Number of islands = 1
0 0 0
Operation #3: addLand(1, 2) turns the water at grid[1][2] into a land.

1 1 0
0 0 1   Number of islands = 2
0 0 0
Operation #4: addLand(2, 1) turns the water at grid[2][1] into a land.

1 1 0
0 0 1   Number of islands = 3
0 1 0
We return the result as an array: [1, 1, 2, 3]
 */
// To-Do  visual simulation
// m * n 长的 array 存的是 root 的位置
public class Solution {
    public List<Integer> numIslands(int m, int n, int[][] positions) {
      // Write your solution here
      List<Integer> res = new ArrayList<>();
      int count = 0;
      int[] roots = new int[m * n];
  
      Arrays.fill(roots, -1);
  
      int[][] dirs = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};
  
      for(int[] p : positions){
        int root = n * p[0] + p[1];
        roots[root] = root;
        count ++;
        // 每添加一个位置的land count ++
  
        // 对于新加的位置的四周 检查有没有已经是land的地方
        for(int[] dir : dirs){
          int x = p[0] + dir[0];
          int y = p[1] + dir[1];
  
          int nb = n * x + y;
          if(x < 0 || x >= m || y < 0 || y >= n || roots[nb] == -1) continue; //不是land或者越界的地方就跳过
  
          //对于四周 去找 核心root
          int rootNb = findIsland(roots, nb);
          if(root != rootNb){
            roots[root] = rootNb;
            root = rootNb;
            count --;
          }
        }
        res.add(count);
      }
      return res;
    }
  
    public int findIsland(int[] roots, int id){
      while(id != roots[id]) id = roots[id];
      return id;
    }
  }