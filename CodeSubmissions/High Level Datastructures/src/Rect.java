public class Rect {
    private int xmin, ymin, xmax, ymax;

    // -----

    Rect(int xmin, int ymin, int xmax, int ymax) {
        this.xmin = xmin;
        this.ymin = ymin;
        this.xmax = xmax;
        this.ymax = ymax;
    }

    int getXmin() {
        return xmin;
    }

    int getYmin() {
        return ymin;
    }

    int getXmax() {
        return xmax;
    }

    int getYmax() {
        return ymax;
    }

    // -----

    Rect leftOf(Point p) {
        return new Rect(xmin, ymin, p.getX(), ymax);
    }

    Rect rightOf(Point p) {
        return new Rect(p.getX(), ymin, xmax, ymax);
    }

    Rect underOf(Point p) {
        return new Rect(xmin, ymin, xmax, p.getY());
    }

    Rect aboveOf(Point p) {
        return new Rect(xmin, p.getY(), xmax, ymax);
    }

    boolean contains(Point p) {
        if(p.getX() >= xmin && p.getX() <= xmax && p.getY() >= ymin && p.getY() <= ymax){
            return true;
        }
        else return false;
    }

    boolean intersect(Rect r) {
        if(r.xmin > xmax || xmin > r.xmax){
            return false;
        }
        if(r.ymax < ymin || ymax < r.ymin){
            return false;
        }
        return true;
    }

    int distanceFormula(Point p1, Point p2){
        return (p2.getY() - p1.getY()) * (p2.getY() - p1.getY()) + (p2.getX() - p1.getX()) * (p2.getX() - p1.getX());

    }

    int distanceSquaredTo(Point p) {
        if(contains(p)){
            return 0;
        }
        if(p.getX() < xmax && p.getX() > xmin){
            if(p.getY() > ymax){
                return distanceFormula(new Point(p.getX(), ymax), p);
            }
            else
                return distanceFormula(new Point(p.getX(), ymin), p);
        }
        if(p.getY() < ymax && p.getY() > ymin){
            if(p.getX() > xmax){
                return distanceFormula(new Point(xmax, p.getY()), p);
            }
            else
                return distanceFormula(new Point(xmin, p.getY()), p);
        }
        else{
            if(p.getX() > xmax && p.getY() > ymax){
                return distanceFormula(new Point(xmax, ymax), p);
            }
            if(p.getX() > xmax && p.getY() < ymin){
                return distanceFormula(new Point(xmax, ymin) ,p);
            }
            if(p.getX() < xmin && p.getY() < ymin){
                return distanceFormula(new Point(xmin, ymin), p);
            }
            else{
                return distanceFormula(new Point(xmin, ymax), p);
            }
        }
    }

    // -----

    public String toString() {
        return String.format("R[(%d,%d)--(%d,%d)]", xmin, ymin, xmax, ymax);
    }

    public boolean equals(Object o) {
        if (o instanceof Rect) {
            Rect other = (Rect) o;
            return xmin == other.xmin && xmax == other.xmax &&
                    ymin == other.ymin && ymax == other.ymax;
        } else return false;
    }

    public int hashCode() {
        return Integer.hashCode(xmin) + Integer.hashCode(xmax) +
                Integer.hashCode(ymin) + Integer.hashCode(ymax);
    }
}
