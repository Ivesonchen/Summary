/**
 * Given a binary tree, flatten it to a linked list in-place.

For example,
Given

         1
        / \
       2   5
      / \   \
     3   4   6
The flattened tree should look like:

   1
    \
     2
      \
       3
        \
         4
          \
           5
            \
             6
 */

 // Post order concept
public class Solution {
    public TreeNode flatten(TreeNode root) {
      // Write your solution here
      if(root == null) return root;
  
      TreeNode newRoot = new TreeNode(-1);
      helper(root, newRoot);
  
      return newRoot.right;
    }
  
    public TreeNode helper(TreeNode cur, TreeNode newRoot){
      if(cur == null) return newRoot;
  
      newRoot.right = new TreeNode(cur.key);
      newRoot = newRoot.right;
      TreeNode temp = helper(cur.left, newRoot);
      return helper(cur.right, temp);
    }
  
}
    public TreeNode flatten(TreeNode root){
        helper(root, null);
        return root;
    }
    
    public TreeNode helper(TreeNode root, TreeNode pre){
        if(root == null) return pre;
    
        pre = helper(root.right, pre);
        pre = helper(root.left, pre);
        root.right = pre;
        root.left = null;
        pre = root;
        return pre;
    }

    /**
     *     1
   / \
  2   5
 / \   \
3   4   6
-----------        
pre = 5
cur = 4

    1
   / \
  2   \
 / \  |
3   4 |
     \|
      5
       \
        6
-----------        
pre = 4
cur = 3

    1
   / \
  2  |
 /|  | 
3 |  |
 \|  |
  4  |
   \ |
    5
     \
      6
-----------        
cur = 2
pre = 3

    1
   / \
  2   \
   \   \
    3   \
     \  |
      4 |
       \|
        5
         \
          6
-----------        
cur = 1
pre = 2

1
 \
  2
   \
    3
     \
      4
       \
        5
         \
          6
     */