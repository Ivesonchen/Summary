/**
 * Given a string containing just the characters '(' and ')', find the length of the longest valid (well-formed) parentheses substring.  

Example

")()())", where the longest valid parentheses substring is "()()", which has length = 4.
 */


public class Solution {
    public int longestValidParentheses(String input) {
      // Write your solution here
  
      Stack<Integer> stack = new Stack<>();
  
      int maxLen = 0, start = -1;
  
        for(int i = 0; i < input.length(); i++){
            if(input.charAt(i) == '('){
            stack.push(i);
            } else {
                if(stack.isEmpty()){
                    start = i;
                } else {
                    stack.pop();
        
                    if(stack.isEmpty()){
                    maxLen = Math.max(maxLen, i - start);
                    } else {
                    maxLen = Math.max(maxLen, i - stack.peek());
                    }
                }
            }
        }
      
      return maxLen;
    }
}

  //Dynamic Programming, One Pass
// Longest Valid Parenthese
// 两遍扫描，时间复杂度O(n)，空间复杂度O(1)
class Solution {
    public int longestValidParentheses(final String s) {
        int result = 0, depth = 0, start = -1;
        for (int i = 0; i < s.length(); ++i) {
            if (s.charAt(i) == '(') {
                ++depth;
            } else {
                --depth;
                if (depth < 0) {
                    start = i;
                    depth = 0;
                } else if (depth == 0) {
                    result = Math.max(result, i - start);
                }
            }
        }

        depth = 0;
        start = s.length();
        for (int i = s.length() - 1; i >= 0; --i) {
            if (s.charAt(i) == ')') {
                ++depth;
            } else {
                --depth;
                if (depth < 0) {
                    start = i;
                    depth = 0;
                } else if (depth == 0) {
                    result = Math.max(result, start - i);
                }
            }
        }
        return result;
    }
}