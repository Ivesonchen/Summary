/**
 * Given a string s and a dictionary of strings wordDict, 
 * return true if s can be segmented into a space-separated sequence of one or more dictionary words.
Note that the same word in the dictionary may be reused multiple times in the segmentation.

Input: s = "leetcode", wordDict = ["leet","code"]
Output: true
Explanation: Return true because "leetcode" can be segmented as "leet code".

 * https://leetcode.com/problems/word-break/discuss/43886/Evolve-from-brute-force-to-optimal-a-review-of-all-solutions
 */

 class Solution {
    public boolean wordBreak(String s, List<String> wordDict) {    
      return wordBreak(0,s,new HashSet(wordDict));   
    }
    private boolean wordBreak(int p, String s, Set<String> dict){
        int n=s.length();
        if(p==n) {
            return true;
        }
        for(int i=p+1;i<=n;i++) {
            if(dict.contains(s.substring(p,i)) && wordBreak(i,s,dict)) {
                return true;
            }
        }
        return false;
    }
 }

// DFS + Memoization
class Solution {
  Boolean[] mem;

  public boolean wordBreak(String s, List<String> wordDict) {
    mem = new Boolean[s.length()];
    return wordBreak(0,s,new HashSet<String>(wordDict));
  }
  private boolean wordBreak(int p, String s, Set<String> dict){
    int n=s.length();
    if(p==n) {
        return true;
    }
    if(mem[p]!=null) {
        return mem[p];
    }
    for(int i=p+1;i<=n;i++) {
        if(dict.contains(s.substring(p,i)) && wordBreak(i,s,dict)) { 
            return mem[p]=true;
        }
    }
    return mem[p]=false;
  }
}
