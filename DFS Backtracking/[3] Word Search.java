/**
 * Given a 2D board and a word, find if the word exists in the grid.The word can be constructed from letters of sequentially adjacent cell, where "adjacent" cells are those horizontally or vertically neighboring. The same letter cell may not be used more than once.

Input: board = [
                    [“ABCE”],
                    [“SFCS”],
                    [“ADEE”]
                ]

Output: Word = “ABCCED”   return true

            Word = “SEE”      return true

            Word = “ABCB”      return false
 */

/**
 * 有个技巧用'#' 标记走过的 路径  之后再清除 # 来实现backtracking
 */

public class Solution {
    public boolean isWord(char[][] board, String word) {
      // Write your solution here
      if(board.length == 0) return false;
      int m = board.length;
      int n = board[0].length;
  
      for(int i = 0; i < m; i++){
        for(int j = 0; j < n; j++){
          if(dfs(board, word, i, j, 0)) {
            return true;
          }
        }
      }
  
      return false;
    }
  
    public boolean dfs(char[][] board, String word, int x, int y, int index){
      if(index == word.length()){
        return true;
      }
      if(x < 0 || y < 0 || x >= board.length || y >= board[x].length || board[x][y] == '#') return false;
  
      boolean res = false;
  
      if(board[x][y] == word.charAt(index)){
        int[] dx = {1,0,-1,0};
        int[] dy = {0,1,0,-1};
  
        board[x][y] = '#';
        for(int i = 0; i < 4; i++){
          res = dfs(board, word, x + dx[i], y + dy[i], index + 1);
          if(res) break;
        }
        board[x][y] = word.charAt(index);
      }
  
      return res;
    }
  }