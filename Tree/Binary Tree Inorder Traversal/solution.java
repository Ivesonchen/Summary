//https://leetcode.com/problems/binary-tree-inorder-traversal/solution/
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

class Solution {
  public List < Integer > inorderTraversal(TreeNode root) {
      List<Integer> res = new ArrayList<>();
      helper(root, res);
      return res;
  }

  public void helper(TreeNode root, List <Integer> res) {
    if (root == null) return;

    helper(root.left, res);
    res.add(root.val);
    helper(root.right, res);
  }
}

//Morris Traversal    往左捋直树杈
/**
 * Step 1: Initialize current as root

Step 2: While current is not NULL,

If current does not have left child

    a. Add current’s value

    b. Go to the right, i.e., current = current.right

Else

    a. In current's left subtree, make current the right child of the rightmost node

    b. Go to this left child, i.e., current = current.left
 */

class Solution {
  public List < Integer > inorderTraversal(TreeNode root) {
      List <Integer> res = new ArrayList<>();
      TreeNode curr = root;
      TreeNode pre;
      while (curr != null) {
          if (curr.left == null) {
              res.add(curr.val);
              curr = curr.right; // move to next right node
          } else { // has a left subtree
              pre = curr.left;
              while (pre.right != null) { // find rightmost
                  pre = pre.right;
              }
              pre.right = curr; // put cur after the pre node
              TreeNode temp = curr; // store cur node
              curr = curr.left; // move cur to the top of the new tree
              temp.left = null; // original cur left be null, avoid infinite loops
          }
      }
      return res;
  }
}