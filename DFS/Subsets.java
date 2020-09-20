/**
 * tags: Array, Backtracking, DFS, BFS, Bit Manipulation
time: O(2^n)
space: O(2^n)

给一串unique integers, 找到所有可能的subset. result里面不能有重复.

#### DFS
- dfs的两种路子: 1. pick&&skip dfs, 2. for loop dfs
- 1. pick&&skip dfs: 取或者不取 + backtracking. 当level/index到底，return 一个list. Bottom-up, reach底部, 才生产第一个solution.
- 2. for loop dfs: for loop + backtracking. 记得：做subset的时候, 每个dfs recursive call是一种独特可能，先加进rst.  top-bottom: 有一个solution, 就先加上.
- Time&&space: subset means independent choice of either pick&&not pick. You pick n times: `O(2^n)`, 3ms
 */

// pick&&skip dfs, backtracking, 
// bottom-up: reach leaf to save result
class Solution {
    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        if (nums == null || nums.length == 0) return result;

        dfs(result, new ArrayList<>(), nums, 0); // dfs with depth = 0
        return result;
    }

    private void dfs(List<List<Integer>> result, List<Integer> list, int[] nums, int depth) {
        if (depth >= nums.length) { // closure case
            result.add(new ArrayList<>(list));
            return;
        }
        // pick
        list.add(nums[depth]);
        dfs(result, list, nums, depth + 1);

        // backtracking, and move to the not-pick option
        list.remove(list.size() - 1);
        dfs(result, list, nums, depth + 1);
    }
}


// for loop dfs:
// top-down, add each step as solution, as see fit
class Solution {
    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        if (nums == null || nums.length == 0) return result; // edge case

        List<Integer> list = new ArrayList<>();
        result.add(new ArrayList<>(list));

        // dfs with depth = 0
        dfs(result, list, nums, 0);    
        return result;
    }

    private void dfs(List<List<Integer>> result, List<Integer> list, int[] nums, int depth) {
        for (int i = depth; i < nums.length; i++) {
            list.add(nums[i]);
            result.add(new ArrayList<>(list));
            dfs(result, list, nums, i + 1);
            list.remove(list.size() - 1);
        }
    }
}