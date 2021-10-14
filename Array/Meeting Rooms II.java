class Solution {
  public int minMeetingRooms(int[][] intervals) {
      Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
      PriorityQueue<int[]> pq = new PriorityQueue<>((a,b) -> a[1] - b[1]);
      
      for(int[] interval : intervals) {
          if(!pq.isEmpty() && interval[0] >= pq.peek()[1]) {
              pq.poll();
          }
          
          pq.add(interval);
      }
      
      return pq.size();
  }
}