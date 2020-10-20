/**
 * Given a linked list, reverse the nodes of a linked list k at a time and return its modified list. If the number of nodes is not a multiple of k then left-out nodes in the end should remain as it is. You may not alter the values in the nodes, only nodes itself may be changed.

Example

Given this linked list: 1->2->3->4->5

For k = 2, you should return: 2->1->4->3->5

For k = 3, you should return: 3->2->1->4->5
 */

public class Solution {
    public ListNode reverseKGroup(ListNode head, int k) {
      // Write your solution here
      if(head == null || head.next == null) return head;
  
      ListNode dummy = new ListNode(-1);
      dummy.next = head;
  
      ListNode start = dummy;
      ListNode end = dummy;
      int counter = 0;
      
      while(end != null){
        while(end != null && counter < k){
          end = end.next;
          counter ++;
        }
  
        if(counter == k && end != null){
          ListNode next = end.next;
          ListNode h = start.next;
  
          end.next = null;
          start.next = reverse(h);
          h.next = next;
          counter = 0;
          start = h;
          end = h;
        }
        
      }
  
      return dummy.next;
    }
  
    private ListNode reverse(ListNode head){
      ListNode pre = null;
      ListNode cur = head;
  
      while(cur != null){
        ListNode next = cur.next;
        cur.next = pre;
  
        pre = cur;
        cur = next;
      }
      return pre;
    }
  }