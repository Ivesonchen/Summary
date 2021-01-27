/**
 * Find the height of binary tree.

Examples:

        5
      /    \
    3        8
  /   \        \
1      4        11

The height of above binary tree is 3.         
 */

public class Solution {
    public int findHeight(TreeNode root) {
      // Write your solution here
      if(root == null) {return 0;}
  
      return 1 + Math.max(findHeight(root.left), findHeight(root.right));
    }
}

  /** 
   * 经典 recursion
   * 
   * the max tree height between left and right sub tree + 1
  */