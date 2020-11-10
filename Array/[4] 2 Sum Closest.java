/**
 * 
 * Find the pair of elements in a given array that sum to a value that is closest to the given target number. Return the values of the two numbers.

Assumptions

The given array is not null and has length of at least 2
Examples

A = {1, 4, 7, 13}, target = 7, closest pair is 1 + 7 = 8, return [1, 7].
 */

 // ez O(nlogn)

public class Solution {
    public List<Integer> closest(int[] array, int target) {
      // Write your solution here
        if(array==null || array.length<2) {
          return null;
        }
      List<Integer> res = new ArrayList<>();
      Arrays.sort(array);
      // res =  Arrays.asList(array[0], array[1]);
      // if(array.length == 2) return res;
  
      int dis = Integer.MAX_VALUE;
      int left = 0, right = array.length - 1;
      while(left < right){
        int sum = array[left] + array[right];
        int tempDis = Math.abs(target - sum);
        if(tempDis < dis){
          res = Arrays.asList(array[left], array[right]);
          dis = tempDis;
        }
  
        if(sum > target){
          right--;
        } else if (sum < target){
          left++;
        } else {
          return res;
        }
      }
      return res;
    }
}
  