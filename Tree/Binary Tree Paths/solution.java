/**
 * Given a binary tree, return all root-to-leaf paths from left to right.

For example, given the following binary tree:

   1
 /   \
2     3
 \
  5
All root-to-leaf paths are:

["1->2->5", "1->3"]
 */

public class Solution {
    public String[] binaryTreePaths(TreeNode root) {
      // Write your solution here
      if(root == null) return new String[0];
      List<String> res = new ArrayList<>();
      
      StringBuilder sb = new StringBuilder();
      helper(res, sb, root);
      
      String[] re = new String[res.size()];
      for(int i = 0; i < res.size(); i++){
        re[i] = res.get(i);
      }
  
      return re;
    }
  
    public void helper(List<String> res, StringBuilder sb, TreeNode cur){
  
      int len = sb.length();
      sb.append(cur.key);
      if(cur.left == null && cur.right == null) {
        res.add(sb.toString());
      }
      sb.append("->");
      if(cur.left != null) helper(res, sb, cur.left);
      if(cur.right != null) helper(res, sb, cur.right);
      
      sb.setLength(len); //Backtrack
    }
  }