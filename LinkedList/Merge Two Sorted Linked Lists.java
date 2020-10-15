/**
 * Merge two sorted lists into one large sorted list.

Examples

L1 = 1 -> 4 -> 6 -> null, L2 = 2 -> 5 -> null, merge L1 and L2 to 1 -> 2 -> 4 -> 5 -> 6 -> null
L1 = null, L2 = 1 -> 2 -> null, merge L1 and L2 to 1 -> 2 -> null
L1 = null, L2 = null, merge L1 and L2 to null
 */

public class Solution {
    public ListNode merge(ListNode one, ListNode two) {
      // Write your solution here
      ListNode res = new ListNode(-1);
      ListNode cur = res;
  
      ListNode curOne = one;
      ListNode curTwo = two;
  
      while(curOne != null && curTwo != null){
        int valueOne = curOne.value;
        int valueTwo = curTwo.value;
  
        if(valueOne <= valueTwo){
          cur.next = curOne;
          curOne = curOne.next;
          cur = cur.next;
        } else {
          cur.next = curTwo;
          curTwo = curTwo.next;
          cur = cur.next;
        }
      }
  
      while(curOne != null){
        cur.next = curOne;
        curOne = curOne.next;
        cur = cur.next;
      }
      while(curTwo != null){
        cur.next = curTwo;
        curTwo = curTwo.next;
        cur = cur.next;
      }
  
      return res.next;
    }
  
    public ListNode merge(ListNode one, ListNode two){
      if(one == null) return two;
      if(two == null) return one;
  
      if(one.value <= two.value){
        one.next = merge(one.next, two);
        return one;
      } else {
        two.next = merge(one, two.next);
        return two;
      }
    }
  }