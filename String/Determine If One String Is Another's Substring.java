/**
 * Determine if a small string is a substring of another large string.

Return the index of the first occurrence of the small string in the large string.

Return -1 if the small string is not a substring of the large string.

Assumptions

Both large and small are not null
If small is empty string, return 0
Examples

“ab” is a substring of “bcabc”, return 2
“bcd” is not a substring of “bcabc”, return -1
"" is substring of "abc", return 0
 */

// O(m * n)  合适的移动指针就好
public class Solution {
    public int strstr(String large, String small) {
      // Write your solution here
      if(small.length() == 0) return 0;
      if(large.length() < small.length()) return -1;
  
      int i = 0;
      int j = 0;
      int start = 0;
  
      while (i < large.length()){
        if(large.charAt(i) != small.charAt(j)){
          if(j != 0){
            i = start + 1;
            j = 0;
          } else {
            i++;
            // j++;
          }
        } else {
          if(j == 0) start = i;
          i++;
          j++;
        }
  
        if(j == small.length()) return start;
      }
  
      return -1;
    }
  }