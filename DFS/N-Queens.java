/**
 * N-Queen 问题, 给数字n, 和 nxn board, 找到所有N-queens的答案.

#### Backtracking
- 用dfs找所有情况, 每一个iteration, 从找一行里挑合适的点, dfs
- 选中的点加进candidate list 里面, 记得要backtracking.
- 每一个candidate都需要validation, 检查 row, col, 2 diagnal 有没有queen

#### validate n queue at certain (x, y)
- 1. array 里面不能有 target row#
- 2. diagnal. 记得公式：
- row1 - row2 == col1 - col2.     Diagnal elelment.fail
- row1 - row2 == - (col1 - col2). Diagnal element. fail
- Draw a 3x3 board to test the 2 scanarios:
- (0,0) and (3,3) are diagnal
- (0,2) and (2,0) are diagnal
 */
/*
The n-queens puzzle is the problem of placing n queens on an n×n chessboard such that 
no two queens attack each other.
Given an integer n, return all distinct solutions to the n-queens puzzle.
Each solution contains a distinct board configuration of the n-queens' placement, 
where 'Q' and '.' both indicate a queen and an empty space respectively.
For example,
There exist two distinct solutions to the 4-queens puzzle:
[
 [".Q..",  // Solution 1
  "...Q",
  "Q...",
  "..Q."],
 ["..Q.",  // Solution 2
  "Q...",
  "...Q",
  ".Q.."]
]
Hide Tags Backtracking
*/


class Solution {
    public List<List<String>> solveNQueens(int n) {
        List<List<String>> rst = new ArrayList<>();
        if (n <= 0) return rst;

        dfs(rst, new ArrayList<>(), n);

        return rst;
    }
    // build sequence row
    private void dfs(List<List<String>> rst, List<Integer> list, int n){
        if (list.size() == n) {
            rst.add(createBoard(list));
            return;
        }
        //For next row, which col to put queen? Now do recursive:
        for (int i = 0; i < n; i++) {
            if (validate(list, i)) {
                list.add(i);
                dfs(rst, list, n);
                list.remove(list.size() - 1);
            }
        }
    }
    
    /*
      Validate the board with given input.
      Draw a 3x3 board to test the 2 scanarios:
      - (0,0) and (3,3) are diagnal
      - (0,2) and (2,0) are diagnal
    */
    private boolean validate(List<Integer> list, int newColNum) {
        int newRowNum = list.size(); // the new row that colNum is going to be put on
        for (int rowNum = 0; rowNum < list.size(); rowNum++) {
            //check row, check diagnal
            int colNum = list.get(rowNum);
            if (colNum == newColNum || Math.abs(rowNum - newRowNum) == Math.abs(colNum - newColNum)) {
                return false;
            }
        }
        return true;
    }
    /*
    private boolean validate(List<Integer> list, int newColNum) {
        int newRowNum = list.size(); // the new row that colNum is going to be put on
        for (int rowNum = 0; rowNum < list.size(); rowNum++) {
            //check row
            int colNum = list.get(rowNum);
            if (colNum == newColNum) {
                return false;
            }
            //check diagnal
            //q1 row - newQ row == q1 col - newQ col
            if (rowNum - newRowNum == colNum - newColNum) {
                return false;
            }
            //q1 row - newQ row == -(q1 col - newQ col)
            if (rowNum - newRowNum == - (colNum - newColNum)) {
                return false;
            }
        }
        return true;
    }*/

    private List<String> createBoard(List<Integer> list){
        List<String> board = new ArrayList<>();
        for (int row = 0; row < list.size(); row++) {
            StringBuffer sb = new StringBuffer();
            for (int col : list) {
                if (row == col) {
                    sb.append("Q");
                } else {
                    sb.append(".");
                }
            }
            board.add(sb.toString());
        }
        return board;
    }
}