import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class Tests {
    @Test
    public void testPoint() {
        Point p1, p2, p3, p4;
        p1 = new Point(1, 1);
        p2 = new Point(4, 5);
        p3 = new Point(2, 3);
        p4 = new Point(4, 5);
        assertEquals(4, p2.getX());
        assertEquals(5, p2.getY());
        assertEquals(25, p1.distanceSquaredTo(p2));
        assertEquals(25, p2.distanceSquaredTo(p1));
        assertEquals(0, p1.distanceSquaredTo(p1));
        assertEquals(0, p2.distanceSquaredTo(p2));
        assertEquals(p4, p4);
        assertEquals(p4.hashCode(), p4.hashCode());
    }

    @Test
    public void testEdge() {
        Point p1, p2, p3, p4;
        p1 = new Point(1, 1);
        p2 = new Point(4, 5);
        p3 = new Point(2, 3);
        p4 = new Point(4, 5);

        Edge e1, e2, e3, e4, e5;
        e1 = new Edge(p1, p2);
        e2 = new Edge(p3, p3);
        e3 = new Edge(p2, p4);
        e4 = new Edge(p2, p3);
        e5 = new Edge(p2, p3);

        assertEquals(e1.getFrom(), p1);
        assertEquals(e1.getTo(), p2);
        assertEquals(e2.getFrom(), p3);
        assertEquals(e3.getTo(), p4);
        assertEquals(e2.getFrom(), e2.getTo());
        assertEquals(e4, e5);
        assertEquals(e4.hashCode(), e4.hashCode());
    }

    @Test
    public void testRect() {
        Rect r1, r2, r3, r4, r5, r6, r7, r8;
        r1 = new Rect(1, 1, 5, 6);
        r2 = new Rect(2, 3, 4, 4);
        r3 = new Rect(1, 2, 6, 5);
        r4 = new Rect(7, 3, 8, 4);

        r5 = new Rect(1, 1, 3, 6);
        r6 = new Rect(3, 1, 5, 6);
        r7 = new Rect(1, 1, 5, 3);
        r8 = new Rect(1, 3, 5, 6);

        assertEquals(r3.getXmin(), 1);
        assertEquals(r3.getYmin(), 2);
        assertEquals(r3.getXmax(), 6);
        assertEquals(r3.getYmax(), 5);

        Point p = new Point(3, 3);
        assertEquals(r5, r1.leftOf(p));
        assertEquals(r6, r1.rightOf(p));
        assertEquals(r7, r1.underOf(p));
        assertEquals(r8, r1.aboveOf(p));

        assertTrue(r1.contains(p));
        assertTrue(r2.contains(p));
        assertTrue(r3.contains(p));
        assertFalse(r4.contains(p));

        assertTrue(r1.intersect(r1));
        assertTrue(r1.intersect(r2));
        assertTrue(r1.intersect(r3));
        assertFalse(r1.intersect(r4));

        assertTrue(r2.intersect(r1));
        assertTrue(r2.intersect(r2));
        assertTrue(r2.intersect(r3));
        assertFalse(r2.intersect(r4));

        assertTrue(r3.intersect(r1));
        assertTrue(r3.intersect(r2));
        assertTrue(r3.intersect(r3));
        assertFalse(r3.intersect(r4));

        assertFalse(r4.intersect(r1));
        assertFalse(r4.intersect(r2));
        assertFalse(r4.intersect(r3));
        assertTrue(r4.intersect(r4));

        assertEquals(1, r3.distanceSquaredTo(new Point(3, 1)));
        assertEquals(10, r3.distanceSquaredTo(new Point(0, 2)));
        assertEquals(4, r3.distanceSquaredTo(new Point(3, 0)));
        assertEquals(1, r3.distanceSquaredTo(new Point(7, 3)));
        assertEquals(25, r3.distanceSquaredTo(new Point(2, 10)));
        assertEquals(5, r3.distanceSquaredTo(new Point(0, 0)));
        assertEquals(1, r3.distanceSquaredTo(new Point(0, 5)));
        assertEquals(5, r3.distanceSquaredTo(new Point(7, 0)));
        assertEquals(0, r3.distanceSquaredTo(new Point(6, 5)));
    }

    @Test
    public void testRegion() {
        Region r1, r2;
        r1 = new Region();
        r2 = new Region();

        assertTrue(r1.isEmpty());
        assertTrue(r2.getDirs().isEmpty());

        r1.push(DIR.RIGHT);
        assertFalse(r1.isEmpty());
        assertTrue(r1.getDirs().contains(DIR.RIGHT));
        assertTrue(r2.getDirs().isEmpty());

        r1.push(DIR.ABOVE);
        assertEquals(r1.getDirs().get(0), DIR.ABOVE);
        assertEquals(r1.getDirs().get(1), DIR.RIGHT);

        r1.push(DIR.LEFT);
        r2.push(DIR.RIGHT);
        r2.push(DIR.ABOVE);
        r2.push(DIR.LEFT);
        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    public void testItem() {
        Point p1, p2, p3, p4, p5;
        Item<Point> pp1, pp2, pp3, pp4, pp5;

        p1 = new Point(2, 2);
        p2 = new Point(3, 3);
        p3 = new Point(4, 4);
        p4 = new Point(5, 5);
        p5 = new Point(6, 6);

        pp1 = new Item<>(p1, p1.toString(), 0);
        pp2 = new Item<>(p2, p2.toString(), 0);
        pp3 = new Item<>(p3, p3.toString(), 0);
        pp4 = new Item<>(p4, p4.toString(), 0);
        pp5 = new Item<>(p5, p5.toString(), 0);

        assertEquals(pp1, new Item<>(new Point(2, 2), "(2,2)", 0));
        assertEquals(pp1.hashCode(),
                new Item<>(new Point(2, 2), "(2,2)", 0).hashCode());

        pp1.setPrevious(pp2);
        pp2.setPrevious(pp3);
        pp3.setPrevious(pp4);
        pp4.setPrevious(pp5);

        ArrayList<Edge> path = Item.pathToSource(pp1);
        assertEquals(4, path.size());

        Edge e1, e2, e3, e4;
        e1 = path.get(0);
        e2 = path.get(1);
        e3 = path.get(2);
        e4 = path.get(3);

        assertEquals(e1.getFrom(), p1);
        assertEquals(e1.getTo(), p2);

        assertEquals(e2.getFrom(), p2);
        assertEquals(e2.getTo(), p3);

        assertEquals(e3.getFrom(), p3);
        assertEquals(e3.getTo(), p4);

        assertEquals(e4.getFrom(), p4);
        assertEquals(e4.getTo(), p5);
    }

    @Test
    public void testBinaryHeap() {
        Point p1, p2, p3, p4, p5;
        Item<Point> pp1, pp2, pp3, pp4, pp5;

        p1 = new Point(2, 2);
        p2 = new Point(3, 3);
        p3 = new Point(4, 4);
        p4 = new Point(5, 5);
        p5 = new Point(6, 6);

        pp1 = new Item<>(p1, p1.toString(), 2);
        pp2 = new Item<>(p2, p2.toString(), 3);
        pp3 = new Item<>(p3, p3.toString(), 4);
        pp4 = new Item<>(p4, p4.toString(), 5);
        pp5 = new Item<>(p5, p5.toString(), 6);

        BinaryHeap<Point> h = new BinaryHeap<>();
        h.insert(pp1);
        h.insert(pp2);
        h.insert(pp3);
        h.insert(pp4);
        h.insert(pp5);

        ArrayList<Item<Point>> sorted;
        sorted = new ArrayList<>(Arrays.asList(pp1, pp2, pp3, pp4, pp5));
        assertEquals(sorted, h.getElems());
        assertEquals(pp1, h.extractMin());
        sorted = new ArrayList<>(Arrays.asList(pp2, pp4, pp3, pp5));
        assertEquals(sorted, h.getElems());
        h.updateKey(4, 1);
        sorted = new ArrayList<>(Arrays.asList(pp5, pp2, pp3, pp4));
        assertEquals(sorted, h.getElems());
    }

    @Test
    public void testKDTree() {
        ArrayList<Point> xPoints, yPoints;
        Point p1, p2, p3, p4, p5, p6, p7;
        p1 = new Point(2, 7);
        p2 = new Point(4, 4);
        p3 = new Point(5, 5);
        p4 = new Point(6, 3);
        p5 = new Point(7, 9);
        p6 = new Point(8, 6);
        p7 = new Point(9, 7);
        xPoints = new ArrayList<>(Arrays.asList(p1, p2, p3, p4, p5, p6, p7));
        yPoints = new ArrayList<>(Arrays.asList(p4, p2, p3, p6, p7, p1, p5));
        XTree xtree = XTree.makeXTree(xPoints, yPoints, 0);

        Region r;

        r = new Region();
        r.push(DIR.UNDER);
        r.push(DIR.LEFT);
        assertEquals(r, xtree.findRegion(p2));

        r = new Region();
        r.push(DIR.ABOVE);
        r.push(DIR.LEFT);
        assertEquals(r, xtree.findRegion(p1));

        r = new Region();
        r.push(DIR.LEFT);
        assertEquals(r, xtree.findRegion(p3));

        r = new Region();
        assertEquals(r, xtree.findRegion(p4));

        r = new Region();
        r.push(DIR.RIGHT);
        assertEquals(r, xtree.findRegion(p7));

        r = new Region();
        r.push(DIR.UNDER);
        r.push(DIR.RIGHT);
        assertEquals(r, xtree.findRegion(p6));

        r = new Region();
        r.push(DIR.ABOVE);
        r.push(DIR.RIGHT);
        assertEquals(r, xtree.findRegion(p5));
    }

    @Test
    public void testSpatialGraph() {
        Rect grid = new Rect(0, 0, 10, 8);

        Point p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12;
        p1 = new Point(4, 3);
        p2 = new Point(4, 4);
        p3 = new Point(4, 5);
        p4 = new Point(4, 7);
        p5 = new Point(5, 6);
        p6 = new Point(6, 5);
        p7 = new Point(7, 4);
        p8 = new Point(7, 2);
        p9 = new Point(9, 4);
        p10 = new Point(9, 3);
        p11 = new Point(10, 4);
        p12 = new Point(10, 3);

        ArrayList<Point> points =
                new ArrayList<>(Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12));

        Item<Point> pp1, pp2, pp3, pp4, pp5, pp6, pp7, pp8, pp9, pp10, pp11, pp12;
        pp1 = new Item<>(p1, p1.toString(), 0);
        pp2 = new Item<>(p2, p2.toString(), 0);
        pp3 = new Item<>(p3, p3.toString(), 0);
        pp4 = new Item<>(p4, p4.toString(), 0);
        pp5 = new Item<>(p5, p5.toString(), 0);
        pp6 = new Item<>(p6, p6.toString(), 0);
        pp7 = new Item<>(p7, p7.toString(), 0);
        pp8 = new Item<>(p8, p8.toString(), 0);
        pp9 = new Item<>(p9, p9.toString(), 0);
        pp10 = new Item<>(p10, p10.toString(), 0);
        pp11 = new Item<>(p11, p11.toString(), 0);
        pp12 = new Item<>(p12, p12.toString(), 0);

        ArrayList<Item<Point>> nodes =
                new ArrayList<>(Arrays.asList(pp1, pp2, pp3, pp4, pp5, pp6, pp7, pp8, pp9, pp10, pp11, pp12));

        Hashtable<Item<Point>, ArrayList<Item<Point>>> neighbors = new Hashtable<>();
        neighbors.put(pp1, new ArrayList<>());
        neighbors.put(pp2, new ArrayList<>(Arrays.asList(pp1)));
        neighbors.put(pp3, new ArrayList<>(Arrays.asList(pp2)));
        neighbors.put(pp4, new ArrayList<>());
        neighbors.put(pp5, new ArrayList<>(Arrays.asList(pp4)));
        neighbors.put(pp6, new ArrayList<>(Arrays.asList(pp3, pp5)));
        neighbors.put(pp7, new ArrayList<>(Arrays.asList(pp6, pp8, pp9, pp10)));
        neighbors.put(pp8, new ArrayList<>(Arrays.asList(pp12)));
        neighbors.put(pp9, new ArrayList<>(Arrays.asList(pp11)));
        neighbors.put(pp10, new ArrayList<>(Arrays.asList(pp12)));
        neighbors.put(pp11, new ArrayList<>(Arrays.asList(pp12)));
        neighbors.put(pp12, new ArrayList<>());

        Edge e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13;
        e1 = new Edge(p2, p1);
        e2 = new Edge(p3, p2);
        e3 = new Edge(p5, p4);
        e4 = new Edge(p6, p3);
        e5 = new Edge(p6, p5);
        e6 = new Edge(p7, p6);
        e7 = new Edge(p7, p8);
        e8 = new Edge(p7, p9);
        e9 = new Edge(p7, p10);
        e10 = new Edge(p8, p12);
        e11 = new Edge(p9, p11);
        e12 = new Edge(p10, p12);
        e13 = new Edge(p11, p12);

        Hashtable<Edge, Integer> weights = new Hashtable<>();
        weights.put(e1, 10);
        weights.put(e2, 10);
        weights.put(e3, 10);
        weights.put(e4, 10);
        weights.put(e5, 10);
        weights.put(e6, 10);
        weights.put(e7, 10);
        weights.put(e8, 10);
        weights.put(e9, 10);
        weights.put(e10, 2);
        weights.put(e11, 1);
        weights.put(e12, 1);
        weights.put(e13, 1);

        SpatialGraph g = new SpatialGraph(grid, points, nodes, neighbors, weights);

        // all shortest paths from p7
        g.allShortestPaths(pp7);

        assertTrue(pp1.isVisited());
        assertTrue(pp2.isVisited());
        assertTrue(pp3.isVisited());
        assertTrue(pp4.isVisited());
        assertTrue(pp5.isVisited());
        assertTrue(pp6.isVisited());
        assertTrue(pp7.isVisited());
        assertTrue(pp8.isVisited());
        assertTrue(pp9.isVisited());
        assertTrue(pp10.isVisited());
        assertTrue(pp11.isVisited());
        assertTrue(pp12.isVisited());

        assertEquals(pp2, pp1.getPrevious());
        assertEquals(pp3, pp2.getPrevious());
        assertEquals(pp6, pp3.getPrevious());
        assertEquals(pp5, pp4.getPrevious());
        assertEquals(pp6, pp5.getPrevious());
        assertEquals(pp7, pp6.getPrevious());
        assertEquals(pp7, pp8.getPrevious());
        assertEquals(pp7, pp9.getPrevious());
        assertEquals(pp7, pp10.getPrevious());
        assertEquals(pp9, pp11.getPrevious());
        assertEquals(pp10, pp12.getPrevious());

        // shortest path from p7 to p4
        g.shortestPath(pp7, pp4);

        assertFalse(pp1.isVisited());
        assertTrue(pp2.isVisited());
        assertTrue(pp3.isVisited());
        assertTrue(pp5.isVisited());
        assertTrue(pp6.isVisited());
        assertTrue(pp7.isVisited());
        assertTrue(pp8.isVisited());
        assertTrue(pp9.isVisited());
        assertTrue(pp10.isVisited());
        assertTrue(pp11.isVisited());
        assertTrue(pp12.isVisited());

        assertEquals(pp2, pp1.getPrevious());
        assertEquals(pp3, pp2.getPrevious());
        assertEquals(pp6, pp3.getPrevious());
        assertEquals(pp5, pp4.getPrevious());
        assertEquals(pp6, pp5.getPrevious());
        assertEquals(pp7, pp6.getPrevious());
        assertEquals(pp7, pp8.getPrevious());
        assertEquals(pp7, pp9.getPrevious());
        assertEquals(pp7, pp10.getPrevious());
        assertEquals(pp9, pp11.getPrevious());
        assertEquals(pp10, pp12.getPrevious());

        g.buildKDTree(1);
        System.out.printf("p1 Region %s%n", g.getKdtree().findRegion(p1));
        System.out.printf("p2 Region %s%n", g.getKdtree().findRegion(p2));
        System.out.printf("p3 Region %s%n", g.getKdtree().findRegion(p3));
        System.out.printf("p4 Region %s%n", g.getKdtree().findRegion(p4));
        System.out.printf("p5 Region %s%n", g.getKdtree().findRegion(p5));
        System.out.printf("p6 Region %s%n", g.getKdtree().findRegion(p6));
        System.out.printf("p7 Region %s%n", g.getKdtree().findRegion(p7));
        System.out.printf("p8 Region %s%n", g.getKdtree().findRegion(p8));
        System.out.printf("p9 Region %s%n", g.getKdtree().findRegion(p9));
        System.out.printf("p10 Region %s%n", g.getKdtree().findRegion(p10));
        System.out.printf("p11 Region %s%n", g.getKdtree().findRegion(p11));
        System.out.printf("p12 Region %s%n", g.getKdtree().findRegion(p12));


        g.preprocess(0);

        System.out.printf("Regional edges:%n%s%n:", g.getRegionalEdges());

        Region r = g.getKdtree().findRegion(p4);
        Hashtable<Edge, HashSet<Region>> regionalEdges = g.getRegionalEdges();

        assertTrue(regionalEdges.get(e6).contains(r));
        assertFalse(regionalEdges.get(e7).contains(r));
        assertFalse(regionalEdges.get(e8).contains(r));
        assertFalse(regionalEdges.get(e9).contains(r));

        g.regionalShortestPath(pp7, pp4);
        assertFalse(pp1.isVisited());
        assertFalse(pp2.isVisited());
        assertFalse(pp3.isVisited());
        assertTrue(pp5.isVisited());
        assertTrue(pp6.isVisited());
        assertTrue(pp7.isVisited());
        assertFalse(pp8.isVisited());
        assertFalse(pp9.isVisited());
        assertFalse(pp10.isVisited());
        assertFalse(pp11.isVisited());
        assertFalse(pp12.isVisited());
    }

    static long testBigSpatialGraphSlow(Random r, SpatialGraph g) {
        ArrayList<Item<Point>> nodes = g.getNodes();
        long start = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            g.shortestPath(nodes.get(r.nextInt(1000)), nodes.get(r.nextInt(1000)));
        }
        long end = System.nanoTime();
        return (end - start) / 1000000;
    }

    static long testBigSpatialGraphFast(Random r, SpatialGraph g) {
        ArrayList<Item<Point>> nodes = g.getNodes();
        long start = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            g.regionalShortestPath(nodes.get(r.nextInt(1000)), nodes.get(r.nextInt(1000)));
        }
        long end = System.nanoTime();
        return (end - start) / 1000000;
    }


}