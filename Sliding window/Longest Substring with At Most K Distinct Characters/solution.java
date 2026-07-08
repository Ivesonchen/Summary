/*
Given a string s, find the length of the longest substring T that contains at most k distinct characters.
Example
For example, Given s = "eceba", k = 3,
T is "eceb" which its length is 4.
Challenge
O(n), n is the size of the string s.
Tags Expand
String Two Pointers LintCode Copyright Hash Table
*/

// Better coding way    k 可以换成 2
public int lengthOfLongestSubstringKDistinct(String input, int k) {
    // Write your solution here
    String s = input;
    if (s == null || s.length() == 0) return 0;
    int n = s.length();
    Map<Character, Integer> lastOccurMap = new HashMap<>();  //正是由于这个 才能确切的每次从左边删除一个 字符
    int left = 0, res = 0;

    for(int i = 0; i < s.length(); i++){
        lastOccurMap.put(s.charAt(i), i);
        while(lastOccurMap.size() > k){
            if(lastOccurMap.get(s.charAt(left)) == left) {
                lastOccurMap.remove(s.charAt(left));
            }
            left ++;
        }
        res = Math.max(res, i - left + 1);
    }

    return res;
}

class Solution {
    public int lengthOfLongestSubstringTwoDistinct(String s, int k) {
        if (s == null || s.length() == 0) return 0;
        int n = s.length();
        Map<Character, Integer> lastOccurMap = new HashMap<>();  //正是由于这个 才能确切的每次从左边删除一个 字符
        int left = 0, right = 0, max = 0;

        while (right < n) {
            if (lastOccurMap.size() <= k) { // add new char
                lastOccurMap.put(s.charAt(right), right++);
            }
            if (lastOccurMap.size() > k) { // clean up left-most char
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