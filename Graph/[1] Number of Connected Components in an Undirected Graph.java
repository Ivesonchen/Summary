/**
 * Given n nodes labeled from 0 to n - 1 and a list of undirected edges (each edge is a pair of nodes), 
 * write a function to find the number of connected components in an undirected graph.

Example 1:

     0          3
     |          |
     1 --- 2    4
Given n = 5 and edges = [[0, 1], [1, 2], [3, 4]], return 2.

Example 2:

     0           4
     |           |
     1 --- 2 --- 3
Given n = 5 and edges = [[0, 1], [1, 2], [2, 3], [3, 4]], return 1.

Note:
You can assume that no duplicate edges will appear in edges. 
Since all edges are undirected, [0, 1] is the same as [1, 0] and thus will not appear together in edges.
 */

// 思路类似于number of islands  先生成一张邻接链表  Adjacency List   然后进行DFS 或者 BFS 进行 查找

public class Solution {
    public int countComponents(int n, int[][] edges) {
      // Write your solution here
      List<List<Integer>> adList = new ArrayList<>();
      for(int i = 0; i < n; i++){
        adList.add(new ArrayList<>());
      }
  
      for(int[] item : edges){
        adList.get(item[0]).add(item[1]);
        adList.get(item[1]).add(item[0]);
      }
      // building graph
  
      boolean[] visited = new boolean[n];
      int res = 0;
  
      for(int i = 0; i < n; i++){
        if(!visited[i]){
          dfs(adList, visited, i);
          res++;
        }
      }
      return res;
    }
  
    public void dfs(List<List<Integer>> adList, boolean[] visited, int i){
      if(visited[i]) return;
  
      visited[i] = true;
  
      for(int j = 0; j < adList.get(i).size(); j++){
        dfs(adList, visited, adList.get(i).get(j));
      }
    }
  }