/**
 * Given a non-negative number represented as an array of digits, plus one to the number.

Input: [2, 5, 9]

Output: [2, 6, 0]
 */

public class Solution {
    public int[] plus(int[] digits) {
      // Write your solution here
      int ext = 1;
      for(int i = digits.length - 1; i >= 0; i--){
        int sum = digits[i] + ext;
  
        digits[i] = sum % 10;
        ext = sum / 10;
      } //末位加一  ext 处理进位问题         到这里是结果长度和 原来的长度一样  （没有溢出进位）
  
      // 如果还要往前再进位    ext 放新结果第一位   后面的直接拷贝
      if(ext != 0) {
        int[] newRes = new int[digits.length + 1];
  
        newRes[0] = ext;
        for(int i = 0; i < digits.length; i ++){
          newRes[i + 1] = digits[i];
        }
  
        return newRes;
      }
  
      return digits;
    }
  }
  