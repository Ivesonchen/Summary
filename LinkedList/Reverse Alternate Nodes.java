/**
 * Given a linked list, reverse alternate nodes and append at the end.

Examples:

Input List:    1 -> 2 -> 3 -> 4 -> 5 -> 6

Output List: 1 -> 3 -> 5 -> 6 -> 4 -> 2

Input List:    1 -> 2 -> 3 -> 4 -> 5

Output List: 1 -> 3 -> 5 -> 4 -> 2
 */

public class Solution {
    public ListNode reverseAlternate(ListNode head) {
      // Write your solution here
      if(head == null || head.next == null) return head;
      ListNode first = head, second = head.next, secondHead = head.next;
  
      while(second != null && second.next != null){
        first.next = first.next.next;
        second.next = second.next.next;
  
        first = first.next;
        second = second.next;
      }
      ListNode reversed = reverseList(secondHead);
      first.next = reversed;
      
      return head;
    }
  
    private ListNode reverseList(ListNode head){
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
  }