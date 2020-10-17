/**
 * Given a singly-linked list, where each node contains an integer value, sort it in ascending order. The merge sort algorithm should be used to solve this problem.

Examples

null, is sorted to null
1 -> null, is sorted to 1 -> null
1 -> 2 -> 3 -> null, is sorted to 1 -> 2 -> 3 -> null
4 -> 2 -> 6 -> -3 -> 5 -> null, is sorted to -3 -> 2 -> 4 -> 5 -> 6
 */

public class Solution {
    public ListNode mergeSort(ListNode head) {
      // Write your solution here
      if(head == null || head.next == null) return head;
  
      ListNode mid = findMid(head);
  
      ListNode right = mid.next;
      mid.next = null;
  
      ListNode newLeft = mergeSort(head);
      ListNode newRight = mergeSort(right);
  
      return merge(newLeft, newRight);
    }
  
    public ListNode merge(ListNode left, ListNode right){
      ListNode res = new ListNode(-1);
      ListNode r = res;
      while(left != null && right != null){
        if(left.value < right.value){
          res.next = left;
          left = left.next;
        } else {
          res.next = right;
          right = right.next;
        }
        res = res.next;
      }
  
      while(left != null){
        res.next = left;
        left = left.next;
        res = res.next;
      }
  
      while(right != null){
        res.next = right;
        right = right.next;
        res = res.next;
      }
      return r.next;
    }
  
    public ListNode findMid(ListNode head){
      if(head == null) return head;
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
  