/**
 * Given a sorted integer array, remove duplicate elements. 
 * For each group of elements with the same value keep only one of them. 
 * Do this in-place, using the left side of the original array and maintain the relative order of the elements of the array. 
 * Return the array after deduplication.
 */

public class Solution {
    public int[] dedup(int[] array) {
      // Write your solution here
      int i = 0;
      int j = 0;
  
      Set<Integer> set = new HashSet<>();
  
      while(j < array.length) {
        if(!set.contains(array[j])){
          array[i] = array[j];
          set.add(array[j]);
          i++;
        }
        j++;
      }
  
      int[] res = new int[i];
  
      for(int m = 0; m < i; m++){
        res[m] = array[m];
      }
  
      return res;
    }
  }

  /**
   * deduplication  想到 set的 特性  不太用想到 双指针的 奇淫技巧
   * 
   */