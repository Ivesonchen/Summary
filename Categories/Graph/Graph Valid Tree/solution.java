/**
 * Given n nodes labeled from 0 to n - 1 and a list of undirected edges (each edge is a pair of nodes), 
 * write a function to check whether these edges make up a valid tree.

For example:

Given n = 5 and edges = [[0, 1], [0, 2], [0, 3], [1, 4]], return true.

Given n = 5 and edges = [[0, 1], [1, 2], [2, 3], [1, 3], [1, 4]], return false.

Note: you can assume that no duplicate edges will appear in edges. 
Since all edges are undirected, [0, 1] is the same as [1, 0]and thus will not appear together in edges.

https://github.com/grandyang/leetcode/issues/261
 */
/**
 * Make sure it's a tree not a graph
 * 
 * 1. This graph contains all the node (Fully connected graph)
 * 2. No circle in this graph
 */
 // DFS
public class Solution {
    public boolean validTree(int n, int[][] edges) {
      // Write your solution here
      List<List<Integer>> graph = new ArrayList<>();
      for(int i = 0; i < n; i++){
        graph.add(new ArrayList<Integer>());
      }
  
      for(int[] edge : edges){
        graph.get(edge[0]).add(edge[1]);
        graph.get(edge[1]).add(edge[0]);
      }
  
      boolean[] visited = new boolean[n];
  
      if(dfs(graph, visited, 0, -1) == false) return false;
  
      for(boolean item : visited){
        if(!item) return false;
      }
      
      return true;
    }
  
    public boolean dfs(List<List<Integer>> graph, boolean[] visited, int cur, int pre){
      if(visited[cur]) return false;
      visited[cur] = true;
  
      for(Integer item : graph.get(cur)){
        if(item == pre) continue;
        if(dfs(graph, visited, item, cur) == false) return false;
      }
      return true;
    }
  }