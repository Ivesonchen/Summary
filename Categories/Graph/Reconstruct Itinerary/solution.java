/**
 * Given a list of airline tickets represented by pairs of departure and arrival airports [from, to], 
 * reconstruct the itinerary in order. All of the tickets belong to a man who departs from JFK. Thus, the itinerary must begin with JFK.

Note:

If there are multiple valid itineraries, you should return the itinerary that has the smallest lexical order when read as a single string. 
For example, the itinerary ["JFK", "LGA"] has a smaller lexical order than ["JFK", "LGB"].
All airports are represented by three capital letters (IATA code).

You may assume all tickets form at least one valid itinerary.
Example 1:
tickets = [["MUC", "LHR"], ["JFK", "MUC"], ["SFO", "SJC"], ["LHR", "SFO"]]
Return ["JFK", "MUC", "LHR", "SFO", "SJC"].

Example 2:
tickets = [["JFK","SFO"],["JFK","ATL"],["SFO","ATL"],["ATL","JFK"],["ATL","SFO"]]
Return ["JFK","ATL","JFK","SFO","ATL","SFO"].
Another possible reconstruction is ["JFK","SFO","ATL","JFK","ATL","SFO"]. But it is larger in lexical order.

https://github.com/grandyang/leetcode/issues/332
 */

/**
* 每张机票都是有向图的一条边，我们需要找出一条经过所有边的路径，那么DFS不是我们的不二选择。
  先来看递归的结果，我们首先把图建立起来，通过邻接链表来建立。
  由于题目要求解法按字母顺序小的，那么我们考虑用multiset，可以自动排序。
  等我们图建立好了以后，从节点JFK开始遍历，只要当前节点映射的multiset里有节点，我们取出这个节点，将其在multiset里删掉，
  然后继续递归遍历这个节点，由于题目中限定了一定会有解，那么等图中所有的multiset中都没有节点的时候，我们把当前节点存入结果中，
  然后再一层层回溯回去，将当前节点都存入结果，
*/

//DFS
public class Solution {
    public List<String> findItinerary(String[][] tickets) {
      // Write your solution here
      Map<String, PriorityQueue<String>> targets = new HashMap<>();
      List<String> route = new LinkedList<>();
  
      for(String[] ticket : tickets){
        PriorityQueue<String> temp = targets.getOrDefault(ticket[0], new PriorityQueue<>());
        temp.add(ticket[1]);
        targets.put(ticket[0], temp);
      }
  
      dfs(targets, route, "JFK");
  
      return route;
    }
  
    public void dfs(Map<String, PriorityQueue<String>> targets, List<String> route, String airport){
      while(targets.containsKey(airport) && !targets.get(airport).isEmpty()){
        String next = targets.get(airport).poll();
  
        dfs(targets, route, next);
      }
      route.add(0, airport); // 当 没有 下一个airport 可去的时候 加入头
    }
  }

// iterative
  public List<String> findItinerary(String[][] tickets) {
    Map<String, PriorityQueue<String>> targets = new HashMap<>();
    for (String[] ticket : tickets)
        targets.computeIfAbsent(ticket[0], k -> new PriorityQueue()).add(ticket[1]);
    List<String> route = new LinkedList();
    Stack<String> stack = new Stack<>();
    stack.push("JFK");

    while (!stack.empty()) {
        while (targets.containsKey(stack.peek()) && !targets.get(stack.peek()).isEmpty())
            stack.push(targets.get(stack.peek()).poll());
        route.add(0, stack.pop());
    }
    return route;
}