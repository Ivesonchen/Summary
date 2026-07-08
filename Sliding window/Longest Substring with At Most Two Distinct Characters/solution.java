/*
Given a string s , find the length of the longest substring t that contains at most 2 distinct characters.
Example 1:
Input: "eceba"
Output: 3
Explanation: t is "ece" which its length is 3.
Example 2:
Input: "ccaabbb"
Output: 5
Explanation: t is "aabbb" which its length is 5.
*/

class Solution {
    public int lengthOfLongestSubstringTwoDistinct(String s) {
        if (s == null || s.length() == 0) return 0;
        int n = s.length();
        Map<Character, Integer> lastOccurMap = new HashMap<>();  //正是由于这个 才能确切的每次从左边删除一个 字符
        int left = 0, right = 0, max = 0;

        while (right < n) {
            if (lastOccurMap.size() <= 2) { // add new char
                lastOccurMap.put(s.charAt(right), right++);
            }
            if (lastOccurMap.size() > 2) { // clean up left-most char
                int leftMost = right;
                for (int index : lastOccurMap.values()) {
                    leftMost = Math.min(leftMost, index);
                }
                lastOccurMap.remove(s.charAt(leftMost));
                left = leftMost + 1;
            }

            max = Math.max(max, right - left); //不停地计算 最远距离
        }

        return max;
    }
}

/**
 * #### Two Pointer + HashMap
- 原本想用 DP, 但是其实用 sliding window 的思想
- sliding window 的切割: 用hashmap 存 last occurrance of char index; 
- map.remove(c) 之后, 就等于彻底切掉了那一段; 那么 map.get(c) + 1 也就是新的 left window border
 */

 /**
  * /*
Thoughts:
Two pointer, use HashMap to record the passed <char, count>.
If map.size() == k, check string length.
If map.size() > k, start moving front index i until map.size() reduces [while]
*/
//另一种使用 hashmap 时并不记录 last occurance 而是 想办法把 start 标记处的 字符 在 hashmap中 清除数据