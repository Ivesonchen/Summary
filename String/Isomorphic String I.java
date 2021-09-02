/**
 * 
 * Two Strings are called isomorphic if the letters in one String can be remapped to get the second String. Remapping a letter means replacing all occurrences of it with another letter. The ordering of the letters remains unchanged. The mapping is two way and no two letters may map to the same letter, but a letter may map to itself. Determine if two given String are isomorphic.

Assumptions:

The two given Strings are not null.
Examples:

"abca" and "xyzx" are isomorphic since the mapping is 'a' <-> 'x', 'b' <-> 'y', 'c' <-> 'z'.

"abba" and "cccc" are not isomorphic.
 */

// 要排除 一对多 和 多对一 的情况   f -> g   g -> h   h -> f  这种情况是可以的

public class Solution {
  public boolean isomorphic(String source, String target) {
    // Write your solution here
    if(source.length() != target.length()) return false;
    Map<Character, Character> map = new HashMap<>();
    Set<Character> set = new HashSet<>();

    int i = 0;

    while(i < source.length()){
      char sourceChar = source.charAt(i);
      char targetChar = target.charAt(i);

      if(map.get(sourceChar) != null && targetChar != map.get(sourceChar) || map.get(sourceChar) == null && set.contains(targetChar)){
        return false;
      }

      map.put(sourceChar, targetChar);
      set.add(targetChar);
      // map.put(targetChar, sourceChar);
      i++;
    }

    return true;
  }
}