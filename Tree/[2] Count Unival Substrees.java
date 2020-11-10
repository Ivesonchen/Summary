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