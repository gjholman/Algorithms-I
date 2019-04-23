/* *****************************************************************************
 *  Name: Garrett Holman
 *  Date: 4/10/19
 *  Description: Board class for the 8puzzle project. This class represents an
 *  instance of the board. All functions run in n^2 or better time.
 *
 *  Assumptions: constructor receives n-by-n array with n^2 integers between 0
 *  and n^2-1 where 0 represents a blank square.
 *
 *  Example uses:
 *
 *  % java-algs4 Board puzzle01.txt
 *
-------------- Output ---------------
2
 1  0
 3  2

Hamming: 1
Manhattan: 1
2
 3  0
 1  2
Hamming of twin: 3
Manhattan of twin: 3
2
 3  0
 1  2

initial: 2
 1  0
 3  2

Neighbors:

2
 1  2
 3  0
2
 0  1
 3  2
-------------------------------------
 *
 *
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;

public class Board {
    private int n;
    private int[][] board;
    private int manhattan;

    /**
     * Create Board from 2D int array
     *
     * @param blocks a 2D int array to create board
     */
    public Board(int[][] blocks) {
        this.board = blocks.clone();
        this.n = blocks.length;
        calculateManhattan();
    }

    /**
     * The dimension of the board
     *
     * @return dimension of board, length of the 2D array
     */
    public int dimension() {
        return n;
    }

    /**
     * Get the hamming distance (count of how many tiles are out of place
     *
     * @return hamming distance
     */
    public int hamming() {
        int hammingDistance = 0;
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                int curr = board[i][j];
                if (curr != 0 && curr != n * i + j + 1)
                    hammingDistance++;
            }
        }
        return hammingDistance;
    }

    /**
     * Return the precalculated manhattan distance
     *
     * @return manhattan (stored value)
     */
    public int manhattan() {
        return manhattan;
    }

    /**
     * Calculate manhattan distance (how far each tile is from it's correct spot It stores it as a
     * local variable in order to avoid doing this calculation often.
     */
    private void calculateManhattan() {
        int manhattanDistance = 0;
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                int curr = board[i][j];
                if (curr == 0)
                    continue;
                int desiredRow = (curr - 1) / this.n;
                int desiredColumn = (curr - 1) % this.n;
                manhattanDistance += Math.abs(desiredRow - i) + Math.abs(desiredColumn - j);
            }
        }
        manhattan = manhattanDistance;
    }

    /**
     * Check whether or not the board is complete by checking if the hamming distance is 0
     *
     * @return true if hamming() == 0, otherwise false
     */
    public boolean isGoal() {
        // The lazy way to do it
        return hamming() == 0;
    }

    /**
     * Create a twin board by swapping two random non-zero values
     *
     * @return a new board with flip flopped values
     */
    public Board twin() {
        int[][] twin = copyArray(this.board);
        // create four random integers and swap two values at those indeces
        // two while loops, first while twin[i][j] is not 0 and second is that but also i1 != i2 etc
        int i1 = 0, i2 = 0, j1 = 0, j2 = 0;
        // While our first indeces point to a 0, we want to change the first indeces
        while (twin[i1][j1] == 0) {
            i1 = StdRandom.uniform(0, n);
            j1 = StdRandom.uniform(0, n);
        }
        // while our second indeces point to a 0 or they are the same as our first indeces, change
        while (twin[i2][j2] == 0 || (i1 == i2 && j1 == j2)) {
            i2 = StdRandom.uniform(0, n);
            j2 = StdRandom.uniform(0, n);
        }
        swap(twin, i1, j1, i2, j2);
        Board twinBoard = new Board(twin);
        return twinBoard;
    }

    /**
     * Function to determine if Board objects are identical
     *
     * @param y Object to compare this board to
     * @return true if both objects are a Board and the board values are equal will also return true
     * if the objects are the same object (this.equals(this))
     */
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        return this.board == that.board;
    }

    /**
     * Create neighbors by moving the 0 value left, right, up, down
     *
     * @return iterable list of boards that are the neighbors of this board
     */
    public Iterable<Board> neighbors() {
        // find 0, swap with up down left right add to iterable boards and return iterable lists
        ArrayList<Board> neighborList = new ArrayList<Board>();
        int row = 0, col = 0;

        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                if (this.board[i][j] == 0) {
                    row = i;
                    col = j;
                }
            }
        }

        // Kind of a clunky way of doing it but this adds new boards by swapping
        if (row > 0) {
            int[][] boardCopy = copyArray(this.board);
            swap(boardCopy, row, col, row - 1, col);
            Board left = new Board(boardCopy);
            neighborList.add(left);
        }
        if (row < n - 1) {
            int[][] boardCopy = copyArray(this.board);
            swap(boardCopy, row, col, row + 1, col);
            Board right = new Board(boardCopy);
            neighborList.add(right);
        }
        if (col > 0) {
            int[][] boardCopy = copyArray(this.board);
            swap(boardCopy, row, col, row, col - 1);
            Board up = new Board(boardCopy);
            neighborList.add(up);
        }
        if (col < n - 1) {
            int[][] boardCopy = copyArray(this.board);
            swap(boardCopy, row, col, row, col + 1);
            Board down = new Board(boardCopy);
            neighborList.add(down);
        }

        return neighborList;
    }

    /**
     * Make the board into a string to print and show what the puzzle looks like
     *
     * @return String representation of this board
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", board[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    /**
     * Swap (aka exch()) the values of an array at given indeces
     *
     * @param a  2D int array to swap values around in
     * @param i1 row 1
     * @param j1 col 1
     * @param i2 row 2
     * @param j2 col 2
     */
    private void swap(int[][] a, int i1, int j1, int i2, int j2) {
        int temp = a[i1][j1];
        a[i1][j1] = a[i2][j2];
        a[i2][j2] = temp;
    }

    /**
     * Replacement for clone or arrays.copy
     *
     * @param a 2D array to copy
     * @return a copy of the input array
     */
    private int[][] copyArray(int[][] a) {
        int[][] newArray = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                newArray[i][j] = a[i][j];
            }
        }
        return newArray;
    }

    /**
     * Test the board, twin, and neighbors
     *
     * @param args name of text file that represents a board
     */
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        StdOut.println(initial.toString());
        StdOut.println("Hamming: " + initial.hamming());
        StdOut.println("Manhattan: " + initial.manhattan());
        Board twin = initial.twin();
        StdOut.print(twin.toString());
        StdOut.println("Hamming of twin: " + twin.hamming());
        StdOut.println("Manhattan of twin: " + twin.manhattan());
        StdOut.println(twin.toString());
        StdOut.println("initial: " + initial.toString());
        StdOut.println("Neighbors");
        for (Board neighbor : initial.neighbors()) {
            StdOut.print(neighbor.toString());
        }
    }
}
