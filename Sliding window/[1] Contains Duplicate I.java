/**
 * Given an array of integers, find if the array contains any duplicates. 
 * Your function should return true if any value appears at least twice in the array,
 *  and it should return false if every element is distinct.
 */

 /**
  * set 去重   O(n)
  */
public class Solution {
    public boolean containsDuplicate(int[] nums) {
      // Write your solution here
  
      if(nums == null || nums.length == 0) return false;
  
      HashSet<Integer> set = new HashSet<>();
  
      for(int num : nums){
        if(set.contains(num)) return true;
  
        set.add(num);
      }
  
      return false;
    }
  }