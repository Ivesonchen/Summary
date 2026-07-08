/**
 * Determine if a given binary tree is full.

A full binary tree is defined as a binary tree in which all nodes have either zero or two child nodes. 
Conversely, there is no node in a full binary tree, which has one child node.

If the root is null, return false.
 */

 /**
  * Recursion    感悟 make sure 你的返回值 cover了 所有可能出现的情况  true or false 的 情况
  */
public class Solution {
    public boolean isFull(TreeNode root) {
      // Write your solution here
      if(root == null) return false;
      if(root.left == null && root.right == null) return true;
      
      return isFull(root.left) && isFull(root.right);
    }
}  