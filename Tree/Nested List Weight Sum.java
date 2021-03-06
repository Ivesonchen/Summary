/**
 * Given a nested list of integers represented by a string without blank, parse the string and  return the sum of all integers in the list weighted by their depth.

Each element is either an integer, or a list -- whose elements may also be integers or other lists.

Example 1:
Given the list "[[1,1],2,[1,1]]", return 10. (four 1's at depth 2, one 2 at depth 1)

Example 2:
Given the list "[1,[4,[6]]]", return 27. (one 1 at depth 1, one 4 at depth 2, and one 6 at depth 3; 1 + 4*2 + 6*3 = 27)
From LaiCode
 */
// String parse
public class Solution {
    public int depthSum(String nestlists) {
      // Write your solution here
      int ans = 0;
      int depth = 0;
      int num = 0;
      boolean positive = true;
  
      for(int i = 0; i < nestlists.length(); i++){
        char c = nestlists.charAt(i);
        if(c == '['){
          depth ++;
        } else if (c == ','){
          if(positive) ans += num * depth;
          else ans -= num * depth;
          num = 0;
          positive = true;
        } else if(c == ']'){
          if(positive) ans += num * depth;
          else ans -= num * depth;
          num = 0;
          positive = true;
          depth --;
        } else if(c == '-'){
          positive = false;
        } else {
          num = num * 10 + (c - '0');
        }
      }
  
      return ans;
    }
  }

 /**
From LeetCode
Given a nested list of integers, return the sum of all integers in the list weighted by their depth.
Each element is either an integer, or a list -- whose elements may also be integers or other lists.
/**
 * // This is the interface that allows for creating nested lists.
 * // You should not implement it, or speculate about its implementation
 * public interface NestedInteger {
 *     // Constructor initializes an empty nested list.
 *     public NestedInteger();
 *
 *     // Constructor initializes a single integer.
 *     public NestedInteger(int value);
 *
 *     // @return true if this NestedInteger holds a single integer, rather than a nested list.
 *     public boolean isInteger();
 *
 *     // @return the single integer that this NestedInteger holds, if it holds a single integer
 *     // Return null if this NestedInteger holds a nested list
 *     public Integer getInteger();
 *
 *     // Set this NestedInteger to hold a single integer.
 *     public void setInteger(int value);
 *
 *     // Set this NestedInteger to hold a nested list and adds a nested integer to it.
 *     public void add(NestedInteger ni);
 *
 *     // @return the nested list that this NestedInteger holds, if it holds a nested list
 *     // Return null if this NestedInteger holds a single integer
 *     public List<NestedInteger> getList();
 * }
 */
// BFS
public int depthSum(List<NestedInteger> nestedList) {
    if(nestedList == null){
        return 0;
    }
    
    int sum = 0;
    int level = 1;
    
    Queue<NestedInteger> queue = new LinkedList<NestedInteger>(nestedList);
    while(queue.size() > 0){
        int size = queue.size();
        
        for(int i = 0; i < size; i++){
            NestedInteger ni = queue.poll();
            
            if(ni.isInteger()){
                sum += ni.getInteger() * level;
            }else{
                queue.addAll(ni.getList());
            }
        }
        
        level++;
    }
    
    return sum;
}