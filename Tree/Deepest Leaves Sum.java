//Given a binary tree, return the sum of values of its deepest leaves.


/*
hint1 : Traverse the tree to find the max depth.
hint2 : Traverse the tree again to compute the sum required.
*/

class Solution {
    
    public int deepestLeavesSum(TreeNode root) {
        int deepest = getHeights(root);
        if(deepest == 0)return 0;
        return getSum(root, 1, deepest);
    }
    
    public int getSum(TreeNode cur, int level, int deepest){
        if(cur == null) return 0;
        if(level == deepest) return cur.val;
        return getSum(cur.left, level + 1, deepest) + getSum(cur.right, level + 1, deepest);
    }
    
    public int getHeights(TreeNode cur){
        if(cur == null) return 0;
        return Math.max(getHeights(cur.left), getHeights(cur.right)) + 1;
    }
}