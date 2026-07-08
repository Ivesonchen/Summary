/**
 * Given a sorted linked list, delete all duplicates such that each element appear only once.

Input: 1->1->2

Output: 1->2
 */

 //Tao-Lu     双指针 循环寻找 duplicate 区间

 public class Solution {
    public ListNode removeDup(ListNode head) {
      // Write your solution here
  
      if(head == null || head.next == null) return head;
  
      ListNode cur = head;
  
      while(cur != null){
        ListNode next = cur.next;
  
        if(next == null) break;
  
        if(cur.value != next.value){
          cur = cur.next;
        } else {
          while(next != null && next.value == cur.value){
            next = next.next;
          }
          cur.next = next;
          cur = cur.next;
        }
      }
      return head;
    }
  }