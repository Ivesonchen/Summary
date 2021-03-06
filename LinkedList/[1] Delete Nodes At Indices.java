/**
 * Given a linked list and an sorted array of integers as the indices in the list. Delete all the nodes at the indices in the original list.

Examples

1 -> 2 -> 3 -> 4 -> NULL, indices = {0, 3, 5}, after deletion the list is 2 -> 3 -> NULL.

Assumptions

the given indices array is not null and it is guaranteed to contain non-negative integers sorted in ascending order.
 */

// be careful with when to move the pointer which is pointing to the indeices, and also checking if it out of bounds

public class Solution {
    public ListNode deleteNodes(ListNode head, int[] indices) {
      // Write your solution here
      if(indices.length == 0) return head;
      ListNode dummy = new ListNode(-1);
      dummy.next = head;
  
      ListNode pre = dummy;
      ListNode cur = head;
  
      int i = 0, j = 0;
  
      while(cur != null){
  
        if(j < indices.length && i == indices[j]){
          pre.next = cur.next;
          cur = cur.next;
          i ++;
          j ++;
        } else {
          pre = cur;
          cur = cur.next;
          i ++;
        }
      }
  
      return dummy.next;
    }
  }
  