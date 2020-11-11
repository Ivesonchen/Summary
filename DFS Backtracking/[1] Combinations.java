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

public class Solution {
    public List<List<Integer>> combine(int n, int k) {
      // Write your solution here
      List<List<Integer>> res = new ArrayList<>();
      List<Integer> list = new ArrayList<>();
      if(n <= 0) return res;
      if(k <= 0){
        res.add(list);
        return res;
      }
      dfs(res, list, 1, n, k);
  
      return res;
    }
  
    public void dfs(List<List<Integer>> res, List<Integer> list, int depth, int n, int k){
      if(k == 0) {
        res.add(new ArrayList<>(list));
        return;
      }
  
      for(int i = depth; i <= n; i ++){
        list.add(i);
        dfs(res, list, i + 1, n, k - 1);
        list.remove(list.size() - 1);
      }
    }
}

// 另一种 backtracking 的方法
public void dfs(List<List<Integer>> res, List<Integer> list, int depth, int n, int k){
    if(list.size() == k){
        res.add(new ArrayList<>(list));
        return;
    }

    if(depth > n) return;// 在 depth 增加之前 给停掉 (不能提高到 check size 之前, 因为需要 check size 这个动作来作为 叶子节点 处理list结果)

    list.add(depth);
    dfs(res, list, depth + 1, n, k);
    list.remove(list.size() - 1);

    dfs(res, list, depth + 1, n, k);
}
