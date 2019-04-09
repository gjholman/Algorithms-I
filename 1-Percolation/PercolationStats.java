/* *****************************************************************************
 *  Name: Garrett Holman
 *  Date: 3/19/19
 *  Description: Percolation stats uses the Percolation class. Using that class,
 *  create an instance of Percolation and determine how many randomly open sites
 *  it takes to percolate the system of an input size. Repeats the test 'x' times
 *  where x is also an input. The program prints out the mean and standard
 *  deviation.
 *
 *  Compilation: javacs-algs4 PercolationStats
 *  Execute:     java-algs4 PercolationStats [size (int)] [trial count (int)]
 *
 *  Dependencies: Percolation.java
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private double[] trialResults;
    private final int trialCount;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException("Inputs must be greater than 0");

        trialResults = new double[trials];
        trialCount = trials;

        // for i -> trials, make perc
        for (int i = 0; i < trials; i++) {
            Percolation perc = new Percolation(n);
            while (!perc.percolates()) {
                int r = StdRandom.uniform(1, n + 1);
                int c = StdRandom.uniform(1, n + 1);
                if (!perc.isOpen(r, c)) {
                    perc.open(r, c);
                }
            }
            double result = (double) perc.numberOfOpenSites() / Math.pow(n, 2);
            trialResults[i] = result;
        }
    }

    public double mean() {
        return StdStats.mean(trialResults);
    }                         // sample mean of percolation threshold

    public double stddev() {
        if (trialCount == 1) return Double.NaN;
        return StdStats.stddev(trialResults);
    }                      // sample standard deviation of percolation threshold

    public double confidenceLo() {
        return mean() - CONFIDENCE_95 * stddev() / Math.sqrt(trialCount);
    }                // low  endpoint of 95% confidence interval

    public double confidenceHi() {
        return mean() + CONFIDENCE_95 * stddev() / Math.sqrt(trialCount);
    }                 // high endpoint of 95% confidence interval


    // test client (described below)
    public static void main(String[] args) {
        Stopwatch stopwatch = new Stopwatch();
        int size = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats percStats = new PercolationStats(size, trials);

        System.out.println("% java-algs4 PercolationStats " + size + " " + trials);
        System.out.println("mean\t\t\t\t\t= " + percStats.mean());
        System.out.println("stddev \t\t\t\t\t= " + percStats.stddev());
        System.out.println(
                "95% Confidence interval = [" + percStats.confidenceLo() + ", " + percStats
                        .confidenceHi() + "]");
        System.out.println("Stopwatch Time: " + stopwatch.elapsedTime());
    }
}
