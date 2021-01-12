/**
 * 
Given an unsorted integer array, each element is at most k step from its position after the array is sorted.

Can you sort this array with time complexity better than O(nlogn)?

Assumptions:

The given array is not null and length is n, k < n and k >= 0
 */

public class Solution {
    public int[] ksort(int[] array, int k) {
      // Write your solution here
      PriorityQueue<Integer> pq = new PriorityQueue<>();
      int len = array.length;
      for(int i = 0; i < k; i++){
        pq.offer(array[i]);
      }
      int index = 0;
      int start = k;
  
      while(pq.size() > 0){
        if(start < len){
          pq.offer(array[start]);
        }
  
        array[index] = pq.poll();
        start++;
        index++;
      }
      return array;
    }
  }