import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/*************************************************************************
 * Name:antonio saco
 * <p/>
 * Compilation: javac Brute.java
 * <p/>
 * Execution: Dependencies: StdDraw.java
 * <p/>
 * Description: draw every (maximal) line segment that connects a subset of 4
 * points.
 *************************************************************************/
public class Brute {
    private static final Font DEFAULT_FONT = new Font("SansSerif", Font.PLAIN,
            7);

    public static void main(String[] args) {
/*
        // read in the input
        String filename = args[0];
        In in = new In(filename);
        int N = in.readInt();
        Point[] points = new Point[N];
        // read from input and draw all points
        for (int i = 0; i < N; i++) {
            int x = in.readInt();
            int y = in.readInt();
            Point p = new Point(x, y);
            points[i] = p;

            // draw(p, StdDraw.GRAY, .01, 0);
        }
*/
        Brute brute = new Brute();

//        brute.process(in, N, points);

        brute.test();
    }

    private void test() {

        Map<String, Integer> testCases = new LinkedHashMap<String, Integer>();

        testCases.put("rs1423", 423);

        int i;
        for (Entry<String, Integer> testCase : testCases.entrySet()) {
            if (testCase.getValue() == -1)
                continue;

            In in = new In("collinear/" + testCase.getKey() + ".txt");

            int N = in.readInt();
            Point[] points = new Point[N];
            Brute fast = new Brute();

            // read from input and draw all points
            for (i = 0; i < N; i++) {
                int x = in.readInt();
                int y = in.readInt();
                Point p = new Point(x, y);
                points[i] = p;

                // draw(p, StdDraw.GRAY, .01, 0);
            }
            StdOut.println(testCase.getKey() + ":" + testCase.getValue());
            Stopwatch s = new Stopwatch();
            fast.process(in, N, points);
            StdOut.println(s.elapsedTime());
            in.close();
        }

    }

    private void process(In in, int N, Point[] points) {

        StdDraw.clear();
        StdDraw.setXscale(0, 32767);
        StdDraw.setYscale(0, 32767);
        StdDraw.setPenRadius(.01);
        StdDraw.setFont(DEFAULT_FONT);
        StdDraw.setPenRadius(.001);

        Point p, q, r, s;
        int i, j, k, l;
        double slope, tmpSlope;


        Arrays.sort(points);


        for (i = 0; i < N; i++) {
            p = points[i];

            for (j = i; j < N; j++) {
                if (j == i)
                    continue;
                q = points[j];
                slope = p.slopeTo(q);

                for (k = j; k < N; k++) {
                    if (k == i || k == j)
                        continue;
                    r = points[k];
                    tmpSlope = p.slopeTo(r);

                    // found 3-collinear
                    if (slope == tmpSlope) {

                        for (l = k; l < N; l++) {
                            if (l == i || l == j || l == k)
                                continue;
                            s = points[l];
                            tmpSlope = p.slopeTo(s);

                            // found 4-collinear
                            if (slope == tmpSlope) {

                                printPointToStdOutAndDrawIt(p, q, r, s);

                            }
                        }
                    }
                }

            }
        }
    }

    private void paintPoints(Point p, Point q, Point r, Point s) {
        StdDraw.clear();

        draw(p, StdDraw.BLUE, .01, 50);

        draw(q, StdDraw.PINK, .01, 50);

        draw(r, StdDraw.MAGENTA, .01, 50);

        draw(s, StdDraw.GREEN, .01, 50);
    }

    private void findUnusedCollinear(double slope, Point p, Point q, Point r,
                                     Point s, Map<Double, Set<Point>> usedPointsMap) {

        printPointToStdOutAndDrawIt(p, q, r, s);

    }

    private void printPointToStdOutAndDrawIt(Point p, Point q, Point r, Point s) {
        StdOut.print(p);
        StdOut.print(" -> ");
        StdOut.print(q);
        StdOut.print(" -> ");
        StdOut.print(r);
        StdOut.print(" -> ");
        StdOut.println(s);

        p.drawTo(s);

        StdDraw.show(0);
        // StdDraw.show(1000);
    }

    private void draw(Point p, Color color, double penRadius, int msPause) {
        StdDraw.setPenRadius(penRadius);
        StdDraw.setPenColor(color);
        p.draw();
        // StdDraw.text(p.x, p.y, p.toString());

        StdDraw.setPenRadius(.001);
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.show(msPause);
    }

}
