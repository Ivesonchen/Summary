/**
 * Given a string containing only 'A', 'B', 'C', 'D', 
 * return the number of occurrences of substring which has length 4 and includes all of the characters from 'A', 'B', 'C', 'D' with in descending sorted order.

Assumptions:

The given string is not null and has length of >= 4.
In the output, if two substrings have the same frequency, then they should be sorted by the their natural order.
Examples:

 “ABCDABCDD”, --> {"ABCD" : 2, "BCDA" : 1, "CDAB" : 1, "DABC" : 1}
 */

 // 提供了map遍历 和 compartor 的写法  使用hashmap来计数     int有限数组来记录sliding window

/**
 * public class Frequency {
 *   public String str;
 *   public int freq;
 *   public Frequency(String str, int freq) {
 *     this.str = str;
 *     this.freq = freq;
 *   }
 * }
 */
public class Solution {
    public List<Frequency> frequency(String input) {
      // Write your solution here.
      List<Frequency> res = new ArrayList<Frequency>();
      HashMap<String, Integer> map = new HashMap<>();
      int len = input.length();
  
      int[] record = new int[4];
  
      for(int i = 0; i < len; i++){
        record[input.charAt(i) - 'A']++;
        if(i >= 4) {
          record[input.charAt(i - 4) - 'A']--;
        }
        if(check(record)){
          String str = input.substring(i - 3, i + 1);
          map.put(str, map.getOrDefault(str, 0) + 1);
        }
      }
  
      for(Map.Entry<String, Integer> entry : map.entrySet()){
        res.add(new Frequency(entry.getKey(), entry.getValue()));
      }
  
      Collections.sort(res, new Comparator<Frequency>(){
        public int compare(Frequency f1, Frequency f2){
          if(f1.freq != f2.freq) return f2.freq - f1.freq;
          else return f1.str.compareTo(f2.str);
        }
      });
      return res;
    }
  
    public boolean check(int[] arr){
      for(int ele : arr){
        if(ele != 1) return false;
      }
  
      return true;
    }
  }