/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode(int x) { val = x; }
 * }
 */
public class Codec {
  // Encodes a tree to a single string.
  int index = 0;
  
  public String serialize(TreeNode root) {
      StringBuilder sb = new StringBuilder();
      dfs(root, sb);
      
      return sb.toString();
  }
  
  public void dfs(TreeNode cur, StringBuilder sb) {
      if(cur == null) {
          sb.append("none" + ",");
          return;
      }
      
      sb.append(cur.val + ",");
      dfs(cur.left, sb);
      dfs(cur.right, sb);
  }

  // Decodes your encoded data to tree.
  public TreeNode deserialize(String data) {
      String[] order = data.split(",");
      
      return helper(order);
  }
  
  public TreeNode helper(String[] order){
      String st = order[index];
      
      if(order[index].equals("none")) {
          index++;
          return null;
      }
      
      TreeNode cur = new TreeNode(Integer.valueOf(order[index]));
      index++;

      cur.left = helper(order);
      cur.right = helper(order);
      return cur;
  }
}