/**
 * Given a string, return the shortest contiguous substring that contains exactly k type of characters.

Return an empty string if there does not exist such substring.

Assumptions:

The given string is not null.
k >= 0.
Examples:

input = "aabcc", k = 3, output = "abc".
input = "aabbbcccc", k = 3, output = "abbbc".
input = "aabcc", k = 4, output = "".
 */

 /**
  * sliding window 紧缩start  注意不同的情况下合适的紧缩    map.size > k ? 紧缩是为了删除头上多余的一类字符
                                                       map.size == k ? 紧缩是为了删除头上多余的同类(k type包含)字符
  */

public class Solution {
    public String shortest(String input, int k) {
      // Write your solution here
        if(k == 0) return "";
        int len = input.length();
        HashMap<Character, Integer> map = new HashMap<>();
        String res = "";
  
        int start = 0, min = Integer.MAX_VALUE;
        for(int i = 0; i < len; i++){
            char c = input.charAt(i);
            map.put(c, map.getOrDefault(c, 0) + 1);
            while(map.size() > k){
              int count = map.get(input.charAt(start));
              if(count <= 1){
                map.remove(input.charAt(start));
              } else {
                map.put(input.charAt(start), count - 1);
              }
              start ++;
            }
  
            while(map.size() == k) {
              if(i - start + 1 < min){
                min = i - start + 1;
                res = input.substring(start, i + 1);
              }
              int count = map.get(input.charAt(start));
              if(count <= 1){
                map.remove(input.charAt(start));
              } else {
                map.put(input.charAt(start), count - 1);
              }
              start ++;
            }
        }
  
        return res;
    }
  }