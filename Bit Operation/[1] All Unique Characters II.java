/**
 * Determine if the characters of a given string are all unique.

Assumptions

We are using ASCII charset, the value of valid characters are from 0 to 255
The given string is not null
Examples

all the characters in "abA+\8" are unique
"abA+\a88" contains duplicate characters
 */

public class Solution {
    public boolean allUnique(String word) {
        // Write your solution here
        int[] arr = new int[256];

        for(char c : word.toCharArray()){
            if(arr[c] != 0) return false;
            arr[c] += 1;
        }

        return true;
    }
}

public class Solution {
    public boolean allUnique(String word) {
      // Write your solution here
      int[] dic = new int[8];
  
      for(char c : word.toCharArray()){
        int pos = (int) c;
  
        int row = pos / 32;
        int col = pos % 32;
  
        if((dic[row] >> col & 1) == 1){
          return false;
        } else {
          dic[row] = dic[row] | (1 << col);
        }
      }
      return true;
    }
  }