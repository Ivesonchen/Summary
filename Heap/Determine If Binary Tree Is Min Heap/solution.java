/**
 * Determine if the given binary tree is min heap.
 */

 // complete tree + root.value > left.value & right.value



 public class Solution {
    public boolean isMinHeap(TreeNode root) {
      // Write your solution here
      if(root == null) return true;
  
      Queue<TreeNode> queue = new LinkedList<>();
  
      queue.offer(root);
      boolean seenEmpty = false;
  
      while(!queue.isEmpty()){
        int size = queue.size();
        for(int i = 0; i < size; i++){
          TreeNode cur = queue.poll();
          if(cur == null) seenEmpty = true;
          else if(seenEmpty){
            return false;
          } else {
            if(cur.left != null && cur.key > cur.left.key) return false;
            if(cur.right != null && cur.key > cur.right.key) return false;
            queue.offer(cur.left);
            queue.offer(cur.right);
          }
        }
      }
  
      return true;
    }
  }