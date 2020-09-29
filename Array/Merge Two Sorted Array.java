/**
 * Merge given amount of numbers from two sorted arrays.

Note that given amount of numbers are not larger than the length of the respective arrays.

Input: [1, 2, 3], 3, [2, 4, 6], 1

Output: [1,2, 2, 3]
 */
// merge sort   O(m + n)

public class Solution {
    public int[] merge(int[] A, int m, int[] B, int n) {
      // Write your solution here
      int[] res = new int[m + n];
  
      int i = 0, j = 0;
      int k = 0;
  
      while(i < m && j < n){
        if(A[i] <= B[j]){
          res[k] = A[i];
          k++;
          i++;
        } else {
          res[k] = B[j];
          k++;
          j++;
        }
      }
  
      while(i < m){
        res[k] = A[i];
        k++;
        i++;
      }
      while(j < n){
        res[k] = B[j];
        k++;
        j++;
      }
      return res;
    }
  }
  