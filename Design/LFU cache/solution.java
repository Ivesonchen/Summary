public class LFUCache {
  class Node {
      int key, val, cnt;
      Node prev, next;
      Node(int key, int val) {
          this.key = key;
          this.val = val;
          cnt = 1;
      }
  }
  
  class DLList {
      Node head, tail;
      int size;
      DLList() {
          head = new Node(0, 0);
          tail = new Node(0, 0);
          head.next = tail;
          tail.prev = head;
      }
      
      // 在head上添加node
      void add(Node node) {
          head.next.prev = node;
          node.next = head.next;
          node.prev = head;
          head.next = node;
          size++;
      }
      
      // 
      void remove(Node node) {
          node.prev.next = node.next;
          node.next.prev = node.prev;
          size--;
      }
      
      Node removeLast() {
          if (size > 0) {
              Node node = tail.prev;
              remove(node);
              return node;
          }
          else return null;
      }
  }
  
  int capacity, size, min;
  Map<Integer, Node> nodeMap;   // 存的是  key值和 对应的 node
  Map<Integer, DLList> countMap;  // 存的是 frequency   和    一个双链表 (拥有相同出现频率的所有node)
  public LFUCache(int capacity) {
      this.capacity = capacity;
      nodeMap = new HashMap<>();
      countMap = new HashMap<>();
  }
  
  public int get(int key) {
      Node node = nodeMap.get(key);
      if (node == null) return -1;
      update(node);
      return node.val;
  }
  
  public void put(int key, int value) {
      if (capacity == 0) return;
      Node node;
      if (nodeMap.containsKey(key)) {
          node = nodeMap.get(key);
          node.val = value;
          update(node);
      }
      else {
          node = new Node(key, value);
          nodeMap.put(key, node);
          if (size == capacity) {
              DLList lastList = countMap.get(min);
              nodeMap.remove(lastList.removeLast().key);
              size--;
          }
          size++;
          min = 1;
          DLList newList = countMap.getOrDefault(node.cnt, new DLList());
          newList.add(node);
          countMap.put(node.cnt, newList);
      }
  }
  
  private void update(Node node) {
      DLList oldList = countMap.get(node.cnt);
      oldList.remove(node);
      if (node.cnt == min && oldList.size == 0) min++; 
      node.cnt++;
      DLList newList = countMap.getOrDefault(node.cnt, new DLList());
      newList.add(node);
      countMap.put(node.cnt, newList);
  }
}

/**
 * 
 * LFUCache cache = new LFUCache(2); //capacity

cache.put(1, 1);
cache.put(2, 2);
cache.get(1);       // returns 1
cache.put(3, 3);    // evicts key 2
cache.get(2);       // returns -1 (not found)
cache.get(3);       // returns 3.
cache.put(4, 4);    // evicts key 1.
cache.get(1);       // returns -1 (not found)
cache.get(3);       // returns 3
cache.get(4);       // returns 4


get(key) - Get the value (will always be positive) of the key if the key exists in the cache, otherwise return -1.
put(key, value) - Set or insert the value if the key is not already present. 
When the cache reaches its capacity, it should invalidate the least frequently used item before inserting a new item. 
For the purpose of this problem, when there is a tie (i.e., two or more keys that have the same frequency), the least recently used key would be evicted.
 */