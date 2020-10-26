/**
 * Given Inorder and Preorder traversals of a binary tree, get the Postorder traversal without reconstructing a binary tree.

Assumptions:

The given Inorder and Preorder traversals are guaranteed to be valid.
Examples:

Input:

Inorder traversal in[] = {4, 2, 5, 1, 3, 6}
Preorder traversal pre[] = {1, 2, 4, 5, 3, 6}

Output:
Postorder traversal is {4, 5, 2, 6, 3, 1}
 */

class Solution {
    public int[] postOrder(int[] pre, int[] in) {
      // Write your solution here
      Map<Integer, Integer> map = new HashMap<>();
  
      for(int i = 0; i < in.length; i++){
        map.put(in[i], i);
      }
  
      List<Integer> res = new ArrayList<>();
      // int[] preIndex = new int[1];
      helper(0, in.length - 1, pre, new int[1] , map, res);
  
      int[] resArray = new int[in.length];
  
      for(int i = 0; i < in.length; i++){
        resArray[i] = res.get(i);
      }
      return resArray;
    }
  
    public void helper(int start, int end, int[] preorder, int[] preIndex, Map<Integer, Integer> map, List<Integer> res){
      if(start > end) return;
      if(preIndex[0] >= preorder.length) return;
  
      int value = preorder[preIndex[0]++];
  
    //   if(start == end) {
    //     res.add(value);
    //     return;
    //   }
  
      int i = map.get(value);
  
      helper(start, i - 1, preorder, preIndex, map, res);
      helper(i + 1, end, preorder, preIndex, map, res);
  
      res.add(value);
    }
  }