/**
 * Given a sorted integer array, remove duplicate elements. 
 * For each group of elements with the same value keep at most two of them. 
 * Do this in-place, using the left side of the original array and maintain the relative order of the elements of the array. 
 * Return the array after deduplication.

Assumptions

The given array is not null
Examples

{1, 2, 2, 3, 3, 3} → {1, 2, 2, 3, 3}
 */


  /**
   * 使用 hashmap 来进行计数     注意 null 值 的比较     end 指针开始走  对于每个新位置 检测计数器是不是 小于2  如果是 复制和更新计数器  如果不是 进入下一个end位置
   * O(n) O(n)
   */
public class Solution {
    public int[] dedup(int[] array) {
      // Write your solution here                                                                   
      int start = 0;
      int end = 0;
  
      Map<Integer, Integer> map = new HashMap<>();
  
      while(end < array.length){
        if(map.getOrDefault(array[end], 0) < 2){
          array[start] = array[end];
          map.put(array[end], map.getOrDefault(array[end], 0) + 1);
          start ++;
        }
        end++;
      }
      // start 是个 基础指针， end是个 每步都走的指针   最后start在的位置就是新数组的 右边界
      int[] res = new int[start];
      for(int i = 0; i < start; i ++){
        res[i] = array[i];
      }
      return res;

      // return Arrays.copyOfRange(array, 0, start);
  }

  /**
   * 间距为二的 来查找 重复的值  可以随意扩展 间距为 2， 3， 4， 5
   */
  public int[] dedup(int[] array){
    if(array.length < 2) return array;

    int start = 2;

    for(int i = 2; i < array.length; i++){
      if(array[i] != array[start - 2]){ //这个 start - 2       相当于  start 从0开始    i 从 0 开始
        array[start] = array[i];
        start++;
      }
    }

    int[] res = new int[start];

    for(int i = 0; i < start; i++){
      res[i] = array[i];
    }
    return res;
    //     return Arrays.copyOfRange(array, 0, start);
  }
}
