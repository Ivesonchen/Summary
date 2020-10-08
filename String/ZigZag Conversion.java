/**
 * 
 * The string "PAYPALISHIRING" is written in a zigzag pattern on a given number of 3 rows like this:

P       A       H       N
A   P   L   S   I   I   G
Y	    I       R

(you may want to display this pattern in a fixed font for better legibility).

And then read line by line: "PAHNAPLSIIGYIR". 
Write the code that will take a string and make this conversion given a number of rows. 
convert("PAYPALISHIRING", 3) should return "PAHNAPLSIIGYIR".   
 */

public class Solution {
    public String convert(String input, int nRows) {
      // Write your solution here
      char[] arr = input.toCharArray();
  
      StringBuffer[] sb = new StringBuffer[nRows];
  
      for(int i = 0; i < nRows; i++){
        sb[i] = new StringBuffer();
      }
  
      int start = 0;
  
      while(start < arr.length){
        for(int i = 0; i < nRows && start < arr.length; i++){
          //from top to bot
          sb[i].append(arr[start++]);
        }
  
        for(int j = nRows - 2; j > 0 && start < arr.length; j--){
          sb[j].append(arr[start++]);
        }
      }
  
      for(int i = 1; i < nRows; i++){
        sb[0].append(sb[i]);
      }
  
      return sb[0].toString();
    }
  }