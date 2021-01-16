/**
 * Given two integer arrays A1 and A2, sort A1 in such a way that the relative order among the elements will be same as those are in A2.

For the elements that are not in A2, append them in the right end of the A1 in an ascending order.

Assumptions:

A1 and A2 are both not null.
There are no duplicate elements in A2.
Examples:

A1 = {2, 1, 2, 5, 7, 1, 9, 3}, A2 = {2, 1, 3}, A1 is sorted to {2, 2, 1, 1, 3, 5, 7, 9}

 */

/**                                     数值    出现次数
 * maintain 一个 ordered map （treemap<Integer, Integer>）
 * 
 * sorted by key   然后生成 
 */

public class Solution {
    public int[] sortSpecial(int[] A1, int[] A2) {
      // Write your solution here
      TreeMap<Integer, Integer> map = new TreeMap<>();
  
      for(int n : A1){
        map.put(n, map.getOrDefault(n, 0) + 1);
      }
      
      int index = 0;
      for(int n : A2){
        for(int i = 0; i < map.getOrDefault(n, 0); i++){
          A1[index++] = n;
        }
        map.remove(n);
      }
  
      for(int n : map.keySet()){
        for(int i = 0; i < map.get(n); i++){
          A1[index++] = n;
        }
      }
      return A1;
    }
  }
  