/**
 * Implement atoi to convert a string to an integer. Hint: Carefully consider all possible input cases. If you want a challenge, please do not see below and ask yourself what are the possible input cases. 
 * Notes: It is intended for this problem to be specified vaguely (ie, no given input specs). 
 * You are responsible to gather all the input requirements up front.

The function first discards as many whitespace characters as necessary until the first non-whitespace character is found. 
Then, starting from this character, takes an optional initial plus or minus sign followed by as many numerical digits as possible, 
and interprets them as a numerical value.

The string can contain additional characters after those that form the integral number, 
which are ignored and have no effect on the behavior of this function.

If the first sequence of non-whitespace characters in str is not a valid integral number, 
or if no such sequence exists because either str is empty or it contains only whitespace characters, no conversion is performed.

If no valid conversion could be performed, a zero value is returned. 
If the correct value is out of the range of representable values, return INT_MAX (2147483647) if it is positive, 
or INT_MIN (-2147483648) if it is negative.
 */

public class Solution {
    public int atoi(String str) {
      // Write your solution here
      String st = str.trim();
  
      boolean negetive = st.charAt(0) == '-';
  
      long res = 0;
  
      for(int i = 0; i < st.length(); i++){
        if(st.charAt(i) == '-') continue;
        if(st.charAt(i) == '.' || st.charAt(i) == ' ') break;
        int val = st.charAt(i) - '0';
  
        res = res * 10 + val;
      }
  
      res = negetive ? -res : res;
  
      if(res > Math.pow(2, 31) - 1) return Integer.MAX_VALUE;
      if(res < -Math.pow(2, 31)) return Integer.MIN_VALUE;
  
      return (int)res;
    }
  }