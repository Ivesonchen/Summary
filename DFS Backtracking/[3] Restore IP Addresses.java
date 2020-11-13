/**
 * Given a string containing only digits, restore it by retiring all possible valid IP address combinations.

Input:  ”25525511135”

Output: [“255.255.11.135”, “255.255.111.35”]
 */

public class Solution {
    public List<String> Restore(String ip) {
      // Write your solution here
      List<String> res = new ArrayList<>();
  
      if(ip.length() > 12) return res;
  
      dfs(res, new ArrayList<>(), ip, 0, 0);
  
      return res;
    }
  
    public void dfs(List<String> res, List<String> list, String ip, int start, int len){
      if(list.size() > 4 || len > ip.length()) return;
      if(list.size() == 4 && len == ip.length()){
        res.add(String.join(".", list));
        return;
      }
  
      for(int i = 1; i <= 3; i++){
        if(start + i > ip.length()) break;
        String str = ip.substring(start, start + i);
  
        if(!isValid(str)) continue;
  
        list.add(str);
        dfs(res, list, ip, start + i, len + i);
        list.remove(list.size() - 1);
      }
    }
  
    public boolean isValid(String s){
      if(s.length() > 1 && s.startsWith("0")) return false;
  
      int n = Integer.valueOf(s);
      return n <= 255;
    }
}