/**
 * Given the levelorder traversal sequence of a binary search tree, reconstruct the original tree.

Assumptions

The given sequence is not null
There are no duplicate keys in the binary search tree
Examples

levelorder traversal = {5, 3, 8, 1, 4, 11}

the corresponding binary search tree is

        5

      /    \

    3        8

  /   \        \

1      4        11
 */

 // DFS 
public class Solution {
    public TreeNode reconstruct(int[] level) {
      // Write your solution here
      Queue<Integer> queue = new LinkedList<>();
  
      for(int i = 0; i < level.length; i++){
        queue.offer(level[i]);
      }
  
      return helper(queue);
    }
  
    public TreeNode helper(Queue<Integer> queue){
      if(queue.isEmpty()) return null;
  
      Queue<Integer> left = new LinkedList<>();
      Queue<Integer> right = new LinkedList<>();
  
      int val = queue.poll();
  
      for(Integer ele : queue){
        if(ele < val){
          left.offer(ele);
        } else {
          right.offer(ele);
        }
      }
  
      TreeNode root = new TreeNode(val);
  
      root.left = helper(left);
      root.right = helper(right);
  
      return root;
    }
  }