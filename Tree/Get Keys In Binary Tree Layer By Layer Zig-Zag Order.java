/**
 * Get the list of keys in a given binary tree layer by layer in zig-zag order.

Examples

        5

      /    \

    3        8

  /   \        \

 1     4        11

the result is [5, 3, 8, 11, 4, 1]

Corner Cases

What if the binary tree is null? Return an empty list in this case.
How is the binary tree represented?

We use the level order traversal sequence with a special symbol "#" denoting the null node.

For Example:

The sequence [1, 2, 3, #, #, 4] represents the following binary tree:

    1

  /   \

 2     3

      /

    4
 */


 
// 有点蠢   逻辑很难捋清楚
public class Solution {
    public List<Integer> zigZag(TreeNode root) {
      // Write your solution here
      List<Integer> res = new ArrayList<>();
  
      if(root == null) return res;
  
      Deque<TreeNode> dq = new LinkedList<>();
      dq.offerLast(root);
  
      boolean direction = false;  // false means left, true means right
  
      while(!dq.isEmpty()){
        List<Integer> temp = new ArrayList<>();
        int size = dq.size();
        for(int i = 0; i < size; i ++){
          TreeNode cur = new TreeNode(-1);
          if(direction) cur = dq.pollLast();
          else cur = dq.pollFirst();
  
          temp.add(cur.key);
          if(direction){
            if(cur.left != null) dq.offerFirst(cur.left);
            if(cur.right != null) dq.offerFirst(cur.right);
          } else {
            if(cur.right != null) dq.offerLast(cur.right);
            if(cur.left != null) dq.offerLast(cur.left); 
          }
        }
        direction = direction ? false : true;
        res.addAll(temp);
      }
      return res;
    }
  }