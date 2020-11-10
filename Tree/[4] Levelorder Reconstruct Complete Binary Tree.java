/**
 * How to re construct a complete binary tree from its level-order traversal sequence only.

Assumptions:

The given level-order is not null.
Examples:

{1, 2, 3} -->

   1

 /   \

2     3
 */

 // BFS 生成
public class Solution {
    public TreeNode construct(int[] level) {
      // Write your solution here
      if(level == null || level.length == 0) return null;
  
      TreeNode root = new TreeNode(level[0]);
  
      Queue<TreeNode> queue = new LinkedList<>();
      queue.offer(root);
      int counter = -1;
      while(!queue.isEmpty()){
        int size = queue.size();
  
        for(int i = 0; i < size; i++){
          counter++;
          TreeNode cur = queue.poll();
          int leftIndex = counter * 2 + 1;
          int rightIndex = counter * 2 + 2;
  
          if(leftIndex < level.length) {
            cur.left = new TreeNode(level[leftIndex]);
            queue.offer(cur.left);
          }
          
          if(rightIndex < level.length) {
            cur.right = new TreeNode(level[rightIndex]);
            queue.offer(cur.right);
          }
        }
      }
      return root;
    }
  }

  // DFS 生成
  public TreeNode construct(int[] level){
    if(level == null || level.length == 0) return null;

    return contructTree(level, 0);
  }

  public TreeNode contructTree(int[] level, int index){
    if(index >= level.length) return null;

    TreeNode cur = new TreeNode(level[index]);

    cur.left = contructTree(level, index * 2 + 1);
    cur.right = contructTree(level, index * 2 + 2);

    return cur;
  }