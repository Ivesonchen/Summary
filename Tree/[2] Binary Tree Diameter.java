/**
 * Given a binary tree in which each node contains an integer number. The diameter is defined as the longest distance from 
 * 
 * one leaf node to another leaf node. The distance is the number of nodes on the path.

If there does not exist any such paths, return 0.

Examples

    5

  /    \

2      11

     /    \

    6     14

The diameter of this tree is 4 (2 → 5 → 11 → 14)


 */
// leaf node  到 leaf node
public class Solution {
    public int diameter(TreeNode root) {
      // Write your solution here
      int[] res = new int[1];
  
      if(root == null) return 0;
  
      helper(root, res);
  
      return res[0];
    }
  
    public int helper(TreeNode cur, int[] res) {
      if(cur == null) return 0;
  
      int left = helper(cur.left, res);
      int right = helper(cur.right, res);
  
      if(left != 0 && right != 0) {                 // 一定要排除cur是叶子节点的情况
        res[0] = Math.max(res[0], left + right + 1);
      }
      return Math.max(left, right)  + 1;
    }
}

  /**
   * res = leftSubTreeDepth + rightSubTreeDepth     through root node
   */