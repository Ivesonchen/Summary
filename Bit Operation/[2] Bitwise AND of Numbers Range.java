/**
 * Given a range [m, n] where 0 <= m <= n <= 2147483647, return the bitwise AND of all numbers in this range, inclusive.

For example, given the range [5, 7], you should return 4.

https://leetcode.com/problems/bitwise-and-of-numbers-range/discuss/56729/Bit-operation-solution(JAVA)
 */


public class Solution {
    public int rangeBitwiseAnd(int m, int n) {
      // Write your solution here
      int res = m;
      for(int i = m + 1; i <= n; i++){
        res &= i;
      }
  
      return res;
    }
  }

  public class Solution {
    public int rangeBitwiseAnd(int m, int n) {
      // Write your solution here
      int res = 0;
  
      while(m != n){
        m >>= 1;
        n >>= 1;
        res ++;
      }
  
      return m << res;
    }
  }