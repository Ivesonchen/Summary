/**
 * Given an integer n, generate all structurally unique BST's (binary search trees) that store values 1 ... n.

Example:

Input: 3
Output:
[
  [1,null,3,2],
  [3,2,null,1],
  [3,1,null,null,2],
  [2,1,3],
  [1,null,2,null,3]
]
Explanation:
The above output corresponds to the 5 unique BST's shown below:

   1         3     3      2      1
    \       /     /      / \      \
     3     2     1      1   3      2
    /     /       \                 \
   2     1         2                 3
 */
/**
 * I start by noting that 1..n is the in-order traversal for any BST with nodes 1 to n. 
 * So if I pick i-th node as my root, the left subtree will contain elements 1 to (i-1), 
 * and the right subtree will contain elements (i+1) to n. 
 * I use recursive calls to get back all possible trees for left and right subtrees and combine them in all possible ways with the root.
 */
class Solution {
    public List<TreeNode> generateTrees(int n) {
        if(n == 0) return new ArrayList<TreeNode>();
        return genTreeList(1, n);
    }
    
    public List<TreeNode> genTreeList(int start, int end) {
        List<TreeNode> list = new ArrayList<>();
        if(start > end) {
            list.add(null);
        }
        
        for(int i = start; i <= end; i ++){
            List<TreeNode> leftList = genTreeList(start, i - 1);
            List<TreeNode> rightList = genTreeList(i + 1, end);
            for(TreeNode left : leftList){
                for(TreeNode right : rightList){
                    TreeNode root = new TreeNode(i);
                    root.left = left;
                    root.right = right;
                    list.add(root);
                }
            }
        }
        return list;
    }
}