/**
 * 
Diagonal sum in a binary tree is the sum of all the node’s data lying through the dashed lines. 
Given a Binary Tree, print all diagonal sums.



For the above input tree, output should be:
                1
            2       3
        4      5,6      7
            1       1

{ 11, 14, 5 }
 */
// DFS
public class Solution {
    public List<Integer> diagonalSum(TreeNode root) {
      // Write your solution here
      List<Integer> res = new ArrayList<>();
      if(root == null) return res;
      Map<Integer, Integer> map = new HashMap<>();
      helper(root, 0, map);
      for(Integer ele : map.values()){
        res.add(ele);
      }
  
      return res;
    }
  
    public void helper(TreeNode cur, int diagonal, Map<Integer, Integer> map){
      if(cur == null) return;
  
      map.put(diagonal, map.getOrDefault(diagonal, 0) + cur.key);
  
      helper(cur.left, diagonal + 1, map);
      helper(cur.right, diagonal, map);
    }
  }