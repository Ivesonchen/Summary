/**
 * Find the Kth smallest number s such that s = 2 ^ x * 3 ^ y, x >= 0 and y >= 0, x and y are all integers.

Assumptions

K >= 1
Examples

the smallest is 1
the 2nd smallest is 2
the 3rd smallest is 3
the 4th smallest is 2 ^ 2 = 4
the 5th smallest is 2 * 3 = 6
the 6th smallest is 2 ^ 3 * 3 ^ 0 = 8
 */

 /**
  *  pq 时刻保持 某一个状态的 最小结果
    注意 long 的用法    结尾加 L or l  转化为int 用 .intValue()
  */

public class Solution {
    public int kth(int k) {
      // Write your solution here
      PriorityQueue<Long> pq = new PriorityQueue<>();
      HashSet<Long> set = new HashSet<>();
      pq.offer(1L);
      set.add(1L);
      for(int i = 1; i < k; i++){
        Long val = pq.poll();
        Long a = val * 2;
        Long b = val * 3;
        if(!set.contains(a)){
          pq.offer(a);
          set.add(a);
        }
        if(!set.contains(b)){
          pq.offer(b);
          set.add(b);
        }
      }
      return pq.peek().intValue();
    }
}