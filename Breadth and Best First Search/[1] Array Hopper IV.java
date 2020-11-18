/**
 * Given an array A of non-negative integers, you are initially positioned at an arbitrary index of the array. 
 * A[i] means the maximum jump distance from that position (you can either jump left or jump right). 
 * Determine the minimum jumps you need to reach the right end of the array. 
 * Return -1 if you can not reach the right end of the array.

Assumptions

The given array is not null and has length of at least 1.
Examples

{1, 3, 1, 2, 2}, if the initial position is 2, the minimum jumps needed is 2 (jump to index 1 then to the right end of array)

{3, 3, 1, 0, 0}, if the initial position is 2, the minimum jumps needed is 2 (jump to index 1 then to the right end of array)

{4, 0, 1, 0, 0}, if the initial position is 2, you are not able to reach the right end of array, return -1 in this case.
 */
public class Solution {
    public int minJump(int[] array, int index) {
      // Write your solution here
      Queue<Integer> queue = new LinkedList<>();
      Set<Integer> set = new HashSet<>();
      queue.offer(index);
      set.add(index);
      int count = 0;
      while(!queue.isEmpty()) {
        int size = queue.size();
        for(int j = 0; j < size; j++){
          int cur = queue.poll();
          if(cur == array.length - 1) return count;
  
          for(int i = 1; i <= array[cur]; i++){
            if(cur + i < array.length && !set.contains(cur + i)){
              queue.offer(cur + i);
              set.add(cur + i);
            }
            if(cur - i >= 0 && !set.contains(cur - i)){
              queue.offer(cur - i);
              set.add(cur - i);
            }
          }
        }
        count ++;
      }
      return -1;
    }
}