/**
 * Reorder the given singly-linked list N1 -> N2 -> N3 -> N4 -> … -> Nn -> null to be N1- > Nn -> N2 -> Nn-1 -> N3 -> Nn-2 -> … -> null

Examples

L = null, is reordered to null
L = 1 -> null, is reordered to 1 -> null
L = 1 -> 2 -> 3 -> 4 -> null, is reordered to 1 -> 4 -> 2 -> 3 -> null
L = 1 -> 2 -> 3 -> null, is reordred to 1 -> 3 -> 2 -> null

 */

// findMid()  reverse(mid.next) mergeTwoLinkedList()   注意要把mid.next = null    前后两个链要断开
public class Solution {
    public ListNode reorder(ListNode head) {
      // Write your solution here
  
      if(head == null || head.next == null) return head;
  
      ListNode mid = findMid(head);
      ListNode second = reverseList(mid.next);
      mid.next = null;
  
      ListNode dummy = new ListNode(-1);
      ListNode cur = dummy;
      ListNode cur1 = head;
      ListNode cur2 = second;
  
      while(cur1 != null && cur2 != null){
        ListNode next1 = cur1.next;
        ListNode next2 = cur2.next;
  
        cur.next = cur1;
        cur1.next = cur2;
  
        cur = cur2;
        cur1 = next1;
        cur2 = next2;
      }
  
      if(cur1 != null) cur.next = cur1;
  
      return dummy.next;
    }
  
    private ListNode reverseList(ListNode head){
      if(head == null || head.next == null) return head;
  
      ListNode cur = head;
      ListNode pre = null;
  
      while(cur != null){
        ListNode next = cur.next;
  
        cur.next = pre;
        pre = cur;
        cur = next;
      }
  
      return pre;
    }
  
    private ListNode findMid(ListNode head){
      ListNode slow = new ListNode(-1);
  
      slow.next = head;
      ListNode fast = slow;
  
      while(fast != null && fast.next != null){
        slow = slow.next;
        fast = fast.next.next;
      }
  
      return slow;
    }
  }
  