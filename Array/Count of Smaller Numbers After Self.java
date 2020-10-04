/**
 * Given an array A of length N containing all positive integers from [1...N]. How to get an array B such that B[i] represents how many elements A[j] (j > i) in array A that are smaller than A[i].

Assumptions:

The given array A is not null.
Examples:

A = { 4, 1, 3, 2 }, we should get B = { 3, 0, 1, 0 }.
Requirement:

time complexity = O(nlogn).
 */

 /**
    BST tree insert O(logn)
  */

public class Solution {
    class Node {
      int val;
      int count;
      int left_count;
      Node left;
      Node right;
      public Node(int val) { this.val = val; this.count = 1; }
      public int less_or_equal() { return count + left_count; }
    }
  
    public int[] countArray(int[] array) {
      // Write your solution here
      List<Integer> ans = new ArrayList<>();
      if (array.length == 0) return new int[0];
      int n = array.length;
      Node root = new Node(array[n - 1]);
      ans.add(0);
      for (int i = n - 2; i >= 0; --i)
        ans.add(insert(root, array[i]));
      Collections.reverse(ans);
  
      int[] resArray = new int[ans.size()];
      for(int i = 0; i < ans.size(); i ++){
        resArray[i] = ans.get(i);
      }
      return resArray;
    }
    
    private int insert(Node root, int val) {
      if (root.val == val) {
        ++root.count;
        return root.left_count;
      } else if (val < root.val) {
        ++root.left_count;
        if (root.left == null) {
          root.left = new Node(val);            
          return 0;
        } 
        return insert(root.left, val);
      } else  {
        if (root.right == null) {
          root.right = new Node(val);
          return root.less_or_equal();
        }
        return root.less_or_equal() + insert(root.right, val);
      }
    }
  }