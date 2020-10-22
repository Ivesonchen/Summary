/**
 * Given K nodes in a binary tree, find their lowest common ancestor.

Assumptions

K >= 2

There is no parent pointer for the nodes in the binary tree

The given K nodes are guaranteed to be in the binary tree

Examples

        5

      /   \

     9     12

   /  \      \

  2    3      14

The lowest common ancestor of 2, 3, 14 is 5

The lowest common ancestor of 2, 3, 9 is 9
 */
//O(Log(nk))

public class Solution {
    public TreeNode lowestCommonAncestor(TreeNode root, List<TreeNode> nodes) {
      // Write your solution here.
      if(root == null) return root;
  
      for(TreeNode cur : nodes){
        if(root.key == cur.key) return root;
      }
  
      TreeNode left = lowestCommonAncestor(root.left, nodes);
      TreeNode right = lowestCommonAncestor(root.right, nodes);
  
      if(left != null && right != null) return root;
  
      return left == null ? right : left;
    }
  }