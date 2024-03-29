/**
 * Given a string, determine if a permutation of the string could form a palindrome.

For example,
"code" -> False, "aab" -> True, "carerac" -> True.
https://leetcode.com/problems/palindrome-permutation/solution/
 */
 
// 字符串里的所有字符出现的次数 只能有一个或者小于一个字符可以出现一次
public class Solution {
    public boolean canPermutePalindrome(String input) {
      // Write your solution here
      int[] count = new int[256];
      
      int flag = 0;
  
      for(int i = 0; i < input.length(); i++){
        count[(int)input.charAt(i)] ++;
  
        if(count[input.charAt(i)] % 2 == 0){
          flag --;
        } else {
          flag ++;
        }
      }
  
      return flag <= 1 ? true : false;
    }
  }