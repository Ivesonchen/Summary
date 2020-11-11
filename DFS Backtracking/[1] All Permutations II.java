/**
 * Given a string with possible duplicate characters, return a list with all permutations of the characters.

Examples

Set = “abc”, all permutations are [“abc”, “acb”, “bac”, “bca”, “cab”, “cba”]
Set = "aba", all permutations are ["aab", "aba", "baa"]
Set = "", all permutations are [""]
Set = null, all permutations are []
 */
/**
 * The difficulty is to handle the duplicates.
With inputs as [1a, 1b, 2a],
If we don't handle the duplicates, the results would be: [1a, 1b, 2a], [1b, 1a, 2a]...,
so we must make sure 1a goes before 1b to avoid duplicates
By using nums[i-1]==nums[i] && !used[i-1], we can make sure that 1b cannot be choosed before 1a
 */

 // sort + input[i] == input[i - 1] && !visited[i-1]    用来处理duplicate的情况  只能 
// 不能只用used[i] true or false 来判断  因为duplicates顺序问题会导致最终的答案中 会有重复的排列(尽管duplicates顺序不一样)

/*
Another explanation for why both

1. if(i > 0 && nums[i] == nums[i - 1] && !use[i - 1]) continue;
and

2. if(i > 0 && nums[i] == nums[i - 1] && use[i - 1]) continue;
work is given below:

The problem for Permutation II is that different orders of duplicates should only be considered as one permutation. In other words, you should make sure that when these duplicates are selected, there has to be one fixed order.

Now take a look at code 1 and 2.

Code 1 makes sure when duplicates are selected, the order is ascending (index from small to large). However, code 2 means the descending order.
*/
public class Solution {
    public List<String> permutations(String input) {
      // Write your solution here
      List<String> res = new ArrayList<>();
      if(input == null) return res;
      if(input.length() == 0) {
        res.add("");
        return res;
      }
      char[] inputArray = input.toCharArray();
      Arrays.sort(inputArray);
  
      dfs(res, new StringBuilder(), inputArray, new boolean[inputArray.length]);
  
      return res;
    }
  
    public void dfs(List<String> res, StringBuilder sb, char[] input, boolean[] visited){
      if(sb.length() == input.length){
        res.add(sb.toString());
        return;
      }
  
      for(int i = 0; i < input.length; i++){
        if(visited[i] || i > 0 && input[i] == input[i - 1] && !visited[i-1]) continue;
  
        visited[i] = true;
        sb.append(input[i]);
        dfs(res, sb, input, visited);
        visited[i] = false;
        sb.deleteCharAt(sb.length() - 1);
      }
    }
  }