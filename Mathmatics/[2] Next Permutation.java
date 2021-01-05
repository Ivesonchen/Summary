/**
 * Implement next permutation, which rearranges numbers into the lexicographically next greater permutation of numbers. 
 * If such arrangement is not possible, it must rearrange it as the lowest possible order (ie, sorted in ascending order).
The replacement must be in-place, do not allocate extra memory.  

Example

1,2,3 → 1,3,2

3,2,1 → 1,2,3

1,1,5 → 1,5,1
 */

 /**
    1　　2　　7　　4　　3　　1
    下一个排列为：
    1　　3　　1　　2　　4　　7

    那么是如何得到的呢，我们通过观察原数组可以发现，如果从末尾往前看，数字逐渐变大，到了2时才减小的，然后再从后往前找第一个比2大的数字，是3，
    那么我们交换2和3，再把此时3后面的所有数字转置一下即可，步骤如下：

    1　　2　　7　　4　　3　　1

    1　　2　　7　　4　　3　　1

    1　　3　　7　　4　　2　　1

    1　　3　　1　　2　　4　　7
  */

public class Solution {
    public int[] nextPermutation(int[] num) {
      // Write your solution here
      //从最右边找 将一个大值 跟 它前面的最近 一个小值交换
      int len = num.length;
  
      for(int i = len - 2; i >= 0; i--){
        if(num[i] <= num[i + 1]){
          for(int j = len - 1; j > i; j--){
            if(num[i] < num[j]){
              swap(num, i, j);
              reverse(num, i + 1, len - 1);
              return num;
            }
          }
        }
      }
      reverse(num, 0, len - 1);
      return num;
    }
  
    public void reverse(int[] A, int i, int j) {
      while(i < j) swap(A, i++, j--);
    }
  
    public void swap(int[] A, int i, int j) {
      int tmp = A[i];
      A[i] = A[j];
      A[j] = tmp;
    }
  }