/**
 * Given an array of numbers in string type, sort them in a way that the concatenation of them yields the largest value. 
 * Return the largest number in string type.

Assumptions:

The given array is not null.
Each of the strings in the array is not null and they are all numbers.
Examples:

{“54”, “546”, “648”, “88”}, the arrangement “8864854654” gives the largest value. 
 */

 //Sorting via Custom Comparator

public class Solution {
    public String largestNumber(String[] input) {
      // Write your solution here
      PriorityQueue<String> pq = new PriorityQueue<>(new Comparator<String>(){
        public int compare(String s1, String s2){
  
          return (s2 + s1).compareTo(s1 + s2);
        }
      });
  
      for(String str : input){
        pq.offer(str);
      }
  
      StringBuilder sb = new StringBuilder();
  
      while(pq.size() != 0){
        sb.append(pq.poll());
      }
  
      return sb.toString().charAt(0) == '0' ? "0" : sb.toString();
    }
  }
  