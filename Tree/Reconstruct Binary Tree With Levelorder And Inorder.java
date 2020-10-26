/**
 * Given the levelorder and inorder traversal sequence of a binary tree, reconstruct the original tree.

Assumptions

The given sequences are not null and they have the same length
There are no duplicate keys in the binary tree
Examples

levelorder traversal = {5, 3, 8, 1, 4, 11}

inorder traversal = {1, 3, 4, 5, 8, 11}

the corresponding binary tree is

        5

      /    \

    3        8

  /   \        \

1      4        11

How is the binary tree represented?

We use  level order traversal sequence with a special symbol "#" denoting the null node.

For Example:

The sequence [1, 2, 3, #, #, 4] represents the following binary tree:

    1

  /   \

 2     3

      /

    4
 */
/**
 *  LevelOrder   [ 5, 3, 8, 1, 4, 11 ]
 *  inOrder      [ 1, 3, 4, 5, 8, 11 ]
 * 
 */
public class Solution {
    public TreeNode reconstruct(int[] inOrder, int[] levelOrder) {
      // Write your solution here
      Map<Integer, Integer> map = new HashMap<>();
  
      for(int i = 0; i < levelOrder.length; i++){
        map.put(levelOrder[i], i);
      }
  
      return helper(inOrder, 0, inOrder.length - 1, map);
    }
  	// Recursive function to construct a binary tree from in-order and
	// level-order traversals
    public TreeNode helper(int[] inOrder, int start, int end, Map<Integer, Integer> map){
      if(start > end) return null;
  
    // find the index of root node in inorder[] to determine the
    // boundary of left and right subtree
      int index = start;
      for(int i = start + 1; i <= end; i++){
        if(map.get(inOrder[i]) < map.get(inOrder[index])){ // in the current range, trying to find the index of root of subTree
                                                        // 如果 在一个区间中 某个 node 有最小的 index (在levelOrder 的序列中) 说明这个node是root
          index = i;
        }
      }
  
      TreeNode root = new TreeNode(inOrder[index]);
  
      //然后传递正确的 区间start end 给递归函数
      root.left = helper(inOrder, start, index - 1, map);
      root.right = helper(inOrder, index + 1, end, map);
  
      return root;
    }
  }