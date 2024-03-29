/**
 * One way to serialize a binary tree is to use pre-order traversal. 
 * When we encounter a non-null node, we record the node's value. 
 * If it is a null node, we record using a sentinel value such as #.

      9
    /   \
   3     2
  / \   / \
 4   1  #  6
/ \ / \   / \
# # # #   # #
For example, the above binary tree can be serialized to the string "9,3,4,#,#,1,#,#,2,#,6,#,#", 
where # represents a null node.

Given a string of comma separated values, verify whether it is a correct preorder traversal serialization of a binary tree. 
Find an algorithm without reconstructing the tree.

Each comma separated value in the string must be either an integer or a character '#' representing null pointer.

You may assume that the input format is always valid, 
for example it could never contain two consecutive commas such as "1,,3".

Example 1:
"9,3,4,#,#,1,#,#,2,#,6,#,#"
Return true

#

Example 2:
"1,#"
Return false

Example 3:
"9,#,#,1"
Return false
 */

// using a stack, scan left to right
// case 1: we see a number, just push it to the stack
// case 2: we see #, check if the top of stack is also #
// if so, pop #, pop the number in a while loop, until top of stack is not #
// if not, push it to stack
// in the end, check if stack size is 1, and stack top is #
public class Solution {
    public boolean isValidSerialization(String preorder) {
      // Write your solution here
      if(preorder == null) return false;
  
      Stack<String> st = new Stack<>();
  
      String[] strs = preorder.split(",");
  
      for(int pos = 0; pos < strs.length; pos++){
        String cur = strs[pos];
        while(cur.equals("#") && !st.isEmpty() && st.peek().equals(cur)){ // 一次删除两个 （删除一个小树）
          st.pop();
          if(st.isEmpty()) return false;
  
          st.pop();
        }
        st.push(cur);
      }
      return st.size() == 1 && st.peek().equals("#");
    }
  }

/**
    Some used stack. Some used the depth of a stack. Here I use a different perspective. 
    In a binary tree, if we consider null as leaves, 
    then all non-null node provides 2 outdegree and 1 indegree (2 children and 1 parent), except root
         all null node provides 0 outdegree and 1 indegree (0 child and 1 parent).
    Suppose we try to build this tree. 
    
    During building, we record the difference between out degree and in degree diff = outdegree - indegree. 
    When the next node comes, we then decrease diff by 1, because the node provides an in degree. 
    If the node is not null, we increase diff by 2, because it provides two out degrees. 
    If a serialization is correct, diff should never be negative and diff will be zero when finished.
 */
  public boolean isValidSerialization(String preorder) {
    String[] nodes = preorder.split(",");
    int diff = 1;
    for (String node: nodes) {
        if (--diff < 0) return false;
        if (!node.equals("#")) diff += 2;
    }
    return diff == 0;
}