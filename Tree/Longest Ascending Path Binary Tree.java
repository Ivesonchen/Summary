/**
 * Determine the length of the longest ascending path in the binary tree.
A valid path is a part of the path from root to any of the leaf nodes.
Examples:

        5

      /    \

    3        2

  /   \        \

1      0        11
the longest ascending path is 2 -> 11, length is 2.
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
    public int longest(TreeNode root) {
      // Write your solution here
      if(root == null) return 0;
      int[] res = new int[1];
  
      helper(root, 1, res);
      
      return res[0];
    }
  
    public void helper(TreeNode cur, int counter, int[] res){
      if(cur == null) return;
  
      res[0] = Math.max(counter, res[0]);
  
      if(cur.left != null){
        if(cur.left.key <= cur.key) helper(cur.left, 1, res);
        else helper(cur.left, counter + 1, res);
      }
  
      if(cur.right != null){
        if(cur.right.key <= cur.key) helper(cur.right, 1, res);
        else helper(cur.right, counter + 1, res);
      }
    }
  }