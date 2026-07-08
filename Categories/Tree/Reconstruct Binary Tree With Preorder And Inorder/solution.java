/**
 * Given the preorder and inorder traversal sequence of a binary tree, reconstruct the original tree.

Assumptions
The given sequences are not null and they have the same length
There are no duplicate keys in the binary tree
Examples
preorder traversal = {5, 3, 1, 4, 8, 11}
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
    public TreeNode reconstruct(int[] inOrder, int[] preOrder) {
      // Write your solution here
      return helper(0, 0, inOrder.length - 1, preOrder, inOrder);
    }
  
    public TreeNode helper(int preStart, int inStart, int inEnd, int[] preorder, int[] inorder){
      if(preStart > preorder.length - 1 || inStart > inEnd) return null;
  
      TreeNode root = new TreeNode(preorder[preStart]);
      int inIndex = 0;
      // 在inorder 序列中找 新的root对应的位置在哪里
      for(int i = inStart; i <= inEnd; i++){
        if(inorder[i] == preorder[preStart]){
          inIndex = i;
        }
      }
      root.left = helper(preStart + 1, inStart, inIndex - 1, preorder, inorder);
      root.right = helper(preStart + inIndex - inStart + 1, inIndex + 1, inEnd, preorder, inorder);
                  //新的preStart是在之前基础上加上inOrder左边部分的长度
      return root;
    }
  }