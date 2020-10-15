/**
 * Reverse pairs of elements in a singly-linked list.

Examples

L = null, after reverse is null
L = 1 -> null, after reverse is 1 -> null
L = 1 -> 2 -> null, after reverse is 2 -> 1 -> null
L = 1 -> 2 -> 3 -> null, after reverse is 2 -> 1 -> 3 -> null

 */

public class Solution {
    public ListNode reverseInPairs(ListNode head) {
      // Write your solution here
      if(head == null || head.next == null) return head;
  
      ListNode dummy = new ListNode(-1);
      dummy.next = head;
  
      ListNode pre = dummy;
      ListNode cur = head;
  
  
      while(cur != null && cur.next != null){
        ListNode first = cur;
        ListNode second = cur.next;
  
        first.next = second.next;
  
        second.next = first;
  
        pre.next = second;
        
        pre = first;
        cur = first.next;
      }
      return dummy.next;
    }
  }