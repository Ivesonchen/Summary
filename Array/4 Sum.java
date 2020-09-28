/**
 * Determine if there exists a set of four elements in a given array that sum to the given target number.

Assumptions

The given array is not null and has length of at least 4
Examples

A = {1, 2, 2, 3, 4}, target = 9, return true(1 + 2 + 2 + 4 = 9)

A = {1, 2, 2, 3, 4}, target = 12, return false
 */

/**
 * Option 1: 四指针的做法， 变种于3sum的三指针的做法 
 * 先排序 然后左右夹逼    O(N^3)
 * 如果返回的是 排列结果的话， 注意要不要去重 
 */
// Tao-Lu

public class Solution {
    public boolean exist(int[] array, int target) {
      // Write your solution here  
      Arrays.sort(array);
  
      for(int i = 0; i < array.length - 3; i++){
        for(int j = i + 1; j < array.length - 2; j++){
          int k = j + 1;
          int l = array.length - 1;
  
          while(k < l){
            int sum = array[i] + array[j] + array[k] + array[l];
            if(sum > target){
              l--;
            } else if (sum < target){
              k++;
            } else {
                return true;
            }
          }
        }
      }
      return false;
    }
  }
  

// 这里是一个 返回排列组合的方法
// Tao-Lu
// Time Complexity: O(n^3)，Space Complexity: O(1)
public class Solution {
    public List<List<Integer>> fourSum(int[] nums, int target) {
        List<List<Integer>> result = new ArrayList<>();
        if (nums.length < 4) return result;
        Arrays.sort(nums);

        for (int i = 0; i < nums.length - 3; ++i) {
            if (i > 0 && nums[i] == nums[i-1]) continue;
            for (int j = i + 1; j < nums.length - 2; ++j) {
                if (j > i+1 && nums[j] == nums[j-1]) continue;
                int k = j + 1;
                int l = nums.length - 1;
                while (k < l) {
                    final int sum = nums[i] + nums[j] + nums[k] + nums[l];
                    if (sum < target) {
                        ++k;
                        while(nums[k] == nums[k-1] && k < l) ++k;
                    } else if (sum > target) {
                        --l;
                        while(nums[l] == nums[l+1] && k < l) --l;
                    } else {
                        result.add(Arrays.asList(nums[i], nums[j], nums[k], nums[l]));
                        ++k;
                        --l;
                        while(nums[k] == nums[k-1] && k < l) ++k;
                        while(nums[l] == nums[l+1] && k < l) --l;
                    }
                }
            }
        }
        return result;
    }
}