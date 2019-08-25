/*
Given an unsorted array nums, reorder it in-place such that nums[0] <= nums[1] >= nums[2] <= nums[3]....
For example, given nums = [3, 5, 2, 1, 6, 4], one possible answer is [1, 6, 2, 5, 3, 4].
Tags: Array Sort
Similar Problems: (M) Sort Colors
*/

class Solution {
    public void wiggleSort(int[] nums) {
        if(nums == null || nums.length == 0) return;
        
        for(int i = 1; i < nums.length; i++) {
            if(i % 2 == 0) {
                if(nums[i] > nums[i - 1]) swap(nums, i, i - 1);
                // even position
            } else {
                if(nums[i] < nums[i - 1]) swap(nums, i, i - 1);
                //odd position
            }
        }
    }
    
    public void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
}

/**
 * 
方法1:
排序, nLog(n). 然后把直线上坡变成层叠山峰, 需要每隔几个(题目中是每隔2位)就做个swap 造成高低不平.
Note: 每隔山峰之间是相互没有关系的, 所以每次只要操心 [i], [i-1]两个位置就好了.

方法2:
O(n)
看好奇数偶数位的规律, 然后根据题目给出的规律, 跑一遍, 每次只关注两个位置: 把不合适的[i], [i-1]调换位置就好了.

方法3:
跟法2一样, 只是更巧妙一点罢了:
第一遍想太多. 其实做一个fall-through就能把问题解决，原因是因为：
这样的fall-through每次在乎两个element，可以一口气搞定，无关乎再之前的elements。
特别的一点：flag来巧妙的掌控山峰和低谷的变化。又是神奇的一幕啊！
这样子的奇观，见过就要知道了，没见过的时候有点摸不着头脑。
 */
class Solution {
    public void wiggleSort(int[] nums) {
        if (nums == null || nums.length <= 1) {
            return;
        }
        Arrays.sort(nums);
        for (int i = 2; i < nums.length; i+=2) {
            swap(nums, i, i - 1);
        }
    }
    
    private void swap(int[] nums, int x, int y) {
        int temp = nums[x];
        nums[x] = nums[y];
        nums[y] = temp;
    }
}

public class Solution {
    public void wiggleSort(int[] nums) {
    	if (nums == null || nums.length <= 1) {
    		return;
    	}
    	int flag = 1;
    	for (int i = 1; i < nums.length; i++) {
    		if (flag * nums[i] < flag * nums[i - 1]) {
    			swap(nums, i, i - 1);
    		}
    		flag = -1 * flag;
    	}
    }

	public void swap(int[] nums, int x, int y) {
    	int temp = nums[x];
    	nums[x] = nums[y];
    	nums[y] = temp;
    }
}