/**
 * Get the list of keys in a given binary search tree in a given range[min, max] in ascending order, both min and max are inclusive.

Examples

        5

      /    \

    3        8

  /   \        \

 1     4        11

get the keys in [2, 5] in ascending order, result is  [3, 4, 5]

Corner Cases

What if there are no keys in the given range? Return an empty list in this case.
 */

public class Solution {
    public List<Integer> getRange(TreeNode root, int min, int max) {
      // Write your solution here
      List<Integer> res = new ArrayList<>();
  
      helper(root, min, max, res);
      return res;
    }
  
    public void helper(TreeNode cur, int min, int max, List<Integer> res){
      if(cur == null) return;
      if(cur.key > min){                // 以这个条件 避免进入不必要的递归
        helper(cur.left, min, max, res);
      }
  
      if(cur.key >= min && cur.key <= max){
        res.add(cur.key);
      }
      if(cur.key < max){
        helper(cur.right, min, max, res);
      }
    }
}