/**
 * Given two strings s and t, write a function to determine if t is an anagram of s.

For example,
s = "anagram", t = "nagaram", return true.
s = "rat", t = "car", return false.

Note:
You may assume the string contains only lowercase alphabets.

Follow up:
What if the inputs contain unicode characters? How would you adapt your solution to such case?
 */

 // 使用 hashmap 计数器

public class Solution {
    public boolean isAnagram(String source, String target) {
      // Write your solution here
      if(source.length() != target.length()) return false;
  
      Map<Character, Integer> map = new HashMap<>();
  
      for(int i = 0; i < source.length(); i++){
        map.put(source.charAt(i), map.getOrDefault(source.charAt(i), 0) + 1);
      }
  
      for(int i = 0; i < target.length(); i++){
        map.put(target.charAt(i), map.getOrDefault(target.charAt(i), 2) - 1);
      }
  
      for(Map.Entry<Character, Integer> entry : map.entrySet()){
        if(entry.getValue() != 0){
          return false;
        }
      }
  
      return true;
    }
  }