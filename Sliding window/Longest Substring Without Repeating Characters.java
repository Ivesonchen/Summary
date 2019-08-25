/**
 * Given a string, 
 * find the longest substring without any repeating characters and return the length of it. 
 * The input string is guaranteed to be not null.

For example, the longest substring without repeating letters for "bcdfbd" is "bcdf", 
we should return 4 in this case.
 */

 /**
  * 方法1:
Two Pointers
双指针: 从start开始遍历, 但是第一步是while loop来推进end; 直到推不动end, 然后start++
巧妙点: 因为end是外围variable, 在start的loop上, end不会重置;[star ~ end] 中间不需要重复计算.
最终可以O(n);

通过 first duplicate 解锁了 第二个字符
*/

class Solution {
    public int lengthOfLongestSubstring(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        boolean[] chars = new boolean[256]; // 256 ASCII code
        int rst = 0;
        int start = 0;
        int end = 0;
        while (start < s.length()) {
            while (end < s.length() && !chars[s.charAt(end)]) {
                chars[s.charAt(end)] = true;
                rst = Math.max(rst, end - start + 1);
                end++;
            }
            chars[s.charAt(start)] = false;
            start++;
        }
        return rst;
    }
}

/*
Thoughts:
1. Use hashmap<c, index> to mark indexes of chars.
2. When no duplicate, put in map, and compare Math.max(rst, map.size())
3. If duplicated c appears, should ignore all index before the fist c, and start fresh:
    reset i = map.get(c); map = new HashMap<>(), clean up the hash.
Time:
O(n^2): when 'abcdefg.....xyza' happends    contains  惹的祸
*/
class Solution {
    public int lengthOfLongestSubstring(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        HashMap<Character, Integer> map = new HashMap<>();
        char[] arr = s.toCharArray();
        int rst = Integer.MIN_VALUE;
        for (int i = 0; i < arr.length; i++) {
            char c = arr[i];
            if (map.containsKey(c)) {
                i = map.get(c); // reset beginning
                map = new HashMap<>();
            } else {
                map.put(c, i);
            }
            rst = Math.max(rst, map.size());
        }
        return rst == Integer.MIN_VALUE ? 0 : rst;
    }
}