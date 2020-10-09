/**
 * Given an array of words and a length L, format the text such that each line has exactly L characters and is fully (left and right) justified. For the last line, it should be left justified and no extra space is inserted between words.

Input: words: ["This", "is", "an", "example", "of", "text", "justification."]    L: 16.

Output:[

          "This    is    an",

            "example  of text",

            "justification.  "

           ]

    

Input: words: [“This”, “is”, “my”]       L = 5

    Output: [

         “This ”,

         “is my”    

        ]    
 */

public class Solution {
    public ArrayList<String> fullJustify(String[] words, int L) {
      //Input your code here
      int left = 0;
      ArrayList<String> result = new ArrayList<>();
  
      while(left < words.length){
        int right = findRight(left, words, L);
        result.add(justify(left, right, words, L));
        left = right + 1;
      }
  
      return result;
    }
  
    // 找到一句话的右边界
    private int findRight(int left, String[] words, int maxWidth){
      int right = left;
      int sum = words[right++].length(); // init value
  
      while(right < words.length && (sum + 1 + words[right].length()) <= maxWidth){
        // add another word behind and length is not overflow
        sum += 1 + words[right++].length();
      }
      return right - 1;
    }
  
    // 有了 左边界和右边界 需要调整一句话中的 空格
    private String justify(int left, int right, String[] words, int maxWidth){
      if(right - left == 0) return padResult(words[left], maxWidth);
  
      boolean isLastLine = right == words.length - 1;
      int numSpaces = right - left;
      int totalSpace = maxWidth - wordsLength(left, right, words);
  
      String space = isLastLine ? " " : blank(totalSpace / numSpaces);
      int remainder = isLastLine ? 0 : totalSpace % numSpaces;
  
      StringBuilder result = new StringBuilder();
      for(int i = left; i <= right; i++){
        result.append(words[i])
              .append(space)
              .append(remainder-- > 0 ? " " : "");
      }
  
      return padResult(result.toString().trim(), maxWidth);
    }
  
    private int wordsLength(int left, int right, String[] words){
      int wordsLength = 0;
      for(int i = left; i <= right; i++){
        wordsLength += words[i].length();
      }
      return wordsLength;
    }
  
    private String padResult(String result, int maxWidth){
      return result + blank(maxWidth - result.length());
    }
  
    private String blank(int length){
      return new String(new char[length]).replace('\0', ' ');
    }
  }