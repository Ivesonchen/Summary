/**
 * Given two nodes in a binary tree, find their lowest common ancestor.

Assumptions

There is no parent pointer for the nodes in the binary tree

The given two nodes are guaranteed to be in the binary tree

Examples

        5
      /   \
     9     12
   /  \      \
  2    3      14

The lowest common ancestor of 2 and 14 is 5

The lowest common ancestor of 2 and 9 is 9
 */

public class Solution {
    public TreeNode lowestCommonAncestor(TreeNode root,
        TreeNode one, TreeNode two) {
      // Write your solution here.
      if(root == null) return null;
      if(root.key == one.key || root.key == two.key) return root;
  
      TreeNode left = lowestCommonAncestor(root.left, one, two);
      TreeNode right = lowestCommonAncestor(root.right, one, two);
  
      if(left != null && right != null) return root;
      return left == null ? right : left;
    }
}

/**
 * Recursion 
 * 将找到的目标往上返回       不同的case  如果同时找到了 就返回 那个父节点
 * 
 * 从下向上传值
 */