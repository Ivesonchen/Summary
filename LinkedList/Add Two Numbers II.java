/**
 * You are given two non-empty linked lists representing two non-negative integers. 
 * The most significant digit comes first and each of their nodes contains a single digit. 
 * Add the two numbers and return the sum as a linked list.

You may assume the two numbers do not contain any leading zero, except the number 0 itself.

Follow up: Could you solve it without reversing the input lists?
 */

/**
 * 方式1. reverse 两个链表之后 就变成了 add two numbers 1 的计算方式
 * 方式2. 用stack 在第一遍遍历的时候 把值存进stack
 */

 class Solution {
   public ListNode addTwoNumbers (ListNode l1, ListNode l2) {
     Stack<Integer> s1 = new Stack<Integer>();
     Stack<Integer> s2 = new Stack<Integer>();

     while(l1 != null) {
       s1.push(l1.val);
       l1 = l1.next;
     }

     while(l2 != null) {
       s2.push(l2.val);
       l2 = l2.next;
     }

     int sum = 0;
     ListNode list = new ListNode(0);
     while(!s1.empty() || !s2.empty()) {
       if(!s1.empty()) sum += s1.pop();
       if(!s2.empty()) sum += s2.pop();

       list.val = sum % 10;
       ListNode head = new ListNode(sum / 10);
       head.next = list;
       list = head;
       sum /= 10;
     }

     return list.val == 0 ? list.next : list;
   }
 }