/**
 *        root
 *      左    右
 * 
 * post order:    左 右 root
 */

// 树结构 从上往下走 注定了 先处理root那一层的数值   所以要用 addFirst
// 然后处理 右部分  到底    要使用stack     
// stack 处理     先入左部分   后入右部分
public class Solution {
    public List<Integer> postOrder(TreeNode root) {
      // Write your solution here
  
      List<Integer> res = new ArrayList<>();
      if(root == null) return res;
  
      Deque<TreeNode> stack = new ArrayDeque<>();
      stack.push(root);
  
      while(!stack.isEmpty()) {
        TreeNode cur = stack.pop();
        res.add(0, cur.key);
  
        if(cur.left != null) {
          stack.push(cur.left);
        }
  
        if(cur.right != null) {
          stack.push(cur.right);
        }
      }
  
      return res;
    }
  }

/**
 * Stack
- 双stack的思想, 需要在图纸上画一画
- 原本需要的顺序是: 先leftChild, rightChild, currNode.
- 营造一个stack, reversely process: 先currNode, 再rightChild, 再leftChild
- 这样出来的结果是reverse的, 那么翻转一下就可以了.
- v1做的时候用了stack1, stack2, 因为根据这个双stack的思想而来
- v2简化, 可以放在一个stack里面, 每次record result 的时候: rst.add(0, item);

#### Recursive
trivial, 先加left recursively, 再加right recursively, 然后组成头部.
 */

 // Method2, Iterative, Option2: regular sequence add to stack: add curr, right, left
// only process curr if its children is processed
class Solution {
  public List<Integer> postorderTraversal(TreeNode root) {
      List<Integer> rst = new ArrayList<>();
      if (root == null) return rst;
      Stack<TreeNode> stack = new Stack<>();
      Set<TreeNode> set = new HashSet<>();
      stack.push(root);
      
      while (!stack.isEmpty()) {
          TreeNode node = stack.peek();
          if (validate(set, node)) {
              stack.pop();
              rst.add(node.val);
              set.add(node);
              continue;
          }
          if (node.right != null) stack.push(node.right);
          if (node.left != null) stack.push(node.left);
      }
      return rst;
  }
  
  private boolean validate(Set<TreeNode> set, TreeNode node) {
      if(node.left == null && node.right == null) return true;
      boolean left = set.contains(node.left), right = set.contains(node.right);
      if (left && right) return true;
      if ((node.left == null && right) || (node.right == null && left)) return true;
      return false;
  }
}