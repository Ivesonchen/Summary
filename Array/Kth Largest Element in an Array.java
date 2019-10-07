/**
 * #### PriorityQueue, MinHeap
- Need to maintain k large elements, where the smallest will be compared and dropped if applicable: 
- Maintain k elements with min value: consider using minHeap
- add k base elements first
- Maintain MinHeap: only allow larger elements (which will squzze out the min value)
- Remove peek() of queue if over size
- O(nlogk)


#### Quick Sort
- 用Quick Sort 里面partion的一部分
- sort结束后是ascending的, 那么 n - k 就是第k大. 
- partion的结果是那个low, 去找 low==nums.size() - k， 也就是倒数第K个。    
- 没找到继续partion recursively.
- sort的过程是排一个从小到大的list. (同样的代码还可以好xth smallest，mid变成x就好)
- Steps:
- 每个iteration, 找一个pivot,然后从low,和high都和pivot作比较。    
- 找到一个low>pivot, high<pivot, 也就可以swap了。    
- 得到的low就是当下的partion point了
- Overall O(nlogN), average O(n) for this problem.
 */


class Solution {
    public int findKthLargest(int[] nums, int k) {
        if (nums == null || nums.length == 0) return -1;

        PriorityQueue<Integer> queue = new PriorityQueue<>(); // min-heap
        
        for (int i = 0; i < nums.length; i++) { 
            if (i < k || nums[i] > queue.peek()) queue.offer(nums[i]);
            if (queue.size() > k) queue.poll();
        }
        
        return queue.poll();
    }
}

// Quick sort/ partition
// Partition to return the `low` index, which should match targetIndex.
class Solution {
    public int findKthLargest(int[] nums, int k) {
        if (nums == null || nums.length == 0) return -1;
        int n = nums.length;
        return partition(nums, 0, n - 1, n - k);
    }
    
    private int partition (int[] nums, int start, int end, int targetIndex) {
        // define low/high
        int pivot = end;
        int low = start, high = end, num = nums[pivot];
        
        // move pointer and swap
        while (low < high) {
            while (low < high && nums[low] < num) {
                low++;
            }
            while (low < high && nums[high] >= num) {
                high--;
            }
            swap(nums, low, high);
        }
        swap(nums, low, pivot);

        // compare if low == targetIndex; or recursively partition to find targetIndex
        if (low == targetIndex) {
            return nums[low];
        } else if (low < targetIndex) {
            return partition(nums, low + 1, end, targetIndex);
        } else {
            return partition(nums, start, low - 1, targetIndex);
        }
    }
    
    private void swap(int[] nums, int x, int y) {
        int temp = nums[x];
        nums[x] = nums[y];
        nums[y] = temp;
    }
}