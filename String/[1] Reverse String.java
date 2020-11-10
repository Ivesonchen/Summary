/**
 * Reverse a given string.

Assumptions

The given string is not null.
*/


public class Solution {
    public String reverse(String input) {
      // Write your solution here
      int len = input.length();
      if(len == 0) return input;
  
      char[] arr = input.toCharArray();
      int left = 0;
      int right = len - 1;
  
      while(left < right){
        char temp = arr[left];
        arr[left] = arr[right];
        arr[right] = temp;
        left++;
        right--;
      }
  
      return new String(arr);
    }
  }