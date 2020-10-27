/**
 * Implement a solution to parse a ternary expression into a tree.

Assumptions:

The input ternary expression is a string, and it is guaranteed to be valid.
Examples:

a?b:c  -->

   a

 /   \

b     c

a?b?c:d:e  -->

      a

    /    \

  b       e

/    \

c    d
 */

public class Solution {
    public ExpNode tree(String exp) {
      // Write your solution here.
      return helper(exp, new int[1]);
    }
  
    public ExpNode helper(String exp, int[] i){
      if(i[0] >= exp.length()) return null;
  
      ExpNode root = new ExpNode(exp.charAt(i[0]));
      i[0]++;
      
      if(i[0] < exp.length() && exp.charAt(i[0]) == '?'){
        i[0]++;
        root.left = helper(exp, i);
      }
  
      if(i[0] < exp.length() && exp.charAt(i[0]) == ':' && root.left != null){
        i[0]++;
        root.right = helper(exp, i);
      }
  
      return root;
    }
  }