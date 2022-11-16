import java.util.Stack;

public class Slot {
    private int id;
    private int x;
    private int y;
    private int maxHeight;
    private Stack<Integer> stack;

    // Wordt eigenlijk niet gebruikt, bij het in lezen van de input worden de
    // variabelen van een slot direct ingevuld
    Slot(int x, int y, int id) {
        this.id = id;
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
