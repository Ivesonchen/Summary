/**
 * Given the postorder and inorder traversal sequence of a binary tree, reconstruct the original tree.

Assumptions

The given sequences are not null and they have the same length
There are no duplicate keys in the binary tree
Examples

postorder traversal = {1, 4, 3, 11, 8, 5}

inorder traversal = {1, 3, 4, 5, 8, 11}

the corresponding binary tree is

        5

      /    \

    3        8

  /   \        \

1      4        11

How is the binary tree represented?

We use the pre order traversal sequence with a special symbol "#" denoting the null node.

For Example:

The sequence [1, 2, #, 3, 4, #, #, #] represents the following binary tree:

    1

  /   \

 2     3

      /

    4
 */

public class Solution {
    public TreeNode reconstruct(int[] inOrder, int[] postOrder) {
      // Write your solution here
      return helper(postOrder.length - 1, 0, inOrder.length - 1, inOrder, postOrder);
    }
  
    public TreeNode helper(int postStart, int inStart, int inEnd, int[] inOrder, int[] postOrder){
      if(postStart < 0 || inStart > inEnd) return null;
  
      TreeNode root = new TreeNode(postOrder[postStart]);
      int inIndex = 0;
      for(int i = inStart; i <= inEnd; i ++){
        if(inOrder[i] == postOrder[postStart]){
          inIndex = i;
        }
      }
  
      root.right = helper(postStart - 1, inIndex + 1, inEnd, inOrder, postOrder);
      root.left = helper(postStart - (inEnd - inIndex) - 1, inStart, inIndex - 1, inOrder, postOrder);
  
      return root;
    }
  }