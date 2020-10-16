/**
 * Check if a given linked list has a cycle. Return true if it does, otherwise return false.

Assumption:

You can assume there is no duplicate value appear in the linked list.
 */
public class Solution {
    public boolean hasCycle(ListNode head) {
      // write your solution here
      if(head == null || head.next == null) return false;
  
      ListNode dummy = new ListNode(-1);
      dummy.next = head;
      ListNode slow = dummy;
      ListNode fast = dummy;
  
      while(fast != null && fast.next != null){
        slow = slow.next;
        fast = fast.next.next;
        if(fast == slow) return true;
      }
      return false;
    }
  }