/**
 * Merge K sorted lists into one big sorted list in ascending order.

Assumptions

ListOfLists is not null, and none of the lists is null.
 */

 // linkedList 自带 位置和数值信息
public class Solution {
    public ListNode merge(List<ListNode> listOfLists) {
      // Write your solution here
      ListNode dummy = new ListNode(-1);
      ListNode res = dummy;
      PriorityQueue<ListNode> pq = new PriorityQueue<>(new Comparator<ListNode>(){
        public int compare(ListNode node1, ListNode node2){
          return node1.value - node2.value;
        }
      });
  
      for(int i = 0; i < listOfLists.size(); i++){
        pq.add(listOfLists.get(i));
      }
  
      while(!pq.isEmpty()){
        ListNode cur = pq.poll();
        if(cur.next != null) pq.add(cur.next);
        res.next = cur;
        res = res.next;
      }
  
  
      return dummy.next;
    }
  }