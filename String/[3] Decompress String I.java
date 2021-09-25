/**
 * Given a string in compressed form, decompress it to the original string. 
 The adjacent repeated characters in the original string are compressed to have the character followed by the number of repeated occurrences. 
 If the character does not have any adjacent repeated occurrences, it is not compressed.

Assumptions

The string is not null

The characters used in the original string are guaranteed to be ‘a’ - ‘z’

There are no adjacent repeated characters with length > 9

Examples

“acb2c4” → “acbbcccc”
 */

 /**
  * 从后往前跑  有数字的话从后往前比较方便计算
  */
public class Solution {
    public String decompress(String input) {
      // Write your solution here
      if(input.length() <= 1) return input;
      StringBuilder sb = new StringBuilder();
      char[] arr = input.toCharArray();
  
      int num = 0;
      int pos = 0;
  
      for(int i = arr.length - 1; i >= 0; i--){
        char cur = arr[i];
        if(Character.isDigit(cur)){
          // update num;
          num += (cur - '0') * Math.pow(10, pos); 
        } else {
          if(num == 0){
            sb.append(cur);
          } else {
            for(int j = 0; j < num; j++){
              sb.append(cur);
            }
          }
          num = 0;
          pos = 0;
        }
      }
  
      return sb.reverse().toString();
    }
  }
  