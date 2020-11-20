/**
 * Write a program to find the nth super ugly number.

Super ugly numbers are positive numbers whose all prime factors are in the given prime list primes of size k. For example, [1, 2, 4, 7, 8, 13, 14, 16, 19, 26, 28, 32] is the sequence of the first 12 super ugly numbers given primes = [2, 7, 13, 19] of size 4.

Note:
(1) 1 is a super ugly number for any given primes.
(2) The given numbers in primes are in ascending order.
(3) 0 < k ≤ 100, 0 < n ≤ 106, 0 < primes[i] < 1000.
(4) The nth super ugly number is guaranteed to fit in a 32-bit signed integer.

https://leetcode.com/problems/super-ugly-number/
 */
// same idea with 2,3 factor     terrible running time and space usage

public class Solution {
    public int nthSuperUglyNumber(int n, int[] primes) {
      // Write your solution here
      PriorityQueue<Long> queue = new PriorityQueue<>();
      Set<Long> set = new HashSet<>();
  
      queue.offer(1L);
  
      for(int i = 1; i < n; i++){
        long val = queue.poll();
  
        for(int j = 0; j < primes.length; j++){
          long temp = val * primes[j];
          if(!set.contains(temp))
              queue.offer(temp);
              set.add(temp);
        }
      }
  
      return queue.peek().intValue();
    }
  }