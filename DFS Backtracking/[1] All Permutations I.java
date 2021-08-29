/**
 * Given a string with no duplicate characters, return a list with all permutations of the characters.
Assume that input string is not null.

Examples
Set = “abc”, all permutations are [“abc”, “acb”, “bac”, “bca”, “cab”, “cba”]
Set = "", all permutations are [""]
 */

 // O(n!)

public class Solution {
    public List<String> permutations(String input) {
      // Write your solution here
      List<String> res = new ArrayList<>();
      if(input == null || input.length() == 0) {
        res.add("");
        return res;
      } 
  
      dfs(res, new StringBuilder(), new boolean[input.length()], input);
  
      return res;
    }
  
    public void dfs(List<String> res, StringBuilder sb, boolean[] visited, String input){
      if(sb.length() == input.length()){
        res.add(sb.toString());
        return;
      }
  
      for(int i = 0; i < input.length(); i++){
        if(visited[i]) continue;
  
        visited[i] = true;
        sb.append(input.charAt(i));
        dfs(res, sb, visited, input);
        visited[i] = false;
        sb.deleteCharAt(sb.length() - 1);
      }
    }
  }


public List<List<Integer>> permute(int[] nums) {
    List<List<Integer>> list = new ArrayList<>();
    // Arrays.sort(nums); // not necessary
    backtrack(list, new ArrayList<>(), nums);
    return list;
 }
 
 private void backtrack(List<List<Integer>> list, List<Integer> tempList, int [] nums){
    if(tempList.size() == nums.length){
       list.add(new ArrayList<>(tempList));
    } else{
       for(int i = 0; i < nums.length; i++){ 
          if(tempList.contains(nums[i])) continue; // element already exists, skip      使用原生方法检测走没走过
          tempList.add(nums[i]);
          backtrack(list, tempList, nums);
          tempList.remove(tempList.size() - 1);
       }
    }
 }



class test {

 public List<String> func (String str) {
   List<String> res = new ArrayList<>();

   if(str == null || str.length() == 0) {
     res.add("");
     return res;
   }

   dfs(res, new StringBuilder(), new boolean[str.length()], str);

   return res;
 }

 public static void dfs(List<String> res, StringBuilder sb, boolean[] visited, String str) {
   if(sb.length() === str.length()) {
     res.add(sb.toString());
     return;
   }

   for(int i = 0; i < str.length(); i ++){
     if(visited[i]) continue;
     visited[i] = true;
     sb.append(str.charAt(i));
     dfs(res, sb, visited, str);
     sb.deleteCharAt(str.length() - 1);
     visited[i] = false;
   }
 }
}