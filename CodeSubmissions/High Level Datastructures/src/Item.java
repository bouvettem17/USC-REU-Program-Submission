import java.util.ArrayList;

public class Item<E> {
    private E data;
    private String name;
    private int value;
    private int position;
    private boolean visited;
    private Item<E> previous;

    static ArrayList<Edge> pathToSource(Item<Point> u) {
        ArrayList<Edge> previousSequence = new ArrayList<>();
        Item<Point> current = u;
        while (!(current.previous == null)) {
            previousSequence.add(new Edge(current.data, current.previous.data));
            current = current.previous;
        }
        return previousSequence;
    }

    // -----

    Item(E data, String name, int value) {
        this.data = data;
        this.name = name;
        this.value = value;
        this.reset();
    }

    void reset() {
        this.position = -1;
        this.visited = false;
        this.previous = null;
    }

    E getData() {
        return data;
    }

    String getName() {
        return name;
    }

    int getValue() {
        return value;
    }

    int getPosition() {
        return position;
    }

    boolean isVisited() {
        return visited;
    }

    Item<E> getPrevious() {
        return previous;
    }

    // -----

    void setValue(int value) {
        this.value = value;
    }

    void setPosition(int position) {
        this.position = position;
    }

    void setVisited(boolean visited) {
        this.visited = visited;
    }

    void setPrevious(Item<E> previous) {
        this.previous = previous;
    }

    // -----

    public String toString() {
        return String.format("%s", name);
    }

    public boolean equals(Object o) {
        if (o instanceof Item) {
            Item that = (Item) o;
            return name.equals(that.getName());
        } else return false;
    }

    public int hashCode() {
        return name.hashCode();
    }
}
