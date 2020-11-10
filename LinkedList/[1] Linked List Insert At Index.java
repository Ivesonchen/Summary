/**
 * Insert a new element at a specific index in the given linked list. The index is 0 based, and if the index is out of the list's scope, you do not need to do anything.

Examples:

1 -> 2 -> 3 -> null, insert 4 at index 3, --> 1 -> 2 -> 3 -> 4 -> null

1 -> 2 -> null, insert 4 at index 0, --> 4 -> 1 -> 2 -> null
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
    public ListNode insert(ListNode head, int index, int value) {
      // Write your solution here
      ListNode dummy = new ListNode(-1);
      dummy.next = head;
  
      ListNode cur = head;
      ListNode pre = dummy;
      boolean flag = false;
      for(int i = 0; i < index; i++){
        if(cur == null && i < index) {
          flag = true;
          break;
        }
        pre = cur;
        cur = cur.next;
      }
      if(flag) return head;
      ListNode newNode = new ListNode(value);
      pre.next = newNode;
      newNode.next = cur;
  
      return dummy.next;
    }
  }