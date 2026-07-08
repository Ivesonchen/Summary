/**
 * Given a binary tree, the path cost from the root node to each leaf node is defined to be the number of levels that the leaf is on.

For example, in the following binary tree, the node 5 has its path cost to be 3, and node 8 has its path cost to be 4.

                          1

                      /       \

                   2             3

                /     \             \

             4          5             6

           /   \                      /

         7      8                  9

Given a binary tree, try to delete all the nodes that have no paths whose cost is >= k that go through it. 
In the above example, node 5 will be deleted  for k = 4.
 */

 // 这个解法 是按照 排除 所有 长度小于k的分支
public class Solution {
    public TreeNode trimTree(TreeNode root, int k) {
      // Write your solution here
      if(root == null) return null;
  
      root.left = trimTree(root.left, k - 1);
      root.right = trimTree(root.right, k - 1);
  
      if(root.left == null && root.right == null && k > 1) return null;
  
      return root;
    }
}

// 这个解法 是按照排除 所有 长度大于k的分支
public class Solution {
    public TreeNode trimTree(TreeNode root, int k) {
      // Write your solution here
      if(root == null || k <= 0) return null;
  
      root.left = trimTree(root.left, k - 1);
      root.right = trimTree(root.right, k - 1);
  
      return root;
    }
}