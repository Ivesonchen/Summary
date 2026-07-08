//Given a stack, sort it in non-decreasing order
public static void stackSorting(Stack<Integer> stack) {
 Stack<Integer> t = new Stack<>();
  while(!stack.isEmpty()) {
        int item = stack.pop();
        while(!t.isEmpty() && t.peek() > item) stack.push(t.pop());
        t.push(item);   
  }   
  while(!t.isEmpty()) stack.push(t.pop());
}