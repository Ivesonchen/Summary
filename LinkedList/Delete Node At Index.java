/**
 * Delete the node at the given index for the given linked list.

Examples

[1, 2, 3], delete at 1 --> [1, 3]

[1, 2, 3], delete at 4 --> [1, 2, 3]

[1, 2, 3], delete at 0 --> [2, 3]
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
    public ListNode deleteNode(ListNode head, int index) {
      // Write your solution here
      ListNode dummy = new ListNode(-1);
      dummy.next = head;
      ListNode pre = dummy;
      ListNode cur = head;
  
      int i = 0;
      while(i < index){
        if(cur == null) break;
  
        pre = cur;
        cur = cur.next;
        i++;
      }
  
      if(cur == null) return head;
  
      pre.next = cur.next;
  
      return dummy.next;
    }
  }