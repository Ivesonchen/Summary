/**
 * In a binary search tree, find k nodes containing the closest numbers to the given target number. 
 * return them in sorted array

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

closest number to 6 is 6
 */

class Solution {
    public List<Integer> closestKValues(TreeNode root, double target, int k) {
        Stack<TreeNode> stack = new Stack<>();

        Queue<Integer> queue = new LinkedList<>();

        TreeNode cur = root;

        while(!stack.isEmpty() || cur != null){
          while(cur != null){
            stack.push(cur);
            cur = cur.left;
          }

          cur = stack.pop();

          if(queue.size() < k){
            queue.offer(cur.val);
          } else {
            if(Math.abs(queue.peek() - target) > Math.abs(cur.val - target)){
              queue.poll();
              queue.offer(cur.val);
            }
          }
          cur = cur.right;
        }
        

        return (List<Integer>) queue;
    }
}