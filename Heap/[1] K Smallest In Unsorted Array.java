/**
 * Find the K smallest numbers in an unsorted integer array A. The returned numbers should be in ascending order.

Assumptions

A is not null
K is >= 0 and smaller than or equal to size of A
Return

an array with size K containing the K smallest numbers in ascending order
Examples

A = {3, 4, 1, 2, 5}, K = 3, the 3 smallest numbers are {1, 2, 3}
 */

public class Solution {
    public int[] kSmallest(int[] array, int k) {
      // Write your solution here
      PriorityQueue<Integer> pq = new PriorityQueue<>(new Comparator<Integer>(){
        public int compare(Integer a, Integer b){
          return b - a;
        }
      });
  
      for(int i : array){
        pq.offer(i);
  
        if(pq.size() > k){
          pq.poll();
        }
      }

      // pq 转化为 int array
      int[] res = new int[pq.size()];
  
      int i = pq.size() - 1;
      while(!pq.isEmpty()){
        res[i] = pq.poll();
        i --;
      }
  
      return res;
    }
  }
  