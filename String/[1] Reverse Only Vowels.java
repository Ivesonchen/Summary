/**
 * Only reverse the vowels('a', 'e', 'i', 'o', 'u') in a given string, the other characters should not be moved or changed.

Assumptions:

The given string is not null, and only contains lower case letters.
Examples:

"abbegi" --> "ibbega"
 */

public class Solution {
    public String reverse(String input) {
      // Write your solution here
      int len = input.length();
      if(len == 0) return input;
      int l = 0;
      int r = len - 1;
      char[] arr = input.toCharArray();
      Set<Character> set = new HashSet<>();
      set.add('a');
      set.add('e');
      set.add('i');
      set.add('o');
      set.add('u');
  
      while(l < r){
        while(l < r && !set.contains(arr[l]) ){
          l++;
        }
        while(r > l && !set.contains(arr[r]) ){
          r--;
        }
  
        swap(arr, l, r);
        l++;
        r--;
      }
  
      return new String(arr);
    }
  
    public void swap(char[] arr, int left, int right){
      char temp = arr[left];
      arr[left] = arr[right];
      arr[right] = temp;
    }
  }
  