/**
 * design a hashmap
 */

class ListNode{
    int key;
    int val;
    ListNode next;

    ListNode(int key, int value){
        this.key = key;
        this.val = value;
    }
}

class HashMap{

    final ListNode[] hashmap = new ListNode[10000];

    public int hashCode(int key){
        return Integer.hashCode(key) % hashmap.length;
    }

    public void put(int key, int value){
        int idx = hashCode(key);
        if(hashmap[idx] == null){
            hashmap[idx] = new ListNode(-1, -1);
        }
        ListNode found = find(hashmap[idx], key);
        if(found.next == null){
            found.next = new ListNode(key, value);
        } else {
            found.next.val = value;
        }
    }

    public int get(int key){
        int idx = hashCode(key);
        if(hashmap[idx] == null){
            return -1;
        }
        ListNode found = find(hashmap[idx], key);
        if(found.next == null){
            return -1;
        } else {
            return found.next.val;
        }
    }

    public Boolean remove(int key){
        int idx = hashCode(key);
        if(hashmap[idx] == null){
            return false;
        }
        ListNode found = find(hashmap[idx], key);
        if(found.next == null){
            return false;
        } else {
            return found.next = found.next.next;
        }
    }

    public ListNode find(ListNode bucket, int key){
        ListNode cur = bucket;
        ListNode pre = null;
        while(cur != null || cur.key != key){
            pre = cur;
            cur = cur.next;
        }

        return pre;
    }
}