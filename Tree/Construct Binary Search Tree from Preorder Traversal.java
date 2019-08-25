/**
 * Return the root node of a binary search tree that matches the given preorder traversal.

(Recall that a binary search tree is a binary tree where for every node, 
any descendant of node.left has a value < node.val, and any descendant of node.right has a value > node.val.  
Also recall that a preorder traversal displays the value of the node first, then traverses node.left, 
then traverses node.right.)
 */

class Solution {
    int index = 0;
    public TreeNode bstFromPreorder(int[] preorder) {
        if(preorder == null || preorder.length == 0) return null;
        
        return helper(preorder,Integer.MAX_VALUE);
    }
    
    public TreeNode helper(int[] preorder, int upper){
        if(index >= preorder.length || preorder[index] > upper) return null;
        
        TreeNode root = new TreeNode(preorder[index]);
        index ++;
        
        root.left = helper(preorder, root.val);
        root.right = helper(preorder, upper);
        
        return root;
    }
}


//index 很巧妙    会将 满足 数字 小于upper的 root 往下放   

//如果大于 说明这个数字不应该属于这个位置  就返回空   随之就进入了 右子树（upper = 上一层的界限值） or 最右子树（upper = max_value）