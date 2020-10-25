/**
 * Given a binary Tree and the two keys, determine whether the two nodes are cousins of each other or not. Two nodes are cousins of each other if they are at the same level and have different parents.

Assumptions:

It is not guranteed the two keys are all in the binary tree.
There are no duplicate keys in the binary tree.
Examples:

     6
   /   \
  3     5
 / \   / \

7   8 1   13
7 and 1, result is true.
3 and 5, result is false.
7 and 5, result is false.
 */

public class Solution {
    TreeNode xParent = null;
    TreeNode yParent = null;
    int xDepth = -1, yDepth = -1;

    public boolean isCousin(TreeNode root, int a, int b) {
        // Write your solution here
        getDepthAndParent(root, a, b, 0, null);

        return xDepth == yDepth && xParent != yParent ? true : false;
    }

    public void getDepthAndParent(TreeNode root, int x, int y, int depth, TreeNode parent){
        if(root == null) return;

        if(root.key == x){
        xParent = parent;
        xDepth = depth;
        } else if(root.key == y){
        yParent = parent;
        yDepth = depth;
        }

        getDepthAndParent(root.left, x, y, depth + 1, root);
        getDepthAndParent(root.right, x, y, depth + 1, root);
    }
}