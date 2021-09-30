/**
 * Given a list, rotate the list to the right by k places, where k is non-negative.

Input: 1->2->3->4->5->NULL, k = 2

Output: 4->5->1->2->3->NULL

Input: 1->2->3->4->5->NULL, k = 12

Output: 4->5->1->2->3->NULL
 */

public class Solution {
    public ListNode rotateKplace(ListNode head, int n) {
      // Write your solution here
      if(head == null || head.next == null) return head;
      ListNode dummy = new ListNode(-1);
      dummy.next = head;
      ListNode cur = head;
      ListNode tail = dummy;
      int counter = 0;
  
      while(cur != null){
        tail = cur;
        cur = cur.next;
        counter ++;
      } // 
  
      int move = n % counter;
      
      if(move == 0) return head;
  
      ListNode now = dummy;
      int i = 0;
      while(i < counter - move){
        now = now.next;
        i++;
      }
      ListNode newHead = now.next;
      now.next = null;
      tail.next = dummy.next;
      dummy.next = newHead;
  
      return dummy.next;
    }
  }
  