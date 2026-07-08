/**
 * You are given a helper function bool knows(a, b) which tells you whether A knows B. 
 * Implement a function int findCelebrity(n). 
 * There will be exactly one celebrity if he/she is in the party. 
 * Return the celebrity's label if there is a celebrity in the party. 
 * If there is no celebrity, return -1.
 */


/* The knows API is defined in the parent class Relation.
      boolean knows(int a, int b); */

      public class Solution extends Relation {
    
        private int numberOfPeople;
        
        public int findCelebrity(int n) {
            numberOfPeople = n;
            for(int i = 0; i < n; i++) {
                if(isCelebrity(i)) {
                    return i;
                }
            }
            return -1;
        }
        
        public boolean isCelebrity(int i) {
            for(int j = 0; j < numberOfPeople; j++) {
                if(i == j) continue;
                if(knows(i, j) || !knows(j, i)) {
                    return false;
                }
            }
            return true;
        }
    }