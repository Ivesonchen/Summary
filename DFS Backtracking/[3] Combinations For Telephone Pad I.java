/**
 * Given a telephone keypad, and an int number, print all words which are possible by pressing these numbers, the output strings should be sorted.

{0 : "", 1 : "", 2 : "abc", 3 : "def", 4 : "ghi", 5 : "jkl", 6 : "mno", 7 : "pqrs", 8 : "tuv", 9 : "wxyz"} 

Assumptions:

The given number >= 0
Examples:

if input number is 231, possible words which can be formed are:

[ad, ae, af, bd, be, bf, cd, ce, cf]
 */
public class Solution {
    public String[] combinations(int number) {
      // Write your solution here
      String[] digitLetter = {"", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"};
  
      List<String> res = new ArrayList<>();
      List<Integer> num = new ArrayList<>();
  
      while(number != 0){
        num.add(0, number % 10);
        number /= 10;
      }
  
      dfs(res, num, digitLetter, 0, new StringBuilder());
      //.....
      String[] resStr = new String[res.size()];
      for(int i = 0; i < res.size(); i++){
        resStr[i] = res.get(i);
      }
      //.....
      return resStr;
    }
  
    public void dfs(List<String> res, List<Integer> num, String[] digitLetter, int depth, StringBuilder sb){
      if(depth == num.size()){
        res.add(sb.toString());
        return;
      }
  
      int digit = num.get(depth);
      char[] letters = digitLetter[digit].toCharArray();
  
      if(letters.length == 0) dfs(res, num, digitLetter, depth + 1, sb); 
      // In order to deal with this situation "231". if we get digitLetter length == 0
      // in the last press, we wont reach to the end of if(depth == num.size()) condition
  
      for(int i = 0; i < letters.length; i++){
        sb.append(letters[i]);
        dfs(res, num, digitLetter, depth + 1, sb);
        sb.deleteCharAt(sb.length() - 1);
      }
    }
  }