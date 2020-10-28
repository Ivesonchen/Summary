/**
 * Check if a given binary tree is completed. A complete binary tree is one in which every level of the binary tree is completely filled except possibly the last level. Furthermore, all nodes are as far left as possible.

Examples

        5

      /    \

    3        8

  /   \

1      4

is completed.

        5

      /    \

    3        8

  /   \        \

1      4        11

is not completed.

Corner Cases

What if the binary tree is null? Return true in this case.
https://leetcode.com/problems/check-completeness-of-a-binary-tree/
 */
// Idea is if we do a level order traversal and we see a non emptyNode followed by an empty node, it isn't a complete binary tree.
public class Solution {
    public boolean isCompleted(TreeNode root) {
      // Write your solution here
      if(root == null) return true;
  
      Queue<TreeNode> queue = new LinkedList<>();
      queue.offer(root);
  
      boolean seenEmpty = false;
      while(!queue.isEmpty()){
  
        TreeNode cur = queue.poll();
  
        if(cur == null) seenEmpty = true;
        else if(seenEmpty){
          return false;
        } else {
          queue.offer(cur.left);
          queue.offer(cur.right);
        }
      }
      return true;
    }
}