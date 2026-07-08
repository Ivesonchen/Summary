/**
Given a root of an N-ary tree, you need to compute the length of the diameter of the tree.

The diameter of an N-ary tree is the length of the longest path between any two nodes in the tree. 
This path may or may not pass through the root.

(Nary-Tree input serialization is represented in their level order traversal, each group of children is separated by the null value.)
 */

 class Solution {
     public int diameter (Node node) {
         int[] result = new int[1];
         helper(root, result);

         return result[0];
     }

     public void helper(Node root, int[] result) {
         if(root == null) return 0;

         PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());

         for(Node node : root.children) {
             int temp = 0;

             temp = helper(node, result);
             pq.add(temp);
         }

         int first = !pq.isEmpty() ? pq.poll() : 0;
         int second = !pq.isEmpty() ? pq.poll() : 0;

         result[0] = Math.max(first + second + 1, result[0]);

         return Math.max(first, second);
     }
 }