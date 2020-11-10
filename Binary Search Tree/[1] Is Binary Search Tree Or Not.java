/**
 * Determine if a given binary tree is binary search tree.There should no be duplicate keys in binary search tree.

Assumptions

You can assume the keys stored in the binary search tree can not be Integer.MIN_VALUE or Integer.MAX_VALUE.
Corner Cases

What if the binary tree is null? Return true in this case.
 */
/**
 * // pre means pre pointer in the inorder sequence     it supposed to meed the requirements that   pre < cur in the inorder sequence
 */
public class Solution {
    TreeNode pre;
  
    public boolean isBST(TreeNode root) {
      // Write your solution here
      if(root == null) return true;
  
      return helper(root);
    }
  
    public boolean helper(TreeNode cur){
      if(cur == null) return true;
  
      if(!helper(cur.left)) return false;
  
      if(pre != null && pre.key >= cur.key) return false;
  
      pre = cur;
      
      if(!helper(cur.right)) return false;
      
      return true;
    }
  }

  class Solution {
    public boolean isValidBST(TreeNode root) {
        if(root == null) return true;
        
        Stack<TreeNode> stack = new Stack<>();
        
        TreeNode cur = root;
        TreeNode pre = null;
        
        while(cur != null || !stack.isEmpty()) {
            while(cur != null) {
                stack.push(cur);
                cur = cur.left;
            }
            
            cur = stack.pop();
            
            if(pre != null && cur.val <= pre.val) return false;
            
            pre = cur;
            cur = cur.right;
        }
        return true;
    }
}