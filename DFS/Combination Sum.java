/**
 * time: O(n!)
space: O(n!)

给一串数字candidates (no duplicates), 和一个target. 

找到所有unique的 组合(combination) int[], 要求每个combination的和 = target.

注意: 同一个candidate integer, 可以用任意多次.


#### DFS, Backtracking
- 考虑input: 没有duplicate, 不需要sort
- 考虑重复使用的规则: 可以重复使用, 那么for loop里面dfs的时候, 使用curr index i
- the result is trivial, save success list into result.

##### Time complexity for Combination (reuse-candidate)
- at each level dfs, we have the index as starting point: 
- if we are at `index=0, we can have n child dfs() options via for loop`; 
- if at `index=1, we will have (n-1) dfs options via for loop`. 
- Consider it as the `pick/not-pick` problem, where the difference is you can pick `x` times at each index rather than only 2 times. 
- Overall, we will multiply the # of possibilities: n * (n - 1) * (n - 2) ... * 1 = n! => `O(n!)`

##### Combination DFS 思想
- 在每个index上面都要面临: `pick/not pick的选择`, 用for loop over index + backtracking 实现 picks.
- 每次pick以后, 就生成一条新的routine, from this index
- 下一个level的dfs从这个index开始, 对后面(或者当下/if allow index reuse) 进行同样的 pick/not pick 的选择
- 注意1: 每个level dfs 里面, for loop 里会有 end condition: 就不必要dfs下去了.
- 注意2: Backtracking在success case && dfs case 后都要做, 因为backtrack 是为了之前上一层dfs.
 */

 /*
- one item can be picked for multiple times:
- use dfs, for loop to aggregate candidates
- do target - val to track, and when target == 0, that’s a solution
- dfs(curr index i), instead of (i + 1): allows reuse of item
*/
class Solution {
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> result = new ArrayList<>();
        if (validate(candidates, target)) return result;

        dfs(result, new ArrayList<>(), candidates, 0, target);
        return result;
    }

    // for loop, where dfs is performed
    private void dfs(List<List<Integer>> result, List<Integer> list,
                     int[] candidates, int index, int target) {
        for (int i = index; i < candidates.length; i++) {
            int value = candidates[i];
            list.add(value);
            if (target == value) result.add(new ArrayList<>(list)); // one closure
            else if (target - value > 0) dfs(result, list, candidates, i, target - value);
            list.remove(list.size() - 1); // backtrack
        }
    }

    private boolean validate(int[] candidates, int target) {
        return candidates == null || candidates.length == 0 || target <= 0;
    }
}


/**
 *      1  2  3  4  5
        i
        i->
           i->
 */