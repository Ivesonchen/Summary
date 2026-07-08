/**
 * Remove given characters in input string, the relative order of other characters should be remained. 
 Return the new string after deletion.

Assumptions

The given input string is not null.
The characters to be removed is given by another string, it is guaranteed to be not null.
Examples

input = "abcd", t = "ab", delete all instances of 'a' and 'b', the result is "cd".
 */

public class Solution {

    public String remove(String input, String t) {
      // Write your solution here
  
      if(t.length() == 0) return input;
  
      Set<Character> set = new HashSet<>();
      for(int i = 0; i < t.length(); i++){
        set.add(t.charAt(i));
      }
  
      int start = 0;
      char[] arr = input.toCharArray();
  
      for(int i = 0; i < arr.length; i++){
        if(!set.contains(arr[i])){
          arr[start++] = arr[i];
        }
      }
  
      return new String(arr, 0, start);
    }
}
/**
 *      用来完全匹配 t 整个字符串
        public String remove(String input, String t) {
          // Write your solution here

          if(t.length() == 0) return input;

          int start = 0;
          int j = 0;
          char[] arr = input.toCharArray();
          int prev = 0;

          for(int i = 0; i < arr.length; i++){
              arr[start] = arr[i];
              start ++;

              if(arr[i] == t.charAt(j)){
              if(j == 0) prev = start - 1;
                j++;
              } else {
                j = 0;
              }
          
              if(j == t.length()) {
                start = prev;
                j = 0;
              }
          }

          return new String(arr, 0, start);
        }
 */
  