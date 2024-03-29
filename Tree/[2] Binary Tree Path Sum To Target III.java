/**
 * Given a binary tree in which each node contains an integer number. 
 * Determine if there exists a path (the path can only be from one node to itself or to any of its descendants), 
 * the sum of the numbers on the path is the given target number.

Examples

    5
  /    \
2      11
     /    \
    6     14
  /
 3

If target = 17, There exists a path 11 + 6, the sum of the path is target.

If target = 20, There exists a path 11 + 6 + 3, the sum of the path is target.

If target = 10, There does not exist any paths sum of which is target.

If target = 11, There exists a path only containing the node 11.

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
    public boolean exist(TreeNode root, int target) {
      // Write your solution here
      if(root == null) return false;
  
      return helper(root, target, 0);
    }
  
    public boolean helper(TreeNode cur, int target, int counter){
      if(cur == null && counter != target) return false;
      if(counter == target) return true;
  
      return helper(cur.left, target, counter + cur.key) || helper(cur.left, target, cur.key) 
              || helper(cur.right, target, counter + cur.key) || helper(cur.right, target, cur.key);
    }
  }