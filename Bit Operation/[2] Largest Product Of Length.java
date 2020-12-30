/**
 * Given a dictionary containing many words, find the largest product of two words’ lengths, 
 * such that the two words do not share any common characters.

Assumptions

The words only contains characters of 'a' to 'z'
The dictionary is not null and does not contains null string, and has at least two strings
If there is no such pair of words, just return 0
Examples

dictionary = [“abcde”, “abcd”, “ade”, “xy”], the largest product is 5 * 2 = 10 (by choosing “abcde” and “xy”)

https://github.com/grandyang/leetcode/issues/318
https://leetcode.com/problems/maximum-product-of-word-lengths/discuss/76959/JAVA-Easy-Version-To-Understand!!!!!!!!!!!!!!!!!
 */

 /**
  * 这道题给我们了一个单词数组，让我们求两个没有相同字母的单词的长度之积的最大值。
  我开始想的方法是每两个单词先比较，如果没有相同字母，则计算其长度之积，然后每次更新结果就能找到最大值。
  但是我开始想的两个单词比较的方法是利用哈希表先将一个单词的所有出现的字母存入哈希表，然后检查另一个单词的各个字母是否在哈希表出现过，
  若都没出现过，则说明两个单词没有相同字母，则计算两个单词长度之积并更新结果。但是这种判断方法无法通过OJ的大数据集，
  上网搜大神们的解法，都是用了mask，因为题目中说都是小写字母，那么只有26位，一个整型数int有32位，
  我们可以用后26位来对应26个字母，若为1，说明该对应位置的字母出现过，那么每个单词的都可由一个int数字表示，
  两个单词没有共同字母的条件是这两个int数想与为0，用这个判断方法可以通过OJ，
  */

public class Solution {
    public int largestProduct(String[] dict) {
      // Write your solution here
      if(dict == null || dict.length == 0) return 0;
      int len = dict.length;
      int[] value = new int[len];
  
      for(int i = 0; i < len; i ++){
        String temp = dict[i];
        value[i] = 0;
        for(int j = 0; j < temp.length(); j++){
          value[i] |= 1 << (temp.charAt(j) - 'a');
        }
      }
  
      int maxProduct = 0;
      for(int i = 0; i < len; i++){
        for(int j = i + 1; j < len; j++){
          if((value[i] & value[j]) == 0 && (dict[i].length() * dict[j].length() > maxProduct)){
            maxProduct = dict[i].length() * dict[j].length();
          }
        }
      }
      return maxProduct;
    }
}