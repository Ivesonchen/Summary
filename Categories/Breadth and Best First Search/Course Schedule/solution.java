class Solution {
  public boolean canFinish(int numCourses, int[][] prerequisites) {
      int[] inDegree = new int[numCourses];
      Map<Integer, List<Integer>> adjcent = new HashMap<>();
      Set<Integer> seen = new HashSet<>();
      
      for(int[] line : prerequisites) {
          inDegree[line[0]] ++;
          List<Integer> temp = adjcent.getOrDefault(line[1], new ArrayList<>());
          temp.add(line[0]);
          adjcent.put(line[1], temp);
      }
      
      Queue<Integer> queue = new LinkedList<>();
      
      for(int i = 0; i < inDegree.length; i++) {
          if(inDegree[i] == 0) queue.offer(i);
      }
      
      while(!queue.isEmpty()) {
          int cur = queue.poll();
          seen.add(cur);
          List<Integer> ad = adjcent.get(cur);
          if(ad == null) continue;
          for(int i = 0; i < ad.size(); i++) {
              if(--inDegree[ad.get(i)] == 0) queue.offer(ad.get(i));
          }
      }
      
      return seen.size() == numCourses;
  }
}