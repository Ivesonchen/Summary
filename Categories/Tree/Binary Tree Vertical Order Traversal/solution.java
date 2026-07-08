/**
 * Given a binary tree, return the vertical order traversal of its nodes' values. (ie, from top to bottom, column by column).

If two nodes are in the same row and column, the order should be from left to right.
https://leetcode.com/problems/binary-tree-vertical-order-traversal/solution/
 */

 // from left to right  Not need to sort it by value of the node

 // Approach 1: Breadth-First Search (BFS)
 /**
  * Time Complexity: O(NlogN) where NN is the number of nodes in the tree.
  Space Complexity: O(N) where NN is the number of nodes in the tree.
  */
class Solution {
    public List<List<Integer>> verticalOrder(TreeNode root) {
      List<List<Integer>> output = new ArrayList();
      if (root == null) {
        return output;
      }
  
      Map<Integer, ArrayList> columnTable = new HashMap();
      Queue<Pair<TreeNode, Integer>> queue = new ArrayDeque();
      int column = 0;
      queue.offer(new Pair(root, column));
  
      while (!queue.isEmpty()) {
        Pair<TreeNode, Integer> p = queue.poll();
        root = p.getKey();
        column = p.getValue();
  
        if (root != null) {
          if (!columnTable.containsKey(column)) {
            columnTable.put(column, new ArrayList<Integer>());
          }
          columnTable.get(column).add(root.val);
  
          queue.offer(new Pair(root.left, column - 1));
          queue.offer(new Pair(root.right, column + 1));
        }
      }
  
      List<Integer> sortedKeys = new ArrayList<Integer>(columnTable.keySet());
      Collections.sort(sortedKeys);
      for(int k : sortedKeys) {
        output.add(columnTable.get(k));
      }
  
      return output;
    }
  }

  // Approach 2: BFS without Sorting
  /**
   * Time Complexity: O(N) where NN is the number of nodes in the tree.
   * Space Complexity: O(N) where NN is the number of nodes in the tree. 
   * The analysis follows the same logic as in the previous BFS approach.
   */
  class Solution {
    public List<List<Integer>> verticalOrder(TreeNode root) {
      List<List<Integer>> output = new ArrayList();
      if (root == null) {
        return output;
      }
  
      Map<Integer, ArrayList> columnTable = new HashMap();
      // Pair of node and its column offset
      Queue<Pair<TreeNode, Integer>> queue = new ArrayDeque();
      int column = 0;
      queue.offer(new Pair(root, column));
  
      int minColumn = 0, maxColumn = 0;
  
      while (!queue.isEmpty()) {
        Pair<TreeNode, Integer> p = queue.poll();
        root = p.getKey();
        column = p.getValue();
  
        if (root != null) {
          if (!columnTable.containsKey(column)) {
            columnTable.put(column, new ArrayList<Integer>());
          }
          columnTable.get(column).add(root.val);
          minColumn = Math.min(minColumn, column);
          maxColumn = Math.max(maxColumn, column);
  
          queue.offer(new Pair(root.left, column - 1));
          queue.offer(new Pair(root.right, column + 1));
        }
      }
  
      for(int i = minColumn; i < maxColumn + 1; ++i) {
        output.add(columnTable.get(i));
      }
  
      return output;
    }
  }

  //Approach 3: Depth-First Search (DFS)
  /**
   * Time Complexity: O(W⋅HlogH)) 
   * where W is the width of the binary tree (i.e. the number of columns in the result) and H is the height of the tree.
   * Space Complexity: O(N) where N is the number of nodes in the tree.
   */

  class Solution {
    Map<Integer, ArrayList<Pair<Integer, Integer>>> columnTable = new HashMap();
    int minColumn = 0, maxColumn = 0;
  
    private void DFS(TreeNode node, Integer row, Integer column) {
      if (node == null)
        return;
  
      if (!columnTable.containsKey(column)) {
        this.columnTable.put(column, new ArrayList<Pair<Integer, Integer>>());
      }
  
      this.columnTable.get(column).add(new Pair<Integer, Integer>(row, node.val));
      this.minColumn = Math.min(minColumn, column);
      this.maxColumn = Math.max(maxColumn, column);
      // preorder DFS traversal
      this.DFS(node.left, row + 1, column - 1);
      this.DFS(node.right, row + 1, column + 1);
    }
  
    public List<List<Integer>> verticalOrder(TreeNode root) {
      List<List<Integer>> output = new ArrayList();
      if (root == null) {
        return output;
      }
  
      this.DFS(root, 0, 0);
  
      // Retrieve the resuts, by ordering by column and sorting by row
      for (int i = minColumn; i < maxColumn + 1; ++i) {
  
        Collections.sort(columnTable.get(i), new Comparator<Pair<Integer, Integer>>() {
          @Override
          public int compare(Pair<Integer, Integer> p1, Pair<Integer, Integer> p2) {
            return p1.getKey() - p2.getKey();
          }
        });
  
        List<Integer> sortedColumn = new ArrayList();
        for (Pair<Integer, Integer> p : columnTable.get(i)) {
          sortedColumn.add(p.getValue());
        }
        output.add(sortedColumn);
      }
  
      return output;
    }
  }