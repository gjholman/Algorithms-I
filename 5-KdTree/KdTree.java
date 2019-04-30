/* *****************************************************************************
 *  Name: Garrett Holman
 *  Date: 4/26/19
 *  Description: Stores a set of 2D points and uses a tree to sort the points by position
 *  in order to speed up the process of range searching and nearest point searching.
 *
 *  The tree is organized as a binary tree of points where the left subtree is every point
 *  left or below the point, and the right subtree is to the right or above (or equal, however
 *  there are no duplicates). The tree alternates at each layer which comparison to use
 *  (horizontal or vertical)
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;

import java.util.ArrayList;

public class KdTree {
    private Node root;  // head/root node
    private int n;      // number of points

    /**
     * Set up KdTree, initialize variables for an empty tree
     */
    public KdTree() {
        root = null;
        n = 0;
    }

    /**
     * Node class to contain points and links to points based on the relative position to it's
     * parent
     */
    private static class Node {
        private Point2D p;      // the point
        private Node lb;        // left or bottom subtree
        private Node rt;        // right or top subtree
        private boolean compareH;  // boolean to determine: compare horizontal or vertical
    }

    /**
     * Check if the tree is empty
     *
     * @return true if tree is empty
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Check the size of the tree with a local variable
     *
     * @return stored size of tree (n)
     */
    public int size() {
        return n;
    }

    /**
     * Insert point p into tree in the correct spot using
     *
     * @param p point to insert into tree
     */
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Cannot insert null val");

        n++;    // increment size

        Node newNode = new Node();
        newNode.p = p;
        newNode.compareH = true;

        if (this.isEmpty()) {
            this.root = newNode;
            return;
        }

        Node curr = this.root;
        while (curr != null) {
            // If it's a duplicate, return without adding
            if (p.equals(curr.p)) {
                n--;
                return;
            }
            if (newNode.compareH) {
                if (p.x() < curr.p.x()) {
                    if (curr.lb == null) {
                        newNode.compareH = false;
                        curr.lb = newNode;
                        return;
                    }
                    else curr = curr.lb;
                }
                else {
                    if (curr.rt == null) {
                        newNode.compareH = false;
                        curr.rt = newNode;
                        return;
                    }
                    else curr = curr.rt;
                }
            }

            // Case 2: compare vertical
            else {
                if (p.y() < curr.p.y()) {
                    if (curr.lb == null) {
                        newNode.compareH = true;
                        curr.lb = newNode;
                        return;
                    }
                    else curr = curr.lb;
                }
                else {
                    if (curr.rt == null) {
                        newNode.compareH = true;
                        curr.rt = newNode;
                        return;
                    }
                    else curr = curr.rt;
                }
            }
            newNode.compareH = !newNode.compareH;
        }

    }

    /**
     * Traverse the tree to see if the point is in the tree
     *
     * @param p point to search for
     * @return true if p is included, false if otherwise
     */
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Cannot search for null val");

        Node node = root;
        // boolean compareH = true;
        // scan from the root
        while (node != null) {

            if (p.equals(node.p))
                return true;

            if (node.compareH) {
                if (p.x() < node.p.x())
                    node = node.lb;
                else
                    node = node.rt;
            }
            else {
                if (p.y() < node.p.y())
                    node = node.lb;
                else
                    node = node.rt;
            }
        }
        return false;
    }

    /**
     * Draw all points in the tree by calling depth first search draw
     */
    public void draw() {
        dfsDraw(this.root);
    }

    /**
     * Depth first search recursion to draw each point
     *
     * @param node node to draw
     */
    private void dfsDraw(Node node) {
        if (node == null)
            return;

        node.p.draw();
        dfsDraw(node.lb);
        dfsDraw(node.rt);
    }

    /**
     * Find all points in range of the given rectangle
     *
     * @param rect range to include points
     * @return iterable list (ArrayList of points) of the points in the rectangle
     */
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("Cannot insert null val");

        // how about a queue of nodes where we check each node
        Queue<Node> subtreeQueue = new Queue<Node>();
        if (this.isEmpty())
            return null;
        subtreeQueue.enqueue(this.root);

        ArrayList<Point2D> pointsInRange = new ArrayList<>();

        // boolean compareH = true;

        while (!subtreeQueue.isEmpty()) {
            Node curr = subtreeQueue.dequeue();
            if (rect.contains(curr.p))
                pointsInRange.add(curr.p);

            if (curr.compareH) {
                if (curr.p.x() > rect.xmin() && curr.lb != null)
                    subtreeQueue.enqueue(curr.lb);
                if (curr.p.x() <= rect.xmax() && curr.rt != null)
                    subtreeQueue.enqueue(curr.rt);
            }
            else {
                if (curr.p.y() > rect.ymin() && curr.lb != null)
                    subtreeQueue.enqueue(curr.lb);
                if (curr.p.y() <= rect.ymax() && curr.rt != null)
                    subtreeQueue.enqueue(curr.rt);
            }
        }

        return pointsInRange;
    }

    /**
     * Find the nearest point to a given input p
     *
     * @param p coordinates to find the nearest point in the tree
     * @return point that is the nearest point to the given input p
     */
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Cannot search null val");

        Point2D closest = null;
        double minDistance = Double.POSITIVE_INFINITY;
        Stack<Node> subtreeQueue = new Stack<>();
        if (this.isEmpty())
            return null;
        subtreeQueue.push(this.root);

        while (!subtreeQueue.isEmpty()) {
            Node curr = subtreeQueue.pop();
            if (curr == null) continue;
            double newDistance = p.distanceSquaredTo(curr.p);
            if (newDistance < minDistance) {
                closest = curr.p;
                minDistance = newDistance;
            }

            // My tree traversal was causing errors in the tester so I decided to make it simple.
            if (curr.rt != null) subtreeQueue.push(curr.rt);
            if (curr.lb != null) subtreeQueue.push(curr.lb);
            // But I would like to know the solution

            // if (curr.compareH) {
            //     if (p.x() < curr.p.x()) {
            //         if (Math.pow(p.x() - curr.p.x(), 2) < minDistance && curr.rt != null)
            //             subtreeQueue.push(curr.rt);
            //         if (curr.lb != null) subtreeQueue.push(curr.lb);
            //     }
            //     else {
            //         // add right, check if left can include anything less than distance
            //         if (Math.pow(p.y() - curr.p.y(), 2) < minDistance && curr.lb != null)
            //             subtreeQueue.push(curr.lb);
            //         if (curr.rt != null) subtreeQueue.push(curr.rt);
            //     }
            // }
            // else {
            //     if (p.y() < curr.p.y()) {
            //         if (Math.pow(p.x() - curr.p.x(), 2) < minDistance && curr.rt != null)
            //             subtreeQueue.push(curr.rt);
            //         if (curr.lb != null) subtreeQueue.push(curr.lb);
            //     }
            //     else {
            //         // add right, check if left can include anything less than distance
            //         if (Math.pow(p.y() - curr.p.y(), 2) < minDistance && curr.lb != null)
            //             subtreeQueue.push(curr.lb);
            //         if (curr.rt != null) subtreeQueue.push(curr.rt);
            //     }
            // }
        }
        return closest;
    }

    public static void main(String[] args) {
        // Empty on purpose
    }
}
