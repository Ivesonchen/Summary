/**
 * Given two nodes in a binary tree, find their lowest common ancestor (the given two nodes are not guaranteed to be in the binary tree).

Return null If any of the nodes is not in the tree.
Assumptions
There is no parent pointer for the nodes in the binary tree
The given two nodes are not guaranteed to be in the binary tree
Examples

        5
      /   \
     9     12
   /  \      \
  2    3      14

The lowest common ancestor of 2 and 14 is 5
The lowest common ancestor of 2 and 9 is 9
The lowest common ancestor of 2 and 8 is null (8 is not in the tree)
 */

// 额外加一个 DFS检测 所有节点里面 有没有 target node
public class Solution {
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode one, TreeNode two) {
      // write your solution here
      if(findNode(root, one) == false || findNode(root, two) == false) return null;
      
      return helper(root, one, two);
    }
  
    public boolean findNode(TreeNode root, TreeNode target){
      if(root == null || target == null) return false;
      if(root == target) return true;
  
      return findNode(root.left, target) || findNode(root.right, target);
    }
  
    public TreeNode helper(TreeNode root, TreeNode one, TreeNode two){
      if(root == null || root.key == one.key || root.key == two.key) return root;
  
      TreeNode left = helper(root.left, one, two);
      TreeNode right = helper(root.right, one, two);
  
      if(left != null && right != null) return root;
  
      return left == null ? right : left;
    }
}