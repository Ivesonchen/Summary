/**
 * Given two numbers represented as strings, return multiplication of the numbers as a string. 
 * The numbers can be arbitrarily large and are non-negative.

Example

Input: "19", "20"

Output: "380"
 */

 /**            需要画一画 理解 i 和 j 所指的位置
  *                 1 2
                    1 3
                  -------
                    3 6
                  1 2
  */

public class Solution {
    public String multiply(String num1, String num2) {
      // Write your solution here
      int len1 = num1.length();
      int len2 = num2.length();
      StringBuilder sb = new StringBuilder();
  
      int[] values = new int[len1 + len2];
  
      for(int i = len1 - 1; i >= 0; i--){
        for(int j = len2 - 1; j >= 0; j--){
          int mul = (num1.charAt(i) - '0') * (num2.charAt(j) - '0');
          int p1 = i + j, p2 = i + j + 1, sum = mul + values[p2];       // p2 代表着 后一位
          values[p1] += sum / 10;
          values[p2] = sum % 10;
        }
      }
  
      for(int val : values){
         if(sb.length() == 0 && val == 0) continue;  // 跳过leading ‘0’
         sb.append("" + val);
      }
  
      return sb.length() == 0 ? "0" : sb.toString();
    }
  }