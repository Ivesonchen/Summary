/**
 * We can scramble a string s to get a string t using the following algorithm:

If the length of the string is 1, stop.
If the length of the string is > 1, do the following:
Split the string into two non-empty substrings at a random index, i.e., if the string is s, divide it to x and y where s = x + y.
Randomly decide to swap the two substrings or to keep them in the same order. i.e., after this step, s may become s = x + y or s = y + x.
Apply step 1 recursively on each of the two substrings x and y.
Given two strings s1 and s2 of the same length, return true if s2 is a scrambled string of s1, otherwise, return false.

#1
Input: s1 = "great", s2 = "rgeat"
Output: true
Explanation: One possible scenario applied on s1 is:
    "great" --> "gr/eat" // divide at random index.
    "gr/eat" --> "gr/eat" // random decision is not to swap the two substrings and keep them in order.
    "gr/eat" --> "g/r / e/at" // apply the same algorithm recursively on both substrings. divide at ranom index each of them.
    "g/r / e/at" --> "r/g / e/at" // random decision was to swap the first substring and to keep the second substring in the same order.
    "r/g / e/at" --> "r/g / e/ a/t" // again apply the algorithm recursively, divide "at" to "a/t".
    "r/g / e/ a/t" --> "r/g / e/ a/t" // random decision is to keep both substrings in the same order.
    The algorithm stops now and the result string is "rgeat" which is s2.
    As there is one possible scenario that led s1 to be scrambled to s2, we return true.

#2
Input: s1 = "abcde", s2 = "caebd"
Output: false

#3
Input: s1 = "a", s2 = "a"
Output: true

https://leetcode-cn.com/problems/scramble-string/solution/
 */

// 关键在于读懂题意   需要比较的是  compare(s1's left part, s2's left part) && compare(s1's right part, s2's right part) 或者
//                             compare(s1's left part, s2's right part) && compare(s1's right part, s2's left part)

 // recursive most basic
public class Solution {
    public boolean isScramble(String s1, String s2) {
      // Write your solution here
      if(s1.length() != s2.length()) return false;
  
      if(s1.equals(s2)) return true;
  
      int[] letters = new int[26];
      for(int i = 0; i < s1.length(); i++){
        letters[s1.charAt(i) - 'a']++;
        letters[s2.charAt(i) - 'a']--;
      }
  
      for(int i = 0; i < 26; i++){
        if(letters[i] != 0) return false;
      }
  
      for(int i = 1; i < s1.length(); i++){
        if(isScramble(s1.substring(0, i), s2.substring(0, i)) && isScramble(s1.substring(i), s2.substring(i))) return true;
  
        if(isScramble(s1.substring(0, i), s2.substring(s2.length() - i)) && isScramble(s1.substring(i), s2.substring(0, s2.length() - i))) return true;
      }
      return false;
    }
  }

// 三维dp 

/**
 * dp[i][j][k][h] 表示 T[k..h]T[k..h] 是否由 S[i..j]S[i..j] 变来。由于变换必须长度是一样的，因此这边有个关系 j - i = h - kj−i=h−k ，
 * 可以把四维数组降成三维。dp[i][j][len]dp[i][j][len] 表示从字符串 SS 中 ii 开始长度为 lenlen 的字符串是否能变换为从字符串 TT 中 jj 开始长度为 lenlen 的字符串
 */
public boolean isScramble4(String s1, String s2) {
    if (s1.length() != s2.length()) {
        return false;
    }
    if (s1.equals(s2)) {
        return true;
    }

    int[] letters = new int[26];
    for (int i = 0; i < s1.length(); i++) {
        letters[s1.charAt(i) - 'a']++;
        letters[s2.charAt(i) - 'a']--;
    }
    for (int i = 0; i < 26; i++) {
        if (letters[i] != 0) {
            return false;
        }
    }

    int length = s1.length();
    boolean[][][] dp = new boolean[length + 1][length][length];
	//遍历所有的字符串长度
    for (int len = 1; len <= length; len++) {
        //S1 开始的地方
        for (int i = 0; i + len <= length; i++) {
            //S2 开始的地方
            for (int j = 0; j + len <= length; j++) {
                //长度是 1 无需切割
                if (len == 1) {
                    dp[len][i][j] = s1.charAt(i) == s2.charAt(j);
                } else {
                    //遍历切割后的左半部分长度
                    for (int q = 1; q < len; q++) {
                        dp[len][i][j] = dp[q][i][j] && dp[len - q][i + q][j + q]
                            || dp[q][i][j + len - q] && dp[len - q][i + q][j];
                        //如果当前是 true 就 break，防止被覆盖为 false
                        if (dp[len][i][j]) {
                            break;
                        }
                    }
                }
            }
        }
    }
    return dp[length][0][0];
}