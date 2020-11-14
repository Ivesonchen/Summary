/**
 * Given a binary tree, count the number of uni-value subtrees.

A Uni-value subtree means all nodes of the subtree have the same value.

For example:
Given binary tree,

              5
             / \
            1   5
           / \   \
          5   5   5
return 4.
 */

class Solution {
    public int count = 0;
    public int countUnivalSubtrees(TreeNode root) {

        if(root == null) return count;
        
        if(isUnivalTree(root)) count++;
        countUnivalSubtrees(root.left);
        countUnivalSubtrees(root.right);
        return count;
        
    }
    
    public boolean isUnivalTree(TreeNode root) {
        if(root == null) return true;
        
        if(root.left != null) {
            if(root.left.key != root.key) return false;
        }
        
        if(root.right != null) {
            if(root.right.key != root.key) return false;
        }

        return isUnivalTree(root.left) && isUnivalTree(root.right);
    }
}

/**
 * DFS travsel all the node in the root tree and check every subtree if it's a Unival Tree
 */

 // from bottom to up   根据合理条件 发现合成的root 为 univaltree 时 count ++

public int countUnivalSubtrees(TreeNode root) {
    if(root == null) return 0;
    helper(root);
    
    return count;
}

public boolean helper(TreeNode node){
    if(node == null) return true;
    
    boolean left = helper(node.left);
    boolean right = helper(node.right);
    
    if(!left || !right) return false;
    
    boolean flag = true;
    
    if(node.left != null && node.left.val != node.val) flag = false;
    if(node.right != null && node.right.val != node.val) flag = false;
    if(flag) count++;
    
    return flag;        
}