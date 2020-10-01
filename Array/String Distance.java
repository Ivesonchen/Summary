/**
 * Given an array of strings, and two different string s and t. We need to return the smallest indices difference between the two given strings.

Return -1 if we can not find s or t in the array.

Assumptions:

The given array is not null, none of the strings in the array is null.
s and t are different and they are not null.
Examples:

array =  {"this", "is", "a", "is", "fox", "happy"}, the distance of "fox" and "is" is 1.
 */

 // 暗藏的规则  一直保持最大的 sourceIndex 和 targetIndxe 就可以。 然后一直取最小的 res
 // O(n)

public class Solution {
    public int distance(String[] array, String source, String target) {
      // Write your solution here
      if(array.length == 0) return -1;
      int res = Integer.MAX_VALUE;
      int sourceIndex = -1;
      int targetIndex = -1;
  
      for(int i = 0; i < array.length; i++){
        if(array[i] == source){
          sourceIndex = i;
          if(targetIndex != -1) res = Math.min(res, Math.abs(sourceIndex - targetIndex));
        }
        if(array[i] == target){
          targetIndex = i;
          if(sourceIndex != -1) res = Math.min(res, Math.abs(sourceIndex - targetIndex));
        }
      }
      if(res == Integer.MAX_VALUE) return -1;
  
      return res;
    }
  }