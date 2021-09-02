/**
 * Reverse the words in a sentence.

Assumptions

Words are separated by single space

There are no heading or tailing white spaces

Examples

“I love Google” → “Google love I”

Corner Cases

If the given string is null, we do not need to do anything.
 */

public class Solution {
    public String reverseWords(String input) {
      // Write your solution here
      int len = input.length();
      if(input == null || len == 0) return input;
  
      char[] arr = input.toCharArray();
  
      int fast = 0;
      int slow = fast;
  
      while(fast < arr.length){
        if(arr[fast] != ' '){
          fast ++;
        } else {
          // 这里就是 找到 单个单词的 情况
          reverse(arr, slow, fast - 1);
  
          fast += 1;
          slow = fast;
        }
      }
      // 翻转最后一个单词
      reverse(arr, slow, fast - 1);
  
      // 翻转整个字符串
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