/**
 * Given a string S, you are allowed to convert it to a palindrome by adding characters in front of it. 
 * Find and return the shortest palindrome you can find by performing this transformation.

For example:

Given "aacecaaa", return "aaacecaaa".

Given "abcd", return "dcbabcd".
 */

 // hmmmm 认为 对称 右侧的 字符串是完整的  只要找出 左侧缺几个
public class Solution {
    public String shortestPalindrome(String input) {
      // Write your solution here
      int i = 0, j = input.length() - 1;
      int end = input.length() - 1;
  
      while(i < j) {
        if(input.charAt(i) == input.charAt(j)){
          i++;
          j--;
        } else {
          // 不停地 移动右侧的挡板 去掉字符
          i = 0;
          end--;
          j = end;
        }
      }
      return new StringBuilder(input.substring(end + 1)).reverse().toString() + input;
    }
  }