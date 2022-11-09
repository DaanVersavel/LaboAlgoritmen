import java.util.Stack;

public class Slot {
    private int x;
    private int y;
    private Stack<Integer> stack= new Stack<>() ;

    Slot(int x, int y) {
    this.x = x;
    this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void addContainer(int c) {
        stack.push(c);
    }

    public int getTopContainer() {
        return stack.pop();
    }
}
