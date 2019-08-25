/**
 * Check if two given binary trees are identical. Identical means the equal valued keys are at the same position in the two binary trees.

Examples

 

        5

      /    \

    3        8

and

        5

      /    \

    3        8

are identical trees.
 */

public class Solution {
    public boolean isIdentical(TreeNode one, TreeNode two) {
      // Write your solution here
  
      if(one == null && two == null) return true;
      if(one == null || two == null) return false;
  
      return one.key == two.key && isIdentical(one.left, two.left) && isIdentical(one.right, two.right); 
    }
  }

  /**
   * check if they are exactly same
   * means same value on same position
   */