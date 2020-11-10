/**
 * Given a linked list and a target value T, partition it such that all nodes less than T are listed before the nodes larger than or equal to target value T. 
 * The original relative order of the nodes in each of the two partitions should be preserved.

Examples

L = 2 -> 4 -> 3 -> 5 -> 1 -> null, T = 3, is partitioned to 2 -> 1 -> 4 -> 3 -> 5 -> null
 */

/**
 * small 链条
 * large 链条   然后 连起来
 */

public class Solution {
    public ListNode partition(ListNode head, int target) {
      // Write your solution here
      if(head == null) return head;
  
      ListNode fakeHeadSmall = new ListNode(-1);
      ListNode fakeHeadLarge = new ListNode(-1);
      ListNode smallTail = fakeHeadSmall;
      ListNode largeTail = fakeHeadLarge;
      ListNode current = head;
  
      while(current != null){
        if(current.value < target){
          smallTail.next = current;
          smallTail = current;
        } else {
          largeTail.next = current;
          largeTail = current;
        }
        current = current.next;
      }
      largeTail.next = null;
      smallTail.next = fakeHeadLarge.next;
  
      return fakeHeadSmall.next;
    }
  }