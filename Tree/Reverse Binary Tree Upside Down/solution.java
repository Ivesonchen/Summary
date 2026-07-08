/**
 * Given a binary tree where all the right nodes are leaf nodes, flip it upside down and turn it into a tree with left leaf nodes as the root.

Examples

        1

      /    \

    2        5

  /   \

3      4

is reversed to

        3

      /    \

    2        4

  /   \

1      5
 */

public class Solution {
    public TreeNode reverse(TreeNode root) {
      // Write your solution here
      if(root == null || root.left == null) return root;
  
      TreeNode newRoot = reverse(root.left);
  
      root.left.left = root;
      root.left.right = root.right;
      root.left = null;
      root.right = null;
  
      return newRoot;
    }
}

public TreeNode upsideDownBinaryTree(TreeNode root) {
    TreeNode curr = root;
    TreeNode next = null;
    TreeNode temp = null;
    TreeNode prev = null;
    
    while(curr != null) {
        next = curr.left;
        
        // swapping nodes now, need temp to keep the previous right child
        curr.left = temp;
        temp = curr.right;
        curr.right = prev;
        
        prev = curr;
        curr = next;
    }
    return prev;
}  

/**
        1                             1

      /    \                   (new)/    \(delete)

    2        5      ---->        2    ->   5

  /   \                   (new)/    \ (delete) 

3      4                     3  ->   4
*/