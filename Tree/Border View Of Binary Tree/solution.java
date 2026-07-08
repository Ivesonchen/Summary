/**
 * Given a binary tree, return its border view. The border view is defined as follow: 
 * first get all the border nodes at left side(from root and always go to the left subtree unless the left subtree does not exist until reach a leaf node), 
 * then get all the leaf nodes(from left to right), at last get all the border nodes at right side(similar to left border but from bottom to top), 
 * the list of border view should not contain duplicate nodes. 
 * Note that for the given root, if it has no left sub-tree or right sub-tree, it is considered the left border/right border, 
 * but this rule applies to only the input tree not any sub-tree of it.

Example 1:

           1
        /    \
       2      3
      / \    /  \
     4   5   6  7
      \          \
       9           8
          \
            11

the border view = [1, 2, 4, 9, 11, 5, 8, 7, 3]

1, 2, 4, 9, 11 are the left border, 11, 5, 8, 7 are the leaf nodes, 7, 3, 1 are the right border.

Example 2:

           1
            \
              3
             /  \
           4   5
            \
            6

the border view = [1 6 5 3]

In this case, the left border contains only one node [1], because the root doesn't have a left child.

Example 3:

                  1
                /   \ 
              2      3   
               \        \ 
                4       5     
                /
               6

the border view = [1, 2, 4, 6, 5, 3]

In this case, the left border contains [1, 2, 4, 6].
 */

// muti DFS
public class Solution {
    List<Integer> res = new ArrayList<>();
  
    public List<Integer> borderView(TreeNode root) {
      // Write your solution here
      if(root == null) return res;
      res.add(root.key);
      printBoundaryLeft(root.left);
      printLeaves(root.left);
      printLeaves(root.right);
      printBoundaryRight(root.right);
      return res;
    }
  
    public void printBoundaryLeft(TreeNode cur){
      if(cur == null) return;
      if(cur.left != null){
        // to ensure top down order, print the node 
        // before calling itself for left subtree 
        res.add(cur.key);
        printBoundaryLeft(cur.left);
      } else if(cur.right != null){
        res.add(cur.key);
        printBoundaryLeft(cur.right);
      }
      // do nothing if it is a leaf node, this way we avoid 
      // duplicates in output 
    }
  
    public void printBoundaryRight(TreeNode cur){
      if(cur == null) return;
      if(cur.right != null){
              // to ensure bottom up order, first call for right 
              // subtree, then print this node 
        printBoundaryRight(cur.right);
        res.add(cur.key);
      } else if(cur.left != null){
        printBoundaryRight(cur.left);
        res.add(cur.key);
      }
      // do nothing if it is a leaf node, this way we avoid 
      // duplicates in output 
    }
  
    public void printLeaves(TreeNode cur){
      if(cur == null) return;
      
      printLeaves(cur.left);
      if(cur.left == null && cur.right == null) res.add(cur.key);
      printLeaves(cur.right);
    }
  }