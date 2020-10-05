/**
 * Given a string, remove all leading/trailing/duplicated empty spaces.

Assumptions:

The given string is not null.
Examples:

“  a” --> “a”
“   I     love MTV ” --> “I love MTV”
 */

public class Solution {
    public String removeSpaces(String input) {
      // Write your solution here
  
      char[] charArray = input.toCharArray();
      int start = 0;
      int i = 0;
      while(i < charArray.length){
        if(start == 0 && charArray[i] == ' '){
          i++;
          continue;
        }
  
        if(charArray[i] != ' '){
          charArray[start] = charArray[i];
          start++;
          i++;
        } else {
          if(charArray[i - 1] != ' '){
            charArray[start] = charArray[i];
            start++;
            i++;
          } else {
            i++;
          }
        }
      }
      
      if(start > 1 && charArray[start - 1] == ' '){
        start --;
      }
  
      char[] res = Arrays.copyOfRange(charArray, 0, start);
  
      return new String(res);
    }
  }
  