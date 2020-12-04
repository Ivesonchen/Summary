/**
 * Say you have an array for which the ith element is the price of a given stock on day i.

Design an algorithm to find the maximum profit. You may complete as many transactions as you like 
(ie, buy one and sell one share of the stock multiple times) with the following restrictions:

You may not engage in multiple transactions at the same time (ie, you must sell the stock before you buy again).
After you sell your stock, you cannot buy stock on next day. (ie, cooldown 1 day)
Example:

prices = [1, 2, 3, 0, 2]
maxProfit = 3
transactions = [buy, sell, cooldown, buy, sell]
 */

/**
  * 1. 定义状态
	hold[i] : 第i天 hold股票的最大profit
	unhold[i] : 第i天不hold股票的最大profit
    2. Target: unhold[n - 1]
            
    3. Base Case:
        hold[0] = -prices[0]
        hold[1] = max(-prices[1], -prices[0])
        unhold[0] = 0

    4. 状态转移
        hold[i] 取以下情况最大值
                1. 第i天买入	   unhold[i - 2] - prices[i]
                2. 第i天没有买入 hold[i - 1]
        unhold[i] 取一下情况最大值
            1. 第i天有卖出      hold[i - 1] + prices[i]
            2. 第i天没有卖出	  unhold[i - 1]

    Time: O(n)
    Space: O(n) ---> O(1)
*/

public class Solution {
    public int maxProfit(int[] prices) {
      // Write your solution here
      int n = prices.length;
      if(n == 0) return 0;
      int[] hold = new int[n];
      int[] unhold = new int[n];
      
      hold[0] = -prices[0];
      
      for(int i = 1; i < n; i++){
        if(i == 1){
          hold[i] = Math.max(hold[i - 1], -prices[1]);
        } else {
          hold[i] = Math.max(hold[i - 1], unhold[i - 2] - prices[i]);
        }
        unhold[i] = Math.max(unhold[i - 1], hold[i - 1] + prices[i]);
      }
      
      return unhold[n - 1];
    }
  }
