/**
 * A string s is called good if there are no two different characters in s that have the same frequency.

Given a string s, return the minimum number of characters you need to delete to make s good.

The frequency of a character in a string is the number of times it appears in the string. 
For example, in the string "aab", the frequency of 'a' is 2, while the frequency of 'b' is 1.
 */

class Solution {
  public int minDeletions(String s) {
    int freq[] = new int[26];
    for (char c : s.toCharArray())
      freq[c - 'a']++;
    Arrays.sort(freq);
    int keep = freq[25], prev = keep;
    for (int i = 24; i >= 0 && freq[i] != 0 && prev != 0; i--) {
      prev = Math.min(freq[i], prev - 1);
      keep += prev;
    }
    return s.length() - keep;
  }
}