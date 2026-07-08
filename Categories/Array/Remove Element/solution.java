/**
 * 
 * Given an array and a value, remove all instances of that value in place and return the new length.
The order of elements can be changed. It doesn't matter what you leave beyond the new length.
 */


// Remove Element
// Time Complexity: O(n), Space Complexity: O(1)
public class Solution {
    public int removeElement(int[] nums, int target) {
        int index = 0;
        for (int i = 0; i < nums.length; ++i) {
            if (nums[i] != target) {
                nums[index++] = nums[i];
            }
        }
        return index;
    }
};

// Tao-Lu
// 两个指针 都从左边 开始    start  index
// 相关题目    move zero to then end