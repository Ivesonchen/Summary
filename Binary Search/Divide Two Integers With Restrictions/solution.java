/**
 * Given two integers a and b, calculate a / b without using divide/mod operations.

Assuming any number divided by 0 is Integer.MAX_VALUE.

Examples:

0 / 1 = 0

1 / 0 = Integer.MAX_VALUE

-1 / 0 = Integer.MAX_VALUE

11 / 2 = 5

-11 / 2 = -5

11 / -2 = -5

-11 / -2 = 5
 */

public class Solution {
    public int divide(int a, int b) {
      // Write your solution here
      if(b == 0) return Integer.MAX_VALUE;
      boolean isNegative = (a < 0 && b > 0) || (a > 0 && b < 0) ? true : false;
      long absDividend = Math.abs((long) a);
      long absDivisor = Math.abs((long) b);
      long result = 0;
  
      while(absDividend >= absDivisor){
        long tmp = absDivisor, count = 1;
        while(tmp <= absDividend){
            tmp <<= 1;
            count <<= 1;
        }
        result += count >> 1;
        absDividend -= tmp >> 1;
      }
      return  isNegative ? (int) ~result + 1 : result > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) result;
    }
  }