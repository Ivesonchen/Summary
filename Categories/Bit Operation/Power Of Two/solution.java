/**
 * Determine if a given integer is power of 2.

    Examples

    16 is power of 2 (2 ^ 4)
    3 is not
    0 is not
    -1 is not

 */

public class Solution {
    public boolean isPowerOfTwo(int number) {
      // Write your solution here
      if(number < 1) return false;
  
      while(number > 1){
        if(number % 2 != 0) return false;       // 如果除不进 就说明不对
        number /= 2;
      }
  
      return true;
    }
  }

/**
 * Power of 2 说明 只有一位是1
  1000000000     x
                 &
  0111111111     x - 1
-----------------------
  0000000000
*/

public class Solution {
    public boolean isPowerOfTwo(int number) {
        // Write your solution here
        if(number <= 0) return false;
        int temp = number & (number - 1);
        return temp == 0;
    }
}
  