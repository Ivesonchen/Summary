/**
 * Given a linked list, check whether it is a palindrome.

Examples:

Input:   1 -> 2 -> 3 -> 2 -> 1 -> null

output: true.

Input:   1 -> 2 -> 3 -> null  

output: false.

Requirements:

Space complexity must be O(1)
 */
// 因为不能使用额外空间 所以只能锁定正确的比对位置 来依次比对 就要用到 findMid 和 reverseList
public class Solution {
    public boolean isPalindrome(ListNode head) {
      // Write your solution here
      if(head == null || head.next == null) return true;
  
      ListNode mid = findMid(head);
  
      ListNode second = reverseList(mid.next);
  
      ListNode cur1 = head;
      ListNode cur2 = second;
  
      while(cur1 != null && cur2 != null){
        if(cur1.value != cur2.value) return false;
  
        cur1 = cur1.next;
        cur2 = cur2.next;
      }
  
      return true;
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
  