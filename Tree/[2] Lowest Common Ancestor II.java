/**
 * Given two nodes in a binary tree (with parent pointer available), find their lowest common ancestor.

Assumptions

There is parent pointer for the nodes in the binary tree

The given two nodes are not guaranteed to be in the binary tree

Examples

        5
      /   \
     9     12
   /  \      \
  2    3      14

The lowest common ancestor of 2 and 14 is 5

The lowest common ancestor of 2 and 9 is 9

The lowest common ancestor of 2 and 8 is null (8 is not in the tree)

some node can be not in this tree;
Parent node existing
 */

public class Solution {
    public ParentTreeNode lowestCommonAncestorII(ParentTreeNode root,
                                    ParentTreeNode A,ParentTreeNode B) {
        if (root == null || (A == null && B == null)) {
            return null;
        } else if (A == null || B == null) {
            return A == null ? B : A;
        }
        //Populate listA, listB
        ArrayList<ParentTreeNode> listA = new ArrayList<ParentTreeNode>();
        ArrayList<ParentTreeNode> listB = new ArrayList<ParentTreeNode>();

        while (A != root) {
            listA.add(0, A);
            A = A.parent;
        }
        listA.add(0, A);
        while (B != root) {
            listB.add(0, B);
            B = B.parent;
        }
        listB.add(0, B);

        int size = listA.size() > listB.size() ? listB.size() : listA.size();

        for (int i = 0; i < size; i++) {
            if (listA.get(i) != listB.get(i)) {
                return listA.get(i).parent;
            }
        }

        return listA.get(size - 1);
    }
}

public class Solution {
    public TreeNodeP lowestCommonAncestor(TreeNodeP one, TreeNodeP two) {
      // Write your solution here.
      int height1 = getHeight(one);
      int height2 = getHeight(two);
  
      if(height1 > height2){
        int diff = height1 - height2;
        while(diff > 0){
          one = one.parent;
          diff--;
        }
      } else if(height1 < height2){
        int diff = height2 - height1;
        while(diff > 0){
          two = two.parent;
          diff--;
        }
      }
      while(one != null && two != null && one != two){
        one = one.parent;
        two = two.parent;
      }
      return one;
    }
  
    public int getHeight(TreeNodeP one){
      int result = 0;
      while(one != null){
        result++;
        one = one.parent;
      }
      return result;
    }
  }

 /**
  * 给一个Binary Tree root, 以及两个node A, B. 特点: node里面存了parent pointer. 找 lowest common ancestor


#### Hash Set
- 这个题有个奇葩的地方, 每个node还有一个parent, 所以可以自底向上.
- save visited in hashset. 第一个duplicate, 就是A B 的 lowest common ancestor

#### Save in lists
- 自底向上。利用parent往root方向返回
- 把所有parent存下来, 然后在两个list里面找最后一个 common node

#### 注意
- 无法从root去直接搜target node 而做成两个list. 因为根本不是Binary Search Tree！
*/