/**
 * Given a binary tree, return the vertical order traversal of its nodes values.

For each node at position (X, Y), its left and right children respectively will be at positions (X-1, Y-1) and (X+1, Y-1).

Running a vertical line from X = -infinity to X = +infinity, whenever the vertical line touches some nodes, we report the values of the nodes in order from top to bottom (decreasing Y coordinates).

If two nodes have the same position, then the value of the node that is reported first is the value that is smaller.

Return an list of non-empty reports in order of X coordinate.  Every report will have a list of values of nodes.
https://leetcode.com/problems/vertical-order-traversal-of-a-binary-tree/
 */

 // need to compare the value when having same position so need triplet to comparr three part (column, row, value);

 //Approach 1: BFS/DFS with Global Sorting
 /**
  * Time Complexity: O(NlogN), which applies to both the BFS and DFS approaches.
    Space Complexity: O(N). Again this applies to both the BFS and DFS approaches.
  */

  class Triplet<F, S, T> {
      public final F first;
      public final S second;
      public final T third;
  
      public Triplet(F first, S second, T third) {
          this.first = first;
          this.second = second;
          this.third = third;
      }
  }

//BFS Traversal
class Solution {
    List<Triplet<Integer, Integer, Integer>> nodeList = new ArrayList<>();

    private void BFS(TreeNode root) {
        Queue<Triplet<TreeNode, Integer, Integer>> queue = new ArrayDeque();
        int row = 0, column = 0;
        queue.offer(new Triplet(root, row, column));

        while (!queue.isEmpty()) {
            Triplet<TreeNode, Integer, Integer> triplet = queue.poll();
            root = triplet.first;
            row = triplet.second;
            column = triplet.third;

            if (root != null) {
                this.nodeList.add(new Triplet(column, row, root.val));
                queue.offer(new Triplet(root.left, row + 1, column - 1));
                queue.offer(new Triplet(root.right, row + 1, column + 1));
            }
        }
    }

    public List<List<Integer>> verticalTraversal(TreeNode root) {

        List<List<Integer>> output = new ArrayList();
        if (root == null) {
            return output;
        }

        // step 1). BFS traversal
        BFS(root);

        // step 2). sort the global list by <column, row, value>
        Collections.sort(this.nodeList, new Comparator<Triplet<Integer, Integer, Integer>>() {
            @Override
            public int compare(Triplet<Integer, Integer, Integer> t1,
                    Triplet<Integer, Integer, Integer> t2) {
                if (t1.first.equals(t2.first))
                    if (t1.second.equals(t2.second))
                        return t1.third - t2.third;
                    else
                        return t1.second - t2.second;
                else
                    return t1.first - t2.first;
            }
        });

        // step 3). extract the values, partitioned by the column index.
        List<Integer> currColumn = new ArrayList();
        Integer currColumnIndex = this.nodeList.get(0).first;

        for (Triplet<Integer, Integer, Integer> triplet : this.nodeList) {
            Integer column = triplet.first, value = triplet.third;
            if (column == currColumnIndex) {
                currColumn.add(value);
            } else {
                output.add(currColumn);
                currColumnIndex = column;
                currColumn = new ArrayList();
                currColumn.add(value);
            }
        }
        output.add(currColumn);

        return output;
    }
}

//DFS Traversal
class Solution {
    List<Triplet<Integer, Integer, Integer>> nodeList = new ArrayList<>();

    private void DFS(TreeNode node, Integer row, Integer column) {
        if (node == null)
            return;
        nodeList.add(new Triplet(column, row, node.val));
        // preorder DFS traversal
        this.DFS(node.left, row + 1, column - 1);
        this.DFS(node.right, row + 1, column + 1);
    }

