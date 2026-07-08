/**
 * Given k sorted integer arrays, pick k elements (one element from each of sorted arrays), what is the smallest range.

We define the range [a,b] is smaller than range [c,d] if b-a < d-c or a < c if b-a == d-c.

Assumptions:

k >= 2
None of the k arrays is null or empty
Examples:

{ { 1, 4, 6 },

  { 2, 5 },

  { 8, 10, 15} }

pick one element from each of 3 arrays, the smallest range is {5, 8} (pick 6 from the first array, pick 5 from the second array and pick 8 from the third array).
 */

 /**
        1   4   6
        i

        2   5
        j
        
        8   10  15
        k
  */



public class Solution {
    public int[] smallestRange(int[][] arrays) {
      // Write your solution here
  
      int[] pointer = new int[arrays.length];
      int max = Integer.MIN_VALUE;
      int minY = Integer.MAX_VALUE;
      int minX = 0;
      boolean flag = true;
  
      // PriorityQueue<Integer> queue = new PriorityQueue<>((i,j)-> arrays[i][pointer[i]] - arrays[j][pointer[j]]);
  
      PriorityQueue<Integer> queue = new PriorityQueue<>(new Comparator<Integer>(){
        public int compare(Integer i, Integer j){
          return arrays[i][pointer[i]] - arrays[j][pointer[j]];
        }
      });
  
      for(int i = 0; i < arrays.length; i++){
        queue.offer(i);
        max = Math.max(max, arrays[i][0]);
      }
  
      for(int i = 0; i < arrays.length && flag; i ++){
        for(int j = 0; j < arrays[i].length && flag; j++){
          int minTemp = queue.poll();
          if(minY - minX > max - arrays[minTemp][pointer[minTemp]]){
            minX = arrays[minTemp][pointer[minTemp]];
            minY = max;
          }
          pointer[minTemp]++;
          if(pointer[minTemp] == arrays[minTemp].length){
            flag = false;
            break;
          }
          queue.offer(minTemp);
          max = Math.max(max, arrays[minTemp][pointer[minTemp]]);
        }
      }
      return new int[]{minX, minY};
    }
  }
  