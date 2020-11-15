/**
 * Additive number is a string whose digits can form additive sequence.

A valid additive sequence should contain at least three numbers. Except for the first two numbers, each subsequent number in the sequence must be the sum of the preceding two.

For example:
"112358" is an additive number because the digits can form an additive sequence: 1, 1, 2, 3, 5, 8.

1 + 1 = 2, 1 + 2 = 3, 2 + 3 = 5, 3 + 5 = 8
"199100199" is also an additive number, the additive sequence is: 1, 99, 100, 199.

1 + 99 = 100, 99 + 100 = 199
Note: Numbers in the additive sequence cannot have leading zeros, so sequence 1, 2, 03 or 1, 02, 3 is invalid.

Given a string containing only digits '0'-'9', write a function to determine if it's an additive number.
 */

 
public class Solution {
    public boolean isAdditiveNumber(String num) {
      // Write your solution here
      int L = num.length();

      // choose the first number A
      for(int i = 1; i <= (L - 1)/2; i++){
        if(num.charAt(0) == '0' && i >= 2) break; // number can't start with '0' if length of number is greater than 2;
        //choose the second number B
        for(int j=i+1; L-j >= j-i && L-j >= i; j++){
          if(num.charAt(i) == '0' && j-i >= 2) break; // number validation
  
          long num1 = Long.parseLong(num.substring(0, i));
          long num2 = Long.parseLong(num.substring(i, j));
          String substr = num.substring(j);
  
          if(isAdditive(substr, num1, num2)) return true;
        }
      }
  
      return false;
    }
    
    public boolean isAdditive(String str, long num1, long num2){
      if(str.equals("")) return true;       // reach the end of string means True
  
      long sum = num1 + num2;
  
      String s = ((Long)sum).toString();
      if(!str.startsWith(s)) return false;      // checking the sum with .startsWith() function
  
      return isAdditive(str.substring(s.length()), num2, sum);   // recursively checking the rest of the string
    }
  }