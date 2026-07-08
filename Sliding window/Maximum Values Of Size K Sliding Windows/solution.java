/**
 * Given an integer array A and a sliding window of size K, find the maximum value of each window as it slides from left to right.

    Assumptions

    The given array is not null and is not empty

    K >= 1, K <= A.length

    Examples

    A = {1, 2, 3, 2, 4, 2, 1}, K = 3, the windows are {{1,2,3}, {2,3,2}, {3,2,4}, {2,4,2}, {4,2,1}},

    and the maximum values of each K-sized sliding window are [3, 3, 4, 4, 4]

    https://github.com/grandyang/leetcode/issues/239
 */



// 单调栈  monotonic queue
class Solution {
    public int[] maxSlidingWindow(int[] nums, int k) {
        if(nums == null || k <= 0) return new int[0];
        
        int n = nums.length;
        int[] r = new int[n - k + 1];
        int ri = 0;
        
        Deque<Integer> q = new ArrayDeque<>();
        
        for(int i = 0; i < nums.length; i++){
            while(!q.isEmpty() && q.peek() < i - k + 1){
                q.poll();
            }
            
            while(!q.isEmpty() && nums[q.peekLast()] < nums[i]){
                q.pollLast();
            }
            
            q.offer(i);
            
            if(i >= k - 1){
                r[ri++] = nums[q.peek()];
            }
        }
        return r;
    }
}



public class Solution {
    public List<Integer> maxWindows(int[] array, int k) {
      // Write your solution here
      List<Integer> res = new ArrayList<>();
  
      PriorityQueue<Integer> queue = new PriorityQueue<Integer>(new Comparator<Integer>(){
        public int compare(Integer a1, Integer a2){
          return a2 - a1;
        }
      });
  
      for(int i = 0; i < k; i++){
        queue.add(array[i]);
      }
  
      res.add(queue.peek());
  
      for(int i = k; i < array.length; i++){
        queue.add(array[i]);
        queue.remove(array[i - k]);   // 这个问题 remove 可能会移除 多个(不同语言中) 
        res.add(queue.peek());
      }
  
      return res;
    }
  }

  // Max heap 版本 用来替代上面的版本
  public int[] maxSlidingWindow(int[] nums, int k) {
    List<Integer> res = new ArrayList<>();
    int[] rst = new int[nums.length - k + 1];

    PriorityQueue<Pair<Integer, Integer>> queue = new PriorityQueue<Pair<Integer, Integer>>(new Comparator<Pair<Integer, Integer>>(){
      public int compare(Pair a1, Pair a2){
        return (int)a2.getKey() - (int)a1.getKey();
      }
    });
    int index = 0;
      
    for(int i = 0; i < nums.length; i++){
        while(!queue.isEmpty() && queue.peek().getValue() <= i - k) queue.poll();
        queue.offer(new Pair(nums[i], i));
        if(i >= k - 1) rst[index ++] = queue.peek().getKey();
    }
    
    return rst;
  }