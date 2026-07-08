/**
 * Given a list of unique words, find all pairs of distinct indices (i, j) in the given list, 
 so that the concatenation of the two words, i.e. words[i] + words[j] is a palindrome.

Example 1:
Given words = ["bat", "tab", "cat"]
Return [[0, 1], [1, 0]]
The palindromes are ["battab", "tabbat"]

Example 2:
Given words = ["abcd", "dcba", "lls", "s", "sssll"]
Return [[0, 1], [1, 0], [3, 2], [2, 4]]
The palindromes are ["dcbaabcd", "abcddcba", "slls", "llssssll"]
 */

/**
There are several cases to be considered that isPalindrome(s1 + s2):

Case 1: If s1 is a blank string, then for any string that is palindrome s2, s1+s2 and s2+s1 are palindrome.
//  easy understanding

Case 2: If s2 is the reversing string of s1, then s1+s2 and s2+s1 are palindrome.
//  abc + cba

Case 3: If s1[0:cut] is palindrome and there exists s2 is the reversing string of s1[cut+1:] , then s2+s1 is palindrome.
//  s1: abba|abc      s2: cba               s2 + s1 = cba|abba|abc  

Case 4: Similiar to case3. If s1[cut+1: ] is palindrome and there exists s2 is the reversing string of s1[0:cut] , then s1+s2 is palindrome.
//  s1: abc|abba      s2: cba               s1 + s2 = abc|abba|cba

To make the search faster, build a HashMap to store the String-idx pairs.
*/


public class Solution {
    public List<List<Integer>> palindromePairs(String[] words) {
      // Write your solution here
      List<List<Integer>> res = new ArrayList<List<Integer>>();
      if(words == null || words.length == 0){
        return res;
      }
  
      //build the map save the key-val pairs: String - idx
      HashMap<String, Integer> map = new HashMap<>();
      for(int i = 0; i < words.length; i++){
        map.put(words[i], i);
      }
      //special cases: "" can be combine with any palindrome string
      if(map.containsKey("")){
        int blankIdx = map.get("");
        for(int i = 0; i < words.length; i++){
          if(isPalindrome(words[i])){
            if(i == blankIdx) continue;
            res.add(Arrays.asList(blankIdx, i));
            res.add(Arrays.asList(i, blankIdx));
          }
        }
      }
      //find all string and reverse string pairs
      for(int i = 0; i < words.length; i++){
        String cur_r = reverseStr(words[i]);
        if(map.containsKey(cur_r)){
          int found = map.get(cur_r);
          if(found == i) continue;
          res.add(Arrays.asList(i, found));
        }
      }
  
      //find the pair s1, s2 that 
      //case1 : s1[0:cut] is palindrome and s1[cut+1:] = reverse(s2) => (s2, s1)
      //case2 : s1[cut+1:] is palindrome and s1[0:cut] = reverse(s2) => (s1, s2)
      for(int i = 0; i < words.length; i++){
        String cur = words[i];
        for(int cut = 1; cut < cur.length(); cut++){
          if(isPalindrome(cur.substring(0, cut))){
            String cut_r = reverseStr(cur.substring(cut));
            if(map.containsKey(cut_r)){
              int found = map.get(cut_r);
              if(found == i) continue;
              res.add(Arrays.asList(found, i));
            }
          }
  
          if(isPalindrome(cur.substring(cut))){
            String cut_r = reverseStr(cur.substring(0, cut));
            if(map.containsKey(cut_r)){
              int found = map.get(cut_r);
              if(found == i) continue;
              res.add(Arrays.asList(i, found));
            }
          }
        }
      }
  
      return res;
    }
  
    private String reverseStr(String str){
      StringBuilder sb = new StringBuilder(str);
      return sb.reverse().toString();
    }
  
    private boolean isPalindrome(String s){
      int i = 0;
      int j = s.length() - 1;
      while(i <= j){
        if(s.charAt(i) != s.charAt(j)){
          return false;
        }
        i++;
        j--;
      }
      return true;
    }
  }
  