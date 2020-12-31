/**
 * Each positive integer larger than 1 is the production of several prime numbers. 
 * Return the list of prime factors in ascending order.

Assumptions:

The given number is >= 2.
Examples:

12 = 2 * 2 * 3, return [2, 2, 3]
5 = 5, return [5]
 */

 // hmm  kinda silly
public class Solution {
    public List<Integer> factors(int target) {
      // Write your solution here
  
      List<Integer> res = new ArrayList<>();
  
      while(target % 2 == 0) {
        res.add(2);
        target /= 2;
      }
  
      for(int i = 3; i <= Math.sqrt(target); i++){
        while(target % i == 0){
          res.add(i);
          target /= i;
        } 
      }
  
      if(target > 2){
        res.add(target);
      }
  
      return res;
    }
  }