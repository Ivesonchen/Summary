/**
 * Generate the hexadecimal representation for a given non-negative integer number as a string. 
 * The hex representation should start with "0x".

There should not be extra zeros on the left side.

Examples

0's hex representation is "0x0"
255's hex representation is "0xFF"
 */

public class Solution {
    public String hex(int number) {
      // Write your solution here
      String res = "";
      String dic = "0123456789ABCDEF";
  
      int count = 0;
  
      while(number != 0 && count++ < 8){
        res = dic.charAt(number & 0xf) + res;
        number = number >> 4;
      }
  
      return res == "" ? "0x0" : "0x" + res;
    }
  }