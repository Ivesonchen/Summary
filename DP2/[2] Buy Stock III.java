/**
 * Given an array of positive integers representing a stock’s price on each day. 
 * On each day you can only make one operation: either buy or sell one unit of stock, 
 * at any time you can only hold at most one unit of stock, and you can make at most 2 transactions in total. 
 * Determine the maximum profit you can make.

Assumptions

The give array is not null and is length of at least 2
Examples

{2, 3, 2, 1, 4, 5, 2, 11}, the maximum profit you can make is (5 - 1) + (11 - 2) = 13
https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iii/solution/
 */

 

 /**
  * Here, the oneBuy keeps track of the lowest price, and oneBuyOneSell keeps track of the biggest profit we could get.
Then the tricky part comes, how to handle the twoBuy? Suppose in real life, you have bought and sold a stock and made $100 dollar profit. When you want to purchase a stock which costs you $300 dollars, how would you think this? You must think, um, I have made $100 profit, so I think this $300 dollar stock is worth $200 FOR ME since I have hold $100 for free.
There we go, you got the idea how we calculate twoBuy!! We just minimize the cost again!! The twoBuyTwoSell is just making as much profit as possible.
  */
public class Solution {
    public int maxProfit(int[] array) {
      // Write your solution here
      int oneBuy = Integer.MAX_VALUE;
      int oneBuyOneSell = 0;
      int twoBuy = Integer.MAX_VALUE;
      int twoBuyTwoSell = 0;
  
      for(int price: array){
        oneBuy = Math.min(oneBuy, price);
        oneBuyOneSell = Math.max(oneBuyOneSell, price - oneBuy);
        twoBuy = Math.min(twoBuy, price - oneBuyOneSell);
        twoBuyTwoSell = Math.max(twoBuyTwoSell, price - twoBuy);
      }
  
      return twoBuyTwoSell;
    }
  }
  