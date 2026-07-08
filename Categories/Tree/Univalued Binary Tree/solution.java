/**
 * A binary tree is univalued if every node in the tree has the same value.

Return true if and only if the given tree is univalued.
 */


class Solution {
    public boolean isUnivalTree(TreeNode root) {
        if(root == null) return true;
        
        if(root.left != null) {
            if(root.left.val != root.val) return false;
        }
        
        if(root.right != null) {
            if(root.right.val != root.val) return false;
        }
        
        return isUnivalTree(root.left) && isUnivalTree(root.right);
    }
}

/**
 *         if(root.left != null && root.left.val != root.val) return false;
 */