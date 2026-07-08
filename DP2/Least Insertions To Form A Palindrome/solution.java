/**
 * Insert the least number of characters to a string in order to make the new string a palindrome. 
 * Return the least number of characters should be inserted.

Assumptions:

The given string is not null.
 */

/**
 * Intuition
    Split the string s into to two parts,
    and we try to make them symmetrical by adding letters.

    The more common symmetrical subsequence they have,
    the less letters we need to add.

    Now we change the problem to find the length of longest common sequence.
    This is a typical dynamic problem.


    Explanation
    Step1.
    Initialize dp[n+1][n+1],
    wheredp[i][j] means the length of longest common sequence between
    i first letters in s1 and j first letters in s2.

    Step2.
    Find the the longest common sequence between s1 and s2,
    where s1 = s and s2 = reversed(s)

    Step3.
    return n - dp[n][n]


    Complexity
    Time O(N^2)
    Space O(N^2)
*/

public class Solution {
    public int leastInsertion(String input) {
      // Write your solution here
      int n = input.length();
  
      int[][] dp = new int[n + 1][n + 1];
  
      for(int i = 0; i < n; i++){
        for(int j = 0; j < n; j++){
          if(input.charAt(i) == input.charAt(n - 1 - j)){
            dp[i + 1][j + 1] = dp[i][j] + 1;
          } else {
            dp[i + 1][j + 1] = Math.max(dp[i + 1][j], dp[i][j + 1]);
          }
        }
      }
  
      return n - dp[n][n];
    }
  }