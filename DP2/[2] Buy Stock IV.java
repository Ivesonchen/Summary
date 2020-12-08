/**
 * Given an array of integers representing a stock’s price on each day. 
 * On each day you can only make one operation: either buy or sell one unit of stock, 
 * and at any time you can only hold at most one unit of stock, and you can make at most K transactions in total. 
 * Determine the maximum profit you can make.

Assumptions

The give array is not null and is length of at least 2
Examples

{2, 3, 2, 1, 4, 5, 2, 11}, K = 3, the maximum profit you can make is (3 - 2) + (5 - 1) + (11 - 2)= 14
https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iv/discuss/54113/A-Concise-DP-Solution-in-Java

& AlgoExpert
 */

 
public class Solution {
    public int maxProfit(int[] array, int k) {
      // Write your solution here
      int len = array.length;
  
      int[][] t = new int[k + 1][len];
      for(int i = 1; i <= k; i++){
        int tempMax = -array[0];
        for(int j = 1; j < len; j++){
          t[i][j] = Math.max(t[i][j - 1], array[j] + tempMax);    
          // 此处的最大值为 之前有最大收益且手中持有某股票 此时以当前价卖掉所获得的收益
          tempMax = Math.max(tempMax, t[i - 1][j - 1] - array[j]); 
          // tempMax 记录的是上一行中(0 <= tempMax <j) 最大收益 - price     就是之前拥有最大收益并且持有股票
        }
      }
  
      return t[k][len - 1];
    }
  }