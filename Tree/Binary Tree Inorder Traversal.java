public class Solution {
    public List<Integer> inOrder(TreeNode root) {
      // Write your solution here
      List<Integer> res = new ArrayList<>();
      Stack<TreeNode> stack = new Stack<>();
  
      if(root == null) return res;
  
      TreeNode cur = root;
  
      while(cur != null || !stack.isEmpty()) {
        while(cur != null) {
          stack.push(cur);
          cur = cur.left;
        }
  
        cur = stack.pop();
        res.add(cur.key);
        cur = cur.right; //很重要
      }
      return res;
    }
  }

  /**
   * #### Iterative: Stack
- Add left nodes all the way   
- Print curr   
- Move to right, add right if possible
- O(n) time, O(h) space
  
注意stack.pop()在加完left-most child 的后，一定要curr = curr.right.

若不右移, 很可能发生窘境:
curr下一轮还是去找自己的left-most child，不断重复curr and curr.left, 会infinite loop, 永远在左边上下上下。
   */