/**
 * Given a matrix of size N x M. For each row the elements are sorted in ascending order, and for each column the elements are also sorted in ascending order. Find the Kth smallest number in it.

Assumptions

the matrix is not null, N > 0 and M > 0
K > 0 and K <= N * M
Examples

{ {1,  3,   5,  7},

  {2,  4,   8,   9},

  {3,  5, 11, 15},

  {6,  8, 13, 18} }

the 5th smallest number is 4
the 8th smallest number is 6

https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/solution/
 */
// Min-Heap approach  每一行取出一个值 放进min-heap中
class MyHeapNode {
    int row;
    int column;
    int value;
  
    public MyHeapNode(int v, int r, int c) {
      this.value = v;
      this.row = r;
      this.column = c;
    }
  
    public int getValue() {
      return this.value;
    }
  
    public int getRow() {
      return this.row;
    }
  
    public int getColumn() {
      return this.column;
    }
}
  
  class MyHeapComparator implements Comparator<MyHeapNode> {
    public int compare(MyHeapNode x, MyHeapNode y) {
      return x.value - y.value;
    }
  }
  
  class Solution {
  
    public int kthSmallest(int[][] matrix, int k) {
  
      int N = matrix.length;
  
      PriorityQueue<MyHeapNode> minHeap =
          new PriorityQueue<MyHeapNode>(Math.min(N, k), new MyHeapComparator());
  
      // Preparing our min-heap
      for (int r = 0; r < Math.min(N, k); r++) {
  
        // We add triplets of information for each cell
        minHeap.offer(new MyHeapNode(matrix[r][0], r, 0));
      }
  
      MyHeapNode element = minHeap.peek();
      while (k-- > 0) {
  
        // Extract-Min
        element = minHeap.poll();
        int r = element.getRow(), c = element.getColumn();
  
        // If we have any new elements in the current row, add them
        if (c < N - 1) {
  
          minHeap.offer(new MyHeapNode(matrix[r][c + 1], r, c + 1));
        }
      }
  
      return element.getValue();
    }
  }

  // Binary Search
public class Solution {
    public int kthSmallest(int[][] matrix, int k) {
      // Write your solution here
  
      int low = matrix[0][0], hi = matrix[matrix.length - 1][matrix[0].length - 1] + 1;
      while(low < hi){
        int mid = low + (hi - low) / 2;
        int count = 0, j = matrix[0].length - 1;
        for(int i = 0; i < matrix.length; i++){
          while(j >= 0 && matrix[i][j] > mid) j--;
          count += (j + 1);
        }
        if(count < k) low = mid + 1;
        else hi = mid;
      }·
      return low;
    }
  }