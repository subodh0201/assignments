import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final double CONFIDENCE_95 = 1.96;
    private final int trials;
    private final double meanVal;
    private final double stdDevVal;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("Size and trials should both be greater than zero");
        }
        this.trials = trials;
        double[] pt = new double[trials];
        for(int i = 0; i < trials; i++){
            Percolation p = new Percolation(n);
            while (!p.percolates()) {
                int a = StdRandom.uniform(n*n);
                if (!p.isOpen(a/n + 1, a%n + 1)) {
                    p.open(a/n + 1, a%n + 1);
                }
            }
            double tr = (double)p.numberOfOpenSites() / (n*n);
            pt[i] = tr;
        }
        meanVal = StdStats.mean(pt);
        stdDevVal = StdStats.stddev(pt);
    }

    // sample mean of percolation threshold
    public double mean() {
        return meanVal;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stdDevVal;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return  meanVal - ((CONFIDENCE_95 * stdDevVal)/Math.sqrt(trials));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return  meanVal + ((CONFIDENCE_95 * stdDevVal)/Math.sqrt(trials));
    }

    // test client
    public static void main(String[] args) {
        int n = 0 , t = 0;
        if (args.length > 1) {
            n = Integer.parseInt(args[0]);
            t = Integer.parseInt(args[1]);
        }
        else {
            n = StdIn.readInt();
            t = StdIn.readInt();
        }
        PercolationStats ps = new PercolationStats(n, t);
        StdOut.println("mean\t\t\t\t\t = " + ps.meanVal);
        StdOut.println("stddev\t\t\t\t\t = " + ps.stdDevVal);
        StdOut.println("95% confidence interval\t = [" + ps.confidenceLo() + ", " + ps.confidenceHi() + "]");
    }
}
