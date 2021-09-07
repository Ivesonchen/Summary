/**
 * Given a binary tree in which each node contains an integer number. 
 * Find the maximum possible sum from any node to any node (the start node and the end node can be the same). 
Assumptions
​The root of the given binary tree is not null
Examples

   -1
  /    \
2      11
     /    \
    6    -14
one example of paths could be -14 -> 11 -> -1 -> 2
another example could be the node 11 itself
The maximum path sum in the above binary tree is 6 + 11 + (-1) + 2 = 18
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
      int[] res = new int[]{Integer.MIN_VALUE};
      helper(root, res);
  
      return res[0];
    }
  
    public int helper(TreeNode cur, int[] res){
      if(cur == null) return 0;
  
      int left = helper(cur.left, res);
      int right = helper(cur.right, res);
  
      int leftMax = Math.max(left, 0);              // 可以这样取 是因为不要求是 Leaf node
      int rightMax = Math.max(right, 0);
  
      res[0] = Math.max(res[0], leftMax + rightMax + cur.key);
  
      return Math.max(leftMax, rightMax) + cur.key;
    }
}