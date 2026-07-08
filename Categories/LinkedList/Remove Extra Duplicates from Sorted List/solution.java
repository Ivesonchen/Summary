/**
 * Given a sorted linked list, delete all nodes that have duplicate numbers, leaving only distinct numbers from the original list.

Input:  1->2->3->3->4->4->5

Output: 1->2->5    

Input:  1->1->1

Output: NULL
 */

 /**        #   1   2   3   3   4   4   5
  *        pre cur
        循环寻找 next 然后 使用pre 连接 停下来的 next指针
  */

public class Solution {
    public ListNode removeDup(ListNode head) {
      // Write your solution here
      if(head == null || head.next == null) return head;
  
      ListNode dummy = new ListNode(-1);
      dummy.next = head;
  
      ListNode pre = dummy;
      ListNode cur = head;
  
      while(cur != null){
        ListNode next = cur.next;
        if(next == null) break;
        if(cur.value != next.value){
          pre = pre.next;
          cur = cur.next;
        } else {
          while(next != null && cur.value == next.value){
            next = next.next;
          }
          pre.next = next;
          cur = next;
        }
  
      }
      return dummy.next;
    }
  }