/**
 * Given an array of elements, reorder it as follow:

{ N1, N2, N3, …, N2k } → { N1, Nk+1, N2, Nk+2, N3, Nk+3, … , Nk, N2k }

{ N1, N2, N3, …, N2k+1 } → { N1, Nk+1, N2, Nk+2, N3, Nk+3, … , Nk, N2k, N2k+1 }

Try to do it in place.

Assumptions

The given array is not null
Examples

{ 1, 2, 3, 4, 5, 6} → { 1, 4, 2, 5, 3, 6 }

{ 1, 2, 3, 4, 5, 6, 7, 8 } → { 1, 5, 2, 6, 3, 7, 4, 8 }

{ 1, 2, 3, 4, 5, 6, 7 } → { 1, 4, 2, 5, 3, 6, 7 }
 */

/**
 *  A B C | D E F G | 1 2 3 | 4 5 6 7
    Chunk1  Chunk2    Chunk3  Chunk4

    A B C | 1 2 3 | D E F G | 4 5 6 7

    A B | 1 2 | C | 3 | D E | 4 5 | F G | 6 7

    A 1 B 2 C 3 D 4 E 5 F 6 G 7

 */

public class Solution {
    public int[] reorder(int[] array) {
        // Write your solution here
        if (array.length % 2 == 1) {
            reorder(array, 0, array.length - 2);
        } else {
            reorder(array, 0, array.length - 1);
        }
        return array;
    }
   
    private void reorder(int[] array, int left, int right) {
        int length = right - left + 1;
        // Base case
        if(length <= 2){
            return;
        }
        int mid = left + length / 2;
        int lmid = left + length / 4;
        int rmid = left + length * 3 / 4;
        reverse(array, lmid, mid - 1);
        reverse(array, mid, rmid - 1);
        reverse(array, lmid, rmid - 1);  // 这三个 reverse 就是 实现了 block 平移交换

        reorder(array, left, left + 2 * (lmid - left) - 1);   // 还是一分为二
        reorder(array, left + 2 * (lmid - left), right);
    }

    private void reverse(int[] array, int start, int end) {
        while (start < end) {
            int temp = array[end];
            array[end] = array[start];
            array[start] = temp;
            start++;
            end--;
        }
    }
  }