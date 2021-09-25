/**
 * Given two binary strings, return their sum (also a binary string).

Input: a = “11”
        b = “1”

Output: “100”
 */

// Tao-Lu  注意这种双指针 while 循环的 合理使用     
// 我们可以 "或" 这种条件 使用sum 来贯穿 数组a 和 b对应数值的加和    来减少代码的冗余

public class Solution {
  public String addBinary(String a, String b) {
    // Write your solution here
    StringBuilder sb = new StringBuilder();
    char[] arrA = a.toCharArray();
    char[] arrB = b.toCharArray();

    int i = arrA.length - 1;
    int j = arrB.length - 1;
    int c = 0;
    while(i >= 0 || j >= 0){
      int newRes = c;
      if(i >= 0) {
        newRes += Integer.parseInt(String.valueOf(arrA[i]));
        i--;
      }

      if(j >= 0) {
        newRes += Integer.parseInt(String.valueOf(arrB[j]));
        j--;
      }

      sb.append(String.valueOf(newRes % 2));
      c = newRes / 2;
    }

    if(c > 0) {
      sb.append(String.valueOf(c));
    }

    return sb.reverse().toString();
  }
}