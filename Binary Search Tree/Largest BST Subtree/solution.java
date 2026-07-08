/**
 * Given a binary tree, find the largest subtree which is a Binary Search Tree (BST), 
 * where largest means subtree with largest number of nodes in it.

Note:
A subtree must include all of its descendants.

Example:

Input: [10,5,15,1,8,null,7]

   10 
   / \ 
  5  15 
 / \   \ 
1   8   7

Output: 3
Explanation: The Largest BST Subtree in this case is the highlighted one.
             The return value is the subtree's size, which is 3.
Follow up:
Can you figure out ways to solve it with O(n) time complexity?

Hint:
You can recursively use algorithm similar to 98. 
Validate Binary Search Tree at each node of the tree, which will result in O(nlogn) time complexity.
 */
//O()
class Solution {
    public int largestBSTSubtree(TreeNode root) {
        if(root == null) return 0;
        if(isValid(root, Integer.MIN_VALUE, Integer.MAX_VALUE)) return count(root);
        return Math.max(largestBSTSubtree(root.left), largestBSTSubtree(root.right));
    }
    
    public boolean isValid(TreeNode root, int min, int max) {
        if(root == null) return true;
        if(root.val <= min || root.val >= max) return false;
        return isValid(root.left, min, root.val) && isValid(root.right, root.val, max);
    }
    
    public int count(TreeNode root) {
        if(root == null) return 0;
        return count(root.left) + count(root.right) + 1;
    }
}

//O(n)

public class Solution {
    
  // return array for each node: 
  //     [0] --> min
  //     [1] --> max
  //     [2] --> largest BST in its subtree(inclusive)
  
  public int largestBSTSubtree(TreeNode root) {
      int[] ret = largestBST(root);
      return ret[2];
  }
  
  private int[] largestBST(TreeNode node){
      if(node == null){
          return new int[]{Integer.MAX_VALUE, Integer.MIN_VALUE, 0};
      }
      int[] left = largestBST(node.left);
      int[] right = largestBST(node.right);
      if(node.val > left[1] && node.val < right[0]){
          return new int[]{Math.min(node.val, left[0]), Math.max(node.val, right[1]), left[2] + right[2] + 1};
      }else{
          return new int[]{Integer.MIN_VALUE, Integer.MAX_VALUE, Math.max(left[2], right[2])};
      }
  }
}