/* *****************************************************************************
 *  Name: Garrett Holman
 *  Date: 4/26/19
 *  Description: Set of 2D points with methods to find points in a given
 *  rectangle and find nearest point. These methods are done in linear time
 *  proportional to number of points.
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

import java.util.ArrayList;

public class PointSET {
    private final SET<Point2D> points;

    public PointSET() {
        points = new SET<Point2D>();
    }

    public boolean isEmpty() {
        return points.isEmpty();
    }

    public int size() {
        return points.size();
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Cannot insert null val");
        points.add(p);
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Cannot search for null val");
        return points.contains(p);
    }

    public void draw() {
        for (Point2D point : points)
            point.draw();
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("Cannot insert null val");

        ArrayList<Point2D> pointsInRange = new ArrayList<Point2D>();

        for (Point2D point : this.points) {
            if (rect.contains(point))
                pointsInRange.add(point);
        }
        return pointsInRange;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Cannot insert null val");

        Point2D closest = null;
        double distance = Double.POSITIVE_INFINITY;

        for (Point2D point : this.points) {
            double newDistance = p.distanceSquaredTo(point);
            if (newDistance < distance) {
                closest = point;
                distance = newDistance;
            }
        }
        return closest;
    }

    public static void main(String[] args) {
        // Empty on purpuse
    }
}
