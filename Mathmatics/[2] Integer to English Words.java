/**
 * Convert a non-negative integer to its english words representation. Given input is guaranteed to be less than 231 - 1.

For example,

123 -> "One Hundred Twenty Three"
12345 -> "Twelve Thousand Three Hundred Forty Five"
1234567 -> "One Million Two Hundred Thirty Four Thousand Five Hundred Sixty Seven"
 */

public class Solution {
    public String[] v1 = {"", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"};
    public String[] v2 = {"", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"};
    public String[] v3 = {"", "Thousand", "Million", "Billion"};
    public String numberToWords(int num) {
        if (num < 0)  return "";
        if (num == 0) return "Zero";

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 4; i++) {
            int partial = num % 1000; // Obtain smaller 3-digit section
            if (partial > 0) { // Append suffix depending on i, where v3[0] = "";
                sb.insert(0, calcPartialNumber(partial) + " " + v3[i] + " ");
            }
            num /= 1000;
        }
        
        return sb.toString().trim();
    }
    
    private String calcPartialNumber(int num) {
        StringBuffer sb = new StringBuffer();
        if (num >= 100) {
            int hund = num / 100;
            sb.append(v1[hund] + " Hundred ");
            num = num % 100;
        }

        if (num < 20) sb.append(v1[num] + " ");
        else {
            int numTens = num / 10;
            int numDigit = num % 10;
            sb.append(v2[numTens] + " " + v1[numDigit] + " ");
        }

        return sb.toString().trim();
    }
}