/**
 * 08/29/2012
 * 
 * Estimate the value of the percolation threshold via Monte Carlo simulation.
 * 
 * Execution:
 * 
 * <pre>
 * java PercolationStats {N} {T}
 * </pre>
 * 
 * where N is number of side of the grid and T independent computational
 * experiments
 * 
 * @author antonio saco
 * 
 */
public class PercolationStats {

    private double[] results;

    /**
     * perform T independent computational experiments on an N-by-N grid
     * 
     * @param N
     *            number of side of the grid
     * @param T
     *            number og test to perform
     */
    public PercolationStats(int N, int T) {
        if (N <= 0) {
            throw new IllegalArgumentException(
                    "N cannot be equals or lower than 0");
        }
        if (T <= 0) {
            throw new IllegalArgumentException(
                    "T cannot be equals or lower than 0");
        }
        results = new double[T];
        int sitesOpened, N2 = N * N;
        for (int i = 0; i < T; i++) {
            sitesOpened = percolatesSystem(N);
            // StdOut.println("sites opened: " + sitesOpened);
            results[i] = (double) sitesOpened / (double) N2;
        }
    }

    private int percolatesSystem(final int N) {
        final Percolation percolation = new Percolation(N);
        int i, j, sitesOpened = 0, N2 = N * N;
        while (sitesOpened < N2) {
            i = StdRandom.uniform(1, N + 1);
            j = StdRandom.uniform(1, N + 1);
            if (!percolation.isOpen(i, j)) {
                percolation.open(i, j);
                sitesOpened++;
                if (percolation.percolates()) {
                    break;
                }
            }
        }
        return sitesOpened;
    }

    /**
     * @return sample mean of percolation threshold
     */
    public double mean() {
        if (results != null)
            return StdStats.mean(results);
        return Double.NaN;
    }

    /**
     * @return sample standard deviation of percolation threshold
     */
    public double stddev() {
        if (results != null)
            return StdStats.stddev(results);
        return Double.NaN;
    }

    /**
     * test client
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        int N; // N-by-N percolation system
        int T; // T independent experiments to
        if (args!=null && args.length > 0) {
            if (args.length != 2) {
                throw new Exception("java PercolationStats {N} {T}");
            }
            N = Integer.parseInt(args[0]); // N-by-N percolation system
            T = Integer.parseInt(args[1]); // T independent experiments to
        } else {
            // demo
            N = 10; // N-by-N percolation system
            T = 4; // T independent experiments to
        }
        // Stopwatch stopwatch = new Stopwatch();

        // perform
        PercolationStats stats = new PercolationStats(N, T);

        double mean = stats.mean();
        double stddev = stats.stddev();
        double confidence95 = (1.96 * stddev) / Math.sqrt(T);

        StdOut.printf("mean                    = %.17f\n", mean);
        StdOut.printf("stddev                  = %.17f\n", stddev);
        StdOut.printf("95%% confidence interval = %" + ".17f, %.17f\n", mean
                - confidence95, mean + confidence95);

        // StdOut.printf("elapsed time = %fs\n", stopwatch.elapsedTime());
    }
}
