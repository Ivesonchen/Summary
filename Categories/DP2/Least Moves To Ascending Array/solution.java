/**
 * Given an integer array, what is the minimum number of operations to convert it to an ascending array.

One operation you can move one element of the array to another position.



Examples:

{1, 3, 2, 4}, the least moves needed is 1, move 2 to the middle of 1 and 3.
 */

 // 思路就是 总长 减去 longest ascending subsequence

public class Solution {
    public int leastMoves(int[] array) {
      // Write your solution here
  
      int len = array.length;
      int[] dp = new int[len];
  
      int max = 0;
  
      for(int i = 0; i < len; i++){
        for(int j = 0; j < i; j++){
          if(array[j] < array[i]){
            dp[i] = Math.max(dp[j] + 1, dp[i]);
          }
        }
  
        max = Math.max(max, dp[i]);
      }
  
      return len - max - 1;
    }
  }