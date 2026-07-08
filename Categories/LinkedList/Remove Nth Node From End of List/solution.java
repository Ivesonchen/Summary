/**
 * Given a linked list, remove the nth node from the end of list and return its head.

Assumptions
If n is not valid, you do not need to do anything to the original list.
Try to do this in one pass.

Examples

Given linked list: 1->2->3->4->5, and n = 2.

After removing the second node from the end, the linked list becomes 1->2->3->5.
*/

// maintaining 间隔为 n 的 两个node指针 然后 向后同时移动 当头指针撞到尾的时候 slow指针就是该删的位置
class Solution {
    public ListNode removeNthFromEnd(ListNode head, int n) {
        
        if(head == null) return head;
        ListNode dummy = new ListNode(-1);
        dummy.next = head;
        ListNode slow = dummy, fast = dummy;
        
        for(int i = 0; i < n; i++){
            fast = fast.next;
        }
        
        while(fast.next != null){
            fast = fast.next;
            slow = slow.next;
        }
        
        slow.next = slow.next.next;//一步删除
        
        return dummy.next;
    }
    // 设置两个指针  用一个for循环来设定探测窗口宽度 然后把窗口推到最后 就得到想要删除节点的位置了
}


// O(N) 额外空间 map
public class Solution {
    public ListNode removeNthFromEnd(ListNode head, int n) {
      // Write your solution here
      HashMap<Integer, ListNode> map = new HashMap<>();
  
      ListNode dummy = new ListNode(-1);
      dummy.next = head;
      ListNode cur = dummy;
      int counter = 0;
  
      while(cur != null){
        map.put(counter, cur);
        counter++;
        cur = cur.next;
      }
      map.put(counter, null);
  
      int pos = counter - n;
      if(pos < 1) return head;
  
      ListNode pre = map.get(pos - 1);
      pre.next = pre.next.next;
  
      return dummy.next;
    }
  }
