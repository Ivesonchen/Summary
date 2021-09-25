/**
 * Right shift a given string by n characters.

Assumptions

The given string is not null.
n >= 0.
Examples

"abc", 4 -> "cab"
 */


 /**
 
    a b c             c a b
    0 1 2             
 
  */
// Tao-Lu
public class Solution {
  public String rightShift(String input, int n) {
    // Write your solution here
    int len = input.length();
    if(len == 0) return input;
    int shift = n % len;

    char[] arr = input.toCharArray();

    reverse(arr, 0, len - 1 - shift);
    reverse(arr, len - shift, len - 1);
    reverse(arr, 0, len - 1);

    return new String(arr);
  }

  public void reverse(char[] arr, int left, int right){
    while(left < right){
      char temp = arr[left];
      arr[left] = arr[right];
      arr[right] = temp;
      left++;
      right--;
    }
  }
}
  