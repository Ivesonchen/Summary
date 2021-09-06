/**
 * Given a binary tree, convert it to a doubly linked list in place (use the left pointer as previous, use the right pointer as next).
The values in the nodes of the doubly linked list should follow the in-order traversal sequence of the binary tree.
Examples:

    10

   /  \

  5    15

 /

2
Output:  2 <-> 5 <-> 10 <-> 15
 */

 // 需要深刻理解一下
public class Solution {
    public TreeNode toDoubleLinkedList(TreeNode root) {
      // Write your solution here.
      if(root == null) return root;
      root = helper(root);
      while(root.left != null) {
        root = root.left;
      }
      
      return root;
    }
  
    public TreeNode helper(TreeNode cur){
      if(cur == null) return cur;
  
      if(cur.left != null){
        TreeNode left = helper(cur.left);
        while(left.right != null){
          left = left.right;
        }
        left.right = cur;
        cur.left = left;
      }
  
      if(cur.right != null){
        TreeNode right = helper(cur.right);
        while(right.left != null){
          right = right.left;
        }
        right.left = cur;
        cur.right = right;
      }
      return cur;
    }


  }

/**

dummy -> 1st   ->  2nd					root dfs
	    <-	     <-
   	               prev	
   */
Node prev = null;
public Node treeToDoublyList(Node root) {
	if (root == null) return null;
	Node dummy = new Node(0, null, null);
	prev = dummy;
	helper(root);
	//connect head and tail
	prev.right = dummy.right;
	dummy.right.left = prev;
	return dummy.right;
}

private void helper (Node cur) {
	if (cur == null) return;
	helper(cur.left);
	prev.right = cur;
	cur.left = prev;
	prev = cur;
	helper(cur.right);
}