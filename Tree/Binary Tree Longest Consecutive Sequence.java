/**
 * Given a binary tree, find the length of the longest consecutive sequence path.

The path refers to any sequence of nodes from some starting node to any node in the tree along the parent-child connections. 
The longest consecutive path need to be from parent to child (cannot be the reverse).

For example,

   1
    \
     3
    / \
   2   4
        \
         5
Longest consecutive sequence path is 3-4-5, so return 3.

   2
    \
     3
    / 
   2    
  / 
 1
Longest consecutive sequence path is 2-3,not3-2-1, so return 2.
 */

 // Top - Down  recursion    pass counter and update global max value accordingly
 
public class Solution {
    int length = 0;
    public int longestConsecutive(TreeNode root) {
      // Write your solution here
      helper(root, 1);
  
      return length;
    }
  
    public void helper(TreeNode cur, int counter) {
      if(cur == null) return;
  
      length = Math.max(length, counter);
  
      if(cur.left != null) {
        if(cur.left.key == cur.key + 1) helper(cur.left, counter + 1);
        else helper(cur.left, 1);
      }
  
      if(cur.right != null) {
        if(cur.right.key == cur.key + 1) helper(cur.right, counter + 1);
        else helper(cur.right, 1);
      }
    }
}