/**
 * Given a collection of intervals, merge all overlapping intervals.

Example 1:

Input: [[1,3],[2,6],[8,10],[15,18]]
Output: [[1,6],[8,10],[15,18]]
Explanation: Since intervals [1,3] and [2,6] overlaps, merge them into [1,6].
Example 2:

Input: [[1,4],[4,5]]
Output: [[1,5]]
Explanation: Intervals [1,4] and [4,5] are considered overlapping.
 */

class Solution {
    public List<Interval> merge(List<Interval> intervals) {
        if(intervals == null || intervals.size() <= 1) return intervals;
        Collections.sort(intervals, (a,b) -> a.start - b.start);//java 8
        int start = intervals.get(0).start;
        int end = intervals.get(0).end;
        
        //两个指针
        List<Interval> res = new ArrayList<>();
        for(Interval interval : intervals) {
            if(interval.start <= end) {//这是有交叉的情况
                end = Math.max(end, interval.end);
            } else {//这是没有交叉的情况 把前一个interval 入res 然后start 和 end 重新赋值
                res.add(new Interval(start, end));
                start = interval.start;
                end = interval.end;  //相当于设置下一轮的 开始
            }
        }
        res.add(new Interval(start, end)); // 最后一轮的 入 res
        return res;
    }
}

class Solution {
    public int[][] merge(int[][] intervals) {
        List<int[]> res = new ArrayList<>();
        Arrays.sort(intervals, (a,b) -> a[0] - b[0]);
        
        int start = intervals[0][0];
        int end = intervals[0][1];
        
        for(int i = 1; i < intervals.length; i++) {
            int newStart = intervals[i][0];
            int newEnd = intervals[i][1];
            
            if(newStart <= end) {
                // overlap
                start = Math.min(start, newStart);
                end = Math.max(end, newEnd);
            } else {
                res.add(new int[] {start, end});
                
                start = newStart;
                end = newEnd;
            }
        }
        
        res.add(new int[]{start, end});
        
//         int[][] fResult = new int[res.size()][];
        
//         for(int i = 0; i < res.size(); i++) {
//             fResult[i] = res.get(i);
//         }
        
        return res.toArray(new int[res.size()][]);
    }
}