/**
 * 双Stack：一个正常stack，另一个minStack存当下level最小值. 注意维护minStack的变化

另外. 如果要maxStack，也是类似做法
 */
public class MaxStack{
    private Stack<Integer> stack;
    private Stack<Integer> maxStack;

    MaxStack(){
        stack = new Stack<Integer>();
        maxStack = new Stack<Integer>();
    }

    public void push(int value){
        stack.push(value);
        if(maxStack.isEmpty()){
            maxStack.push(value);
        } else {
            if(maxStack.peek() <= value ){
                maxStack.push(value);
            }else {
                maxStack.push(maxStack.peek());
            }
        }
    }

    public int pop(){
        maxstack.pop();
        return stack.pop();
    }

    public int peek(){
        return maxStack.peek();
    }
}