import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/*************************************************************************
 * Name:antonio saco
 * <p/>
 * Compilation: javac Fast.java
 * <p/>
 * Execution: Dependencies: StdDraw.java
 * <p/>
 * Description: draw every (maximal) line segment that connects a subset of 4 or
 * more of the points.
 *************************************************************************/
public class MoreBrute {
    private static final Font DEFAULT_FONT = new Font("SansSerif", Font.PLAIN,
            7);

    public static void main(String[] args) {
        // read in the input
/*        String filename = args[0];
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
        }*/
        MoreBrute fast = new MoreBrute();

        // fast.process(in, N, points);

        fast.test();
    }

    private void test() {

        Map<String, Integer> testCases = new LinkedHashMap<>();

 /*       testCases.put("input6", -1);
        testCases.put("input9", 1);
        testCases.put("input8", 2);
        testCases.put("input10", 2);
        testCases.put("input20", 5);
        testCases.put("input40", 4);
        testCases.put("input48", 6);
        testCases.put("input50", 7);
        testCases.put("input80", 31);
        testCases.put("input100", -1);
        testCases.put("input299", 6);
        testCases.put("input300", 6);
        testCases.put("grid4x4", -1);
        testCases.put("grid5x5", -1);
        testCases.put("grid6x6", -1);
        testCases.put("equidistant", 4);
        testCases.put("random23", -1);
        testCases.put("random38", -1);
        testCases.put("horizontal5", 5);
        testCases.put("horizontal25", 25);
        testCases.put("horizontal50", 50);
        testCases.put("horizontal75", 75);
        testCases.put("horizontal100", 100);
        testCases.put("vertical5", 5);
        testCases.put("vertical25", 25);
        testCases.put("vertical50", 50);
        testCases.put("vertical75", 75);
        testCases.put("vertical100", 100);

        testCases.put("input1000", 0);
        testCases.put("input2000", 0);
        testCases.put("input3000", 0);
        testCases.put("input4000", 0);*/

        testCases.put("rs1423", 443);


        int i;
        for (Entry<String, Integer> testCase : testCases.entrySet()) {
            if (testCase.getValue() == -1)
                continue;

            In in = new In("collinear/" + testCase.getKey() + ".txt");

            int N = in.readInt();
            Point[] points = new Point[N];
            MoreBrute fast = new MoreBrute();

            // read from input and draw all points
            for (i = 0; i < N; i++) {
                int x = in.readInt();
                int y = in.readInt();
                Point p = new Point(x, y);
                points[i] = p;

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

        Point p, q, r, last = null;
        int i, j, k;
        double slope = 0, tmpSlope;
        Set<Point> collinearPoints = new LinkedHashSet<>();
        Map<Double, Set<Point>> usedPointsMap = new HashMap<>();

        Arrays.sort(points);
        Point[] pointsCopy = Arrays.copyOf(points, N);

        for (i = 0; i < N; i++) {
            p = points[i];
            for (j = i + 1; j < N; j++) {
                q = points[j];
                Arrays.sort(pointsCopy, q.SLOPE_ORDER);

                slope = p.slopeTo(q);

                for (k = j + 1; k < N; k++) {
                    r = points[k];
                    tmpSlope = p.slopeTo(r);

                    // found collinear
                    if (slope == tmpSlope) {
                        if (collinearPoints.isEmpty()) {
                            // StdDraw.clear();
                            // draw(p, StdDraw.BLUE, .01, 100);
                            collinearPoints.add(p);

                            // draw(q, StdDraw.RED, .01, 50);
                            collinearPoints.add(q);
                        }
                        // draw(r, StdDraw.GREEN, .01, 50);
                        collinearPoints.add(r);
                        last = r;
                    }
                }

                if (findUniqueCollinear(slope, last, collinearPoints,
                        usedPointsMap)) {
                    // countPrintResults++;

                }
                collinearPoints.clear();
            }
        }

        // display count results
        // StdOut.println(countPrintResults);

        // StdDraw.show(0);
    }

    private boolean findUniqueCollinear(double slope, Point last,
                                        Set<Point> collinearPoints, Map<Double, Set<Point>> usedPointsMap) {
        boolean found = false;
        Set<Point> usedPoints = new HashSet<Point>();
        boolean foundSubSegment = false;
        if (collinearPoints.size() >= 4) {
            if (usedPointsMap.containsKey(slope)) {
                usedPoints = usedPointsMap.get(slope);
                for (Point cp : collinearPoints) {
                    if (usedPoints.contains(cp)) {
                        foundSubSegment = true;
                        break;
                    }
                }
            }

            if (!foundSubSegment) {
                printPointToStdOutAndDrawIt(collinearPoints);
                found = true;

                usedPoints.add(last);
                usedPointsMap.put(slope, usedPoints);
            }

            collinearPoints.clear();
        }

        return found;
    }

    private void printPointToStdOutAndDrawIt(Set<Point> points) {
        StringBuilder res = new StringBuilder();
        Point s = null, e = null;
        int i = 0;
        for (Point p : points) {
            if (i == 0)
                s = p;
            if (i == points.size() - 1)
                e = p;
            res.append(p);
            if (i < points.size() - 1)
                res.append(" -> ");
            i++;
        }
        StdOut.println(res.toString());

        s.drawTo(e);
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
