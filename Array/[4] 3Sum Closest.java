/**
 * Given an array S of n integers, find three integers in S such that the sum is closest to a given number, target. 
 * Return the difference  between the sum of the three integers and the given number. 
 * You may assume that each input would have exactly one solution.

  Example

  For example, given array S = {-1 2 1 -4}, and target = 1.
  The sum that is closest to the target is 2. (-1 + 2 + 1 = 2) and the difference is 1.
 */

// 先sort 然后 很简单的做一个 三指针的 遍历    然后mantain 一个 最小值value    O(n^2)

// Tao-Lu
public class Solution {
  public int threeSumClosest(int[] num, int target) {
    // Write your solution here

    int res = Integer.MAX_VALUE;
    Arrays.sort(num);

    for(int i= 0; i < num.length - 2; i++){
      int j = i + 1;
      int k = num.length - 1;

      while(j < k){
        int sum = num[i] + num[j] + num[k];
        if(sum > target){
          res = Math.min(res, Math.abs(target - sum));
          k--;
        } else {
          res = Math.min(res, Math.abs(target - sum));
          j++;
        }
      }
    }

    return res;
  }
}
  