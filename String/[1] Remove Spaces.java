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
          if(charArray[i - 1] != ' '){    // 遇到第一个空格
            charArray[start] = charArray[i];
            start++;
            i++;
          } else {
            i++;
          }
        }
      }
      
      if(start > 1 && charArray[start - 1] == ' '){    // 结尾是空格
        start --;
      }
  
      char[] res = Arrays.copyOfRange(charArray, 0, start);
  
      return new String(res);
    }

    public String removeSpaces(String input) {
      char[] arr = input.toCharArray();
      int slow = 0;
      int fast = 0;
      int word_count = 0;
  
      while(true){
        while(fast < arr.length && arr[fast] == ' '){
          fast++;                                         //1: skip all leading ' ' in front of each word
        }
  
        if(fast == arr.length){
          break;
        }
        if(word_count > 0){
          arr[slow++] = ' ';                              //2: add ' ' in front of (2nd +) word
        }
  
        while(fast < arr.length && arr[fast] != ' '){
          arr[slow++] = arr[fast++];                      //3. copy a word
        }
        word_count++;
      }
  
      return new String(Arrays.copyOfRange(arr, 0, slow));
    }
  }
  