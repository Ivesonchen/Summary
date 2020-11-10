/**
 * Given an array of n integers nums and a target, find the number of index triplets i, j, k with 0 <= i < j < k < n that satisfy the condition nums[i] + nums[j] + nums[k] < target.

For example, given nums = [-2, 0, 1, 3], and target = 2.

Return 2. Because there are two triplets which sums are less than 2:

[-2, 0, 1]
[-2, 0, 3]
 */

/**
 * 注意这里给的数组 是 无顺序的 
 * 1. 暴力解的话 就变成 n^3 复杂度
 * 
 * 2. 先排序 nLogn
 *    然后使用三个指针 比较
 */

public class Solution {
    public int threeSumSmaller(int[] num, int target) {
      // Write your solution here
      int res = 0;
      Arrays.sort(num);
  
      for(int i = 0; i < num.length - 2; i++){
        int j = i + 1;
        int k = num.length - 1;
  
        while(j < k){
          if(num[i] + num[j] + num[k] >= target){
            k--;
          } else {
            res += k - j;
            j++;
          }
        }
      }
      return res;
    }
}