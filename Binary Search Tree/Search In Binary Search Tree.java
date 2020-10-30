/**
 * Find the target key K in the given binary search tree, return the node that contains the key if K is found, otherwise return null.

Assumptions

There are no duplicate keys in the binary search tree
 */

public class Solution {
    public TreeNode search(TreeNode root, int key) {
      // Write your solution here
      if(root == null) return null;
  
      if(root.key < key){
        return search(root.right, key);
      } else if(root.key > key){
        return search(root.left, key);
      } else {
        return root;
      }
    }
}