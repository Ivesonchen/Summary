/**
 * Check if a given linked list has a cycle. Return the node where the cycle starts. 
 * Return null if there is no cycle.
 */

 /**
  * To understand this solution, you just need to ask yourself these question.
Assume the distance from head to the start of the loop is x1
the distance from the start of the loop to the point fast and slow meet is x2
the distance from the point fast and slow meet to the start of the loop is x3
What is the distance fast moved? What is the distance slow moved? And their relationship?

x1 + x2 + x3 + x2
x1 + x2
x1 + x2 + x3 + x2 = 2 (x1 + x2)
Thus x1 = x3
  */
public class Solution {
    public ListNode cycleNode(ListNode head) {
      // write your solution here
        if(head == null || head.next == null) return null;
    
        ListNode dummy = new ListNode(-1);
        dummy.next = head;
        ListNode slow = dummy;
        ListNode fast = dummy;
    
        while(fast != null && fast.next != null){
          slow = slow.next;
          fast = fast.next.next;
          if(fast == slow) {
            ListNode again = dummy;
            while(again != slow){
              again = again.next;
              slow = slow.next;
            }
            return again;
          };
        }
      return null;
    }
  }