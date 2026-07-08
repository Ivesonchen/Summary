/**
 * Given a string s, return all the palindromic permutations (without duplicates) of it. 
 Return an empty list if no palindromic permutation could be form.

Example 1:

Input: "aabb"
Output: ["abba", "baab"]
Example 2:

Input: "abc"
Output: []
 */
 // Time exceed  
class Solution {
    public List<String> generatePalindromes(String s) {
      // Write your solution here
      List<String> res = new ArrayList<>();
      if(s == null || s.length() == 0) return res;
  
      char[] inputArr = s.toCharArray();
      Arrays.sort(inputArr);
  
      dfs(res, new StringBuilder(), inputArr, new boolean[s.length()]);
  
      return res;
    }
  
    public void dfs(List<String> res, StringBuilder sb, char[] input, boolean[] visited){
      if(sb.length() == input.length){
        String str = sb.toString();
        if(isPalindrome(str)) {
          res.add(str);
          return;
        } else return;
        // res.add(str);
        // return;
      }
      for(int i = 0; i < input.length; i++){
        if(visited[i] || i > 0 && input[i] == input[i-1] && !visited[i - 1]) continue;
        visited[i] = true;
        sb.append(input[i]);
        dfs(res, sb, input, visited);
        visited[i] = false;
        sb.deleteCharAt(sb.length() - 1);
      }
    }
  
    public boolean isPalindrome(String str){
      int left = 0;
      int right = str.length() - 1;
  
      while(left <= right){
        if(str.charAt(left) != str.charAt(right)) return false;
        left++;
        right--;
      }
      return true;
    }
  }



  // standerd backtrack   with little bit modified
class Solution {
    List<String> res = new ArrayList<>();
    public List<String> generatePalindromes(String s) {
        int[] count = new int[256];
        int odd = 0;
        for (char c: s.toCharArray()) {
            count[c]++;
            if (count[c] % 2 == 1) odd++;
            else odd--;
        }
        // 计算字符频率 奇数频率
        List<String> ans = new ArrayList<>();
        if (odd > 1 || s.length() == 0) return ans;
        StringBuilder sb = new StringBuilder();
        if (odd == 1) {
            for (int i = 0; i < count.length; i++) {
                if (count[i] % 2 == 1) {
                    count[i]--;
                    sb.append((char) i);
                    break;
                }
            }
        }
        backtracking(count, sb, s.length());
        return res;
    }
    private void  backtracking(int[] count, StringBuilder sb, int len) {
        if (sb.length() == len) {
            res.add(sb.toString());
        } else {
            for (int i = 0; i < 256; i++) {
                if (count[i] > 0) {
                    count[i] -= 2;
                    sb.insert(0, (char) i);
                    sb.append((char) i);
                    backtracking(count, new StringBuilder(sb), len);
                    count[i] += 2;
                    sb.setLength(sb.length() - 1);
                    sb.deleteCharAt(0);
                }
            }
        }
    }
}

// only deal with half of the string source
public class Solution {
    Set < String > set = new HashSet < > ();
    public List < String > generatePalindromes(String s) {
        int[] map = new int[128];
        char[] st = new char[s.length() / 2];
        if (!canPermutePalindrome(s, map))
            return new ArrayList < > ();
        char ch = 0;
        int k = 0;
        for (int i = 0; i < map.length; i++) {
            if (map[i] % 2 == 1)
                ch = (char) i;
            for (int j = 0; j < map[i] / 2; j++) {
                st[k++] = (char) i;
            }
        }
        permute(st, 0, ch);
        return new ArrayList < String > (set);
    }
    public boolean canPermutePalindrome(String s, int[] map) {
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            map[s.charAt(i)]++;
            if (map[s.charAt(i)] % 2 == 0)
                count--;
            else
                count++;
        }
        return count <= 1;
    }
    public void swap(char[] s, int i, int j) {
        char temp = s[i];
        s[i] = s[j];
        s[j] = temp;
    }
    void permute(char[] s, int l, char ch) {
        if (l == s.length) {
            set.add(new String(s) + (ch == 0 ? "" : ch) + new StringBuffer(new String(s)).reverse());
        } else {
            for (int i = l; i < s.length; i++) {
                if (s[l] != s[i] || l == i) {
                    swap(s, l, i);
                    permute(s, l + 1, ch);
                    swap(s, l, i);
                }
            }
        }
    }
}