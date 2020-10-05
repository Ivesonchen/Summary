/**
 * Reverse the words in a sentence and truncate all heading/trailing/duplicate space characters.

Examples

“ I  love  Google  ” → “Google love I”

Corner Cases

If the given string is null, we do not need to do anything.
 */

public class Solution {
    public String reverseWords(String input) {
      // Write your solution here
  
      int len = input.length();
      if(input == null || len == 0) return input;
      char[] arr = input.toCharArray();
      int end = truncate(arr);
  
      reverse(arr, end - 1);
      
      return new String(Arrays.copyOfRange(arr, 0, end));
    }
  
    public int truncate(char[] arr){
      int fast = 0;
      int slow = 0;
      int len = arr.length;
  
      while(fast < len){
        if(slow == 0 && arr[fast] == ' '){
          fast++;
          continue;
        }
  
        if(arr[fast] != ' '){
          arr[slow++] = arr[fast++];
        } else {
          if(arr[fast - 1] == ' '){
            fast++;
          } else {
            arr[slow++] = arr[fast++];
          }
        }
      }
      if(arr[slow - 1] == ' ') slow--;
  
      return slow;
    }
  
    public void reverse(char[] arr, int end){
      int fast = 0;
      int slow = 0;
  
      while(fast <= end){
        if(arr[fast] == ' '){
          swap(arr, slow, fast - 1);
  
          fast++;
          slow = fast;
        } else {
          fast ++;
        }
      }
  
      swap(arr, slow, fast - 1);
      swap(arr, 0, end);
    }
  
    public void swap(char[] arr, int left, int right){
      while(left < right){
        char temp = arr[left];
        arr[left] = arr[right];
        arr[right] = temp;
        left ++;
        right --;
      }
    }
  }
  