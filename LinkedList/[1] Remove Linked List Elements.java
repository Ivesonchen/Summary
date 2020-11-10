/**
 * Remove all elements from a linked list of integers that have value val.

Example
Given: 1 --> 2 --> 6 --> 3 --> 4 --> 5 --> 6, val = 6
Return: 1 --> 2 --> 3 --> 4 --> 5
 */

public class Solution {
    public ListNode removeElements(ListNode head, int val) {
      // Write your solution here
      ListNode dummy = new ListNode(-1);
      dummy.next = head;
  
      ListNode pre = dummy;
      ListNode cur = head;
  
      while(cur != null){
        if(cur.value == val){
          pre.next = cur.next;
          cur = cur.next;
        } else {
          pre = cur;
          cur = cur.next;
        }
      }
  
      return dummy.next;
    }
  }