/**
 * Given a binary tree in which each node contains an integer number. 
 * Find the maximum possible sum from one leaf node to another leaf node. 
 * If there is no such path available, return Integer.MIN_VALUE(Java)/INT_MIN (C++).
Examples

  -15

  /    \

2      11

     /    \

    6     14
The maximum path sum is 6 + 11 + 14 = 31.
How is the binary tree represented?
We use the level order traversal sequence with a special symbol "#" denoting the null node.
For Example:
The sequence [1, 2, 3, #, #, 4] represents the following binary tree:
    1

  /   \

 2     3

      /

    4
 */

public class Solution {
    public int maxPathSum(TreeNode root) {
      // Write your solution here
      if(root == null) return 0;
      int[] res = new int[1];
      res[0] = Integer.MIN_VALUE;
      helper(root, res);
  
      return res[0];
    }
  
    public int helper(TreeNode cur, int[] counter){                 // 注意 counter 地址变量 的正确使用
        if(cur == null) return 0;
  
        int left = helper(cur.left, counter);                       // counter
        int right = helper(cur.right, counter);
  
        if(cur.left != null && cur.right != null){
          int sum = left + right + cur.key;
          counter[0] = Math.max(counter[0], sum);                   // counter
          return Math.max(left, right) + cur.key;                   // 要注意比较left right， 取较大者
        }
  
      return cur.left == null ? right + cur.key : left + cur.key;   // 注意这里 不能以 0 计算 缺失的分支
    }
}