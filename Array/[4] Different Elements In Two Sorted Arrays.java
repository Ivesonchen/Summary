/**
 * Given two sorted arrays a and b containing only integers, return two list of elements: the elements only in a but not in b, and the elements only in b but not in a.

Do it in one pass.

Assumptions:

The two given arrays are not null.
Examples:

a = {1, 2, 2, 3, 4, 5}

b = {2, 2, 2, 4, 4, 6}

The returned two lists are:

[

  [1, 3, 5],

  [2, 4, 6]  // there are two 2s in a, so there is one 2 in b not in a

]
 */
// 分析
public class Solution {
    public int[][] diff(int[] a, int[] b) {
      // Write your solution here
      int[][] res = new int[2][];
      List<Integer> aList = new ArrayList<>();
      List<Integer> bList = new ArrayList<>();
  
      int i = 0, j = 0;
  
      while(i < a.length && j < b.length){
        if(a[i] < b[j]){
          aList.add(a[i]);
          i++;
        } else if (a[i] > b[j]){
          bList.add(b[j]);
          j++;
        } else {
          i++;
          j++;
        }
      }
  
      while(i < a.length){
        aList.add(a[i]);
        i++;
      }
  
      while(j < b.length){
        bList.add(b[j]);
        j++;
      }
  
      int[] aArray = new int[aList.size()];
      int[] bArray = new int[bList.size()];
      for(int m = 0; m < aList.size(); m++){
        aArray[m] = aList.get(m);
      }
      for(int m = 0; m < bList.size(); m++){
        bArray[m] = bList.get(m);
      }
      res[0] = aArray;
      res[1] = bArray;
  
      return res;
    }
  }