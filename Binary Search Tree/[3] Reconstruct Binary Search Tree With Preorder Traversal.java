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

  class Solution {
    int idx = 0;
    int[] preorder;
    int n;
  
    public TreeNode helper(int lower, int upper) {
      // if all elements from preorder are used
      // then the tree is constructed
      if (idx == n) return null;
  
      int val = preorder[idx];
      // if the current element 
      // couldn't be placed here to meet BST requirements
      if (val < lower || val > upper) return null;
  
      // place the current element
      // and recursively construct subtrees
      idx++;
      TreeNode root = new TreeNode(val);
      root.left = helper(lower, val);
      root.right = helper(val, upper);
      return root;
    }
  
    public TreeNode bstFromPreorder(int[] preorder) {
      this.preorder = preorder;
      n = preorder.length;
      return helper(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }
  }