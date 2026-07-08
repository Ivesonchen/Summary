/**
 * Given a composition with different kinds of words, return a list of the top K most frequent words in the composition.

Assumptions

the composition is not null and is not guaranteed to be sorted
K >= 1 and K could be larger than the number of distinct words in the composition, in this case, just return all the distinct words
Return

a list of words ordered from most frequent one to least frequent one (the list could be of size K or smaller than K)
Examples

Composition = ["a", "a", "b", "b", "b", "b", "c", "c", "c", "d"], top 2 frequent words are [“b”, “c”]
Composition = ["a", "a", "b", "b", "b", "b", "c", "c", "c", "d"], top 4 frequent words are [“b”, “c”, "a", "d"]
Composition = ["a", "a", "b", "b", "b", "b", "c", "c", "c", "d"], top 5 frequent words are [“b”, “c”, "a", "d"]
 */

 // size k min-heap

public class Solution {
    public String[] topKFrequent(String[] combo, int k) {
      // Write your solution here
      //min-heap
      HashMap<String, Integer> map = new HashMap<>();
  
      for(String str : combo){
        map.put(str, map.getOrDefault(str, 0) + 1);
      }
  
      PriorityQueue<Map.Entry<String, Integer>> pq = new PriorityQueue<>(new Comparator<Map.Entry<String, Integer>>(){
        public int compare(Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2){
          return e1.getValue() == e2.getValue() ? e1.getKey().compareTo(e2.getKey()) : e1.getValue() - e2.getValue();
        }
      });
  
      for(Map.Entry<String, Integer> entry : map.entrySet()){
        pq.offer(entry);
  
        if(pq.size() > k){
          pq.poll();
        }
      }
  
      String[] res = new String[pq.size()];
      int index = pq.size() - 1;
  
      while(pq.size() > 0){
        res[index--] = pq.poll().getKey();
      }
  
      return res;
    }
  }