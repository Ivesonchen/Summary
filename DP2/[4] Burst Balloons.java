/**
 * Given n balloons, indexed from 0 to n-1. Each balloon is painted with a number on it represented by array nums. 
 * You are asked to burst all the balloons. If the you burst balloon i you will get nums[left] * nums[i] * nums[right] coins. 
 * Here left and right are adjacent indices of i. After the burst, the left and right then becomes adjacent.

Find the maximum coins you can collect by bursting the balloons wisely.

Note: 
(1) You may imagine nums[-1] = nums[n] = 1. They are not real therefore you can not burst them.
(2) 0 ≤ n ≤ 500, 0 ≤ nums[i] ≤ 100

Example:

Given [4, 1, 3, 9]

Return 165

nums = [4,1,3,9] -->  [4,3,9] -->   [4,9]   -->   [9]  --> []
coins =  4*1*3      +  4*3*9      + 1*4*9      + 1*9*1   = 165

https://www.youtube.com/watch?v=_4qGDebH_ws
 */

 //To-Do
public class Solution {
    public int maxCoins(int[] nums) {
      // Write your solution here
      int n = nums.length;
      int[] array = new int[n + 2];
  
      array[0] = 1;
      for(int i = 0; i < n; i++){
        array[i + 1] = nums[i];
      }
      array[array.length - 1] = 1;
  
      int[][] dp = new int[n + 2][n + 2];
  
      for(int len = 1; len <= n; len ++){           //计算粒子的大小        不断地放大计算粒子的大小 最后结果就是 1 ~ n
        for(int i = 1; i <= n - len + 1; ++i){      // i 为左区间   j为右区间
          int j = i + len - 1;
          for(int k = i; k <= j; k++){
            dp[i][j] = Math.max(dp[i][j], dp[i][k - 1] + dp[k + 1][j] + array[k] * array[i - 1] * array[j + 1]);
          }
        }
      }
  
      return dp[1][n];
    }
  }