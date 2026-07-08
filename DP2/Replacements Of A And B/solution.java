/**
 * Given a string with only character 'a' and 'b', 
 * replace some of the characters such that the string becomes in the forms of all the 'b's are on the right side of all the 'a's.

Determine what is the minimum number of replacements needed.

Assumptions:

The given string is not null.
Examples:

"abaab", the minimum number of replacements needed is 1 (replace the first 'b' with 'a' so that the string becomes "aaaab").
 */

 /**
  * DP: DP1[len] DP2[len]

从左到右数: 在index = i 的时候, DP1[i] = 如果把0~i全变成'a'需要多少次替换

从右到左数: 在index = i 的时候 DP2[i] = 如果吧i~len-1全变成'b'需要多少次替换.

最后算从0~len-1 , 哪里DP1[i] + DP2[i + 1] 的值最低.
  */

public class Solution {
    public int minReplacements(String input) {
      // Write your solution here
      int len = input.length();
      if(len == 0) return 0;
      int[] dp1 = new int[len];
      int[] dp2 = new int[len];
  
      dp1[0] = 0;
      for(int i = 1; i < len; i++){
        if(input.charAt(i - 1) == 'b'){
          dp1[i] = dp1[i - 1] + 1;
        } else {
          dp1[i] = dp1[i - 1];
        }
      }
  
      dp2[len - 1] = 0;
      for(int i = len - 2; i >= 0; i--){
        if(input.charAt(i + 1) == 'a'){
          dp2[i] = dp2[i + 1] + 1;
        } else {
          dp2[i] = dp2[i + 1];
        }
      }
  
      int res = Integer.MAX_VALUE;
      for(int i = 0; i < len; i++){
        res = Math.min(res, dp1[i] + dp2[i]);
      }
  
      return res;
    }
  }