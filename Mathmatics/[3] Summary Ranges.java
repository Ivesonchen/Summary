/**
 * Given a sorted integer array without duplicates, return the summary of its ranges.

For example, given [0,1,2,4,5,7], return ["0->2","4->5","7"].
 */

public class Solution {
    public String[] summaryRanges(int[] nums) {
      // Write your solution here
      int len = nums.length;
      List<String> list = new ArrayList<>();
      int start = nums[0];
      int end = nums[0];
  
      for(int i = 1; i < len; i++){
        if(nums[i - 1] == nums[i] - 1){
          end = nums[i];
        } else {
          if(start != end){
            list.add(start + "->" + end);
          } else {
            list.add("" + start);
          }
          start = nums[i];
          end = nums[i];
        }
      }
      if(start != end){
        list.add(start + "->" + end);
      } else {
        list.add("" + start);
      }
      String[] res = new String[list.size()];
  
      for(int i = 0; i < list.size(); i++){
        res[i] = list.get(i);
      }
  
      return res;
    }
  }

public List summaryRanges(int[] nums) {
    int start = 0;
    int end = 0;
    List<String> list = new ArrayList<>();
    for(int i = 0; i < nums.length;i++){
        while(i < nums.length - 1 && nums[i] + 1 == nums[i + 1]){
            end++;
            i++;
        }
        if(start == end){
            list.add(String.valueOf(nums[start]));
        }else{
            list.add(nums[start] + "->" + nums[end]);
        }
        end++;
        start = end;

    }
   return list;
}