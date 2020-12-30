/**
 * Determine whether an integer is a palindrome.

Assumptions

Could negative integers be palindromes? (ie, -1) No.

If you are thinking of converting the integer to string, note the restriction of using extra space.
You could also try reversing an integer. However, if you have solved the problem "Reverse Integer", you know that the reversed integer might overflow. 
How would you handle such case? There is a more generic way of solving this problem.
 */

public class Solution {
    public boolean isPalindrome(int x) {
      // Write your solution here
      if(x < 0) return false;
  
      int len = 1;
  
      while(x / len >= 10) {
        len *= 10;
      } // 计算 x有多长
  
      while(x > 0){
        int left = x / len; // 取最左边数字
        int right = x % 10; // 取最右边数字
  
        if(left != right) return false;
  
        x %= len;
        x /= 10;            // x 左缩一位 右缩一位
        len /= 100;         // 长度缩小两位
      }
      return true;
    }
  }