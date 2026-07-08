/**
 * There are a total of n courses you have to take, labeled from 0 to n - 1.

Some courses may have prerequisites, for example to take course 0 you have to first take course 1, which is expressed as a pair: [0,1]

Given the total number of courses and a list of prerequisite pairs, return the ordering of courses you should take to finish all courses.

There may be multiple correct orders, you just need to return one of them. If it is impossible to finish all courses, return an empty array.

For example:

2, [[1,0]]
There are a total of 2 courses to take. To take course 1 you should have finished course 0. So the correct course order is [0,1]

4, [[1,0],[2,0],[3,1],[3,2]]
There are a total of 4 courses to take. To take course 3 you should have finished both courses 1 and 2. 
Both courses 1 and 2 should be taken after you finished course 0. So one correct course order is [0,1,2,3]. 
 Another correct ordering is[0,2,1,3].

Note:

The input prerequisites is a graph represented by a list of edges, not adjacency matrices. Read more about how a graph is represented.
You may assume that there are no duplicate edges in the input prerequisites.
 */

 // 基于 course schedule II 的 BFS 做法
public class Solution {
    public int[] findOrder(int numCourses, int[][] prerequisites) {
      // Write your solution here
      List<List<Integer>> graph = new ArrayList<>();
      for(int i = 0; i < numCourses; i++){
        graph.add(new ArrayList<Integer>());
      }
  
      int[] inDegree = new int[numCourses];
      List<Integer> res = new ArrayList<>();
  
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
        res.add(cur);
        for(Integer item : graph.get(cur)){
          inDegree[item]--;
          if(inDegree[item] == 0) queue.offer(item);
        }
      }
  
      if(res.size() != numCourses) res.clear();
      
      int[] resArray = new int[res.size()];
      for(int i = 0; i < res.size(); i++){
        resArray[i] = res.get(i);
      }
      return resArray;
    }
  }