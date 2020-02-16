class Point {
    private int x, y;

    // -----

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    // -----
    int square(int x) {
        return x * x;
    }

    int distanceSquaredTo(Point p) {
        return square(x - p.getX()) + square(y - p.getY());
    }

    // -----

    public String toString() {
        return String.format("(%d,%d)", x, y);
    }

    public boolean equals(Object other) {
        if (other instanceof Point) {
            Point otherp = (Point) other;
            return x == otherp.getX() && y == otherp.getY();
        } else return false;
    }

    public int hashCode() {
        return Integer.hashCode(x) + Integer.hashCode(y);
    }
}
