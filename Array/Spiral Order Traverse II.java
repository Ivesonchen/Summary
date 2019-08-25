/**
 * Traverse an M * N 2D array in spiral order clock-wise starting from the top left corner. Return the list of traversal sequence.

Assumptions

The 2D array is not null and has size of M * N where M, N >= 0
Examples

{ {1,  2,  3,  4},

  {5,  6,  7,  8},

  {9, 10, 11, 12} }

the traversal sequence is [1, 2, 3, 4, 8, 12, 11, 10, 9, 5, 6, 7]
 */

 /*
Thoughts:
- Keep visited
- keep moving until hit visited, then turn
*/class Solution {
    int[] dx = {0, 1, 0, -1}; // RIGHT->DOWN->LEFT->UP
    int[] dy = {1, 0, -1, 0};

    public List<Integer> spiralOrder(int[][] matrix) {
        // check edge case
        List<Integer> rst = new ArrayList<>();
        if (matrix == null || matrix.length == 0 || matrix[0] == null || matrix[0].length == 0) {
            return rst;
        }
        
        int m = matrix.length;
        int n = matrix[0].length;
        // handle single col case
        if (n == 1) {
            for (int i = 0; i < m; i++) {
                rst.add(matrix[i][0]);
            }
            return rst;
        }

        // construct boolean visited[][], dx{}, dy{}
        boolean[][] visited = new boolean[m][n];

        // while keep moving until count == m*n
        int i = 0, x = 0, y = 0;
        int direction = 0;
        while (i < m * n) {
            i++;
            rst.add(matrix[x][y]);
            visited[x][y] = true;

            // compute x/y based on current direction
            direction = computeDirection(visited, x, y, direction); // 0 1 2 3 represent it's current moving direction
            x += dx[direction];
            y += dy[direction];
        }

        return rst;
    }

    private int computeDirection(boolean[][] visited, int x, int y, int currDirection) {
        int nextX = x + dx[currDirection];
        int nextY = y + dy[currDirection];
        if (nextX >= 0 && nextX < visited.length && nextY >= 0 && nextY < visited[0].length && !visited[nextX][nextY]) {
            return currDirection;
        }
        return (currDirection + 1) % 4; // change to another direction   (loop in the direction array)
    }
}