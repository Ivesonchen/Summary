/**
 * Given a string, return the longest contiguous substring that contains exactly k type of characters.

Return null if there does not exist such substring.

Assumptions:

The given string is not null and guaranteed to have at least k different characters.
k > 0.
Examples:

input = "aabcc", k = 3, output = "aabcc".
input = "aabcccc", k = 2, output = "bcccc".
 */
/**
 * hashmap 可以 记录 last occur position     也可以记录 出现的次数
 */
 //Much better
public class Solution {
    public String longest(String input, int k) {
      // Write your solution here
      int len = input.length();
  
      Map<Character, Integer> lastOccurMap = new HashMap<>();  //正是由于这个 才能确切的每次从左边删除一个 字符
      int left = 0, res = 0;
      String str = "";
  
      for(int i = 0; i < len; i++){
          lastOccurMap.put(input.charAt(i), i);
          while(lastOccurMap.size() > k){
              if(lastOccurMap.get(input.charAt(left)) == left) {
                  lastOccurMap.remove(input.charAt(left));
              }
              left ++;
          }
  
          if(lastOccurMap.size() == k){
            if(i - left + 1 > res){
              res = i - left + 1;
              str = input.substring(left, i + 1);
            }
          }
          // res = Math.max(res, i - left + 1);
      }
  
      return str;
    }
  }

public class Solution {
    public String longest(String input, int k) {
      // Write your solution here
      int len = input.length();
      HashMap<Character, Integer> map = new HashMap<>();  // <字符, 出现次数>
      String res = "";
      int length = 0;
      int start = 0;
  
      for(int i = 0; i < len; i++){
        char c = input.charAt(i);
        map.put(c, map.getOrDefault(c, 0) + 1);
  
        while(map.size() > k){
          // remove charAt(i - k)
          char preChar = input.charAt(start);
          int count = map.get(preChar);
          if(count <= 1){
            map.remove(preChar);
          } else {
            map.put(preChar, count - 1);
          }
          start ++;
        }
        if(map.size() == k){
          if(i - start + 1 > length){
            res = input.substring(start, i + 1);
            length = i - start + 1;
          }
        }
      }
      return res;
    }
}