/**
 * Given a string, determine if it is a palindrome, considering only alphanumeric characters('0'-'9','a'-'z','A'-'Z') and ignoring cases.

For example,
"an apple, :) elp pana#" is a palindrome.

"dia monds dn dia" is not a palindrome.
 */
// two pointer O(n) O(1)

// Stack???

public class Solution {
    // stack
    // double pointer
    public boolean valid(String input) {
      // Write your solution here
  
      input = input.toLowerCase();
      
      int left = 0;
      int right = input.length() - 1;
  
      while(left < right){
        char leftChar = input.charAt(left);
        char rightChar = input.charAt(right);
  
        if(!Character.isLetterOrDigit(leftChar)){
          left++;
        } else if (!Character.isLetterOrDigit(rightChar)){
          right--;
        } else if (leftChar != rightChar){
          return false;
        } else {
          left++;
          right--;
        }
      }
  
      return true;
    }
  }