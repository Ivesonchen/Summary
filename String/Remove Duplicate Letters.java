/**
 * Given a string which contains only lowercase letters, remove duplicate letters so that every letter appear once and only once. You must make sure your result is the smallest in lexicographical order among all possible results.

Example:

Given "bcabc"
Return "abc"

Given "cbacdcbc"
Return "acdb"

https://leetcode.com/problems/remove-duplicate-letters/
 */

public class Solution {
    public String removeDuplicateLetters(String s) {

      Stack<Character> stack = new Stack<>();

      // this lets us keep track of what's in our solution in O(1) time
      HashSet<Character> seen = new HashSet<>();

      // this will let us know if there are any more instances of s[i] left in s
      HashMap<Character, Integer> last_occurrence = new HashMap<>();
      for(int i = 0; i < s.length(); i++) last_occurrence.put(s.charAt(i), i);

      for(int i = 0; i < s.length(); i++){
          char c = s.charAt(i);
          // we can only try to add c if it's not already in our solution
          // this is to maintain only one of each character
          if (!seen.contains(c)){
              // if the last letter in our solution:
              //     1. exists
              //     2. is greater than c so removing it will make the string smaller
              //     3. it's not the last occurrence
              // we remove it from the solution to keep the solution optimal
              while(!stack.isEmpty() && c < stack.peek() && last_occurrence.get(stack.peek()) > i){
                  seen.remove(stack.pop());
              }
              seen.add(c);
              stack.push(c);
          }
      }
      StringBuilder sb = new StringBuilder(stack.size());
      for (Character c : stack) sb.append(c.charValue());
      return sb.toString();
  }
}


public class Solution {
    public String removeDuplicateLetters(String s) {
        // find pos - the index of the leftmost letter in our solution
        // we create a counter and end the iteration once the suffix doesn't have each unique character
        // pos will be the index of the smallest character we encounter before the iteration ends
        int[] cnt = new int[26];
        int pos = 0;
        for (int i = 0; i < s.length(); i++) cnt[s.charAt(i) - 'a']++;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) < s.charAt(pos)) pos = i;
            if (--cnt[s.charAt(i) - 'a'] == 0) break;
        }
        // our answer is the leftmost letter plus the recursive call on the remainder of the string
        // note that we have to get rid of further occurrences of s[pos] to ensure that there are no duplicates
        return s.length() == 0 ? "" : s.charAt(pos) + removeDuplicateLetters(s.substring(pos + 1).replaceAll("" + s.charAt(pos), ""));
    }
}