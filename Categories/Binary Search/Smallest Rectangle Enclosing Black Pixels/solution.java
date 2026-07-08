/**
 * An image is represented by a binary matrix with 0 as a white pixel and 1 as a black pixel. The black pixels are connected, i.e., there is only one black region. 
 * Pixels are connected horizontally and vertically. 
 * Given the location (x, y) of one of the black pixels, return the area of the smallest (axis-aligned) rectangle that encloses all black pixels.

For example, given the following image:

[
  "0010",
  "0110",
  "0100"
]
and x = 0, y = 2,

Return 6.
 */

public class Solution {
    public int minArea(int[][] image, int x, int y) {
      // Write your solution here
      if(image.length == 0 || image[0].length == 0) return 0;
  
      int rowNum = image.length, colNum = image[0].length;    // colNum and rowNum 为什么不用-1 
                                                              // 是因为 这里的最后的结果值真的需要取到 bound 之外 来表明 一直到区间外都没有找到合适的列或行
      int left = horizontalSearch(0, y, 0, rowNum, image, 1);
      int right = horizontalSearch(y + 1, colNum, 0, rowNum, image, 0);
      int up = verticalSearch(0, x, left, right, image, 1);
      int down = verticalSearch(x + 1, rowNum, left, right, image, 0);
  
      return (right - left) * (down - up);
    }
    
    private int horizontalSearch(int start, int end, int top, int bottom, int[][] image, int target){
      while(start < end){
        int mid = start + (end - start) / 2;
        int tempTop = top;
        int found = 0;
        while(tempTop < bottom){
          if(image[tempTop++][mid] == 1){
            found = 1;
            break;
          }
        }
        if(target == found) end = mid;
        else start = mid + 1;
      }
      return start;
    }
  
    private int verticalSearch(int start, int end, int left, int right, int[][] image, int target){
      while(start < end){
        int mid = start + (end - start) / 2;
        int tempLeft = left;
        int found = 0;
        while(tempLeft < right){
          if(image[mid][tempLeft++] == 1){
            found = 1;
            break;
          }
        }
        if(target == found) end = mid;
        else start = mid + 1;
      }
      return start;
    }
  }
  
  public class SmallestRectangleEnclosingBlackPixels {
    public int minArea(char[][] image, int x, int y) {
        int row = image.length;
        int col = image[0].length;

        int left = binarySearchLeft(image, 0, y, true);
        int right = binarySearchRight(image, y, col - 1, true);

        int top = binarySearchLeft(image, 0, x, false);
        int bottom = binarySearchRight(image, x, row - 1, false);

        return (right - left + 1) * (bottom - top + 1);
    }

    private int binarySearchLeft(char[][] image, int left, int right, boolean isHor) {
        while (left + 1 < right) {
            int mid = (right - left) / 2 + left;
            if (hasBlack(image, mid, isHor)) {
                right = mid;
            } else {
                left = mid;
            }
        }
        if (hasBlack(image, left, isHor)) {
            return left;
        }
        return right;
    }

    private int binarySearchRight(char[][] image, int left, int right, boolean isHor) {
        while (left + 1 < right) {
            int mid = (right - left) / 2 + left;
            if (hasBlack(image, mid, isHor)) {
                left = mid;
            } else {
                right = mid;
            }
        }
        if (hasBlack(image, right, isHor)) {
            return right;
        }
        return left;
    }

    private boolean hasBlack(char[][] image, int x, boolean isHor) {
        if (isHor) {
            for (int i = 0; i < image.length; i++) {
                if (image[i][x] == '1') return true;
            }
        } else {
            for (int i = 0; i < image[0].length; i++) {
                if (image[x][i] == '1') return true;
            }
        }
        return false;
    }
}