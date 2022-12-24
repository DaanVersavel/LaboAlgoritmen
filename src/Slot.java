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

    @Override
    public boolean equals(Object o) {
        // If the object is compared with itself then return true
        if (o == this) {
            return true;
        }
        /* Check if o is an instance of Complex or not
        "null instanceof [type]" also returns false */
        if (!(o instanceof Slot)) {
            return false;
        }
        // typecast o to Complex so that we can compare data members
        Slot c = (Slot) o;
        // Compare the data members and return accordingly
        return (id == c.id && x == c.x && y == c.y &&maxHeight ==c.maxHeight
                && stack.equals(c.stack));
    }

    // Doordat we de constructor eigenlijk niet gebruiken wordt de stack nooit geïnitialiseerd.
    public void initialiseStack() {
        this.stack = new Stack<>();
    }
    public void initialiseMaxHeight() { this.maxHeight = 3; }

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

    public void addContainer(int containerId) { stack.push(containerId); }

    public int getTopContainer() {
        return stack.peek();
    }

    public int removeTopContainer() {
        return stack.pop();
    }

    public int getMaxHeight() { return maxHeight; }

    public Stack<Integer> getStack() { return stack; }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public void setStack(Stack<Integer> stack) {
        this.stack = stack;
    }
}
