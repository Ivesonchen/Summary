class MedianFinder {
  PriorityQueue<Integer> pq1;// MaxHeap
  PriorityQueue<Integer> pq2;// MinHeap
  
  // 保持两个pq 的 size 差距不超过1  MaxHeap 存 从小到中间的值 MinHeap 存 从中间到大的值
  
  public MedianFinder() {
      pq1 = new PriorityQueue<>(Collections.reverseOrder());
      pq2 = new PriorityQueue<>();
  }
  
  public void addNum(int num) {
      if(pq1.isEmpty() || pq1.peek() > num) {
          pq1.add(num);
      } else {
          pq2.add(num);
      }
      
      if(pq1.size() > pq2.size() + 1) {
          pq2.add(pq1.poll());
      } else if (pq2.size() > pq1.size() + 1) {
          pq1.add(pq2.poll());
      }
  }
  
  public double findMedian() {
      if(pq1.size() == pq2.size()) {
          return (double)(pq1.peek() + pq2.peek()) / 2;
      } else if (pq1.size() > pq2.size()) {
          return (double)pq1.peek();
      } else {
          return (double)pq2.peek();
      }
  }
}

/**
* Your MedianFinder object will be instantiated and called as such:
* MedianFinder obj = new MedianFinder();
* obj.addNum(num);
* double param_2 = obj.findMedian();
*/