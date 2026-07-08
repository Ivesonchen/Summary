/**
 * Given a sorted integer array, remove duplicate elements. 
 * For each group of elements with the same value keep only one of them. 
 * Do this in-place, using the left side of the original array and maintain the relative order of the elements of the array. 
 * Return the array after deduplication.
 */
/**
 * deduplication  想到 set的 特性  不太用想到 双指针的 奇淫技巧
 */

public class Solution {
  public int[] dedup(int[] array) {
    // Write your solution here
    int i = 0;
    int j = 0;

    Set<Integer> set = new HashSet<>();

    while(j < array.length) {
      if(!set.contains(array[j])){
        array[i] = array[j];
        set.add(array[j]);
        i++;
      }
      j++;
    }

    int[] res = new int[i];

    for(int m = 0; m < i; m++){
      res[m] = array[m];
    }

    return res;
    //    return Arrays.copyOfRange(array, 0, start);
  }
}

// 比较 i 和 start - 1 的值   如果不等  说明此位置的值是可以的   count 是可以进入下一个位置的
// start 就是下一个不同的数应该放的位置
public static int removeDuplicates(int[] nums) {
  if (nums == null || nums.length == 0) return 0;
  int start = 1;
  for (int i = 1; i < nums.length; i++) {
      if (nums[start - 1] != nums[i]) {
          nums[start++] = nums[i];
      }
  }
  return start;
} 