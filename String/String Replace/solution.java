/**
 * Given an original string input, and two strings S and T, 
 replace all occurrences of S in input with T.

Assumptions

input, S and T are not null, S is not empty string
Examples

input = "appledogapple", S = "apple", T = "cat", input becomes "catdogcat"
input = "dodododo", S = "dod", T = "a", input becomes "aoao"
 */

 /**
  * Two solutions

1.  Use StringBuilder to avoid overhead
    iterate over the input, whenever there is a match of the source, append the target to the StringBuilder object.
    otherwise, append whatever character it is in the input
    we can utilize String's indexOf() method, but never the replace() method directly

2.  Use char[] array
    source.length() vs. target.length()
    if (source.length() >= target.length())
    Two pointers/sliding window
    if (source.length() < target.length()
    Iterate over the input, count the number of source's appearances to get the number of extra spaces needed for the output
    Populate the output array according to the input
  */

public class Solution {
    public String replace(String input, String source, String target) {
      // Write your solution here
      if(input.length()==0) return input;
  
      int fromIndex = 0;
      int matchIndex = input.indexOf(source, fromIndex);
  
      StringBuilder sb = new StringBuilder();
  
      while(matchIndex != -1){
  
        sb.append(input, fromIndex, matchIndex);
        sb.append(target);
  
        fromIndex = matchIndex + source.length();
  
        matchIndex = input.indexOf(source, fromIndex);
      }
  
      sb.append(input, fromIndex, input.length());
  
      return sb.toString();
    }
}