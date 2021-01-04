/**
 * Return the primes less than or equals to target in ascending order.

Assumptions:

The given target is >= 2.
Examples:

target = 3,  return [2, 3]
target = 10, return [2, 3, 5, 7]

 */

// 筛子法 找质数
public class Solution {
    public List<Integer> primes(int target) {
      // Write your solution here
  
      boolean[] arr = new boolean[target + 1];
  
      for(int i = 2; i <= target; i++){
        if(arr[i]) continue;
        int j = 2;
        while(i * j <= target){
          arr[i * j] = true;
          j++;
        }
      }
  
      List<Integer> res = new ArrayList<>();
  
      for(int i = 2; i <= target; i++){
        if(!arr[i]) res.add(i);
      }
  
      return res;
    }
  }