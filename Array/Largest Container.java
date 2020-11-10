/**
 * Given n non-negative integers a1, a2, ..., an , where each represents a point at coordinate (i, ai). 
 * n vertical lines are drawn such that the two endpoints of line i is at (i, ai) and (i, 0). 
 * Find two lines, which together with x-axis forms a container, such that the container contains the most water.

Note: You may not slant the container and n is at least 2.
 */

class Solution {
    public int maxArea(int[] height) {
        int left = 0;
        int right = height.length - 1;
        
        int res = Integer.MIN_VALUE;
        
        while(left < right){
            res = Math.max(res, (Math.min(height[left], height[right]) * (right - left)));
            if(height[left] < height[right]){  // 注意两个指针的移动条件  总是移动那个小的指针 这样才有可能遇到更大的面积
                left ++;
            } else {
                right --;
            }
        }
        return res;
    }
}