/**
 * Determine the number of bits that are different for two given integers.

  Examples

  5(“0101”) and 8(“1000”) has 3 different bits
 */

 /**
  * XOR to find the difference between two numbers (by count of 1)
    Then count the number of 1     by & 00000...001 to determine if the last digit is 1 then move right

    检测某位是否为1 用 & 1 的方法
  */
public class Solution {
    public int diffBits(int a, int b) {
      // Write your solution here
      int res = a ^ b;
  
      int count = 0;
  
      for(int i = 0; i < 32; i++){
        if((res >> i & 1) == 1) {
          count ++;
        }
      }
  
      return count;
    }
  }
  