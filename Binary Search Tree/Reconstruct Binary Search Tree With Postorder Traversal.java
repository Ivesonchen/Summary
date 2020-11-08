/**
 * Given the postorder traversal sequence of a binary search tree, reconstruct the original tree.

Assumptions

The given sequence is not null
There are no duplicate keys in the binary search tree
Examples

postorder traversal = {1, 4, 3, 11, 8, 5}

the corresponding binary search tree is

        5

      /    \

    3        8

  /   \        \

1      4        11
 */

// Tao-Lu
public class Solution {
    int index = -1;
    public TreeNode reconstruct(int[] post) {
      // Write your solution here
      this.index = post.length - 1;
  
      return helper(post, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }
  
    public TreeNode helper(int[] post, int lower, int upper){
      if(index < 0 ) return null;
      int val = post[index];
  
      if(val < lower || val > upper) return null;
  
  
      index --;
      TreeNode root = new TreeNode(val);
  
      root.right = helper(post, val, upper);
      root.left = helper(post, lower, val);
  
      return root;
    }
}