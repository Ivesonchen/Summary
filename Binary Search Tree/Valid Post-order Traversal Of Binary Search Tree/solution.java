/**
 * Given an array with integers, determine whether the array contains a valid postorder traversal sequence a BST.

Assumptions:

The given postorder traversal array is not null.
Examples:

{ 3, 5, 4 }  is valid
{ 3,  6,  2,  5,  4} is not valid
 */

 /**
  * We don't need to actually construct the BST. We don't need any Node class. We can modify the method to construct BST a little
bit and recursively scan the whole array to check if it can be a valid BST postorder traversal.
Detail steps:
1, Use a private recursive helper method which takes three arguments: the array nodes[], int start, int end.
start and end set the current range of nodes in the array for current sub-tree.
2, In the helper method, if start<=end, that means this sub-tree is empty or has only one node, then it must be a valid BST.
So directly return true.
3, If start>end, first get the root value of current sub-tree which is the last node in current range, nodes[end]. Then traverse
back from nodes[end] to nodes[start] while i>=start && nodes[i]>nodes[end]. Then after the while loop, i will stay at the partition
point for the left and right sub-tree. In other words, i will be at the nearest node that is smaller than root, which is also
the last node in left sub-tree.
4, Then store the partition index i to another variable left and keep moving i to the left while i>=start && nodes[i]<nodes[end].
After the while loop, i should stay at the start-1 index. Because we just move i through all nodes belonging to left sub-tree.
5, Then check i to see if it is equal to start-1, if it's not, then we know there are values that don't satisfy BST, return false.
6, If i is equal to start-1, then for the current sub-tree, it could be a valid BST. So we recursively check for the left subtree
and the right subtree. 
For left subtree: start = start, end = left
For right subtree: start = left+1, end = end-1.
7, if both of left and right subtree are valid BST, return true. Otherwise, return false.
Time complexity: O(nlogn)  Space complexity: O(1)
  */

public class Solution {
    public boolean validPostOrder(int[] post) {
      // Write your solution here
      return helper(post, 0, post.length - 1);
    }
  
    public boolean helper(int[] post, int start, int end){
      if(end <= start) return true;
  
      int root = post[end];
      int i = end - 1;
      while(i >= start && post[i] > root) i--;
  
      int div = i;
  
      while(i >= start && post[i] < root) i--;
  
      if(i != start - 1) return false;
  
      return helper(post, start, div) && helper(post, div + 1, end - 1);
    }
  }