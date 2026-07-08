/**
 * This is a follow up of Shortest Word Distance. The only difference is now word1 could be the same as word2.

Given a list of words and two words word1 and word2, return the shortest distance between these two words in the list.

word1 and word2 may be the same and they represent two individual words in the list.

For example,
Assume that words = ["practice", "makes", "perfect", "coding", "makes"].

Given word1 = “makes”, word2 = “coding”, return 1.
Given word1 = "makes", word2 = "makes", return 3.

Note:
You may assume word1 and word2 are both in the list.
 */

public class Solution {
    public int shortestWordDistance(String[] words, String word1, String word2) {
      // Write your solution here
      int indexWord1 = -1;
      int indexWord2 = -1;
      boolean flag = word1.equals(word2);
  
      int distance = Integer.MAX_VALUE;
      for (int i = 0; i < words.length; i++) {
          if (words[i].equals(word1)) {
              if(flag){ // 如果两个词相同   则只更新一个index       因为index2之后还会更新一次 相当于排除了之后那个word2的判断
                indexWord1 = indexWord2;
                indexWord2 = i;
              } else {
                indexWord1 = i;
              }
          }
          
          if (words[i].equals(word2)) {
              indexWord2 = i;
          }
          if (indexWord1 >= 0 && indexWord2 >= 0) {
              distance = Math.min(distance, Math.abs(indexWord2 - indexWord1));
          }
      }
      return distance;
    }
  }