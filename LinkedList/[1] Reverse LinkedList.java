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
    if(head == null || head.next == null){      // 这个head == null 的判断 纯是为了 排除 一串 null 的 LinkedList 的情况
        return head;
    }

    ListNode newHead = ReverseLinkedList(head.next);

    head.next.next = head;  // 建立 新的向前的指针
    head.next = null;       // 断掉 原向后的指针

    return newHead;         // 深入理解一下这个newHead  是不是就是 head指针 走到最后 的那次 执行 ReverseLinkedList() 函数的返回值   一直是同一个值 没有变过
                            // newHead值穿透了 N 多层 给到了最后的结果
}