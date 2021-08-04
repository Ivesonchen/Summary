/**
 * #### Recursive: Backtracking
- Given a remaining list: 取, 或者不取
- always iterate over full `nums[]`, use list.contains() to check if item has been added.
- Improvement: maintain list (add/remove elements) instead of 'list.contains'
- time O(n!): visit all possible outcome
- T(n) = n * T(n-1) + O(1)
 */
/*
Given a collection of distinct numbers, return all possible permutations.
For example,
[1,2,3] have the following permutations:
[
  [1,2,3],
  [1,3,2],
  [2,1,3],
  [2,3,1],
  [3,1,2],
  [3,2,1]
]
Challenge
Do it without recursion
Tags Expand 
Recursion Search
*/
class Solution {
    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        if (nums == null || nums.length == 0) {
            return result;
        }
        
        dfs(result, new ArrayList<>(), nums);
        return result;
    }
    
    private void dfs(List<List<Integer>> result, List<Integer> levelList, int[][] nums, int index ) {
        if (levelList.size() == nums.length) {
            result.add(new ArrayList<>(levelList));
            return;
        }
        for (int i = 0; i < nums[index].length; i++) {
            levelList.add(nums[i]);
            dfs(result, levelList, nums, indx+1);
            levelList.remove(levelList.size() - 1);
        }
    }

    private void dfs(List<List<Integer>> result, List<Integer> levelList, int[] nums) {
        if (levelList.size() == nums.length) {
            result.add(new ArrayList<>(levelList));
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (levelList.contains(nums[i])) continue;
            levelList.add(nums[i]);
            dfs(result, levelList, nums);
            levelList.remove(levelList.size() - 1);
        }
    }
}

/**
 *               a          b             c
 *            a  b  c     a b c         a b c
 */

 main(){
    nums[][]
    m, n
 }

dfs(nums, result, level, indexOfCurrRow){
    // add to result
    if(level.length === n) result.add(level)

    for(int i = 0; i < n, i++){
        level.add(nums[indexOfCurrRow][i]);
        dfs(result, level, nums, indexOfCurrRow+1);
        level.remove(levelList.size() - 1);
    }
}


