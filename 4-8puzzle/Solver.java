/* *****************************************************************************
 *  Name: Garrett Holman
 *  Date: 4/14/19
 *  Description: Program that solves an 8puzzle problem using the Board class.
 *  The program utilizes a Priority Queue in order to come to the solution
 *  with the A* algorithm.
 *
 *  Dependencies: algs4.jar, Board.java
 *
 *  Uses:
 *  % java-algs4 Solver puzzle*.txt
 *
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Solver {
    private SearchNode goal;  // Solved board

    /**
     * SearchNode is what we will make our Priority Queue out of, and involves a board and a pointer
     * to the previuos board in order to achieve our stack.
     */
    private class SearchNode {
        private int moves;
        private Board board;
        private SearchNode prev;

        public SearchNode(Board board) {
            this.moves = 0;
            this.prev = null;
            this.board = board;
        }
    }

    /**
     * Another helper class that we use to implement the comparator for our priority queue!
     */
    private class PriorityOrder implements Comparator<SearchNode> {
        public int compare(SearchNode a, SearchNode b) {
            int priorityA = a.board.manhattan() + a.moves;
            int priorityB = b.board.manhattan() + b.moves;
            return Integer.compare(priorityA, priorityB);
        }
    }

    /**
     * Solve board by using a priority queue of SearchNode organized by min defined by Priority
     * Order. The solver removes the min, which is a board, and inserts all of its neighbors into
     * the priority queue. Once the board is solved, we can backtrack through the searchnodes (using
     * pointer to previous node).
     * <p>
     * A twin is created and solved simultaneously, and if the twin is solved then the puzzle is
     * unsolvable
     *
     * @param initial Board at the starting position
     */
    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException("No null arguments, please");

        PriorityOrder order = new PriorityOrder();
        MinPQ<SearchNode> initialPQ = new MinPQ<SearchNode>(order);
        SearchNode initialSN = new SearchNode(initial);
        initialPQ.insert(initialSN);
        MinPQ<SearchNode> twinPQ = new MinPQ<SearchNode>(order);
        SearchNode twinSN = new SearchNode(initial.twin());
        twinPQ.insert(twinSN);

        SearchNode minSN = initialPQ.delMin();
        SearchNode twinMinSN = twinPQ.delMin();


        while (!minSN.board.isGoal()) {
            if (twinMinSN.board.isGoal()) {
                goal = null;
                return;
            }

            for (Board board : minSN.board.neighbors()) {
                if (minSN.prev == null || !minSN.prev.board.equals(board)) {
                    SearchNode n = new SearchNode(board);
                    n.prev = minSN;
                    n.moves = minSN.moves + 1;
                    initialPQ.insert(n);
                }
            }

            for (Board board : twinMinSN.board.neighbors()) {
                if (twinMinSN.prev == null || !twinMinSN.prev.board.equals(board)) {
                    SearchNode n = new SearchNode(board);
                    n.prev = twinMinSN;
                    n.moves = twinMinSN.moves + 1;
                    twinPQ.insert(n);
                }
            }

            minSN = initialPQ.delMin();
            twinMinSN = twinPQ.delMin();
        }
        this.goal = minSN;
    }

    /**
     * Return if the goal is null
     *
     * @return true if there is a goal, being the solved board
     */
    public boolean isSolvable() {
        return goal != null;
    }

    /**
     * Return the number of moves it takes to get to the solved board
     *
     * @return the solved search node's board move count, or -1 if its unsolvable
     */
    public int moves() {
        if (!isSolvable()) return -1;
        else return goal.moves;
    }

    /**
     * Create a stack and add every element in the solution Search Node List (which works like a
     * linked list with a pointer to the previous move)
     *
     * @return iterable solution stack
     */
    public Iterable<Board> solution() {
        if (!isSolvable())
            return null;
        Stack<Board> solutionStack = new Stack<Board>();
        for (SearchNode n = goal; n != null; n = n.prev)
            solutionStack.push(n.board);
        return solutionStack;
    }

    /**
     * Test the solver to create the solution
     *
     * @param args filename of a txt file that represents a puzzle
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

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
