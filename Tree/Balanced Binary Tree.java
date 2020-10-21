/**
 * Check if a given binary tree is balanced. A balanced binary tree is one in which the depths of every node’s left and right subtree differ by at most 1.

Examples

        5

      /    \

    3        8

  /   \        \

1      4        11

is balanced binary tree,

        5

      /

    3

  /   \

1      4

is not balanced binary tree.


Corner Cases

What if the binary tree is null? Return true in this case.
 */
public class Solution {
    public boolean isBalanced(TreeNode root) {
      // Write your solution here
      if(root == null) return true;
  
      int leftTree = getHeight(root.left);
      int rightTree = getHeight(root.right);
  
      if(Math.abs(leftTree - rightTree) > 1) {
        return false;
      }
  
      return isBalanced(root.left) && isBalanced(root.right);
    }
  
    public int getHeight(TreeNode cur) {
      if(cur == null) return 0;
  
      int left = getHeight(cur.left);
      int right = getHeight(cur.right);
  
      return Math.max(left, right) + 1;
    }
  }

 /**
  * 
  给一个binary tree, 看是否是height-balanced

  #### DFS
- DFS using depth marker: 每个depth都存一下。然后如果有不符合条件的，存为-1.
- 一旦有 <0 或者差值大于1， 就全部返回Integer.MIN_VALUE. Integer.MIN_VALUE比较极端, 确保结果的正确性。
- 最后比较返回结果是不是<0. 是<0，那 return false.
- Traverse 整个tree, O(n)
  */

  public class Solution {
    public boolean isBalanced(TreeNode root) {
      // Write your solution here
      if(root == null) return true;
      
      return getHeight(root) != -1;
    }
  
    public int getHeight(TreeNode cur) {
      if(cur == null) return 0;
  
      int left = getHeight(cur.left);
      int right = getHeight(cur.right);
  
      if(left == -1 || right == -1 || Math.abs(left - right) > 1){
        return -1;
      }
  
      return Math.max(left, right) + 1;
    }
  }