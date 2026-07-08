/**
 * Given a non-empty string s, you may delete at most one character. 
 Judge whether you can make it a palindrome.


Example 1:

Input: "aba"
Output: True
Example 2:

Input: "abca"
Output: True
Explanation: You could delete the character 'c'.
Note:

The string will only contain lowercase characters a-z. The maximum length of the string is 50000.
 */

 /**
  * 对向双指针  当遇到第一对不等的i 和 j的时候， 检查 (i + 1, j) 和 (i, j - 1)  也就是说检查 去掉任意一个 字符
  */

public class Solution {
    public boolean validPalindrome(String input) {
      // Write your solution here
      int i = 0;
      int j = input.length() - 1;
  
      while(i < j) {
        if(input.charAt(i) == input.charAt(j)){
          i++;
          j--;
        } else {
          return isPalindrome(input, i + 1, j) || isPalindrome(input, i, j - 1);
        }
      }
      return true;
    }
  
    public boolean isPalindrome(String input, int i, int j){
      while(i < j){
        if(input.charAt(i) == input.charAt(j)){
          i++;
          j--;
        } else {
          return false;
        }
      }
      return true;
    }
  }