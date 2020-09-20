/**
 * Given two integers n and k, return all possible combinations of k numbers out of 1 ... n.

#### DFS, Backtracking
- for loop, recursive (dfs).
- 每个item用一次, 下一个level dfs(index + 1)
- Combination DFS. 画个图想想. 每次从1~n里面pick一个数字i
- 因为下一层不能重新回去 [0~i]选，所以下一层recursive要从i+1开始选。
 */


/*
Given two integers n and k, return all possible combinations of k numbers out of 1 ... n.
Example:
Input: n = 4, k = 2
Output:
[
  [2,4],
  [3,4],
  [2,3],
  [1,2],
  [1,3],
  [1,4],
]
*/

/*
dfs, for loop, track index, track size of list.
once size met, add to result, also no need to further dfs.
dfs: result,list, index, k, n
*/

class Solution {
    public List<List<Integer>> combine (int n, int k) {
        List<List<Integer>> result = new ArrayList<>();
        // check edge case
        if (k <= 0 || n <= 0) {
            return result;
        }

        // init result, dfs
        dfs(result, new ArrayList<>(), 1, k , n);
        return result;
    }

    private void dfs(List<List<Integer>> result, List<Integer> list, int index, int k, int n) {
        // for loop
        // check size, dfs with i + 1
        for (int i = index; i <= n; i++) {
            list.add(i);
            // add to result
            if (list.size() == k) {
                result.add(new ArrayList<>(list));
                list.remove(list.size() - 1);
                continue;
            }

            dfs(result, list, i + 1, k, n);
            list.remove(list.size() - 1);
        }
    }
}
