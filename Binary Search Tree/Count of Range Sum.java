/**
 * Given an integer array nums, return the number of range sums that lie in [lower, upper] inclusive.
Range sum S(i, j) is defined as the sum of the elements in nums between indices i and j (i ≤ j), inclusive.

Note:
A naive algorithm of O(n2) is trivial. You MUST do better than that.

Example:
Given nums = [-2, 5, -1], lower = -2, upper = 2,
Return 3.
The three ranges are : [0, 0], [2, 2], [0, 2] and their respective sums are: -2, -1, 2.

https://leetcode.com/problems/count-of-range-sum/discuss/78006/Summary-of-the-Divide-and-Conquer-based-and-Binary-Indexed-Tree-based-solutions
 */

 //To-Do
public class Solution {
    int count = 0;
    int lower;
    int upper;
  
    public int countRangeSum(int[] nums, int lower, int upper) {
      // Write your solution here
      long[] sum = new long[nums.length + 1];
      long[] temp = new long[nums.length + 1];
  
      sum[0] = 0;
      this.lower = lower;
      this.upper = upper;
  
      for(int i = 1; i <= nums.length; i++){
        sum[i] = sum[i - 1] + (long) nums[i - 1];
      }
  
      mergesort(sum, 0, sum.length - 1, temp);
      return count;
    }
  
    private void mergesort(long[] sum, int start, int end, long[] temp){
      if(start >= end) return;
  
      int mid = start + (end - start) / 2;
  
      mergesort(sum, start, mid, temp);
      mergesort(sum, mid + 1, end, temp);
      merge(sum, start, mid, end, temp);
    }
  
    private void merge(long[] sum, int start, int mid, int end, long[] temp){
      int right = mid + 1;
      int index = start;
      int low = mid + 1, high = mid + 1;
      for(int left = start; left <= mid; left++){
        while(low <= end && sum[low] - sum[left] < lower) low++;
        while(high <= end && sum[high] - sum[left] <= upper) high++;
        while(right <= end && sum[right] < sum[left]) temp[index++] = sum[right++];
  
        temp[index++] = sum[left];
        count += high - low;
      }
  
      while(right <= end){
        temp[index++] = sum[right++];
      }
  
      for(int i = start; i <= end; i++){
        sum[i] = temp[i];
      }
    }
  }