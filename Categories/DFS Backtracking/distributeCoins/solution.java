/**
 * 
 * Given the root of a binary tree with N nodes, each node in the tree has node.val coins, and there are N coins total.

In one move, we may choose two adjacent nodes and move one coin from one node to another.  
(The move may be from parent to child, or from child to parent.)

Return the number of moves required to make every node have exactly one coin.
https://leetcode.com/problems/distribute-coins-in-binary-tree/
 */

class Solution {
    
    int steps = 0;
    public int distributeCoins(TreeNode root) {
        helper(root);
        
        return steps;
    }
    
    public int helper(TreeNode cur){
        if(cur == null) return 0;
        
        int left = helper(cur.left);
        int right = helper(cur.right);
        steps += Math.abs(left) + Math.abs(right);
        return cur.val + left + right -1;
    }
}