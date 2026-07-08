/**
 * Insert a key in a binary search tree if the binary search tree does not already contain the key. 
 * Return the root of the binary search tree.
Assumptions
There are no duplicate keys in the binary search tree
If the key is already existed in the binary search tree, you do not need to do anything
Examples

        5
      /    \
    3        8
  /   \
 1     4

insert 11, the tree becomes

        5
      /    \
    3        8
  /   \        \
 1     4       11

insert 6, the tree becomes

        5
      /    \
    3        8
  /   \     /  \
 1     4   6    11
 */
public class Solution {
    public TreeNode insert(TreeNode root, int key) {
      // Write your solution here
      if(root == null){
        return new TreeNode(key);
      }
  
      if(key > root.key) root.right = insert(root.right, key);
      else if(key < root.key) root.left = insert(root.left, key);
  
      return root;
    }
  }

class Solution {
    public TreeNode insertIntoBST(TreeNode root, int val) {
      TreeNode node = root;
      while (node != null) {
        // insert into the right subtree
        if (val > node.val) {
          // insert right now
          if (node.right == null) {
            node.right = new TreeNode(val);
            return root;
          }
          else node = node.right;
        }
        // insert into the left subtree
        else {
          // insert right now
          if (node.left == null) {
            node.left = new TreeNode(val);
            return root;
          }
          else node = node.left;
        }
      }
      return new TreeNode(val);
    }
  }