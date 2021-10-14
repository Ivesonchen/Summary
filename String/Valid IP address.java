/**
 * Input: IP = "172.16.254.1"
Output: "IPv4"
Explanation: This is a valid IPv4 address, return "IPv4".

Input: IP = "2001:0db8:85a3:0:0:8A2E:0370:7334"
Output: "IPv6"
Explanation: This is a valid IPv6 address, return "IPv6".

Input: IP = "2001:0db8:85a3:0:0:8A2E:0370:7334:"
Output: "Neither"

Input: IP = "1e1.4.5.6"
Output: "Neither"
 */
public class Solution {
  public String validIPAddress(String IP) {
      if(IP.equals("")) return "Neither";
      if(isIP4(IP)) return "IPv4";
      if(isIP6(IP)) return "IPv6";
      return "Neither";
  }
  
  public boolean isIP4(String IP){
      if(IP.charAt(0)=='.' || IP.charAt(IP.length()-1)=='.')return false;
      String[] temp = IP.split("\\.");
      if(temp.length!=4)return false;
      for(int i=0;i<4;i++){
          try{
              if(temp[i].startsWith("0")&&temp[i].length()>1) return false;
              if(Integer.parseInt(temp[i])>255 || temp[i].charAt(0)=='-' || temp[i].charAt(0)=='+')return false;    
          }
          catch(NumberFormatException e){
              System.out.println("ERROR");
              return false;
          }
      }
      return true;
  }
  
  public boolean isIP6(String IP){
      if(IP.charAt(0)==':' || IP.charAt(IP.length()-1)==':')return false;
      String[] temp = IP.split(":");
      if(temp.length!=8)return false;
      for(int i=0;i<8;i++){
          if(temp[i].length()>4||temp[i].length()==0)return false;
          for(int j=0;j<temp[i].length();j++){
              if((temp[i].charAt(j)>='0' && temp[i].charAt(j)<='9') || (temp[i].charAt(j)>='a' && temp[i].charAt(j)<='f') || (temp[i].charAt(j)>='A' && temp[i].charAt(j)<='F')){}
              else return false;
          }
      }
      return true;   
  }
}