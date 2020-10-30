/**
 * Delete the target key K in the given binary search tree if the binary search tree contains K. Return the root of the binary search tree.

Find your own way to delete the node from the binary search tree, after deletion the binary search tree's property should be maintained.

Assumptions

There are no duplicate keys in the binary search tree

The smallest larger node is first candidate after deletion
 */
public class Solution {
    public TreeNode deleteTree(TreeNode root, int key) {
      if (root == null) {
        return null;
      }
      
      if (root.key < key) {
        root.right = deleteTree(root.right, key);
      }
      else if (root.key > key) {
        root.left = deleteTree(root.left, key);
      }
      else {
        if (root.left == null && root.right == null) {
          return null;
        }
        else if (root.left == null || root.right == null) {
          return root.left == null ? root.right : root.left;
        }
        else {
          TreeNode smallest = findSmallest(root.right);
          root.key = smallest.key;
          root.right = deleteTree(root.right, smallest.key);
        }
      }
      return root;
    }
    
    private TreeNode findSmallest(TreeNode root) {
      while (root.left != null) {
        root = root.left;
      }
      return root;
    }
  }


  public TreeNode deleteNode(TreeNode root, int key) {
    if (root == null) return null;
    
    if (root.val > key) {
        root.left = deleteNode(root.left, key);
    } else if (root.val < key) {
        root.right = deleteNode(root.right, key);
    } else {
        if (root.left == null) return root.right;
        if (root.right == null) return root.left;
        
        TreeNode rightSmallest = root.right;
        while (rightSmallest.left != null) rightSmallest = rightSmallest.left;
        rightSmallest.left = root.left;
        return root.right;
    }
    return root;
}