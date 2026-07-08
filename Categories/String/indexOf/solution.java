public int strStr(String haystack, String needle) {

  int h=0,n=0;
  while(h < haystack.length() && n < needle.length()){
      if(haystack.charAt(h) == needle.charAt(n)){
          //if both are equals then increase needle index
          n++;
      }else{
          //else get back to index where needle and haystack first char matched
          h=h-n;
          n=0;
      }
      //increase by 1 in both conditions so everytime if complete needles does not match
      //start from the next index of before matched index of heystack
      h++;
  }
  if(n==needle.length()){
      return h-n;
  }
  return -1;
}


/**
 *          a a b b a c c c
 *          a c
 */

public int indexOf(String aStr, String bStr) {
  int a = 0;
  int b = 0;

  while(a < aStr.length() && b < bStr.length()) {
    if(aStr.charAt(a) == b.charAt(b)) {
      b++;
      a++;
    } else {
      a = a - b + 1;
      b = 0;
    }
  }

  if(b == bStr.length()) return a - b;

  return -1;
}