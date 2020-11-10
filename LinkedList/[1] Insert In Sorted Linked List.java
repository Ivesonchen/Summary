/**
 * Insert a value in a sorted linked list.

Examples

L = null, insert 1, return 1 -> null
L = 1 -> 3 -> 5 -> null, insert 2, return 1 -> 2 -> 3 -> 5 -> null
L = 1 -> 3 -> 5 -> null, insert 3, return 1 -> 3 -> 3 -> 5 -> null
L = 2 -> 3 -> null, insert 1, return 1 -> 2 -> 3 -> null
 */

 /**
 * class ListNode {
 *   public int value;
 *   public ListNode next;
 *   public ListNode(int value) {
 *     this.value = value;
 *     next = null;
 *   }
 * }
 */
public class Solution {
    public ListNode insert(ListNode head, int value) {
      // Write your solution here
      ListNode dummy = new ListNode(-1);
      dummy.next = head;
  
      ListNode cur = head;
      ListNode pre = dummy;
  
      while(true){
        if(cur == null) break;
        if(cur.value > value) break;
        else {
          pre = cur;
          cur = cur.next;
        }
      }
  
      ListNode newNode = new ListNode(value);
      pre.next = newNode;
      newNode.next = cur;
  
      return dummy.next;
    }
  }