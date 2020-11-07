/**
 * Given the preorder traversal sequence of a binary search tree, reconstruct the original tree.

Assumptions

The given sequence is not null
There are no duplicate keys in the binary search tree
Examples

preorder traversal = {5, 3, 1, 4, 8, 11}

The corresponding binary search tree is

        5

      /    \

    3        8

  /   \        \

1      4        11
 */

public class Solution {
    int index = 0;
  
    public TreeNode reconstruct(int[] pre) {
      // Write your solution here
      if(pre == null || pre.length == 0) return null;
      return helper(pre, Integer.MAX_VALUE);
    }
  
    public TreeNode helper(int[] preorder, int upper){
       if(index >= preorder.length || preorder[index] > upper) return null;
  
       TreeNode root = new TreeNode(preorder[index]);
       index++;
  
       root.left = helper(preorder, root.key);
       root.right = helper(preorder, upper);
  
       return root;
    }
  }