/**
 * Given an unlimited flow of numbers, keep track of the median of all elements seen so far.

You will have to implement the following two methods for the class

read(int value) - read one value from the flow
median() - return the median at any time, return null if there is no value read so far
Examples

read(1), median is 1
read(2), median is 1.5
read(3), median is 2
read(10), median is 2.5
......
 */

public class Solution {
    private List<Integer> list;
  
    public Solution() {
      // add new fields and complete the constructor if necessary.
      this.list = new ArrayList<Integer>();
    }
    
    public void read(int value) {
      // write your implementation here.
      this.list.add(value);
      Collections.sort(list);
    }
    
    public Double median() {
      // write your implementation here.
      int len = list.size();
      if(len == 0) return null;
      if(len % 2 == 0) {
        return (double)(list.get((len - 1)/ 2) + list.get(len / 2)) / 2;
      } else {
        return (double)list.get(len/2);
      }
    }
  }

  // min-heap 存的是top k largest numbers    max-heap 存的是从小到大
  class MedianFinder {

    PriorityQueue<Integer> minHeap = new PriorityQueue<>();//heap is a minimal heap by default
    PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());//change to a maximum heap

    // Adds a number into the data structure.
    public void addNum(int num) {
        maxHeap.offer(num);
        minHeap.offer(maxHeap.poll()); //通过两个 加入   间接实现排序
        if (maxHeap.size() < minHeap.size())
            maxHeap.offer(minHeap.poll());

        // maxHeap 要多一个一个值  
    }

    // Returns the median of current data stream
    public double findMedian() {
        if (maxHeap.size() == minHeap.size())
            return (maxHeap.peek() + minHeap.peek()) / 2.0;
        else
            return maxHeap.peek();
    }
}