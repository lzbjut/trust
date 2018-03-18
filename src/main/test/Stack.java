// Can you spot the "memory leak"?

import java.util.*;

public class Stack {
    private Object[] elements;
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    public Stack() {
        elements = new Object[DEFAULT_INITIAL_CAPACITY];
    }

    public void push(Object e) {
        ensureCapacity();
        elements[size++] = e;
    }

    public Object pop() {
        if (size == 0)
            throw new EmptyStackException();
        return elements[--size];
    }

    /**
     * Ensure space for at least one more element, roughly
     * doubling the capacity each time the array needs to grow.
     */
    private void ensureCapacity() {
        if (elements.length == size)
            elements = Arrays.copyOf(elements, 2 * size + 1);
    }
    public static void main(String[] args){
        Stack stack=new Stack();
        while(true){
            for(int i=0;i<16;i++){
                stack.push(new String("addasdasdas"));
            }
            for(int j=0;j<16;j++){
                stack.pop();
            }
        }
    }
}
