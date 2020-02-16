import java.util.*;

public class SpatialGraph {
    private Rect grid;
    private ArrayList<Point> points;

    private ArrayList<Item<Point>> nodes;
    private Hashtable<Item<Point>, ArrayList<Item<Point>>> neighbors;
    private Hashtable<Edge, Integer> weights;

    private XTree kdtree; // initialized by preprocess
    private Hashtable<Edge, HashSet<Region>> regionalEdges; // initialized by preprocess

    SpatialGraph(
            Rect grid, ArrayList<Point> points,
            ArrayList<Item<Point>> nodes,
            Hashtable<Item<Point>, ArrayList<Item<Point>>> neighbors,
            Hashtable<Edge, Integer> weights) {
        this.grid = grid;
        this.points = points;
        this.nodes = nodes;
        this.neighbors = neighbors;
        this.weights = weights;
    }

    SpatialGraph(Random r, int numberNodes, int maxNeighbors, int maxWeight, int gridSize) {
        this.grid = new Rect(0, 0, gridSize, gridSize);
        this.points = new ArrayList<>();
        this.nodes = new ArrayList<>();
        this.neighbors = new Hashtable<>();
        this.weights = new Hashtable<>();

        for (int i = 0; i < numberNodes; i++) {
            Point p = new Point(r.nextInt(gridSize), r.nextInt(gridSize));
            Item<Point> n = new Item<>(p, p.toString(), Integer.MAX_VALUE);
            while (nodes.contains(n)) {
                p = new Point(r.nextInt(gridSize), r.nextInt(gridSize));
                n = new Item<>(p, p.toString(), Integer.MAX_VALUE);
            }
            points.add(p);
            nodes.add(n);
        }

        for (int i = 0; i < numberNodes; i++) {
            ArrayList<Item<Point>> neighbors = new ArrayList<>();
            int numberNeighbors = r.nextInt(maxNeighbors);

            for (int j = 0; j < numberNeighbors; j++) {
                int neighborIndex = r.nextInt(numberNodes);
                while (i == neighborIndex) {
                    neighborIndex = r.nextInt(numberNodes);
                }
                Item<Point> neighbor = nodes.get(neighborIndex);
                neighbors.add(neighbor);
                weights.put(new Edge(nodes.get(i).getData(), neighbor.getData()), r.nextInt(maxWeight));
            }

            this.neighbors.put(nodes.get(i), neighbors);
        }
    }

    // -----

    Rect getGrid() {
        return grid;
    }

    ArrayList<Point> getPoints() {
        return points;
    }

    ArrayList<Item<Point>> getNodes() {
        return nodes;
    }

    Hashtable<Item<Point>, ArrayList<Item<Point>>> getNeighbors() {
        return neighbors;
    }

    Hashtable<Edge, Integer> getWeights() {
        return weights;
    }

    XTree getKdtree() {
        return kdtree;
    }

    Hashtable<Edge, HashSet<Region>> getRegionalEdges() {
        return regionalEdges;
    }

    // -----
    // Computes all shortest paths from a given node
    // Nodes are marked with the shortest path to the source

    void allShortestPaths(Item<Point> source) {
        for (Item<Point> b : nodes) {
            b.reset();
            b.setValue(Integer.MAX_VALUE);
        }
        source.setValue(0);
        BinaryHeap heap = new BinaryHeap(nodes);
        while (!heap.isEmpty()) {
            Item hold = heap.extractMin();
            if (!hold.isVisited()) {
                hold.setVisited(true);
                neighbors.get(hold).forEach(v -> {
                    if (weights.get(new Edge((Point) hold.getData(), v.getData())) + hold.getValue() < v.getValue()) {
                        int distance = 0;
                        if (hold.getValue() == Integer.MAX_VALUE) {
                            distance = Integer.MAX_VALUE;
                        } else {
                            distance = weights.get(new Edge((Point) hold.getData(), v.getData())) + hold.getValue();
                        }
                        if (distance < v.getValue()) {
                            heap.updateKey(v.getPosition(), weights.get(new Edge((Point) hold.getData(), v.getData())) + hold.getValue());
                            v.setPrevious(hold);
                        }
                    }
                });
            }
        }
    }

