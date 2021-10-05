/**
 * There are n cities. Some of them are connected, while some are not. 
 * If city a is connected directly with city b, and city b is connected directly with city c, 
 * then city a is connected indirectly with city c.

A province is a group of directly or indirectly connected cities and no other cities outside of the group.

You are given an n x n matrix isConnected where isConnected[i][j] = 1 if the ith city and the jth city are directly connected, 
and isConnected[i][j] = 0 otherwise.

Return the total number of provinces.

Input: isConnected = [[1,1,0],[1,1,0],[0,0,1]]
Output: 2

Input: isConnected = [[1,0,0],[0,1,0],[0,0,1]]
Output: 3
 */

public class Solution {
  public int findCircleNum(int[][] M) {
      boolean[] visited = new boolean[M.length]; //visited[i] means if ith person is visited in this algorithm
      int count = 0;
      for(int i = 0; i < M.length; i++) {
          if(!visited[i]) {
              dfs(M, visited, i);
              count++;
          }
      }
      return count;
  }
  private void dfs(int[][] M, boolean[] visited, int person) {
      for(int other = 0; other < M.length; other++) {
        if(person == other) continue;
        if(M[person][other] == 1 && !visited[other]) {
            //We found an unvisited person in the current friend cycle 
            visited[other] = true;
            dfs(M, visited, other); //Start DFS on this new found person
        }
      }
  }
}


public class Solution {
  class UnionFind {
      private int count = 0;
      private int[] parent, rank;
      
      public UnionFind(int n) {
          count = n;
          parent = new int[n];
          rank = new int[n];
          for (int i = 0; i < n; i++) {
              parent[i] = i;
          }
      }
      
      public int find(int p) {
        while (p != parent[p]) {
              parent[p] = parent[parent[p]];    // path compression by halving
              p = parent[p];
          }
          return p;
      }
      
      public void union(int p, int q) {
          int rootP = find(p);
          int rootQ = find(q);
          if (rootP == rootQ) return;
          if (rank[rootQ] > rank[rootP]) {
              parent[rootP] = rootQ;
          }
          else {
              parent[rootQ] = rootP;
              if (rank[rootP] == rank[rootQ]) {
                  rank[rootP]++;
              }
          }
          count--;
      }
      
      public int count() {
          return count;
      }
  }
  
  public int findCircleNum(int[][] M) {
      int n = M.length;
      UnionFind uf = new UnionFind(n);
      for (int i = 0; i < n - 1; i++) {
          for (int j = i + 1; j < n; j++) {
              if (M[i][j] == 1) uf.union(i, j);
          }
      }
      return uf.count();
  }
}