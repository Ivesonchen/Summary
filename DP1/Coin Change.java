/**
 * You are given an integer array coins representing coins of different denominations and an integer amount representing a total amount of money.

Return the fewest number of coins that you need to make up that amount. 
If that amount of money cannot be made up by any combination of the coins, return -1.

You may assume that you have an infinite number of each kind of coin.
 */

// recursive

public class Solution {
  public int coinChange(int[] coins, int amount) {
      if(amount<1) return 0;
      return helper(coins, amount);
  }
  
  private int helper(int[] coins, int rem, int[] count) { // rem: remaining coins after the last step; count[rem]: minimum number of coins to sum up to rem
      if(rem<0) return -1; // not valid
      if(rem==0) return 0; // completed
      // if(count[rem-1] != 0) return count[rem-1]; // already computed, so reuse
      int min = Integer.MAX_VALUE;
      for(int coin : coins) {
          int res = helper(coins, rem-coin, count);
          if(res>=0 && res < min)
              min = 1+res;
      }

      return (min==Integer.MAX_VALUE) ? -1 : min;
  }
}


// recursive + memo
public class Solution {
  public int coinChange(int[] coins, int amount) {
      if(amount<1) return 0;
      return helper(coins, amount, new int[amount]);
  }
  
  private int helper(int[] coins, int rem, int[] count) { // rem: remaining coins after the last step; count[rem]: minimum number of coins to sum up to rem
      if(rem<0) return -1; // not valid
      if(rem==0) return 0; // completed
      if(count[rem-1] != 0) return count[rem-1]; // already computed, so reuse
      int min = Integer.MAX_VALUE;
      for(int coin : coins) {
          int res = helper(coins, rem-coin, count);
          if(res>=0 && res < min)
              min = 1+res;
      }
      count[rem-1] = (min==Integer.MAX_VALUE) ? -1 : min;
      return count[rem-1];
  }
  }