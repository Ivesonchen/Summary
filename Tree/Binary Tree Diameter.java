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
  
      if(left != 0 && right != 0) {
        res[0] = Math.max(res[0], left + right + 1);
      }
      return Math.max(left, right)  + 1;
    }
  }
// nodes 到 nodes
class Solution {
    public int diameterOfBinaryTree(TreeNode root) {
        int []res = new int[1];
        
        helper(root, res);
        
        return res[0];
    }
    
    // signature: return the length of longest single path in the tree rooted at 'root'
    public int helper(TreeNode root, int[] res){
        // base case
        if(root == null) return 0;
        
        int left = helper(root.left, res);
        int right= helper(root.right, res);
        
        res[0] = Math.max(res[0], left + right);
        
        return Math.max(left, right) + 1;
    }
}

  /**
   * res = leftSubTreeDepth + rightSubTreeDepth     through root node
   */