/**
 * Word “book” can be abbreviated to 4, b3, b2k, etc. Given a string and an abbreviation, return if the string matches the abbreviation.

Assumptions:

The original string only contains alphabetic characters.
Both input and pattern are not null.
Pattern would not contain invalid information like "a0a","0".
Examples:

pattern “s11d” matches input “sophisticated” since “11” matches eleven chars “ophisticate”.
 */

/**
 * 从后往前移动 会比较好计算 数字 
 * 
 * 结束时 指针应该都指向 -1
 * 
 * O（n）
 */

public class Solution {
    public boolean match(String input, String pattern) {
      // Write your solution here
  
      int i = input.length() - 1;
      int j = pattern.length() - 1;
      int pos = 0;
      int num = 0;
  
      while(i >= 0 && j >= 0){                                                                                          
        if(Character.isLetter(pattern.charAt(j))){
          pos = 0;
          num = 0;
          if(i >= 0 && input.charAt(i) == pattern.charAt(j)){
            i--;
            j--;
          } else {
            return false;
          }
        } else {
          int val = Integer.parseInt(String.valueOf(pattern.charAt(j)));
          num += val * Math.pow(10, pos);
          pos++;
          j--;
          i -= num;
        }
      }
  
      if(i == -1 && j == -1) return true;
  
      return false;
    }
  }