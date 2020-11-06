/**
 * Given a BST, change each node’s value, such that its value is equal to the sum of all nodes greater than itself.

Examples:

     11
    /  \
  2     29
 /  \  /  \
1   7 15   40
          /
         35

is transformed to

     119
    /   \
  137    75
 /  \    /  \
139 130 104  0
            /
          40

 similar         https://leetcode.com/problems/binary-search-tree-to-greater-sum-tree/
 */

 // 体会一下   传一个 count 值 从右边开始 不停地穿过所有的node  累积所有的和
public class Solution {
    public TreeNode greaterSum(TreeNode root) {
      // Write your solution here
      helper(root, 0);
  
      return root;
    }
  
    public int helper(TreeNode cur, int count){
      if(cur == null) return count;
  
      int right = helper(cur.right, count);
  
      count = cur.key + right;
      cur.key = right;
  
      return helper(cur.left, count);
    }
  }