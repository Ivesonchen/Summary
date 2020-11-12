/**
 * Given N pairs of parentheses “()”, return a list with all the valid permutations.

Assumptions

N > 0
Examples

N = 1, all valid permutations are ["()"]
N = 3, all valid permutations are ["((()))", "(()())", "(())()", "()(())", "()()()"]
 */

public class Solution {
    public List<String> validParentheses(int n) {
      // Write your solution here
      List<String> res = new ArrayList<>();
      if(n == 0) return res;
  
      dfs(res, new StringBuilder(), n, n);
  
      return res;
    }
  
    public void dfs(List<String> res, StringBuilder sb, int left, int right){
      if(left == 0 && right == 0){
        res.add(sb.toString());
        return;
      } else if (left < 0 || right < 0){
        return;
      }
  
      sb.append("(");
      dfs(res, sb, left - 1, right);
      sb.deleteCharAt(sb.length() - 1);
  
      if(left < right){                         // 不能一对一的那个left 还没出现 right 就先出现了
        sb.append(")");
        dfs(res, sb, left, right - 1);
        sb.deleteCharAt(sb.length() - 1);
      }
    }
  }
  