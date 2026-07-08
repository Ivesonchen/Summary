/**
 * There are a row of n houses, each house can be painted with one of the three colors: red, blue or green. 
 * The cost of painting each house with a certain color is different. 
 * You have to paint all the houses such that no two adjacent houses have the same color.

The cost of painting each house with a certain color is represented by a n x 3 cost matrix. 
For example, costs[0][0] is the cost of painting house 0 with color red; 
costs[1][2] is the cost of painting house 1 with color green, and so on... 
Find the minimum cost to paint all houses.

Note:
All costs are positive integers.
 */
// stright forward   画出来一个二维数组 n 行 3 列

 // 太复杂
public class Solution {
    public int minCost(int[][] costs) {
      // Write your solution here
      int num = costs.length;
      if(num == 0) return 0;
  
      int[][] miniCost = new int[num][3];
  
      for(int i = 0; i < num; i++){
        for(int j = 0; j < 3; j++){
          if(i == 0) miniCost[i][j] = costs[i][j];
          else{
            int min = Integer.MAX_VALUE;
            for(int k = 0; k < 3; k++){
              if(j != k) min = Math.min(min, miniCost[i - 1][k]);
            }
            miniCost[i][j] = costs[i][j] + min;
          }
        }
      }
  
      return Math.min(Math.min(miniCost[num - 1][0], miniCost[num - 1][1]), miniCost[num - 1][2]);
    }
  }

// Good
public int minCost(int[][] costs) {
    if(costs==null||costs.length==0){
        return 0;
    }
    for(int i=1; i<costs.length; i++){
        costs[i][0] += Math.min(costs[i-1][1],costs[i-1][2]);
        costs[i][1] += Math.min(costs[i-1][0],costs[i-1][2]);
        costs[i][2] += Math.min(costs[i-1][1],costs[i-1][0]);
    }
    int n = costs.length-1;
    return Math.min(Math.min(costs[n][0], costs[n][1]), costs[n][2]);
}