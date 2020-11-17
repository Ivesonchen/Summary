/**
 * Given a 2D grid, each cell is either an house 1 or empty 0 (the number zero, one), find the place to build a post office, 
 * the distance that post office to all the house sum is smallest. 
 * Return the smallest distance. Return -1 if it is not possible.
Notice

You can pass through house and empty.
You only build post office on an empty.
Example
Given a grid:

0 1 0 0
1 0 1 1 
0 1 0 0

return 6. (Placing a post office at (1,1), the distance that post office to all the house sum is smallest.)

https://zhengyang2015.gitbooks.io/lintcode/content/build_post_office_573.html
 */
/**
 * 这道题可以用dp来做，但是会超时。首先扫描一遍将所有house的点记录下来，然后遍历图中所有0的点，计算每个0的点到这些house的距离和，选最小的那个即可。这种情况下可以优化到O(k * n ^ 2)，
 * 但是如果数据量很大还是过不了。
因此需要减少搜索的点。想到的方法是在所有房子围成的形状的重心位置附近建邮局则到所有房子的距离之和最短（怎么证明？）。因此步骤如下：
首先找到所有房子的重心。找所有房子x值的median和y值的median（如果是奇数个就是排序后取中间值，如果是偶数则取中间两个数再取平均值）即为重心。
然后用bfs来搜索。将重心加入queue中，然后开始一圈一圈（将出队的每个点周围八个点加入队中）向外找，用的是和逐层遍历二叉树的类似的方法（即在每一层开始的时候记录一下本层的点的个数，
然后一次出队这么多点即可将本层的点全部出队）。
每一圈结束时，返回该圈上的点作为post office能取的最小值，如果存在则停止搜索。即如果存在可以作为post office的点，则外圈点到各个房子的距离一定不会比内圈点更优。
 */
public class Solution {
    /**
     * @param grid a 2D grid
     * @return an integer
     */
    class Node{
        int x;
        int y;
        public Node(int x, int y){
            this.x = x;
            this.y = y;
        }
    }
    int[] dx = {0, 0, -1, 1, -1, 1, -1, 1};
    int[] dy = {-1, 1, 0, 0, -1, -1, 1, 1};

    //Median version
    public int shortestDistance(int[][] grid) {
        // Write your code here
        if(grid == null || grid.length == 0 || grid[0].length == 0){
            return -1;
        }

        int n = grid.length;
        int m = grid[0].length;
        boolean[][] visit = new boolean[n][m];
        ArrayList<Node> house = new ArrayList<Node>();
        ArrayList<Integer> xArr = new ArrayList<Integer>();
        ArrayList<Integer> yArr = new ArrayList<Integer>();
        //find house position
        for(int i = 0; i < n; i++){
            for(int j = 0; j < m; j++){
                if(grid[i][j] == 1){
                    house.add(new Node(i, j));
                    xArr.add(i);
                    yArr.add(j);
                }
            }
        }
        //no empty place
        if(house.size() == m * n){
            return -1;
        }

        if(house.size() == 0){
            return 0;
        }

        //find the median of house positions
        int xMedian = getMedian(xArr);
        int yMedian = getMedian(yArr);

        Queue<Node> queue = new LinkedList<Node>();
        queue.add(new Node(xMedian, yMedian));
        visit[xMedian][yMedian] = true;
        int min = Integer.MAX_VALUE;
        while(!queue.isEmpty()){
            int size = queue.size();
            for(int i = 0; i < size; i++){
                Node curt = queue.poll();
                if(grid[curt.x][curt.y] == 0){
                    min = Math.min(min, search(house, curt));
                }
                for(int j = 0; j < 8; j++){
                    int nextX = curt.x + dx[j];
                    int nextY = curt.y + dy[j];
                    if(nextX >= 0 && nextX < n && nextY >= 0 && nextY < m && !visit[nextX][nextY]){
                        visit[nextX][nextY] = true;
                        queue.add(new Node(nextX, nextY));
                    }
                }
            }
            if(min != Integer.MAX_VALUE){
                return min;
            }
        }

        return -1;
    }

    private int getMedian(ArrayList<Integer> arr){
        Collections.sort(arr);

        int Median = arr.get(arr.size() / 2);

        if(arr.size() % 2 == 0){
            Median = (Median + arr.get(arr.size() / 2 - 1)) / 2;
        }

        return Median;
    }

    private int search(ArrayList<Node> house, Node curt){
        int sum = 0;
        for(Node node : house){
            sum += Math.abs(curt.x - node.x) + Math.abs(curt.y - node.y);
        }
        return sum;
    }
}