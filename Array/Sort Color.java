/**
 * Given an array with n objects colored red, white or blue, sort them in-place so that objects of the same color are adjacent, 
 * with the colors in the order red, white and blue.
Here, we will use the integers 0, 1, and 2 to represent the color red, white, and blue respectively.
Input: [2,0,2,1,1,0]
Output: [0,0,1,1,2,2]
 */

class Solution {
    public void sortColors(int[] nums) {
        int left = 0;
        int right = nums.length - 1;
        
        int index = 0;
        //Tao-Lu
        while(index <= right){
            if(nums[index] == 2){
                swap(nums, index, right--);
            } else if(nums[index] == 1) {
                index++;
            } else {
                swap(nums, index++, left++);
            }
        }
    }
    
    public void swap(int[] nums, int left, int right){
        int temp = nums[left];
        nums[left] = nums[right];
        nums[right] = temp;
    }
}
/**
 * 向左换 不需要 重新 比较 所以 index++, left++
 * 向右换 需要重新比较     所以 index不++， right--
 * #### One pass
- have two pointers, left/right
- start tracks red, end tracks blue. Swap red/blue to right position, and left++ or right--.
- leave white as is and it will be sorted automatically
- be very careful with index i: when swapping with index right, we do not know what is nums[right], so need to re-calculate index i .
- O(n)
- Note: this one pass solution does not work if there are more than 3 colors. Need to use the regular quick sorty.
 */