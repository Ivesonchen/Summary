/**
 * Determine what is the depth of the forest, the depth of the forest is the maximum depth of the trees in the forest.

Examples:

A = {2, 2, -1, 5, 5, -1, 3}, represnts the forest:

  2

 / \

0   1

and

    5

   /  \

  3    4

 /

6

The depth of the forest is 3(the depth of the second tree).

Assumptions:

The given array is not null or empty, all the elements in the array are in the range of [-1, N - 1] where N is the length of the array.
Corner Cases:

You should be able to identify that there could be a cycle in the forest, what if that is the case? Return -1
 */

public class Solution {
    public int depth(int[] forest) {
      // Write your solution here
      int n = forest.length;
  
      int depth[] = new int[n];
  
      for(int i = 0; i < n; i++){
        fillDepth(forest, i, depth);
      }
  
      int res = Integer.MIN_VALUE;
  
      for(int i = 0; i < n; i++){
        res = Math.max(res, depth[i]);
      }
  
      return res;
    }
  
    public void fillDepth(int[] forest, int i, int[] depth){
      if(depth[i] != 0) return;
  
      if(forest[i] == -1){
        depth[i] = 1;
        return;
      }
  
      if(depth[forest[i]] == 0){
        fillDepth(forest, forest[i], depth);
      }
  
      depth[i] = depth[forest[i]] + 1;
    }
  }