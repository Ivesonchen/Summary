/**
 * Given a binary tree, find its minimum depth. The minimum depth is the number of nodes along the shortest path from the root node down to the nearest leaf node.

Example:
Given the below binary tree

             5

          /       \

        3         8

           \

               4

minimum depth is 2,path is 5→8.
 */

 //Tao-lu   Tree 的 BFS 写法

class Solution {
    public int minDepth(TreeNode root) {
        if (root == null) return 0;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        int level = 0;
        while (!queue.isEmpty()) {
            level++;
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                if (node.left == null && node.right == null) return level; // reach the first leaf node
                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }
        }
        return level;
    }
}

public class Solution {
    public int minDepth(TreeNode root) {
      // Write your solution here
      if(root == null) return 0;
  
      int leftDepth = minDepth(root.left);
      int rightDepth = minDepth(root.right);
  
      if(leftDepth == 0 || rightDepth == 0) return leftDepth + rightDepth + 1;
      return Math.min(leftDepth, rightDepth) + 1;
    }
  }

  /**
   * 
#### BFS
- Shortest path; minimum depth: 想到BFS, check level by level, BFS更能确保更快找到结果
- depth definition: reach to a leaf node, where node.left == null && node.right == null
- BFS using queue, track level.

#### DFS
- Divide and Conquery一个最小值. 
- 注意处理Leaf��null: null leaf 出现的时候, 就忽略这个leaf, 直接return算有leaf
- 另一种count的方法: 用Integer.MAX_VALUE代替 null leaf，这样可以避免错误counting. (不能直接recursive)
- 这个无论如何都要走所有node, 所以dfs应该比较适合.
*/