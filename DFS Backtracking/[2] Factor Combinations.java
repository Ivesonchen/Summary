/**
 * Given an integer number, return all possible combinations of the factors that can multiply to the target number.

Example

Give A = 24

since 24 = 2 x 2 x 2 x 3

              = 2 x 2 x 6

              = 2 x 3 x 4

              = 2 x 12

              = 3 x 8

              = 4 x 6

your solution should return

{ { 2, 2, 2, 3 }, { 2, 2, 6 }, { 2, 3, 4 }, { 2, 12 }, { 3, 8 }, { 4, 6 } }

note: duplicate combination is not allowed.
 */

public class Solution {
    public List<List<Integer>> combinations(int target) {
      // Write your solution here
      List<List<Integer>> res = new ArrayList<>();
  
      dfs(res, new ArrayList<>(), target, 2);
  
      return res;
    }
  
    public void dfs(List<List<Integer>> res, List<Integer> list, int target, int start){
      if(target == 1 && list.size() > 1){       //size > 1   确保了 不存在 [24] 这样的单个factor 的情况
        res.add(new ArrayList<>(list));
        return;
      }
  
      for(int i = start; i <= target; i++){     // 这里必须包含上target  因为target 在缩小  有可能 新的 start 直接等于 target
        if(target % i != 0) continue;
  
        list.add(i);
        dfs(res, list, target / i, i);          //  这里 target / i 变为新的 target i 变为新的start [1]
        list.remove(list.size() - 1);
      }
    }
  }

  /**
   * 
   * [1]  2 2 2 3  可以      2 3 2 2 是不需要出现的   发现结果都 是 非递减 序列
   */