/*
Given a binary tree, check whether it is a mirror of itself (i.e., symmetric around its center).
Example
    1
   / \
  2   2
 / \ / \
3  4 4  3
is a symmetric binary tree.
    1
   / \
  2   2
   \   \
   3    3
is not a symmetric binary tree.
Challenge
Can you solve it both recursively and iteratively?
Tags Expand 
Binary Tree
*/
public class Solution {
    public boolean isSymmetric(TreeNode root) {
      // Write your solution here
  
      if(root == null) return true;
  
      return helper(root.left, root.right);
    }
  
    public boolean helper(TreeNode left, TreeNode right){
      if(left == null && right == null) return true;
      if(left == null || right == null) return false;
  
      return left.key == right.key && helper(left.left, right.right) && helper(left.right, right.left);
    }
  }
  

/**
 * 检查tree是否symmetric
 *
  Thoughts:
  verify that left and right tree are identical
  A.val == B.val
  check(A.left, B.left)
  check(A.right, B.right)

注意Symmetric Binary Tree的例子和定义: 是镜面一样的对称. 并不是说左右两个sub-tree相等。
 */

 //Non-recursive, iterative
/*
  Thoughts:
  Use 2 stack to hold the child that's needed to compare.
  Have to use stack, otherwise, can't iterate through root node.
*/
public class Solution {
    public boolean isSymmetric(TreeNode root) {
      if (root == null) {
        return true;
      }
  
      Stack<TreeNode> s1 = new Stack<TreeNode>();
      Stack<TreeNode> s2 = new Stack<TreeNode>();
      s1.push(root.left);
      s2.push(root.right);
      while (!s1.isEmpty() && !s2.isEmpty()) {
        TreeNode node1 = s1.pop();
        TreeNode node2 = s2.pop();
        if (node1 == null && node2 == null) {
          continue;
        } else if (node1 == null || node2 == null) {
          return false;
        } else if (node1.val != node2.val) {
          return false;
        }
        s1.push(node1.left);
        s2.push(node2.right);
        s1.push(node1.right);
        s2.push(node2.left);  
      }
  
      return true;
    }
}