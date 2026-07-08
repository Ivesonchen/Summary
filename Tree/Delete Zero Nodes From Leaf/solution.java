/**
 * Given a binary tree, delete the nodes only if it is 0 and all the nodes on the paths from the node to any leaf nodes are all 0.

In another word, delete the leaf nodes with 0 recursively until there are no such nodes in the tree.

You only need to return the final tree after deletion.

Examples:

          0
        /    \
       0      3
      / \    / \
     0   0   0  7
    /            \
    0             0

     \

      0

After first round, deleting all the leaf nodes with 0, the tree becomes:

          0
        /   \
       0     3
      /     / \
     0     0   7
    /
   0

After second round, deleting all the leaf nodes with 0, the tree becomes:

          0
        /   \
       0     3
      /       \
     0         7

After third round, deleting all the leaf nodes with 0, the tree becomds:

          0
        /   \
       0     3
              \
               7

After another round, deleting all the leaf nodes with 0, the tree becomds:

          0
           \
            3
             \
              7   

The deletion end at this step since there are no more nodes to delete.

You only need to return the final binary tree after deletion.
 */

public class Solution {
    public TreeNode deleteZero(TreeNode root) {
      // Write your solution here
      if(root == null) return null;
  
      root.left = deleteZero(root.left);
      root.right = deleteZero(root.right);
      
      if(root.left == null && root.right == null && root.key == 0) {
        return null;
      }
      return root;
    }
  }
// 把 0 可以换成任意 target 然后进行 递归删除
  public TreeNode removeLeafNodes(TreeNode root, int target) {
    if (root.left != null) root.left = removeLeafNodes(root.left, target);
    if (root.right != null) root.right = removeLeafNodes(root.right, target);
    return root.left == root.right && root.val == target ? null : root;
}