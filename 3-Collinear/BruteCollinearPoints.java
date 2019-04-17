/* *****************************************************************************
 *  Name: Garrett Holman
 *  Date: 4/4/19
 *  Description: Brute Collinear Points is a program that takes a standard input
 *  of a text file with integers that represent coordinates for points on a 2D
 *  plan, and finds a set of 4 points that are collinear
 *
 *  The purpose of this is to show the brute force way of determining collinear
 *  points, which is n^4, very inefficient
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private LineSegment[] segments;

    /**
     * @param points is an array of points for which we find collinear points
     */
    public BruteCollinearPoints(Point[] points) {
        // the brute force is embedding for loops for days! (4)
        if (points == null)
            throw new IllegalArgumentException("Constructor cannot be null");
        int n = points.length;

        // segmentList is a local array list that we will add to segments
        ArrayList<LineSegment> segmentList = new ArrayList<LineSegment>();

        checkNullArguments(points);
        checkRepeatedPoints(points);

        if (n >= 4) {
            for (int w = 0; w < n - 3; w++) {
                Point p = points[w];
                for (int x = w + 1; x < n - 2; x++) {
                    double slope1 = p.slopeTo(points[x]);
                    for (int y = x + 1; y < n - 1; y++) {
                        double slope2 = p.slopeTo(points[y]);
                        if (slope1 == slope2) {
                            for (int z = y + 1; z < n; z++) {
                                double slope3 = p.slopeTo(points[z]);
                                if (slope1 == slope3) {
                                    Point[] group = { points[w], points[x], points[y], points[z] };
                                    Arrays.sort(group);
                                    LineSegment segment = new LineSegment(group[0], group[3]);
                                    segmentList.add(segment);
                                }
                            }
                        }
                    }
                }
            }
        }
        segments = new LineSegment[segmentList.size()];
        segments = segmentList.toArray(segments);
    }

    /**
     * The number of line segments
     *
     * @return the number of segments
     */
    public int numberOfSegments() {
        return segments.length;
    }

    private void checkRepeatedPoints(Point[] points) {
        int n = points.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (points[i].equals(points[j])) {
                    throw new IllegalArgumentException("No repeated Points");
                }
            }
        }
    }

    private void checkNullArguments(Point[] points) {
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("No null points");
            }
        }
    }

    /**
     * return list of line segments stored as a local variable
     *
     * @return list of line segments
     */
    public LineSegment[] segments() {
        LineSegment[] segmentsCopy = segments.clone();
        return segmentsCopy;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
        
    }
}
