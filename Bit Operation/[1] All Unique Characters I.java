/**
 * Determine if the characters of a given string are all unique.

Assumptions

The only set of possible characters used in the string are 'a' - 'z', the 26 lower case letters.
The given string is not null.
Examples

the characters used in "abcd" are unique
the characters used in "aba" are not unique
 */

public class Solution {
    public boolean allUnique(String word) {
      // Write your solution here
      int[] arr = new int[26];
  
      for(char c : word.toCharArray()){
        if(arr[c - 'a'] != 0) return false;
        arr[c - 'a'] += 1;
      }
  
      return true;
    }
  }

  public class Solution {
    public boolean allUnique(String word) {
        // Write your solution here
        int dic = 0;
        for(char c : word.toCharArray()){
            int pos = c - 'a';

            if((dic >> pos & 1) == 1){
                return false;
            } else {
                dic = dic | (1 << pos);
            }
        }

        return true;
    }
}