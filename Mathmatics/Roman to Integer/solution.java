/**
 * Roman numerals are represented by seven different symbols: I, V, X, L, C, D and M.

    Symbol       Value
    I             1
    V             5
    X             10
    L             50
    C             100
    D             500
    M             1000

    For example, two is written as II in Roman numeral, just two one's added together. 
    Twelve is written as, XII, which is simply X+ II. The number twenty seven is written as XXVII, which is XX + V + II.

    Roman numerals are usually written largest to smallest from left to right. 
    However, the numeral for four is not IIII. Instead, the number four is written as IV. 
    Because the one is before the five we subtract it making four. The same principle applies to the number nine, which is written as IX. 
    There are six instances where subtraction is used:

    I can be placed before V (5) and X(10) to make 4 and 9. 
    X can be placed before L (50) and C (100) to make 40 and 90. 
    C can be placed before D (500) and M (1000) to make 400 and 900.
    Given a roman numeral, convert it to an integer. Input is guaranteed to be within the range from 1 to 3999.

    https://github.com/grandyang/leetcode/issues/13
*/


/**
 * 而这道题好就好在没有让我们来验证输入字符串是不是罗马数字，这样省掉不少功夫。需要用到 HashMap 数据结构，来将罗马数字的字母转化为对应的整数值，
 * 因为输入的一定是罗马数字，那么只要考虑两种情况即可：

    第一，如果当前数字是最后一个数字，或者之后的数字比它小的话，则加上当前数字。

    第二，其他情况则减去这个数字。
 */
public class Solution {
    public int romanToInt(String input) {
      // Write your solution here
      Map<Character, Integer> map = new HashMap<>();
  
      map.put('I', 1);
      map.put('V', 5);
      map.put('X', 10);
      map.put('L', 50);
      map.put('C', 100);
      map.put('D', 500);
      map.put('M', 1000);
      int res = 0;
      for(int i = 0; i < input.length(); i++){
  
        int pre = i < 1 ? 0 : map.get(input.charAt(i - 1));
        int cur = map.get(input.charAt(i));
        
        res += cur;
        if(pre < cur) {
          res  -= (2 * pre) ;
        }
      }
  
      return res;
    }
  }
  