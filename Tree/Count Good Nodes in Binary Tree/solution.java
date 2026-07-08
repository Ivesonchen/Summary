/**
 * 1448. Count Good Nodes in Binary Tree
 */

class Solution {
  int res = 0;
  
  public int goodNodes(TreeNode root) {
      if(root == null) return 0;
  
      helper(root, root.val);
      
      return res; 
  }
  
  public void helper(TreeNode cur, int max) {
      if(cur == null) return;
      
      if(cur.val >= max) res++;
      
      max = Math.max(cur.val, max);
      
      helper(cur.left, max);
      helper(cur.right, max);
  }
}