/**
 * In URL encoding, whenever we see an space " ", we need to replace it with "20%". Provide a method that performs this encoding for a given string.

Examples

"google/q?flo wer market" → "google/q?flo20%wer20%market"
Corner Cases

If the given string is null, we do not need to do anything.
 */
//?? not sure

public class Solution {
    public String encode(String input) {
      // Write your solution here
      if(input == null || input.length() == 0) return input;
  
      StringBuilder sb = new StringBuilder();
  
      for(int i = 0; i < input.length(); i++){
        char cha = input.charAt(i);
  
        if(cha != ' '){
          sb.append(String.valueOf(cha));
        } else {
          sb.append("20%");
        }
      }
      return sb.toString();
    }
  }
  