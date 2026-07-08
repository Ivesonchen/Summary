/**
 * Find the greatest common factor of two positive integers.

Examples:

a = 12, b = 18, the greatest common factor is 6, since 12 = 6 * 2, 18 = 6 * 3.
a = 5, b = 16, the greatest common factor is 1.

https://www.cxyxiaowu.com/995.html

欧几里得 辗转相除法
 */

public class Solution {
    public int gcf(int a, int b) {
      // Write your solution here
      if(b == 0) return a;
  
      return gcf(b, a % b);
    }
  }
  

int gcd(int a, int b) {
  int tmp;
  while (b > 0) {
      tmp = b;
      b = a % b;
      a = tmp;
  }
  return a;
}

/**
      a     b
      12    18
      18    12
      12    6
      6     0
 */