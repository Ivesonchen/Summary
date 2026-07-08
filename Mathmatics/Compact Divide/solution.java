/**
 * Given two integers a and b, return the result of a / b in String with compact format. 
 * The repeated decimal part should be identified and enclosed by "()".

Examples

0 / 2 = "0"

4 / 2 = "2"

1 / 2 = "0.5"

-14 / 12 = "-1.1(6)"

1 / 11 = "0.(09)"

1 / 0 = "NaN"

-1 / 0 = "NaN"
 */

// 整数部分 正常除法    小数部分 利用map 记录重复部分的位置 之后再添加括号

public class Solution {
    public String divide(int a, int b) {
      // Write your solution here
      if(b == 0) return "NaN";
      StringBuilder res = new StringBuilder();
  
      res.append(((a >= 0) ^ (b >= 0)) ? "-" : "");
  
      long aa = Math.abs((long)a);
      long bb = Math.abs((long)b);
  
      res.append(aa / bb);
      aa %= bb;
      if(aa == 0) return res.toString();
  
      res.append(".");
  
      HashMap<Long, Integer> map = new HashMap<>();
      map.put(aa, res.length());
  
      while(aa != 0){
        aa *= 10;
        res.append(aa / bb);
        aa %= bb;
        if(map.containsKey(aa)){
          int index = map.get(aa);
          res.insert(index, "(");
          res.append(")");
          break;
        } else {
          map.put(aa, res.length());
        }
      }
      return res.toString();
    }
  }