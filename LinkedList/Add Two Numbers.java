/**
 * You are given two linked lists representing two non-negative numbers. The digits are stored in reverse order and each of their nodes contain a single digit. Add the two numbers and return it as a linked list.  

Example

Input: (2 -> 4 -> 3) + (5 -> 6 -> 4)

Output: 7 -> 0 -> 8
 */

 // 这个是往 l1 上累加   
public class Solution {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
      // Write your solution here
          if(l1 == null) return l2;
          if(l2 == null) return l1;
  
          ListNode cur1 = l1;
          ListNode pre1 = new ListNode(-1);
          pre1.next = cur1;
          
          ListNode cur2 = l2;
  
          int carry = 0;
  
          while(cur1 != null && cur2 != null){
            int sum = cur1.value + cur2.value + carry;
  
            cur1.value = sum % 10;
            carry = sum / 10;
            pre1 = cur1;
            cur1 = cur1.next;
            cur2 = cur2.next;
          }
  
          while(cur1 != null){
            int sum = cur1.value + carry;
  
            cur1.value = sum % 10;
            carry = sum / 10;
            pre1 = cur1;
            cur1 = cur1.next;
          }
  
          while(cur2 != null){
            int sum = cur2.value + carry;
  
            carry = sum / 10;
            pre1.next = new ListNode(sum % 10);
              
            cur2 = cur2.next;
            pre1 = pre1.next;
          }
  
          if(carry >= 1) pre1.next = new ListNode(carry);
  
          return l1;
    }
  }
// 这个是构建新的linkedlist 来存储结果
      public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0);
        
        ListNode cur1 = l1, cur2 = l2, cur = dummy;
        int sum = 0;
        
        //新建一个linkedlist    三个指针在扫描  然后去处理进位的事情。
        while(cur1 != null || cur2 != null){
            if(cur1 != null){
                sum += cur1.val;
                cur1 = cur1.next;
            }
            
            if(cur2 != null){
                sum += cur2.val;
                cur2 = cur2.next;
            }
            
            
            cur.next = new ListNode(sum % 10);
            sum = sum / 10;
            cur = cur.next;
        }
        
        if(sum == 1){
            cur.next = new ListNode(sum);
        }
        return dummy.next;
    }