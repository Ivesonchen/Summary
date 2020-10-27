/**
 * Given a complete binary tree, count the number of nodes.

Definition of a complete binary tree from Wikipedia:
In a complete binary tree every level, except possibly the last, is completely filled, 
and all nodes in the last level are as far left as possible. 
It can have between 1 and 2h nodes inclusive at the last level h.
 */

public class Solution {
    public int countNodes(TreeNode root) {
      // Write your solution here
      if(root == null) return 0;
      int height = getHeight(root);
      int numOfLastLevel = 0;
  
      Queue<TreeNode> queue = new LinkedList<>();
  
      queue.offer(root);
  
      int level = 0;
      while(!queue.isEmpty()){
        int size = queue.size();
        level++;
        if(level == height) numOfLastLevel = size;
        for(int i = 0; i < size; i++){
          TreeNode cur = queue.poll();
  
          if(cur.left != null) queue.offer(cur.left);
          if(cur.right != null) queue.offer(cur.right);
        }
      }
  
      return (int)Math.pow(2, height - 1) - 1 + numOfLastLevel;
    }
  
    public int getHeight(TreeNode cur) {
      if(cur == null) return 0;
  
      return Math.max(getHeight(cur.left), getHeight(cur.right)) + 1;
    }
  }

  //O(log(n)^2)
 class Solution {
      public int countNodes(TreeNode root) {
    
        int leftDepth = leftDepth(root);
        int rightDepth = rightDepth(root);
    
        if (leftDepth == rightDepth)
            return (1 << leftDepth) - 1;
        else
            return 1+countNodes(root.left) + countNodes(root.right);
    
    }
    
    private int rightDepth(TreeNode root) {
        // TODO Auto-generated method stub
        int dep = 0;
        while (root != null) {
            root = root.right;
            dep++;
        }
        return dep;
    }
    
    private int leftDepth(TreeNode root) {
        // TODO Auto-generated method stub
        int dep = 0;
        while (root != null) {
            root = root.left;
            dep++;
        }
        return dep;
    }
  }