/**
 * Find the second largest key in the given binary search tree.

If there does not exist the second largest key, return Integer.MIN_VALUE.

Assumptions:

The given binary search tree is not null.
Examples:

    2

  /   \

 1     4

      /

    3

the second largest key is 3.
 */

public class Solution {

    public int secondLargest(TreeNode root) {
  
      return helper(root, Integer.MIN_VALUE, false);
    }
  
    public int helper(TreeNode cur, int sec, boolean wentLeft){
      if(cur.right != null){
        return helper(cur.right, cur.key, wentLeft);
      } else if (cur.left != null) {
        if(wentLeft == false) return helper(cur.left, cur.key, true);
        else return cur.key;
      } else {
        if(wentLeft) return cur.key;
        else return sec;
      }
    }
}