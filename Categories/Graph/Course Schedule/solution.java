/**
 * There are a total of n courses you have to take, labeled from 0 to n - 1.

Some courses may have prerequisites, for example to take course 0 you have to first take course 1, which is expressed as a pair: [0,1]

Given the total number of courses and a list of prerequisite pairs, is it possible for you to finish all courses?

For example:

2, [[1,0]]
There are a total of 2 courses to take. To take course 1 you should have finished course 0. So it is possible.

2, [[1,0],[0,1]]
There are a total of 2 courses to take. To take course 1 you should have finished course 0, and to take course 0 you should also have finished course 1. So it is impossible.

Note:

The input prerequisites is a graph represented by a list of edges, not adjacency matrices. Read more about how a graph is represented.
You may assume that there are no duplicate edges in the input prerequisites.

https://github.com/grandyang/leetcode/issues/207
 */

 // BFS
public class Solution {
    public boolean canFinish(int numCourses, int[][] prerequisites) {
      // Write your solution here
      List<List<Integer>> graph = new ArrayList<>(numCourses);
      for(int i = 0; i < numCourses; i++){
        graph.add(new ArrayList<Integer>());
      }
      int[] inDegree = new int[numCourses];
  
      for(int[] edge : prerequisites){
        graph.get(edge[1]).add(edge[0]);
        inDegree[edge[0]]++;
      }
      Queue<Integer> queue = new LinkedList<>();
  
      for(int i = 0; i < numCourses; i++){
        if(inDegree[i] == 0) queue.offer(i);
      }
  
      while(!queue.isEmpty()){
        int cur = queue.poll();
        for(Integer item : graph.get(cur)){
          inDegree[item] --;
          if(inDegree[item] == 0) queue.offer(item);
        }
      }
  
      for(int i = 0; i < numCourses; i++){
        if(inDegree[i] != 0) return false;
      }
      return true;
    }
  }

  // DFS
  class Solution {
    public:
        bool canFinish(int numCourses, vector<vector<int>>& prerequisites) {
            vector<vector<int>> graph(numCourses, vector<int>());
            vector<int> visit(numCourses);
            for (auto a : prerequisites) {
                graph[a[1]].push_back(a[0]);
            }
            for (int i = 0; i < numCourses; ++i) {
                if (!canFinishDFS(graph, visit, i)) return false;
            }
            return true;
        }
        bool canFinishDFS(vector<vector<int>>& graph, vector<int>& visit, int i) {
            if (visit[i] == -1) return false;
            if (visit[i] == 1) return true;
            visit[i] = -1;
            for (auto a : graph[i]) {
                if (!canFinishDFS(graph, visit, a)) return false;
            }
            visit[i] = 1;
            return true;
        }
    };