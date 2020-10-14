/**
 * Given two sorted arrays of integers, find the Kth smallest number.

Assumptions

The two given arrays are not null and at least one of them is not empty

K >= 1, K <= total lengths of the two sorted arrays

Examples

A = {1, 4, 6}, B = {2, 3}, the 3rd smallest number is 3.

A = {1, 2, 3, 4}, B = {}, the 2nd smallest number is 2.
 */


public class Solution {
    // merge 的思想 
    public int kth(int[] a, int[] b, int k) {
      // Write your solution here
  
      int i = 0, j = 0, res = Integer.MIN_VALUE;
      for(int m = 0; m < k; m++){
        int com1 = Integer.MAX_VALUE;
        int com2 = Integer.MAX_VALUE;
        if(i < a.length) com1 = a[i];
        if(j < b.length) com2 = b[j];
  
        if(com1 < com2) {
          i++;
          res = com1;
        } else {
          j++;
          res = com2;
        }
      }
  
      return res;
    }

    private int helper(int[] a, int i, int[] b, int j, int k){
      if(i >= a.length) return b[j + k - 1];
      if(j >= b.length) return a[i + k - 1];
  
      if(k == 1) return Math.min(a[i], b[j]);
  
      int amid = i + k/2 - 1;
      int bmid = j + k/2 - 1;
  
      int aval = amid >= a.length ? Integer.MAX_VALUE : a[amid];
      int bval = bmid >= b.length ? Integer.MAX_VALUE : b[bmid];
  
      if(aval >= bval){
        return helper(a, i, b, bmid + 1, k - k/2);
      } else {
        return helper(a, amid + 1, b, j, k - k/2);
      }
    }

    public int kth(int[] a, int[] b, int k){
      int i = 0;
      int j = 0;

      while(k > 1 && i < a.length && j < b.length){
        int amid = i + k/2 - 1;
        int bmid = j + k/2 - 1;
        
        int aval = amid >= a.length ? Integer.MAX_VALUE : a[amid];
        int bval = bmid >= b.length ? Integer.MAX_VALUE : b[bmid];

        if(aval >= bval){
          j = bmid + 1;
          k = k - k/2;
        } else {
          i = amid + 1;
          k = k - k/2;
        }
      }
      if(i >= a.length) return b[j + k - 1];
      if(j >= b.length) return a[i + k - 1];

      return Math.min(a[i], b[j]);
    }
}