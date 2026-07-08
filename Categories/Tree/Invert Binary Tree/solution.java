/**
Invert a binary tree.

     4
   /   \
  2     7
 / \   / \
1   3 6   9
to
     4
   /   \
  7     2
 / \   / \
9   6 3   1
 */
//DFS
public class Solution {
    public TreeNode invertTree(TreeNode root) {
      // Write your solution here
      if(root == null) return root;
  
      helper(root);
  
      return root;
    }
  
    public void helper(TreeNode cur){
      if(cur == null) return;
      invertTree(cur.left);
      invertTree(cur.right);
  
      TreeNode temp = cur.left;
      cur.left = cur.right;
      cur.right = temp;
    }
  }
//DFS
  class Solution {
    public TreeNode invertTree(TreeNode root) {
        if (root == null) {
            return root;
        }
        TreeNode temp = root.left;
        root.left = root.right;
        root.right = temp;
        invertTree(root.left);
        invertTree(root.right);
        
        return root;
    }
}
  /*
Thoughts:
Use a queue to keep track of nodes. Keep swapping until the queue is processed.
*/
public class Solution {
    public void invertBinaryTree(TreeNode root) {
        if (root == null) {
            return root;
        }
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while(!queue.isEmpty()) {
            TreeNode node = queue.poll();
            TreeNode temp = node.left;
            node.left = node.right;
            node.right = temp;
            if (node.left != null) {
                queue.offer(node.left);
            }
            if (node.right != null) {
                queue.offer(node.right);
            }
        }
    }
}

 /**
  * 
#### DFS
- 简单处理swap
- recursively swap children

#### BFS
- BFS with Queue
- 每次process一个node, swap children; 然后把child加进queue里面
- 直到queue process完
  */