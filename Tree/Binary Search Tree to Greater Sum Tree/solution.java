/*
Given the root of a binary search tree with distinct values, 
modify it so that every node has a new value equal to the sum of the values of the original tree that are greater than or equal to node.val.

As a reminder, a binary search tree is a tree that satisfies these constraints:

The left subtree of a node contains only nodes with keys less than the node's key.
The right subtree of a node contains only nodes with keys greater than the node's key.
Both the left and right subtrees must also be binary search trees.
*/
/**
 *                          4:30
 *              1:36                    6:21
 *      0:36        2:35            5:26       7:15
 *                      3:33                        8:8            
 */


class Solution {
    public TreeNode bstToGst(TreeNode root) {
        helper(root, 0);
        return root;
    }
    
    public int helper(TreeNode cur, int num){
        if(cur == null) return num;
        
        int right = helper(cur.right, num);
        cur.val = right + cur.val;
        
        return helper(cur.left, cur.val);
    }
    // pass a number to accumulate and change the node's value
}