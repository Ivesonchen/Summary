//Iterative
public ListNode ReverseLinkedList(ListNode head){
    if(head == null || head.next == null){
        return head;
    }

    ListNode cur = head;
    ListNode pre = null;

    while(cur != null){
        ListNode next = cur.next;

        cur.next = pre;

        pre = cur;
        cur = next;
    }

    return pre;
}

//Recursive
public ListNode ReverseLinkedList(ListNode head){
    if(head == null || head.next == null){
        return head;
    }

    ListNode newHead = ReverseLinkedList(head.next);

    head.next.next = head;
    head.next = null;

    return newHead;
}