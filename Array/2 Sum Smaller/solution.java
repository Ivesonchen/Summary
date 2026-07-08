/**
 * Determine the number of pairs of elements in a given array that sum to a value smaller than the given target number.

Assumptions

The given array is not null and has length of at least 2
Examples

A = {1, 2, 2, 4, 7}, target = 7, number of pairs is 6({1,2}, {1, 2}, {1, 4}, {2, 2}, {2, 4}, {2, 4})
 */

// option 1 : brute force   O(n) ？?  O(n ^ 2)

//option 2: sort + 双指针    O(nLogn);         
public class Solution {
    public int smallerPairs(int[] array, int target) {
      // Write your solution here
      int counter = 0;
  
      for(int i = 0; i < array.length; i ++) {
        for(int j = i + 1; j < array.length; j++){
          int sum = array[i] + array[j];
          if(sum < target){
            counter++;
          }
        }
      }
  
      return counter;
    }

    public int smallerPairs(int[] array, int target) {
        // Write your solution here
        Arrays.sort(array);
        int counter = 0;
        int i = 0, j = array.length - 1;
    
        while(i < j){
          int sum = array[i] + array[j];
    
          if(sum < target){
            counter += j - i; // 当遇到合适的区间的时候 直接计算 个数     1  2  3  4  5            target = 6
            i++;              //                                    i        j               此时i到j 有三个组合
          } else {
            j--;
          }
        }
    
        return counter;
      }
}
  