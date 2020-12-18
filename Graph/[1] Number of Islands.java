/**
 * Given a 2d grid map of 1s (land) and 0s (water), count the number of islands. 
 * An island is surrounded by water and is formed by connecting adjacent lands horizontally or vertically. 
 * You may assume all four edges of the grid are all surrounded by water.

Example 1:

11110
11010
11000
00000
Answer: 1

Example 2:

11000
11000
00100
00011
Answer: 3
 */
// 关键在于 将走过的‘1’ 和相邻的 ‘1’ 变成 ‘0’    然后就可以往下数剩下独立的‘1’    独立的‘1’ 就是 number of islands
public class Solution {
    public int numIslands(char[][] grid) {
      // Write your solution here
      int rows = grid.length;
      if(rows == 0) return 0;
      int cols = grid[0].length;
  
      int res = 0;
      for(int i = 0; i < rows; i++){
        for(int j = 0; j < cols; j++){
          if(grid[i][j] == '1'){
            helper(i, j, grid);
            res ++;
          }
        }
      }
      return res;
    }
  
    public void helper(int i, int j, char[][] grid){
      if(i < 0 || i >= grid.length || j < 0 || j >= grid[0].length || grid[i][j] == '0') return;
      grid[i][j] = '0';
      helper(i + 1, j, grid);
      helper(i, j + 1, grid);
      helper(i - 1, j, grid);
      helper(i, j - 1, grid);
    }
  }