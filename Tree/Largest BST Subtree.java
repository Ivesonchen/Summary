/**
 * Given a binary tree, find the largest subtree which is a Binary Search Tree (BST), where largest means subtree with largest number of nodes in it.

Note:
A subtree must include all of its descendants.
Here's an example:

    10
    / \
   5  15
  / \   \ 
 1   8   7
The Largest BST Subtree in this case is the highlighted one. 
The return value is the subtree's size, which is 3. 

Follow up:
Can you figure out ways to solve it with O(n) time complexity?
 */

 /**
  * int[] {
      min : minValue of current subTree
      max : maxValue of current subTree
      sum : size if current subTree
  } 
  */

public class Solution {
    int gmax = 0;
    public int largestBSTSubtree(TreeNode root) {
      // Write your solution here
      if(root == null) return 0;
  
      helper(root);
      return gmax;
    }
  
    public int[] helper(TreeNode root){
      if(root == null) return new int[]{Integer.MIN_VALUE, Integer.MAX_VALUE, 0};
  
      int[] left = helper(root.left);
      int[] right = helper(root.right);
  
      int sum = 0;
      int min = root.key;
      int max = root.key;
  
      if(root.left != null){
        if(root.key > left[1]){
          sum += left[2];
          min = left[0];
        } 
        else return new int[]{Integer.MIN_VALUE, Integer.MAX_VALUE, 0};
      }  // 更新 min 和 max 边界值     计算sum数
  
      if(root.right != null){
        if(root.key < right[0]) {
          sum += right[2];
          max = right[1];
        }
        else return new int[]{Integer.MIN_VALUE, Integer.MAX_VALUE, 0};
      } // 更新 min 和 max 边界值     计算sum数
  
      gmax = Math.max(gmax, sum + 1);
  
      return new int[]{min, max, sum + 1};
    }
  }