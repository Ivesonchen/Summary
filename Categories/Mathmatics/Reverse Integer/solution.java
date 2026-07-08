/**
 *  Reverse digits of an integer.

    Assumptions
    If the integer's last digit is 0, what should the output be? ie, cases such as 10, 100.
    Did you notice that the reversed integer might overflow? Assume the input is a 32-bit integer, then the reverse of 1000000003 overflows. How should you handle such cases?
    For the purpose of this problem, assume that your function returns 0 when the reversed integer overflows.
    Examples
    Input:      23
    Output:   32

    Input:     -14
    Output:  -41
 */

public class Solution {
    public int reverse(int x) {
      // Write your solution here
      long num = (long)Math.abs(x);
      long res = 0;
  
      while(num > 0){
        res = res * 10 + num % 10;
  
        num /= 10; 
      }
  
      res = x < 0 ? -res: res;
  
      if(res > Math.pow(2, 31) - 1 || res < -Math.pow(2, 31)) return 0;
  
      return (int)res;
    }
  }