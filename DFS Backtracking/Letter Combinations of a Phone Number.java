class Solution {
  public List<String> letterCombinations(String digits) {
      List<String> res = new ArrayList<>();
      if(digits.length() == 0) return res;

      String[] pad = new String[]{"", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"};
      
      dfs(pad, digits, 0, new StringBuilder(), res);
      
      return res;
      
  }
  
  public void dfs(String[] pad, String digits, int index, StringBuilder sb, List<String> res) {
      if (index == digits.length()) {
          res.add(sb.toString());
          return ;
      }
      
      String onePad = pad[Integer.valueOf(String.valueOf(digits.charAt(index)))];
      
      for(int i = 0; i < onePad.length(); i++) {
          sb.append(onePad.charAt(i));
          dfs(pad, digits, index + 1, sb, res);
          sb.deleteCharAt(sb.length() - 1);
      }
  }
}