/**
 * Given a string, replace adjacent, repeated characters with the character followed by the number of repeated occurrences. 
 * If the character does not has any adjacent, repeated occurrences, it is not changed.

Assumptions

The string is not null

The characters used in the original string are guaranteed to be ‘a’ - ‘z’

Examples

“abbcccdeee” → “ab2c3de3”
 */

public class Solution {
    public String compress(String input) {
      // Write your solution here
      if(input.length() <= 1) return input;
      char[] arr = input.toCharArray();
  
      int fast = 0;
      int slow = 0;
      int start = slow;
  
      while(fast <= arr.length){
        if(fast < arr.length && arr[fast] == arr[slow]){
          fast++;
        } else {
          int dis = fast - slow;
          if(dis == 1){
            arr[start] = arr[slow];
            slow = fast;
            start++;
          } else {
            arr[start] = arr[slow];
            start++;
            String disString = String.valueOf(dis);
            for(int i = 0; i < disString.length(); i++){
              arr[start + i] = disString.charAt(i);
            }
            slow = fast;
            start += disString.length();
          }
          if(fast == arr.length) break;
        }
      }
      return new String(Arrays.copyOfRange(arr, 0, start));
    }
  }

  public int compress (char[] chars) {
    int start = 0, index = 0;
    while(index < chars.length) {
        char c = chars[index];
        int counter = 0;
        while(index < chars.length && chars[index] == c) {
            index++;
            counter++;
        }
        
        chars[start++] = c;
        if(counter != 1) {
            for(char cc : Integer.toString(counter).toCharArray())
                chars[start++] = cc;
        }
        
    }
    return start;
}