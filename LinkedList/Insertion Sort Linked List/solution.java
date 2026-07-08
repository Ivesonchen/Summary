/**
 * Given a singly-linked list, where each node contains an integer value, sort it in ascending order. The insertion sort algorithm should be used to solve this problem.

Examples

null, is sorted to null
1 -> null, is sorted to 1 -> null
1 -> 2 -> 3 -> null, is sorted to 1 -> 2 -> 3 -> null
4 -> 2 -> 6 -> -3 -> 5 -> null, is sorted to -3 -> 2 -> 4 -> 5 -> 6
 */

 /**
  * insertion sort 的基本原理  从 dummy 节点开始往后找 不超过 cur 位置 寻找合适的 位置(要比对 value) 确定 start 的位置 
  */

public class Solution {
    public ListNode insertionSort(ListNode head) {
      // Write your solution here
      if(head == null || head.next == null) return head;
  
      ListNode dummy = new ListNode(-1);
      dummy.next = head;
      ListNode cur = head.next;
      ListNode pre = head;
      ListNode start = dummy;
  
      while(cur != null){
        
        while(start.next != cur && start.next != null && start.next.value < cur.value){
          start = start.next;
        }
        // found position
        boolean flag = false;                                   // special case 【5，3，4，6，1，2，7】当走到6的位置时 此时 start.next 新值的插入 会影响 pre的正确指向 
                                                                // 所以要 有一个flag 值 但凡是出现这种 会影响pre的指向的 就单独处理 (pre = pre.next)
        if(start.next == cur){
          flag = true;
        }
        ListNode nextNode = cur.next;
        pre.next = nextNode;
        cur.next = start.next;
        start.next = cur;
        start = dummy;
        if(flag) pre = pre.next;
        
        cur = nextNode;
      }
  
      return dummy.next;
    }
  }