/**
 * Given a Binary Search Tree with only two nodes swapped. Try to find them and recover the binary search tree.

Input: 

                4
              /   \
             2      6  
            / \    / \
           1   5  3   7

Output:            4
                  /  \
                 2    6
               /  \   / \
               1  3   5  7
https://leetcode.com/problems/recover-binary-search-tree/
 */

public class Solution {
    public TreeNode recover(TreeNode root) {
      // Write your solution here
      Stack<TreeNode> stack = new Stack<>();
      TreeNode first = null;
      TreeNode second = null;
  
      TreeNode cur = root;
  
      TreeNode pre = null;
  
      while(!stack.isEmpty() || cur != null){
        while(cur != null){
          stack.push(cur);
          cur = cur.left;
        }
  
        cur = stack.pop();
  
        if(pre != null && cur.key < pre.key){
          second = cur;
  
          if(first == null) first = pre;
        }
        pre = cur;
        cur = cur.right;
      }
  
      int temp = first.key;
      first.key = second.key;
      second.key = temp;
  
      return root;
    }
  }