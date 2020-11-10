public class Solution {
    public List<Integer> preOrder(TreeNode root) {
      // Write your solution here
      List<Integer> res = new ArrayList<>();
      Stack<TreeNode> stack = new Stack<>();
      if(root == null) return res;
  
      stack.add(root);
  
      while(!stack.isEmpty()) {
        TreeNode cur = stack.pop();
        res.add(cur.key);
  
        if(cur.right != null) {
          stack.push(cur.right);
        }
  
        if(cur.left != null) {
          stack.push(cur.left);
        }
      }
      return res;
    }
  }

  /**
   * #### Recursive
- 加root, left, then right. Obvious
- Divide and conquer
- 其实也不需要helper function

#### Iterative
- 先加root, 然后push上需要末尾process的在stack垫底(root.right), 然后push root.left
- Stack: push curr, push right, push left.   
- 不能够先子节点层 后父节点层 ！！！！ 
   */

  class Solution {
    public List<Integer> preorderTraversal(TreeNode root) {
        List<Integer> rst = new ArrayList<>();
        if (root == null) {
            return rst;
        }
        rst.add(root.val);
        rst.addAll(preorderTraversal(root.left));
        rst.addAll(preorderTraversal(root.right));
        return rst;
    }
}