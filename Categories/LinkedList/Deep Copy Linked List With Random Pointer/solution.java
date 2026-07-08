/**
 * Each of the nodes in the linked list has another pointer pointing to a random node in the list or null. 
 * Make a deep copy of the original list.
 */

 // 第一遍循环 生成所有的节点(地址) 并且和 原节点关联起来

public class Solution {
    public RandomListNode copy(RandomListNode head) {
      // Write your solution here.
      HashMap<RandomListNode, RandomListNode> map = new HashMap<>();
  
      RandomListNode cur = head;
      while(cur != null){
        map.put(cur, new RandomListNode(cur.value));
        cur = cur.next;
      } // key 是老node  value 是 新节点
      cur = head;
  
      while(cur != null){
        map.get(cur).next = map.get(cur.next);// 然后循环遍历 把 右边的节点按照左边的节点复制
        map.get(cur).random = map.get(cur.random); // 这个后面的get 就利用到了之前map的里生成新node的意义
        cur = cur.next;
      }
  
      return map.get(head);
    }
  }