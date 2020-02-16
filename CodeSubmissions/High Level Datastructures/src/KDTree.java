import java.util.ArrayList;
import java.util.List;

// -----

abstract class XTree implements TreePrinter.PrintableNode {
    abstract Region findRegion(Point p);

    static XTree makeXTree(List<Point> xPoints, List<Point> yPoints, int bound) {
        if(xPoints.size() <= bound){
            return new XEmpty();
        }

        List<Point> newXPointsLeft = xPoints.subList(0, xPoints.size()/2);
        List<Point> newXPointsRight = xPoints.subList(xPoints.size()/2+1, xPoints.size());
        List<Point> newYPointsLeft = new ArrayList<>();
        List<Point> newYPointsRight = new ArrayList<>();
        for(int i = 0; i < yPoints.size(); i++){
            if(newXPointsLeft.contains(yPoints.get(i))){
                newYPointsLeft.add(yPoints.get(i));
            }
        }
        for(int i = 0; i < yPoints.size();i++){
            if(newXPointsRight.contains(yPoints.get(i))){
                newYPointsRight.add(yPoints.get(i));
            }
        }
        return new XNode(xPoints.get(xPoints.size()/2), YTree.makeYTree(newYPointsLeft, newXPointsLeft, bound), YTree.makeYTree(newYPointsRight, newXPointsRight, bound));
    }
}

abstract class YTree implements TreePrinter.PrintableNode {
    abstract Region findRegion(Point p);

    static YTree makeYTree(List<Point> yPoints, List<Point> xPoints, int bound) {
        if(yPoints.size() <= bound){
            return new YEmpty();
        }

        List<Point> newYPointsLeft = yPoints.subList(0, yPoints.size()/2);
        List<Point> newYPointsRight = yPoints.subList(yPoints.size()/2+1, yPoints.size());
        List<Point> newXPointsLeft = new ArrayList<>();
        List<Point> newXPointsRight = new ArrayList<>();
        for(int i = 0; i < xPoints.size(); i++){
            if(newYPointsLeft.contains(xPoints.get(i))){
                newXPointsLeft.add(xPoints.get(i));
            }
        }
        for(int i = 0; i < xPoints.size();i++){
            if(newYPointsRight.contains(xPoints.get(i))){
                newXPointsRight.add(xPoints.get(i));
            }
        }
        return new YNode(yPoints.get(yPoints.size()/2), XTree.makeXTree(newXPointsLeft, newYPointsLeft, bound), XTree.makeXTree(newXPointsRight, newYPointsRight, bound));
    }
}

// -----

class XEmpty extends XTree {
    public String getText() {
        return "";
    }

    public TreePrinter.PrintableNode getLeft() {
        return null;
    }

    public TreePrinter.PrintableNode getRight() {
        return null;
    }

    public Region findRegion(Point p) {
        return new Region();
    }
}

class YEmpty extends YTree {
    public String getText() {
        return "";
    }

    public TreePrinter.PrintableNode getLeft() {
        return null;
    }

    public TreePrinter.PrintableNode getRight() {
        return null;
    }

    public Region findRegion(Point p) {
        return new Region();
    }
}

// -----

class XNode extends XTree {
    private Point point;
    private YTree left, right;

    XNode(Point point, YTree left, YTree right) {
        this.point = point;
        this.left = left;
        this.right = right;
    }

    public String getText() {
        return point.toString();
    }

    public TreePrinter.PrintableNode getLeft() {
        return left;
    }

    public TreePrinter.PrintableNode getRight() {
        return right;
    }

    public Region findRegion(Point p) {
        Region hold = new Region();
        if(p.equals(point)){
            return hold;
        }
        if(p.getX() < point.getX()){
            hold.push(DIR.LEFT);
            hold.getDirs().addAll(left.findRegion(p).getDirs());
            return hold;
        }

        hold.push(DIR.RIGHT);
        hold.getDirs().addAll(right.findRegion(p).getDirs());
        return hold;
    }
}

class YNode extends YTree {
    private Point point;
    private XTree under, above;

    YNode(Point point, XTree under, XTree above) {
        this.point = point;
        this.under = under;
        this.above = above;
    }

    public String getText() {
        return point.toString();
    }

    public TreePrinter.PrintableNode getLeft() {
        return under;
    }

    public TreePrinter.PrintableNode getRight() {
        return above;
    }

    public Region findRegion(Point p) {
        Region hold = new Region();
        if(p.equals(point)){
            return hold;
        }
        if(p.getY() < point.getY()){
            hold.push(DIR.UNDER);
            hold.getDirs().addAll(under.findRegion(p).getDirs());
            return hold;
        }

        hold.push(DIR.ABOVE);
        hold.getDirs().addAll(above.findRegion(p).getDirs());
        return hold;
    }
}

// -----
