/**
 * Given a binary tree, return the sum of values of nodes with even-valued grandparent.  (A grandparent of a node is the parent of its parent, if it exists.)

If there are no nodes with an even-valued grandparent, return 0.
 */

 /**
  * 

                6
            7       8
         2!    7!  1!   3!
       9     1  4       5!
  */

class Solution {
    public int sumEvenGrandparent(TreeNode root) {
        return helper(root, 1, 1);
    }
    
    public int helper(TreeNode cur, int p, int gp){
        if(cur == null){
            return 0;
        }
        return helper(cur.left, cur.val, p) + helper(cur.right, cur.val, p) + (gp % 2 == 0 ? cur.val : 0);
    }
}