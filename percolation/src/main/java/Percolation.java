/**
 * 08/29/2012
 * <p/>
 * Percolation data type using the weighted quick-union algorithm
 *
 * http://coursera.cs.princeton.edu/algs4/assignments/percolation.html
 *
 * @author antonio saco
 */
public class Percolation {
    private boolean[][] open;
    private boolean[][] full;
    private boolean[] connectedToBottom; // to avoid backwash
    private boolean[] memoConnected; // to avoid extra calls to isConnected
    private int[] memoCount; // to avoid extra calls to isConnected
    private boolean[] memoConnectedAlreadyAsked; // to avoid extra calls to
    // isConnected
    private WeightedQuickUnionUF uf;
    private int size;
    private boolean systemPercolates;

    /**
     * create N-by-N grid, with all sites blocked
     *
     * @param N size of grid
     */
    public Percolation(final int N) {
        size = N;
        open = new boolean[N + 1][N + 1];
        full = new boolean[N + 1][N + 1];
        connectedToBottom = new boolean[N + 1];
        memoConnected = new boolean[N * N + 1];
        memoConnectedAlreadyAsked = new boolean[N * N + 1];
        uf = new WeightedQuickUnionUF(N * N + 1);
        memoCount = new int[N * N + 1];
        for (int i = 1; i <= N; i++) {
            makeUnion(0, i);
        }
    }

    /**
     * open site (row i, column j) if it is not already
     *
     * @param i row index
     * @param j column index
     */
    public void open(final int i, final int j) {
        checkBoundaries(i, j, size);

        if (open[i][j])
            return; // already an open site

        // mark i-j as open
        open[i][j] = true;

        // 10-power i equivalent
        int iSize = (i * size) - size;

        // up
        if (i > 1 && isOpen(i - 1, j)) {
            // mark up as full
            makeUnion((iSize - size) + j, iSize + j);
            if (isConnectedToTop((iSize - size) + j)) {
                makeFull(i, j);
            }
        }

        // down
        if (i < size && isOpen(i + 1, j)) {
            // mark down as full
            makeUnion((iSize + size) + j, iSize + j);
            if (isConnectedToTop((iSize + size) + j)) {
                makeFull(i + 1, j);
            }
        }

        // left
        if (j > 1 && isOpen(i, j - 1)) {
            // mark left as full
            makeUnion(iSize + j, iSize + (j - 1));
            if (isConnectedToTop(iSize + (j - 1))) {
                makeFull(i, j - 1);
            }
        }

        // right
        if (j < size && isOpen(i, j + 1)) {
            // mark right as full
            makeUnion(iSize + j, iSize + (j + 1));
            if (isConnectedToTop(iSize + (j + 1))) {
                makeFull(i, j + 1);
            }
        }

        // check if this is the last row
        if (i == size) {
            connectedToBottom[j] = true;
        }

    }

    /**
     * is site (row i, column j) open?
     *
     * @param i row index
     * @param j column index
     * @return true is site is open
     */
    public boolean isOpen(final int i, final int j) {
        checkBoundaries(i, j, size);
        return open[i][j];
    }

    /**
     * is site (row i, column j) full?
     *
     * @param i row index
     * @param j column index
     * @return true is site is full
     */
    public boolean isFull(final int i, final int j) {
        checkBoundaries(i, j, size);

        if (!full[i][j]) {
            if (open[i][j] && !full[i][j]) {
                if (isConnectedToTop(((i * size) - size) + j)) {
                    makeFull(i, j);
                }
            }
        }
        return full[i][j];
    }

    private void checkBoundaries(final int i, final int j, int N) {
        if (i <= 0 || i > N)
            throw new IndexOutOfBoundsException("row index i out of bounds:"
                    + i); // invalid row
        if (j <= 0 || j > N)
            throw new IndexOutOfBoundsException("column index j out of bounds:"
                    + j); // invalid column
    }

    /**
     * does the system percolate?
     *
     * @return true is system percolates
     */
    public boolean percolates() {
        if (!systemPercolates) {
            for (int j = 1; j <= size; j++) {
                if (connectedToBottom[j]) {
                    if (isConnectedToTop(size * size - (size - j))) {
                        systemPercolates = true;

                        // StdOut.println("finds:" + uf.countFinds());

                        break;
                    }
                }
            }
        }
        return systemPercolates;
    }

    private boolean isConnectedToTop(final int q) {
        if (!memoConnected[q]
                && (memoCount[q] == 0 || memoCount[q] > uf.count())
                && !memoConnectedAlreadyAsked[q]) {
            // StdOut.println("isconn:" + q);
            memoConnected[q] = uf.connected(0, q);
            memoCount[q] = uf.count();
            memoConnectedAlreadyAsked[q] = true;
            // } else {
            // StdOut.println("memo:" + q);
        }
        return memoConnected[q];
    }

    private void makeFull(final int i, final int j) {
        full[i][j] = true;
        for (int k = 0; k <= size * size; k++) {
            memoConnectedAlreadyAsked[k] = false;
        }
    }

    private void makeUnion(final int p, final int q) {
        uf.union(p, q);
    }

    public static void main(String[] args) throws Exception {
        String[] filesPercolates = new String[]{"input1.txt", "input2.txt", "input3.txt", "input4.txt",
                "input5.txt", "input6.txt", "input7.txt", "input8.txt", "input10.txt", "input20.txt", "input50.txt"};
        String[] filesNoPercolates = new String[]{"input1-no.txt", "input2-no.txt", "input8-no.txt", "input10-no.txt"};
        In in;
        for (String sIn : filesPercolates) {
            in = new In("percolation/" + sIn);
            int N = in.readInt();
            Percolation percolation = new Percolation(N);

            while (!in.isEmpty()) {
                int i = in.readInt();
                int j = in.readInt();
                percolation.open(i, j);
            }
            in.close();

            if (percolation.percolates()) {
                StdOut.println(N + ": Ok");
            } else {
                StdOut.println(N + ": Error!");
            }

        }

        for (String sIn : filesNoPercolates) {
            in = new In("percolation/" + sIn);

            int N = in.readInt();
            Percolation percolation = new Percolation(N);

            while (!in.isEmpty()) {
                int i = in.readInt();
                int j = in.readInt();
                percolation.open(i, j);
            }
            in.close();

            if (!percolation.percolates()) {
                StdOut.println(N + ": Ok");
            } else {
                StdOut.println(N + ": Error!");
            }
        }
    }
}
