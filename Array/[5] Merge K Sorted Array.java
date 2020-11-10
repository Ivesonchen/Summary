/**
 * Merge K sorted array into one big sorted array in ascending order.

Assumptions

The input arrayOfArrays is not null, none of the arrays is null either.
 */
/**
 * hashmap key存的是 每个array  value 存的是 该拿去代表这个数组排序的 数值的index
 */
public class Solution {
    public int[] merge(int[][] arrayOfArrays) {
      // Write your solution here
          // Write your solution here
          Map<int[], Integer> map = new HashMap<>();
          PriorityQueue<int[]> pq = new PriorityQueue<>(new Comparator<int[]>() {
              @Override
              public int compare(int[] o1, int[] o2) {
                  return Integer.valueOf(o1[map.get(o1)]).compareTo(Integer.valueOf(o2[map.get(o2)]));
              }
          });
  
          // PriorityQueue<int[]> pq = new PriorityQueue<>((int[] o1, int[] o2) -> Integer.valueOf(o1[map.get(o1)]).compareTo(Integer.valueOf(o2[map.get(o2)])));
  
          int size = 0;
          for (int[] arr : arrayOfArrays) {
              size += arr.length;
              if (arr.length > 0) {
                  map.put(arr, 0);
                  pq.add(arr);
              }
          }
  
          int[] ans = new int[size];
          int idx = 0;
  
          while (!pq.isEmpty()) {
              int[] arr = pq.poll();
              ans[idx++] = arr[map.get(arr)];
              if (map.get(arr) < arr.length - 1) {
                  map.put(arr, map.get(arr) + 1);
                  pq.add(arr);
              }
          }
  
          return ans;
    }
  }