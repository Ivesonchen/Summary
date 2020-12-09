/**
 * There are a row of n houses, each house can be painted with one of the k colors. 
 * The cost of painting each house with a certain color is different. 
 * You have to paint all the houses such that no two adjacent houses have the same color.

The cost of painting each house with a certain color is represented by a n x k cost matrix. 
For example, costs[0][0] is the cost of painting house 0 with color 0; 
costs[1][2] is the cost of painting house 1 with color 2, and so on... 
Find the minimum cost to paint all houses.

Note:
All costs are positive integers.

Optimization solution
https://leetcode.com/problems/paint-house-ii/solution/
 */

 //O(nk^2)
public class Solution {
    public int minCostII(int[][] costs) {
      // Write your solution here
        int num = costs.length;
        if(num == 0) return 0;
        int K = costs[0].length;
    
        int[][] miniCost = new int[num][K];
    
        for(int i = 0; i < num; i++){
          for(int j = 0; j < K; j++){
            if(i == 0) miniCost[i][j] = costs[i][j];
            else{
              int min = Integer.MAX_VALUE;
              for(int k = 0; k < K; k++){
                if(j != k) min = Math.min(min, miniCost[i - 1][k]);
              }
              miniCost[i][j] = costs[i][j] + min;
            }
          }
        }
        int res = Integer.MAX_VALUE;
        for(int ele : miniCost[num - 1]){
          res = Math.min(res, ele);
        }
    
        return res;
    }
  }