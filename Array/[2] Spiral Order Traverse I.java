/**
 * Traverse an N * N 2D array in spiral order clock-wise starting from the top left corner. Return the list of traversal sequence.

Assumptions

The 2D array is not null and has size of N * N where N >= 0
Examples

{ {1,  2,  3},

  {4,  5,  6},

  {7,  8,  9} }

the traversal sequence is [1, 2, 3, 6, 9, 8, 7, 4, 5]

 */

public class Solution {
  public List<Integer> spiral(int[][] matrix) {
    // Write your solution here
    List<Integer> res = new ArrayList<>();

    helper(matrix, res, 0, matrix.length);
    return res;
  }

  public void helper(int[][] matrix, List<Integer> res, int offSet, int size){
    if(size < 1) {
      return;
    } else if (size == 1) {
      res.add(matrix[0 + offSet][0 + offSet]);
      return;
    }

    for(int i = 0; i < size - 1; i++){
      res.add(matrix[offSet][i + offSet]);
    }; // top row from left to right - 1
    for(int i = 0; i < size - 1; i++){
      res.add(matrix[i + offSet][offSet + size - 1]);
    };// right column from top to bottom - 1

    for(int i = 0; i < size - 1; i++){
      res.add(matrix[offSet + size - 1][offSet + size - 1 - i]);
    };//bottom row from right to left - 1
    for(int i = 0; i < size - 1; i++){
      res.add(matrix[offSet + size - 1 - i][offSet]);
    };//left column from bottom to top - 1

    helper(matrix, res, offSet + 1, size - 2);
  }
}

  /**
   * recursion     because of it's N * N    we can control it by using offSet to write the recursion rule (Cycle by cycle)
   */