/**
 * Given a binary tree, return the bottom-up level order traversal of its nodes' values, from left to right. Only need to return lowest level 

Example:

    Given the below binary tree

            5
          /   \
        3       8
      /   \       \
    1      4       11

    return its bottom-up level order traversal as:

      [1, 4, 11],
 */

public class Solution {
    public List<Integer> levelOrderBottom(TreeNode root) {
      // Write your solution here
      List<Integer> res = new ArrayList<>();
      if(root == null) return res;
  
      int height = getHeight(root);
  
      Queue<TreeNode> queue = new LinkedList<>();
      queue.offer(root);
  
      int level = 0;
      while(!queue.isEmpty()){
        int size = queue.size();
        List<Integer> temp = new ArrayList<>();
        level ++;
        for(int i = 0; i < size; i++){
          TreeNode cur = queue.poll();
          temp.add(cur.key);
          if(cur.left != null) queue.offer(cur.left);
          if(cur.right != null) queue.offer(cur.right);
        }
        if(level == height) res = temp;
      }
      return res;
    }
  
    public int getHeight(TreeNode cur){
      if(cur == null) return 0;
  
      return Math.max(getHeight(cur.left), getHeight(cur.right)) + 1;
    }
  }