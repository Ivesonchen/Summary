/**
 * Given a 2D integer matrix, where every row is sorted in ascending order. How to find a common element in all rows. If there is no common element, then returns -1.

Example

matrix = { { 1, 2, 3, 4 }, { 4, 5, 6, 7 }, { 2, 3, 4, 8 } }

the common element is 4.
 */

 /**
  * 使用一个 hashmap key 存 matrix 的数值， value 存 出现过几次
    然后 扫描过matrix所有的 值 更新 value 出现过的次数

    post processing ： 扫描map  找出 value 等于 matrix 的行数   就找到了 在所有行中 出现的数
  */

public class Solution {
    public int search(int[][] matrix) {
      // Write your solution here
      if(matrix.length == 1) return -1;
      Map<Integer,Integer> mp = new HashMap<>();
      int M = matrix.length;
      // int N = matrix[0].length;
          
      // initalize 1st row elements with value 1 
      for (int j = 0; j < matrix[0].length; j++) 
          mp.put(matrix[0][j],1); 
            
      // traverse the matrix 
      for (int i = 1; i < M; i++) { 
          for (int j = 0; j < matrix[i].length; j++) { 
              // If element is present in the map and 
              // is not duplicated in current row. 
              if (mp.get(matrix[i][j]) != null && mp.get(matrix[i][j]) == i) 
              { 
                  // we increment count of the element 
                  // in map by 1 
                  mp.put(matrix[i][j], i + 1); 
    
                  // // If this is last row 
                  // if (i == M - 1) 
                  //     System.out.print(mat[i][j] + " "); 
              } 
          } 
      }
      int counter = 0;
  
      for(Map.Entry<Integer, Integer> entry : mp.entrySet()){
        if(entry.getValue() == M){
          return entry.getKey();
        }
      }
  
      return -1;
    }
  }
  