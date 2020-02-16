public class Edge {
    private Point from, to;

    // -----

    Edge(Point from, Point to) {
        this.from = from;
        this.to = to;
    }

    Point getFrom() {
        return from;
    }

    Point getTo() {
        return to;
    }

    // -----

    /*
     * Returns a new edge whose endpoints are flipped.
     */
    Edge flip() {
        return new Edge(to, from);
    }

    // -----

    public String toString() {
        return String.format("%s -> %s", from, to);
    }

    public boolean equals(Object o) {
        if (o instanceof Edge) {
            Edge that = (Edge) o;
            return from.equals(that.from) && to.equals(that.to);
        }
        return false;
    }

    public int hashCode() {
        return from.hashCode() + to.hashCode();
    }
}