    public List<List<Integer>> verticalTraversal(TreeNode root) {
        List<List<Integer>> output = new ArrayList();
        if (root == null) {
            return output;
        }

        // step 1). DFS traversal
        DFS(root, 0, 0);

        // step 2). sort the list by <column, row, value>
        Collections.sort(this.nodeList, new Comparator<Triplet<Integer, Integer, Integer>>() {
            @Override
            public int compare(Triplet<Integer, Integer, Integer> t1,
                    Triplet<Integer, Integer, Integer> t2) {
                if (t1.first.equals(t2.first))
                    if (t1.second.equals(t2.second))
                        return t1.third - t2.third;
                    else
                        return t1.second - t2.second;
                else
                    return t1.first - t2.first;
            }
        });

        // step 3). extract the values, grouped by the column index.
        List<Integer> currColumn = new ArrayList();
        Integer currColumnIndex = this.nodeList.get(0).first;

        for (Triplet<Integer, Integer, Integer> triplet : this.nodeList) {
            Integer column = triplet.first, value = triplet.third;
            if (column == currColumnIndex) {
                currColumn.add(value);
            } else {
                output.add(currColumn);
                currColumnIndex = column;
                currColumn = new ArrayList();
                currColumn.add(value);
            }
        }
        output.add(currColumn);

        return output;
    }
}

//Approach 2: BFS/DFS with Partition Sorting
/**
 * Time Complexity: O(Nlog (N/K)) where k is the width of the tree, i.e. k is also the number of columns in the result.
 * Space Complexity: O(N). Again this applies to both the BFS and DFS approaches. The analysis is the same as the previous approach.
 */
//BFS Traversal
class Solution {
    // key: column; value: <row, node_value>
    Map<Integer, ArrayList<Pair<Integer, Integer>>> columnTable = new HashMap();
    int minColumn = 0, maxColumn = 0;

    private void BFS(TreeNode root) {
        // tuples of <column, <row, value>>
        Queue<Pair<TreeNode, Pair<Integer, Integer>>> queue = new ArrayDeque();
        int row = 0, column = 0;
        queue.offer(new Pair(root, new Pair(row, column)));

        while (!queue.isEmpty()) {
            Pair<TreeNode, Pair<Integer, Integer>> p = queue.poll();
            root = p.getKey();
            row = p.getValue().getKey();
            column = p.getValue().getValue();

            if (root != null) {
                if (!columnTable.containsKey(column)) {
                    columnTable.put(column, new ArrayList<Pair<Integer, Integer>>());
                }
                columnTable.get(column).add(new Pair<>(row, root.val));
                minColumn = Math.min(minColumn, column);
                maxColumn = Math.max(maxColumn, column);

                queue.offer(new Pair(root.left, new Pair(row + 1, column - 1)));
                queue.offer(new Pair(root.right, new Pair(row + 1, column + 1)));
            }
        }
    }

    public List<List<Integer>> verticalTraversal(TreeNode root) {
        List<List<Integer>> output = new ArrayList();
        if (root == null) {
            return output;
        }

        // step 1). BFS traversal
        this.BFS(root);

        // step 2). retrieve the value from the columnTable
        for (int i = minColumn; i < maxColumn + 1; ++i) {
            // order by both "row" and "value"
            Collections.sort(columnTable.get(i), new Comparator<Pair<Integer, Integer>>() {
                @Override
                public int compare(Pair<Integer, Integer> p1, Pair<Integer, Integer> p2) {
                    if (p1.getKey().equals(p2.getKey()))
                        return p1.getValue() - p2.getValue();
                    else
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

//DFS traversal
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

    public List<List<Integer>> verticalTraversal(TreeNode root) {
        List<List<Integer>> output = new ArrayList();
        if (root == null) {
            return output;
        }

        // step 1). DFS traversal
        this.DFS(root, 0, 0);

        // step 2). retrieve the value from the columnTable
        for (int i = minColumn; i < maxColumn + 1; ++i) {
            // order by both "row" and "value"
            Collections.sort(columnTable.get(i), new Comparator<Pair<Integer, Integer>>() {
                @Override
                public int compare(Pair<Integer, Integer> p1, Pair<Integer, Integer> p2) {
                    if (p1.getKey().equals(p2.getKey()))
                        return p1.getValue() - p2.getValue();
                    else
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