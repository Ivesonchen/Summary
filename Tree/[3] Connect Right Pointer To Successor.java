/**
 * Connect the node whose right child is NULL to the successor node in in-order sequence.

Examples:

     11

    /  \

  2     29

 /  \  /  \

1   7 15  40

          /

         35

the added edges are:

1.right = 2

7.right = 11

15.right = 29

35.right = 15
 */

 // pre 代表了一个 可以被挂靠的 点 (means 它的 右节点为空)
public class Solution {
    TreeNode pre = new TreeNode(-1);
  
    public void connect(TreeNode root) {
      // Write your solution here.
      if(root == null) return ;
  
      connect(root.left);
  
      if(pre != null) {
        pre.right = root;
        pre = null;
      }
      if(root.right == null) pre = root;
      
      connect(root.right);
    }
  }