/**
 * Find the middle node of a given linked list.

Examples

L = null, return null
L = 1 -> null, return 1
L = 1 -> 2 -> null, return 1
L = 1 -> 2 -> 3 -> null, return 2
L = 1 -> 2 -> 3 -> 4 -> null, return 2
 */

public class Solution {
    public ListNode middleNode(ListNode head) {
      // Write your solution here
      if(head == null) return head;
      ListNode dummy = new ListNode(-1);
      dummy.next = head;
      ListNode slow = dummy;
      ListNode fast = dummy;
  
      while(fast != null && fast.next != null){
        slow = slow.next;
        fast = fast.next.next;
      }
  
      return slow;
    }
  }
  