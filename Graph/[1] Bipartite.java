/**
 * Determine if an undirected graph is bipartite. 
 * A bipartite graph is one in which the nodes can be divided into two groups 
 * such that no nodes have direct edges to other nodes in the same group.

Examples

1  --   2

    /   

3  --   4

is bipartite (1, 3 in group 1 and 2, 4 in group 2).

1  --   2

    /   |

3  --   4

is not bipartite.

Assumptions

The graph is represented by a list of nodes, and the list of nodes is not null.

https://leetcode.com/problems/is-graph-bipartite/solution/
 */

 /**
 * public class GraphNode {
 *   public int key;
 *   public List<GraphNode> neighbors;
 *   public GraphNode(int key) {
 *     this.key = key;
 *     this.neighbors = new ArrayList<GraphNode>();
 *   }
 * }
 */

 // BFS 应该是最好理解的 对于每个node 扩展出去 并记录应该的颜色   如果发现某一层的某一个node 颜色不符 就说明 false 了

public class Solution {
    public boolean isBipartite(List<GraphNode> graph) {
      // write your solution here
      int n = graph.size();
      HashMap<Integer, Integer> colors = new HashMap<>();
  
  
      for(GraphNode node : graph){
        if(colors.get(node.key) != null) continue;
  
        Queue<GraphNode> queue = new LinkedList<>();
        queue.offer(node);
        colors.put(node.key, 1);    // Blue: 1; Red: -1.
  
        while(!queue.isEmpty()){
          GraphNode cur = queue.poll();
          for(GraphNode next : cur.neighbors){
            if(colors.get(next.key) == null){ // If this node hasn't been colored;
              colors.put(next.key, -colors.get(cur.key)); // Color it with a different color;
              queue.offer(next);
            } else {
              if(colors.get(next.key) != -colors.get(cur.key)) return false; // If it is colored and its color is different, return false;
            }
          }
        }
      }
      
      return true;
    }
  }