/**
 * 
 * Find all common elements in 3 sorted arrays.

Assumptions

The 3 given sorted arrays are not null
There could be duplicate elements in each of the arrays
Examples

A = {1, 2, 2, 3}, B = {2, 2, 3, 5}, C = {2, 2, 4}, the common elements are [2, 2]
 */

 //stright forward

public class Solution {
    public List<Integer> common(int[] a, int[] b, int[] c) {
      // Write your solution here
      int i =0, j = 0, k = 0;
      List<Integer> res = new ArrayList<>();
  
      while(i < a.length && j < b.length && k < c.length){
        if(a[i] == b[j] && b[j] == c[k]){
          res.add(a[i]);
          i++;
          j++;
          k++;
        } else if (a[i] < b[j]){
          i++;
        } else if (b[j] < c[k]){
          j++;
        } else {
          k++;
        }
      }
  
      return res;
    }
  }
  