    // -----
    // Point-to-point shortest path; stops as soon as it reaches destination

    ArrayList<Edge> shortestPath(Item<Point> source, Item<Point> dest) {
        nodes.forEach(Item::reset);
        for (Item<Point> b : nodes) {
            b.setValue(Integer.MAX_VALUE);
        }
        source.setValue(0);
        BinaryHeap heap = new BinaryHeap(nodes);
        while (!heap.isEmpty()) {
            Item hold = heap.extractMin();
            if (hold.equals(dest)) {
                break;
            }
            if (!hold.isVisited()) {
                hold.setVisited(true);
                neighbors.get(hold).forEach(v -> {
                    if (weights.get(new Edge((Point) hold.getData(), v.getData())) + hold.getValue() < v.getValue()) {
                        //neighbors.get(hold).forEach(v -> {
                        int distance;
                        if (hold.getValue() == Integer.MAX_VALUE) {
                            distance = Integer.MAX_VALUE;
                        } else {
                            distance = weights.get(new Edge((Point) hold.getData(), v.getData())) + hold.getValue();
                        }
                        if (distance < v.getValue()) {
                            heap.updateKey(v.getPosition(), weights.get(new Edge((Point) hold.getData(), v.getData())) + hold.getValue());
                            v.setPrevious(hold);
                        }
                    }
                });
            }
        }
        return Item.pathToSource(dest);
    }

    // -----

    void buildKDTree(int bound) {
        ArrayList x = new ArrayList(points);
        x.sort(Comparator.comparing(Point::getX));
        ArrayList y = new ArrayList(points);
        y.sort(Comparator.comparing(Point::getY));
        kdtree = XTree.makeXTree(x, y, bound);
    }

    void preprocess(int bound) {
        buildKDTree(bound);
        regionalEdges = new Hashtable<>();
        weights.keySet().forEach(v -> regionalEdges.put(v, new HashSet<>()));
        nodes.forEach(s -> {
            allShortestPaths(s);
            nodes.forEach(d -> {
                ArrayList<Edge> edges = Item.pathToSource(d);
                if (!edges.isEmpty()) {
                    Edge last = edges.get(edges.size() - 1).flip();
                    Region region = kdtree.findRegion(d.getData());
                    regionalEdges.get(last).add(region);
                }
            });
        });
    }

    // -----

    ArrayList<Edge> regionalShortestPath(Item<Point> source, Item<Point> dest) {
        preprocess(0);
        nodes.forEach(Item::reset);
        for (Item<Point> b : nodes) {
            b.setValue(Integer.MAX_VALUE);
        }
        source.setValue(0);
        BinaryHeap heap = new BinaryHeap(nodes);
        Region destRegion = kdtree.findRegion(dest.getData());
        while (!heap.isEmpty()) {
            Item hold = heap.extractMin();
            if (hold.equals(dest)) {
                break;
            }
            if (!hold.isVisited() && hold.getValue() != Integer.MAX_VALUE) {
                hold.setVisited(true);
                neighbors.get(hold).forEach(v -> {
                    //neighbors.get(hold).forEach(v -> {
                    Edge test = new Edge((Point) hold.getData(), v.getData());
                    if (regionalEdges.get(test).contains(destRegion)) {
                        int distance;
                        if (hold.getValue() == Integer.MAX_VALUE) {
                            distance = Integer.MAX_VALUE;
                        } else {
                            distance = weights.get(new Edge((Point) hold.getData(), v.getData())) + hold.getValue();
                        }
                        if (distance < v.getValue()) {
                            heap.updateKey(v.getPosition(), weights.get(new Edge((Point) hold.getData(), v.getData())) + hold.getValue());
                            v.setPrevious(hold);
                        }
                    }
                });
            }
        }
        return Item.pathToSource(dest);
    }

    // -----

    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append(String.format("Nodes:%n%s%n", nodes));
        res.append(String.format("Neighbors:%n%s%n", neighbors));
        res.append(String.format("Weights:%n%s%n", weights));
        return new String(res);
    }
}
