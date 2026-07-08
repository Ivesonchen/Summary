/**
 * Make a deep copy of an undirected graph, there could be cycles in the original graph.

Assumptions

The given graph is not null
https://leetcode.com/problems/clone-graph/solution/

 */

 /*
* class GraphNode {
*   public int key;
*   public List<GraphNode> neighbors;
*   public GraphNode(int key) {
*     this.key = key;
*     this.neighbors = new ArrayList<GraphNode>();
*   }
* }
*/
public class Solution {
    public List<GraphNode> copy(List<GraphNode> graph) {
      // Write your solution here.
      if(graph == null || graph.isEmpty()) return null;
  
      Map<GraphNode, GraphNode> map = new HashMap<>();
      for(GraphNode node : graph){
        if(!map.containsKey(node)){
          map.put(node, new GraphNode(node.key));
          dfs(node, map);
        }
      }
  
      return new ArrayList<GraphNode> (map.values());
    }
  
    public void dfs(GraphNode node, Map<GraphNode, GraphNode> map){
      GraphNode copy = map.get(node);
      for(GraphNode neighbor : node.neighbors){
        if(!map.containsKey(neighbor)){
          map.put(neighbor, new GraphNode(neighbor.key));
          dfs(neighbor, map);
        }
        copy.neighbors.add(map.get(neighbor));
      }
    }
  }