/**
 * Given a string containing just the characters '(', ')', '{', '}', '[' and ']', determine if the input string is valid. 
 * The brackets must close in the correct order.

Examples

"()" and "()[]{}", "[{()}()]" are all valid but "(]" and "([)]" are not.
 */


class Solution {
    public boolean isValid(String s) {
        if(s == null || s.length() == 0) return true;
        //  把相反的符号入栈  然后依次pop
        Stack<Character> stack = new Stack<>();
        
        for(Character ch : s.toCharArray()){
            if (ch == '(') {
                stack.push(')');
            } else if (ch == '{') {
                stack.push('}');
            } else if (ch == '[') {
                stack.push(']');
            } else {
                if(stack.isEmpty() || stack.pop() != ch){
                    return false;
                }
            }
        }
        return stack.isEmpty(); //如果到最后还是不为空 也是不能通过的
    }
}