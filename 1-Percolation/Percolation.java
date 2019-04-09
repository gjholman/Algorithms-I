/* *****************************************************************************
 *  Name: Garrett Holman
 *  Date: 3/19/19
 *  Description: Percolation is the first assignment in Princeton University's
 *               Algorithms I class on Coursera. The task of this class is to
 *               estimate p* where p* is (proportionally) the amount of site
 *               vacancy where percolation is possible.
 *
 *               Time requirements: Constructor proportional to n^2, all methods
 *               take constant time with constant calls to union(), find(),
 *               connected(), and count()
 *
 *               There are no storage requirements, however I will still try
 *               to optimize memory
 *
 *               Constructor runs in O(1) time, O(n^2) space
 *               open() runs in O(1) time,
 *
 * Dependencies: WeightedQuickUninoUF
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    // Object variables, using openings to track which sites are opened,
    // connections for the weighted quick union, which tracks connectivity
    // filled connections tracks the connections to the top - this is to avoid the backwash bug
    // and opensites which tracks how many open sites we have thus far
    private boolean[][] openings;
    private final WeightedQuickUnionUF connections;
    private final WeightedQuickUnionUF filledConnections;
    private int openSites;

    /**
     * Create a percolation system of an nxn grid
     *
     * @param n defines the size of the percolation system
     */
    public Percolation(int n) {

        if (n <= 0) throw new IllegalArgumentException("Use an 'n' greater than 0");
        // perc is indicator if it's open
        openings = new boolean[n][n];

        // connections is a union-find object instance where index 0 is 'top' and final index is 'bottom'
        // filled Connections is a second union-find object where there is no 'bottom'. this is used to
        // solve the visualization bug called backwash
        connections = new WeightedQuickUnionUF(n * n + 2);
        filledConnections = new WeightedQuickUnionUF(n * n + 1);

        // Variable to count open sites
        openSites = 0;
    }

    /**
     * Open a new site at a certain row, column
     *
     * @param row is row number
     * @param col is column number
     */
    public void open(int row, int col) {
        int n = openings.length;
        if (row <= 0 || row > n || col <= 0 || col > n) {
            throw new IllegalArgumentException("Row or Column not found");
        }

        // If we've already opened this location
        if (openings[row - 1][col - 1]) return;

        openings[row - 1][col - 1] = true;
        openSites++;

        int current = coordinateToInt(row, col);

        // Connect top row to phantom top location in Union Find trees
        if (row == 1) doubleUnion(current, 0); // Connect top row to phantom top
        if (row == n) connections.union(current, n * n + 1); // Connect bottom to bottom row

        if (row > 1) if (isOpen(row - 1, col)) doubleUnion(current, coordinateToInt(row - 1, col));
        if (row < n) if (isOpen(row + 1, col)) doubleUnion(current, coordinateToInt(row + 1, col));
        if (col > 1) if (isOpen(row, col - 1)) doubleUnion(current, coordinateToInt(row, col - 1));
        if (col < n) if (isOpen(row, col + 1)) doubleUnion(current, coordinateToInt(row, col + 1));

    }

    /**
     * Check if a spot is open
     *
     * @param row is the row number
     * @param col is the column number
     * @return true = open, false = not open
     */
    public boolean isOpen(int row, int col) {
        if (row <= 0 || row > openings.length || col <= 0 || col > openings.length) {
            throw new IllegalArgumentException("Out of Range");
        }
        return openings[row - 1][col - 1];
    }

    /**
     * Check if a spot is filled (connected to the top
     *
     * @param row is the row number
     * @param col is the column
     * @return true = filled, false = not filled
     */
    public boolean isFull(int row, int col) {
        if (row <= 0 || row > openings.length || col <= 0 || col > openings.length) {
            throw new IllegalArgumentException("Out of Range");
        }
        return (filledConnections.connected(0, coordinateToInt(row, col)));
    }

    /**
     * Return the number of open sites
     *
     * @return number of open sites
     */
    public int numberOfOpenSites() {
        return (openSites);
    }

    /**
     * Check if the bottom phantom layer is connected to the top phantom layer
     *
     * @return true if bottom to top connected
     */
    public boolean percolates() {
        // Check if 0 (top) and n^2+1 (bottom) are connected
        int n = openings.length;
        return (connections.connected(0, n * n + 1));
    }

    /**
     * Helper function to convert row, col to index of a Weighted Quick Union
     *
     * @param row is row number
     * @param col is col number
     * @return weighted quick union index
     */
    private int coordinateToInt(int row, int col) {
        int returnVar = openings.length * (row - 1) + col;
        return (returnVar);
    }

    /**
     * Call the union function for both weighted quick unions. Union connects both points
     *
     * @param p is point 1
     * @param q is point 2
     */
    private void doubleUnion(int p, int q) {
        connections.union(p, q);
        filledConnections.union(p, q);
    }

    public static void main(String[] args) {
        // This is meant to remain empty
    }

}
