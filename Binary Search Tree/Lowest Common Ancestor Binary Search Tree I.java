/**
 * Given two keys in a binary search tree, find their lowest common ancestor.

Assumptions

There is no parent pointer for the nodes in the binary search tree

There are no duplicate keys in the binary search tree

The given two nodes are guaranteed to be in the binary search tree

Examples

        5

      /   \

     2     12

   /  \      \

  1    3      14

The lowest common ancestor of 1 and 14 is 5

The lowest common ancestor of 1 and 3 is 2
https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/solution/
 */

 // recursive
public class Solution {
    //根据 bst 的 特点
    public TreeNode lca(TreeNode root, int p, int q) {
      if(root == null) return null;
  
      if(root.key < p && root.key < q){
        return lca(root.right, p, q);
      } else if(root.key > p && root.key > q){
        return lca(root.left, p, q);
      } else {
        return root;
      }
    }
}

//itrative
class Solution {
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {

        // Value of p
        int pVal = p.val;

        // Value of q;
        int qVal = q.val;

        // Start from the root node of the tree
        TreeNode node = root;

        // Traverse the tree
        while (node != null) {

            // Value of ancestor/parent node.
            int parentVal = node.val;

            if (pVal > parentVal && qVal > parentVal) {
                // If both p and q are greater than parent
                node = node.right;
            } else if (pVal < parentVal && qVal < parentVal) {
                // If both p and q are lesser than parent
                node = node.left;
            } else {
                // We have found the split point, i.e. the LCA node.
                return node;
            }
        }
        return null;
    }
}


// 传统LCA的做法 no need
public TreeNode lca(TreeNode root, int p, int q) {
    // Write your solution here
    if(root == null) return null;

    if(root.key == p || root.key == q) return root;

    TreeNode left = lca(root.left, p, q);
    TreeNode right = lca(root.right, p, q);

    if(left != null && right != null) return root;

    return left == null ? right : left;
  }