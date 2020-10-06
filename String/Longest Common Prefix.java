/**
 * Write a function to find the longest common prefix string amongst an array of strings.
 * 
 * Example 1:

Input: strs = ["flower","flow","flight"]
Output: "fl"

Example 2:

Input: strs = ["dog","racecar","car"]
Output: ""

Explanation: There is no common prefix among the input strings.
 */
// 关键在于 这个 sort 

public class Solution {
    public String longestCommonPrefix(String[] strs) {
      // Write your solution here
      if(strs == null || strs.length == 0) return "";
  
      Arrays.sort(strs);
  
      String first = strs[0];
      String last = strs[strs.length -1];
      StringBuilder sb = new StringBuilder();
  
      for(int i = 0; i < first.length(); i++){
  
        if(i < last.length() && first.charAt(i) == last.charAt(i)){
          sb.append(first.charAt(i));
        } else {
          return sb.toString();
        }
      }
      return sb.toString();
    }
  }
  

  public String longestCommonPrefix(String[] strs) {
    if(strs == null || strs.length == 0)    return "";
    String pre = strs[0];
    int i = 1;
    while(i < strs.length){
        while(strs[i].indexOf(pre) != 0)
            pre = pre.substring(0,pre.length()-1);
        i++;
    }
    return pre;
}