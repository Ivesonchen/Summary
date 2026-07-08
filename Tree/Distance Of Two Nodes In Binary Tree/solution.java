/**
 * Find distance between two given keys of a Binary Tree, no parent pointers are given. Distance between two nodes is the minimum number of edges to be traversed to reach one node from other.

Assumptions:

There are no duplicate keys in the binary tree.
The given two keys are guaranteed to be in the binary tree.
The given two keys are different.
Examples:

    1

   /  \

  2    3

 / \  /  \  

4   5 6   7

       \

         8

distance(4, 5) = 2
distance(4, 6) = 4
 */

 /**
  * 用LCA 得出合适的 root node
    用Recurision 计算 distance
  */

public class Solution {
    public int distance(TreeNode root, int k1, int k2) {
      // Write your solution here
      if(root == null) return 0;
  
      TreeNode lca = LCA(root, k1, k2);
      int dis1 = getDistance(lca, k1, 0);
      int dis2 = getDistance(lca, k2, 0);
  
      return dis1 + dis2;
    }
  
    //Tao-Lu 经典LCA 的 写法
    public TreeNode LCA(TreeNode cur, int k1, int k2) {
      if(cur == null || cur.key == k1 || cur.key == k2) return cur;
  
      TreeNode left = LCA(cur.left, k1, k2);
      TreeNode right = LCA(cur.right, k1, k2);
  
      if(left != null && right != null) return cur;
  
      return (left != null) ? left : right;
    }
  
    public int getDistance(TreeNode cur, int target, int counter) {
      if(cur == null) return -1;
      if(cur.key == target) return counter;
  
      int leftLen = getDistance(cur.left, target, counter + 1);
      int rightLen = getDistance(cur.right, target, counter + 1);
  
      return (leftLen == -1) ? rightLen : leftLen;                                                                                                                        
    }
  }