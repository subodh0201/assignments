import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;

/**
 * The {@code KdTree} class represents a set of Points ({@code Point2D})
 * in the unit square {(0,0), (1,1)}
 * This implementation uses a 2d-Tree. A 2d-tree is a generalization
 * of a BST to two-dimensional keys. The idea is to build a BST with
 * points in the nodes, using the x- and y-coordinates of the points
 * as keys in strictly alternating sequence.
 */
public class KdTree {
    // division of space vertically
    private static final boolean VERTICAL = true;
    private Node root;  // root of the KdTree

    private static class Node {
        private final Point2D p;      // the point
        private final RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        private int size;       // the size of tree rooted at this node

        private Node(Point2D p, double xmin, double ymin, double xmax, double ymax, int size) {
            this.p = p;
            this.rect = new RectHV(xmin, ymin, xmax, ymax);
            this.size = size;
        }
    }

    /**
     * Initialize new object of KdTree
     */
    public KdTree() {
        root = null;
    }

    /**
     * Is the KdTree empty?
     * @return true if KdTree is empty
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * returns the size of the set
     * @return size of the set
     */
    public int size() {
        return size(root);
    }

    /**
     * returns the size of subtree rooted at x
     * @param x Node
     * @return the size of subtree rooted at x
     */
    private int size(Node x) {
        if (x == null) return 0;
        return x.size;
    }

    /**
     * returns true if p is less than q wrt axis
     * @param p first point
     * @param q second point
     * @param axis axis about which comparison is being done
     * @return true if p is less than q wrt axis
     */
    private boolean less(Point2D p, Point2D q, boolean axis) {
        if (axis == VERTICAL) return Double.compare(p.x(), q.x()) < 0;
        else return Double.compare(p.y(), q.y()) < 0;
    }

    /**
     * returns true if the set contains point p
     * @param p point to be searched
     * @return true if the set contains point p
     */
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Argument to contains() is null!");
        return contains(root, p, VERTICAL);
    }

    /**
     * returns true if the subtree rooted at x contains point p
     * @param x root of subtree to be searched
     * @param p point to be searched
     * @param axis alternating axis
     * @return true if the set contains point p
     */
    private boolean contains(Node x, Point2D p, boolean axis) {
        if (x == null) return false;
        if (x.p.equals(p)) return true;
        if (less(p, x.p, axis)) return contains(x.lb, p, !axis);
        else return contains(x.rt, p, !axis);
    }

    /**
     * insert point p into the set
     * @param p point to be inserted
     */
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Argument to insert() is null!");
        if (!contains(p))
            root = insert(root, p, VERTICAL, 0, 0, 1, 1);
    }

    /**
     * insert point p into the subtree rooted at x
     * @param x root of subtree into which the point is to be inserted
     * @param p point to be inserted
     * @param axis alternating axis
     */
    private Node insert(Node x, Point2D p, boolean axis, double xmin, double ymin, double xmax, double ymax) {
        if (x == null) return new Node(p, xmin, ymin, xmax, ymax, 1);
        if (less(p, x.p, axis)) {
            x.lb = insert(x.lb, p, !axis, xmin, ymin, (axis) ? x.p.x() : xmax,
                    (axis) ? ymax : x.p.y());
        } else {
            x.rt = insert(x.rt, p, !axis, (axis)? x.p.x() : xmin, (axis) ? ymin : x.p.y(),
                    xmax, ymax);
        }
        x.size = 1 + size(x.lb) + size(x.rt);
        return x;
    }

    /**
     * draw all points and splits
     */
    public void draw() {
        draw(root, VERTICAL);
    }

    /**
     * draw all points and splits of subtree rooted at x
     */
    private void draw(Node x, boolean axis) {
        if (x == null) return;
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.BLACK);
        x.p.draw();
        StdDraw.setPenRadius(0.005);
        if (axis == VERTICAL) {
            Point2D upper = new Point2D(x.p.x(), x.rect.ymax());
            Point2D lower = new Point2D(x.p.x(), x.rect.ymin());
            StdDraw.setPenColor(StdDraw.RED);
            upper.drawTo(lower);
        } else {
            Point2D right = new Point2D(x.rect.xmax(), x.p.y());
            Point2D left  = new Point2D(x.rect.xmin(), x.p.y());
            StdDraw.setPenColor(StdDraw.BLUE);
            right.drawTo(left);
        }
        draw(x.lb, !axis);
        draw(x.rt, !axis);
    }

    /**
     * return all points that lie inside the rectangle rect
     * @param rect rectangle within which to be searched
     * @return all points that lie inside the rectangle rect
     */
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("Argument to range() is null!");
        ArrayList<Point2D> queue = new ArrayList<>();
        range(root, rect, queue);
        return queue;
    }

    /**
     * return all points in subtree rooted at x that lie inside the rectangle rect
     * @param x root of subtree
     * @param rect rectangle within which to be searched
     * @param queue list in which all points that lie in rect added
     */
    private void range(Node x, RectHV rect, ArrayList<Point2D> queue) {
        if (x == null) return;
        if (!x.rect.intersects(rect)) return;
        if (rect.contains(x.p)) queue.add(x.p);
        range(x.lb, rect, queue);
        range(x.rt, rect, queue);
    }

    // nearest point seen yet
    private Point2D nearest;
    /**
     * return point that is nearest to p
     * @param p point p
     * @return point that is nearest to p
     */
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Argument to nearest() is null!");
        if (isEmpty()) return null;
        nearest = root.p;
        nearest(root, p, VERTICAL);
        return nearest;
    }

    /**
     * return true if it is worth going into x looking for nearest point p
     */
    private boolean shouldGo(Node x, Point2D p) {
        if (x == null) return false;
        return !(x.rect.distanceTo(p) > p.distanceTo(nearest));
    }

    /**
     * return point that is nearest to p in subtree rooted at x
     * @param p point p
     */
    private void nearest(Node x, Point2D p, boolean axis) {
        if (x == null) return;
        if (x.rect.distanceTo(p) > p.distanceTo(nearest)) return;
        if (x.p.distanceTo(p) < nearest.distanceTo(p)) nearest = x.p;
        if (less(p, x.p, axis)) {
            if (shouldGo(x.lb, p))
                nearest(x.lb, p, !axis);
            if (shouldGo(x.rt, p))
                nearest(x.rt, p, !axis);
        } else {
            if (shouldGo(x.rt, p))
                nearest(x.rt, p, !axis);
            if (shouldGo(x.lb, p))
                nearest(x.lb, p, !axis);
        }
    }

    public static void main(String[] args) {
        final int N = 1000;
        KdTree kdTree = new KdTree();
        for (int i = 0; i < N; i++) {
            kdTree.insert(new Point2D(Math.random(), Math.random()));
        }
        System.out.println("size : " + kdTree.size());
        System.out.println("is empty? : " + kdTree.isEmpty());
        System.out.println("contains a random point (100% sure that it doesn't!)? : " +
                kdTree.contains(new Point2D(Math.random(), Math.random())));
        kdTree.draw();
        for (Point2D p : kdTree.range(new RectHV(0.1, 0.1, 0.9, 0.9))) {
            System.out.println(p + " is in the range!");
        }
        StdDraw.setPenColor(StdDraw.RED);
        Point2D p = new Point2D(Math.random(), Math.random());
        p.draw();
        Point2D q = kdTree.nearest(p);
        System.out.println("Nearest point to " + p + " is " + q);
        StdDraw.setPenRadius(0.005);
        StdDraw.setPenColor(StdDraw.BLACK);
        p.drawTo(q);
    }
}
