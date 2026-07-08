/**
 * Given a binary search tree, write a function kthSmallest to find the kth smallest element in it.

Note: 
You may assume k is always valid, 1 <=k <= BST's total elements.

Follow up:
What if the BST is modified (insert/delete operations) often and you need to find the kth smallest frequently? 
How would you optimize the kthSmallest routine?

https://leetcode.com/problems/kth-smallest-element-in-a-bst/solution/
 */
public class Solution {
    public int kthSmallest(TreeNode root, int k) {
      // Write your solution here
      
      Stack<TreeNode> stack = new Stack<>();
  
      while(true){
        while(root != null){
          stack.add(root);
          root = root.left;
        }
        root = stack.pop();
        if(--k == 0) return root.key;
  
        root = root.right;
      }
    }
  }