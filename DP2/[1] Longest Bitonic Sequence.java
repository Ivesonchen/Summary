/**
 * Given an array with all integers,  a sub-sequence of it is called Bitonic if it is first sorted in an ascending order, 
 * then sorted in a descending order. How can you find the length of the longest bitonic subsequence.

Assumptions:

The given array is not null.
Corner Cases:

A subsequence, sorted in increasing order is considered Bitonic with the decreasing part as empty. 
Similarly, decreasing order sequence is considered Bitonic with the increasing part as empty.
Examples:

{1, 3, 2, 1, 4, 6, 1}, the longest bitonic sub sequence is {1, 3, 4, 6, 1}, length is 5.
 */

 /**
  * This problem is a variation of standard Longest Increasing Subsequence (LIS) problem. 
  Let the input array be arr[] of length n. We need to construct two arrays lis[] and lds[] using Dynamic Programming solution of LIS problem. 
  lis[i] stores the length of the Longest Increasing subsequence ending with arr[i]. 
  lds[i] stores the length of the longest Decreasing subsequence starting from arr[i]. 
  Finally, we need to return the max value of lis[i] + lds[i] – 1 where i is from 0 to n-1.
  */
public class Solution {
    public int longestBitonic(int[] array) {
      // Write your solution here
      int len = array.length;
      if(len == 0) return 0;
      int[] lis = new int[len];
      int[] lds = new int[len];
  
      lis[0] = 1;
      for(int i = 1; i < len; i++){
        int max = 0;
        for(int j = 0; j < i; j++){
          if(array[j] < array[i]){
            max = Math.max(max, lis[j]);
          }
        }
        lis[i] = max + 1;
      }
  
      lds[len - 1] = 1;
      for(int i = len - 2; i >= 0; i--){
        int max = 0;
        for(int j = len - 1; j > i; j--){
          if(array[i] > array[j]){
            max = Math.max(max, lds[j]);
          }
        }
        lds[i] = max + 1;
      }
  
      int res = 1;
      for(int i = 0; i < len; i++){
        res = Math.max(lis[i] + lds[i], res);
      }
  
      return res - 1;
    }
  }