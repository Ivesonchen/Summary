/**
 * Given an unsorted array of positive integers. Find the number of triangles that can be formed with three different array elements as three sides of triangles.

Assumptions:

The given array is not null and has length of at least 3.
Exmaples:

array = {4, 6, 3, 7}, the output should be 3. There are three triangles possible {3, 4, 6}, {4, 6, 7} and {3, 6, 7}. Note that {3, 4, 7} is impossible.
Preferred time complexity O(n ^ 2).

https://leetcode.com/problems/valid-triangle-number/solution/
 */

//Tao-Lu 的变形 从右开始  因为要符合 比较大小的逻辑  要限制较大的数

public class Solution {
    public int numOfTriangles(int[] array) {
      // Write your solution here
      int counter = 0;
      Arrays.sort(array);
  
      for(int k = array.length - 1; k > 1; k--){
        int j = k - 1;
        int i = 0;
        while(i < j){
          if(array[i] + array[j] <= array[k]){ // 不满足
             i++;
          } else {                 // 满足
            counter += j - i;
            j--;
          }
        }
      }
      return counter;
    }
}