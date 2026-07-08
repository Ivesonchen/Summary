/**
 * Given a binary tree in which each node contains an integer number. 
 * Find the maximum possible subpath sum(both the starting and ending node of the subpath should be on the same path 
 * from root to one of the leaf nodes, 
 * and the subpath is allowed to contain only one node).

Assumptions

The root of given binary tree is not null
Examples

   -5
  /   \
2      11
     /    \
    6     14
           /
        -3
        
The maximum path sum is 11 + 14 = 25

How is the binary tree represented?

We use the level order traversal sequence with a special symbol "#" denoting the null node.

For Example:

The sequence [1, 2, 3, #, #, 4] represents the following binary tree:

    1
  /   \
 2     3
      /
    4
 */

public class Solution {
    public int maxPathSum(TreeNode root) {
      // Write your solution here
      if(root == null) return 0;
      int[] result = new int[]{Integer.MIN_VALUE};
  
      helper(root, 0, result);
  
      return result[0];
    }
  
    public void helper(TreeNode root, int pre, int[] result){
      if(root == null) return;
  
      if(pre <= 0) pre = root.key; // reset;
      else pre += root.key;
  
      result[0] = Math.max(result[0], pre);
      helper(root.left, pre, result);
      helper(root.right, pre, result);
    }
  }