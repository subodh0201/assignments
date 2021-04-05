import edu.princeton.cs.algs4.Merge;
import edu.princeton.cs.algs4.ResizingArrayStack;

import java.util.Arrays;

public class BruteCollinearPoints {
    private final LineSegment[] lineSegments;
    
    public BruteCollinearPoints(Point[] points) {
        if (points == null)
            throw new IllegalArgumentException("Argument may not be null!");
        ResizingArrayStack<Point> pointStack = new ResizingArrayStack<>();
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null)
                throw new IllegalArgumentException("Point at index " + i + " is null");
            for (Point p : pointStack) {
                if (p.equals(points[i]))
                    throw new IllegalArgumentException("Points may not be repeated!");
            }
            pointStack.push(points[i]);
        }
    
        Point[] points1 = new Point[pointStack.size()];
        for (int i = 0; i < points1.length; i++) {
            points1[i] = pointStack.pop();
        }
        
        Merge.sort(points1);
        ResizingArrayStack<LineSegment> segmentStack = new ResizingArrayStack<>();
        int l = points1.length;
        for (int a = 0; a < l - 3; a++) {
            for (int b = a + 1; b < l - 2; b++) {
                for (int c = b + 1; c < l - 1; c++) {
                    double s1 = points1[a].slopeTo(points1[b]);
                    double s2 = points1[a].slopeTo(points1[c]);
                    if (s1 == s2) {
                        for (int d = c + 1; d < l; d++) {
                            if (points1[a].slopeTo(points1[d]) == s1) {
                                segmentStack.push(new LineSegment(points1[a], points1[d]));
                            }
                        }
                    }
                }
            }
        }
        int size = segmentStack.size();
        lineSegments = new LineSegment[size];
        for (int i = 0; i < size; i++) {
            lineSegments[i] = segmentStack.pop();
        }
    }
    
    public int numberOfSegments() {
        return lineSegments.length;
    }
    
    public LineSegment[] segments() {
        LineSegment[] ret = Arrays.copyOf(lineSegments , lineSegments.length);
        return ret;
    }
}
