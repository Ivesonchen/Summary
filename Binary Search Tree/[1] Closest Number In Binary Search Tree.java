/**In a binary search tree, find the node containing the closest number to the given target number.

Assumptions:

The given root is not null.
There are no duplicate keys in the binary search tree.
Examples:

    5
  /    \
2      11
     /    \
    6     14

closest number to 4 is 5

closest number to 10 is 11

closest number to 6 is 6 */

public class Solution {
    public int closest(TreeNode root, int target) {
      // Write your solution here
      int res = root.key;
      TreeNode cur = root;

      while(cur != null){
        int value = cur.key;
        if(Math.abs(value - target) < Math.abs(res - target)){
          res = value;
        }

        if(value < target){
          cur = cur.right;
        } else if(value > target){
          cur = cur.left;
        } else {
          return value;
        }
      }

      return res;
    }
}

public class Solution {
    public int closest(TreeNode root, int target) {
      // Write your solution here
      int high = Integer.MAX_VALUE;
      int low = Integer.MIN_VALUE;
  
      TreeNode cur = root;
  
      while(cur != null){
        int value = cur.key;
  
        if(value < target){
          low = Math.max(low, value);
          cur = cur.right;
        } else if(value > target){
          high = Math.min(high, value);
          cur = cur.left;
        } else {
          return value;
        }
      }
      if(high == Integer.MAX_VALUE) return low;
      if(low == Integer.MIN_VALUE) return high;
  
      if(high - target > target - low){
        return low;
      } else {
        return high;
      }
    }
}

