/**\
 *Given a 2d grid map of '1's (land) and '0's (water), count the number of islands. 
 An island is surrounded by water and is formed by connecting adjacent lands horizontally or vertically. 
 You may assume all four edges of the grid are all surrounded by water.
 */

 /**
  * Input:
11110
11010
11000
00000

Output: 1
  */

  class Solution {
    public int numIslands(char[][] grid) {
        int res = 0;
        int m = grid.length;
        if(m == 0) return 0;
        int n = grid[0].length;
        for(int i = 0; i < m; i++){
            for(int j = 0; j < n; j++){
                if(grid[i][j] == '1'){
                    dfs(grid, i, j, m, n);
                    res++;
                }
            }
        }
        return res;
    }
    
    // dfs 感觉很像滴水 晕染
    private void dfs(char[][] grid, int i, int j, int m, int n){
        if(i < 0 || j < 0 || i >= m || j >= n || grid[i][j] == '0' ) return;
        grid[i][j] = '0';
        dfs(grid, i, j + 1, m, n);
        dfs(grid, i, j - 1, m, n);
        dfs(grid, i + 1, j, m, n);
        dfs(grid, i - 1, j, m, n);
    }
}

/**
 * #### DFS
- More or less like a graph problem: visit all nodes connected with the starting node.
- top level 有一个 double for loop, 查看每一个点.
- 每当遇到1, count+1, 然后DFS helper function 把每个跟这个当下island 相关的都Mark成 '0'
- 这样确保每个visited 过得island都被清扫干净
- O(mn) time, visit all nodes
 */