/**
 * You are playing the following Flip Game with your friend: Given a string that contains only these two characters: + and -, 
 * you and your friend take turns to flip two consecutive "++" into "--". 
 * The game ends when a person can no longer make a move and therefore the other person will be the winner.

Write a function to determine if the starting player can guarantee a win.

For example, given s = "++++", return true. The starting player can guarantee a win by flipping the middle "++" to become "+--+".
 */

public class Solution {
    public boolean canWin(String input) {
      // Write your solution here
      for(int i = 0; i < input.length() - 1; i++){
        if(input.charAt(i) == '+' && input.charAt(i+1) == '+'){
          String str = input.substring(0, i) + "--" + input.substring(i + 2, input.length());
  
          if(!canWin(str)){
            return true;
          }
        }
      }
      return false;
    }
  }
  