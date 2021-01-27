/*
Determine whether two given binary trees are identical assuming any number of ‘tweak’s are allowed. 
A tweak is defined as a swap of the children of one node in the tree.

Examples

        5
      /    \
    3        8
  /   \
1      4

and

        5
      /   \
    8      3
         /   \
        1     4

  the two binary trees are tweaked identical.
  检查binary tree是否 identical.  
 
  特点: subtree如果是有旋转的, 只要tree node value相等, 就可以算是identical
*/

// time O(min(m, n)) space O(min(m,n))
public class Solution {
    public boolean isTweakedIdentical(TreeNode one, TreeNode two) {
      // Write your solution here
      if(one == null || two == null) {
        return one == null && two == null;
      }
      // recursive rule
      if(one.key != two.key) return false;
  
      return isTweakedIdentical(one.left, two.left) && isTweakedIdentical(one.right, two.right) || 
             isTweakedIdentical(one.left, two.right) && isTweakedIdentical(one.right, two.left);
    }
  }

/**
 * #### DFS
- 在DFS的基础上, 比对左左,左右,右左,右右
 */