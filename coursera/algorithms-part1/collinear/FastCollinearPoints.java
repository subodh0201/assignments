import edu.princeton.cs.algs4.Merge;
import edu.princeton.cs.algs4.ResizingArrayStack;

import java.util.Arrays;

public class FastCollinearPoints {
    private final LineSegment[] lineSegments;
    
    public FastCollinearPoints(Point[] points) {
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
        boolean[] addedPoints = new boolean[pointStack.size()];
        Point[] points1 = new Point[pointStack.size()];
        for (int i = 0; i < points1.length; i++) {
            points1[i] = pointStack.pop();
        }
        
        Merge.sort(points1);
    
        ResizingArrayStack<LineSegment> segmentsStack = new ResizingArrayStack<>();
        int l = points1.length;
        for (int i = 0; i < l - 3; i++) {
            Point p = points1[i];
            int al = l - i - 1;
            Point[] aux = new Point[l - i - 1];
            for (int j = 0; j < al; j++) {
                aux[j] = points1[j + i + 1];
            }
            Arrays.sort(aux, p.slopeOrder());
            int j = 0;
            while (j < al) {
                double slope = p.slopeTo(aux[j]);
                int counter = 0;
                while (j < al) {
                    if (p.slopeTo(aux[j]) == slope) {
                        j++;
                        counter++;
                    } else {
                        break;
                    }
                }
                boolean added = false;
                if (counter >= 3) {
                    for (int k = 0; k < i; k++) {
                        if (addedPoints[k] && slope == points1[k].slopeTo(p)) {
                            added = true;
                            break;
                        }
                    }
                    if (!added) {
                        LineSegment s = new LineSegment(p, aux[j - 1]);
                        addedPoints[i] = true;
                        segmentsStack.push(s);
                    }
                }
            }
        }
        lineSegments = new LineSegment[segmentsStack.size()];
        int segmentSize = segmentsStack.size();
        for (int i = 0; i < segmentSize; i++) {
            lineSegments[i] = segmentsStack.pop();
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
