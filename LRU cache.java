/*
Design and implement a data structure for Least Recently Used (LRU) cache. It should support the following operations: get and put.

get(key) - Get the value (will always be positive) of the key if the key exists in the cache, otherwise return -1.
put(key, value) - Set or insert the value if the key is not already present. When the cache reached its capacity, it should invalidate the least recently used item before inserting a new item.

The cache is initialized with a positive capacity.

Follow up:
Could you do both operations in O(1) time complexity?
 */

 /**
  * LRUCache cache = new LRUCache( 2 // capacity );

cache.put(1, 1);
cache.put(2, 2);
cache.get(1);       // returns 1
cache.put(3, 3);    // evicts key 2
cache.get(2);       // returns -1 (not found)
cache.put(4, 4);    // evicts key 1
cache.get(1);       // returns -1 (not found)
cache.get(3);       // returns 3
cache.get(4);       // returns 4
  */

import java.util.Hashtable;
class LRUCache {
    
    class DoubleLinkedListNode {
        int key;
        int value;
        DoubleLinkedListNode pre;
        DoubleLinkedListNode next;
    }
    

    private DoubleLinkedListNode head;
    private DoubleLinkedListNode tail;
    private int count;
    private int capacity;
    private Hashtable<Integer, DoubleLinkedListNode> 
        cache = new Hashtable<Integer, DoubleLinkedListNode>();
    
    /*add to head*/
    public void addToHead(DoubleLinkedListNode node) {
        node.pre = head;
        node.next = head.next;
        
        head.next.pre = node;
        head.next = node;
    }
    
    /*remove node*/
    public void removeNode(DoubleLinkedListNode node) {
        DoubleLinkedListNode prev = node.pre;
        DoubleLinkedListNode post = node.next;
        
        prev.next = post;
        post.pre = prev;
    }
    
        
    /*pop tail*/
    public DoubleLinkedListNode popTail() {
        DoubleLinkedListNode res = tail.pre;
        this.removeNode(res);
        return res;
    }
    
    /*move to head*/
    public void moveToHead(DoubleLinkedListNode node) {
        this.removeNode(node);
        this.addToHead(node);
    }
    
    
    public LRUCache(int capacity) {
        this.count = 0;
        this.capacity = capacity;
        
        head = new DoubleLinkedListNode();
        head.pre = null;
        tail = new DoubleLinkedListNode();
        tail.next = null;
        
        head.next = tail;
        tail.pre = head;
    }
    
    public int get(int key) {
        DoubleLinkedListNode node = cache.get(key);
        
        if(node == null) {
            return -1;
        } else {
            this.moveToHead(node);
            return node.value;
        }
    }
    
    public void put(int key, int value) {
        DoubleLinkedListNode node = cache.get(key);
        if(node == null) {
            DoubleLinkedListNode newNode = new DoubleLinkedListNode();
            newNode.key = key;
            newNode.value = value;
            
            this.cache.put(key, newNode);
            this.addToHead(newNode);
            
            count++;
            
            if(count > capacity) {
                DoubleLinkedListNode tail = this.popTail();
                this.cache.remove(tail.key);
                count--;
            }
        } else {
            node.value = value;
            this.moveToHead(node);
        }
    }
}

/**
 * Your LRUCache object will be instantiated and called as such:
 * LRUCache obj = new LRUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 */