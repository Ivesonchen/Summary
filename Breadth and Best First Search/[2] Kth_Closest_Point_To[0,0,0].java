/**
 * Given three arrays sorted in ascending order. Pull one number from each array to form a coordinate <x,y,z> in a 3D space. Find the coordinates of the points that is k-th closest to <0,0,0>.

We are using euclidean distance here.

Assumptions

The three given arrays are not null or empty, containing only non-negative numbers
K >= 1 and K <= a.length * b.length * c.length
Return

a size 3 integer list, the first element should be from the first array, the second element should be from the second array and the third should be from the third array
Examples

A = {1, 3, 5}, B = {2, 4}, C = {3, 6}

The closest is <1, 2, 3>, distance is sqrt(1 + 4 + 9)

The 2nd closest is <3, 2, 3>, distance is sqrt(9 + 4 + 9)
 */
// hmmmmmm     keep  size = k  max-heap
public class Solution {
    class Triple implements Comparable<Triple> {
      private int i1, i2, i3;
      public Triple(int x, int y, int z) {
        this.i1 = x;
        this.i2 = y;
        this.i3 = z;
      }
      @Override
      public int compareTo(Triple o) {
        // TODO Auto-generated method stub
        long ea = this.i1*this.i1+this.i2*this.i2+this.i3*this.i3;
        long eb = o.i1*o.i1 + o.i2*o.i2 + o.i3*o.i3;
        return (int)(ea-eb);
      }
    }
  
    public List<Integer> closest(int[] a, int[] b, int[] c, int k) {
      if(a==null || b==null || c==null || a.length<1 || b.length<1 || c.length<1) {
        return null;
      }
  
      Queue<Triple> que = new PriorityQueue<Triple>();
      for(int i=0; i<a.length && i<k; i++) {
        for(int j=0; j<b.length && j<k; j++) {
          for(int m=0; m<c.length && m<k; m++) {
            que.add(new Triple(a[i], b[j], c[m]));
          }
        }
      }
      Triple cur = null;
      List<Integer> res = new ArrayList<>();
      while(!que.isEmpty() && k>0) {
        cur = que.poll();
        k--;
      }
      if(k==0) {
        res.addAll(Arrays.asList(cur.i1, cur.i2, cur.i3));
      }
      return res;
    }
}

public class Solution {
    public List<Integer> closest(int[] a, int[] b, int[] c, int k) {
      // Write your solution here
      PriorityQueue<Pair> pq = new PriorityQueue<>(new Comparator<Pair>(){
        public int compare(Pair a, Pair b){
          return a.arr[a.index] - b.arr[b.index];
        }
      });
  
      pq.add(new Pair(a, 0));
      pq.add(new Pair(b, 0));
      pq.add(new Pair(c, 0));
      List<Integer> res = new ArrayList<>();
      
      while(k-- >= 0){
        Pair cur = pq.poll();
        if(cur == null) break;
        int[] curArr = cur.arr;
        if(cur.index + 1 < curArr.length){
          pq.offer(new Pair(cur.arr, cur.index + 1));
        } else {
          res.add(cur.index);
        }
      }
  
      for(Pair ele : pq){
        res.add(ele.arr[ele.index]);
      }
  
      return res;
    }
  }
  
  class Pair{
    int[] arr;
    int index;
  
    public Pair(int[] arr, int index){
      this.arr = arr;
      this.index = index;
    }
  }
  