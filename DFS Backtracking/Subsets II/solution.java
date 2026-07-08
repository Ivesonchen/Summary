/**
 * tags: Array, Backtracking, DFS, BFS
time: O(2^n)
sapce: O(2^n)

给一串integers(may have duplicates), 找到所有可能的subset. result里面不能有重复.

#### DFS
- DFS, 找准需要pass along的几个数据结构. 先`sort input`, 然后DFS
- Using for loop approach: 每个dfs call是一种可能性，直接add into result.     
- 为了除去duplicated result, skip used item at current level: `if (i > depth && nums[i] == nums[i - 1]) continue;`
- sort O(nlogn), subset: O(2^n)
- space O(2^n), save results
 */

 /*
Thoughts:
- have to sort the list, in order to skip items
- using the for loop approach: pick ONE item from the list at a time, with index i, then dfs with i + 1
- skip case: if we've picked an item for once at (i - 1), then in this particular for loop, we should not pick the same item
- thought, important: it can be picked in the next level of dfs
- subset, either take or not take : 2^n space, 2^n time
*/
class Solution {
    public List<List<Integer>> subsetsWithDup(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        if (nums == null || nums.length == 0) return result; // edge case

        Arrays.sort(nums);
        List<Integer> list = new ArrayList<>();

        // dfs with depth = 0
        result.add(new ArrayList<>(list));
        dfs(result, list, nums, 0);    
        return result;
    }

    private void dfs(List<List<Integer>> result, List<Integer> list, int[] nums, int depth) {
        for (int i = depth; i < nums.length; i++) {
            if (i > depth && nums[i] == nums[i - 1]) continue; // IMPORTANT, skip duplicate: i > depth && nums[i] == nums[i - 1]
            list.add(nums[i]);
            result.add(new ArrayList<>(list));
            dfs(result, list, nums, i + 1);
            list.remove(list.size() - 1);
        }
    }
}

/**
 * [1, 2, 2]           选 1, 2(1)     和 选  1, 2(2)  是一样的    在dfs树上 就要相应的剪枝 by checking nums[i] == nums[i - 1]
 */

//sort 很关键        然后通过比对 nums[i] 和 nums[i - 1]  排除重复的值 (跳过i指针的重复值   因为没有用处)

// 1 2 2 2 3       2 2 2 3  和 2 2 3  subset的结果  前者覆盖了后者