import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * The {@code PointSET} class represents a set of Points ({@code Point2D})
 * in the unit square {(0,0) , (1, 1)}
 * This is a brute force implementation using {@code java.util.TreeSet}
 */

public class PointSET {
    private TreeSet<Point2D> pointSet;

    /**
     * Initialise a new PointSET
     */
    public PointSET() {
        pointSet = new TreeSet<>();
    }

    public boolean isEmpty() {
        return pointSet.isEmpty();
    }

    public int size() {
        return pointSet.size();
    }

    /**
     * inserts {@code Point2D p} into the set
     * @param p {@code Point2D} to be inserted
     * @throws IllegalArgumentException if argument is null
     */
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Argument to insert() is null!");
        pointSet.add(p);
    }

    /**
     * @param p Point2D to be checked
     * @return true if p is present in set
     * @throws IllegalArgumentException if argument is null
     */
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Argument to contains() is null!");
        return pointSet.contains(p);
    }

    /**
     * draws all points in the set to standard draw
     */
    public void draw() {
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.BLUE);
        for (Point2D p : pointSet) p.draw();
    }

    /**
     * returns all points that are inside the rectangle
     * @return Iterable<Point2D> of points inside the range given by argument rectangle
     * @throws IllegalArgumentException if argument is null
     */
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("Argument to range() is null!");
        ArrayList<Point2D> iterable = new ArrayList<>();
        for (Point2D p : pointSet) {
            if (rect.contains(p)) iterable.add(p);
        }
        return iterable;
    }

    /**
     * returns the point in the set that is nearest to p
     * @param p Point2D from which nearest Point2D from the set is required
     * @return Point2D which is nearest to p. If set is empty returns {@code null}
     */
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Argument to nearest() is null!");
        double minDist = Double.POSITIVE_INFINITY;
        Point2D q = null;
        for (Point2D point : pointSet) {
            if (p.distanceTo(point) < minDist) {
                q = point;
                minDist = p.distanceTo(point);
            }
        }
        return q;
    }

    public static void main(String[] args) {
        final int N = 100;
        PointSET pointSET = new PointSET();
        for (int i = 0; i < N; i++) {
            pointSET.insert(new Point2D(Math.random(), Math.random()));
        }
        System.out.println("size : " + pointSET.size());
        System.out.println("is empty? : " + pointSET.isEmpty());
        System.out.println("contains a random point (100% sure that it doesn't!)? : " +
                pointSET.contains(new Point2D(Math.random(), Math.random())));
        pointSET.draw();
        for (Point2D p : pointSET.range(new RectHV(0.1, 0.1, 0.9, 0.9))) {
            System.out.println(p + " is in the range!");
        }
        StdDraw.setPenColor(StdDraw.RED);
        Point2D p = new Point2D(Math.random(), Math.random());
        p.draw();
        Point2D q = pointSET.nearest(p);
        System.out.println("Nearest point to " + p + " is " + q);
        StdDraw.setPenRadius(0.005);
        StdDraw.setPenColor(StdDraw.BLACK);
        p.drawTo(q);
    }
}
