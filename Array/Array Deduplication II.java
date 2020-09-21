/**
 * Given a sorted integer array, remove duplicate elements. 
 * For each group of elements with the same value keep at most two of them. 
 * Do this in-place, using the left side of the original array and maintain the relative order of the elements of the array. 
 * Return the array after deduplication.

Assumptions

The given array is not null
Examples

{1, 2, 2, 3, 3, 3} → {1, 2, 2, 3, 3}
 */


public class Solution {
    public int[] dedup(int[] array) {
      // Write your solution here
  
      int start = 0;
      int end = 0;
  
      Map<Integer, Integer> map = new HashMap<>();
  
      while(end < array.length){
        if(map.getOrDefault(array[end], 0) != 2){
          array[start] = array[end];
          map.put(array[end], map.getOrDefault(array[end], 0) + 1);
          start ++;
        }
        end++;
      }
  
      int[] res = new int[start];
      for(int i = 0; i < start; i ++){
        res[i] = array[i];
      }
      return res;
    }
  }

  /**
   * 使用 hashmap 来进行计数     注意 null 值 的比较
   */