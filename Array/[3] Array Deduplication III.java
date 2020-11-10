/**
 * Given a sorted integer array, remove duplicate elements. 
 * For each group of elements with the same value do not keep any of them. 
 * Do this in-place, using the left side of the original array and and maintain the relative order of the elements of the array. 
 * Return the array after deduplication.

Assumptions

The given array is not null
Examples

{1, 2, 2, 3, 3, 3} → {1}
 */

 // hashmap O(n) O(n)

/**
 * 三指针  相当于  
 * 
 * slow 是基础指针
 * 
 * begin 和 fast 来测量重复数字的长度（有几个） 
 *  
 * O(n) O(1)
 */
//Tao-Lu
public class Solution {
  public int[] dedup(int[] array) {
    // Write your solution here

    int fast = 0; 
    int slow = 0;

    int begin = slow;
    while(fast < array.length){

      while(fast < array.length && array[begin] == array[fast]){
        fast++;
      }// 寻找 有重复的 有几个数值    begin 和 fast 之间找到的 是一个相同数字的 区间 【1， 1】， 2，2，

      if(fast - begin == 1){
        array[slow] = array[begin];   // 这个数字只出现一次
        slow ++;
        // begin ++
      }
      begin = fast;  // 不管是 找到区间 为 1 还是区间为多个(有重复的元素)  begin的那个指针都是要跟上 fast指针然后还是重新寻找的
    }

    int[] res = new int[slow];

    for(int i = 0; i < slow; i++){
      res[i] = array[i];
    }

    if(slow == 0){
      return new int[0];
    }
    return res;
  }
}