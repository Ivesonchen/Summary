
public class Solution {
    public List<Integer> postOrder(TreeNode root) {
      // Write your solution here
  
      List<Integer> res = new ArrayList<>();
      if(root == null) return res;
  
      Stack<TreeNode> stack = new Stack<>();
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