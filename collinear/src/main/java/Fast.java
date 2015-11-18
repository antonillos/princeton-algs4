import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
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
public class Fast {
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
        }
*/
        Fast fast = new Fast();

//        fast.process(in, N, points);

        fast.test();
    }

    private void test() {

        Map<String, Integer> testCases = new LinkedHashMap<>();

/*
        testCases.put("input6", -1);
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
        testCases.put("random23", 23);
        testCases.put("random38", 38);
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

        testCases.put("input1000", 1000);
        testCases.put("input2000", 2000);
        testCases.put("input3000", 3000);
        testCases.put("input4000", 4000);
*/

        testCases.put("rs1423", 443);

        int i;
        for (Entry<String, Integer> testCase : testCases.entrySet()) {
            if (testCase.getValue() == -1)
                continue;

            In in = new In("collinear/" + testCase.getKey() + ".txt");

            int N = in.readInt();
            Point[] points = new Point[N];
            Fast fast = new Fast();

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

        Point p, q, r, s, last;
        int i, j, k, l;
        double spq, slope;
        Queue<Point> collinearPoints = new Queue<>();
        Map<Double, Set<Point>> usedPointsMap = new HashMap<>();
        Set<Point> usedPoints;
        boolean found;

        Arrays.sort(points);

        // for (l = 0; l < N; l++)
        // draw(points[l], StdDraw.GRAY, .007, 100, true);
        // StdOut.println(Arrays.toString(points));

        Point[] pointsCopy;

        for (i = 0; i < N; i++) {
            p = points[i];

            pointsCopy = Arrays.copyOfRange(points, i + 1, N);
            Arrays.sort(pointsCopy, p.SLOPE_ORDER);

            for (j = 0; j < pointsCopy.length; j++) {
                found = false;
                q = pointsCopy[j];
                spq = p.slopeTo(q);

                for (k = j + 1; k < pointsCopy.length - 1; k++) {
                    r = pointsCopy[k];
                    slope = q.slopeTo(r);

                    if (slope < spq)
                        continue;
                    if (slope > spq)
                        break;

                    s = pointsCopy[k + 1];
                    slope = r.slopeTo(s);

                    // found 4-collinear
                    if (spq == slope) {
                        collinearPoints.enqueue(p);
                        collinearPoints.enqueue(q);
                        collinearPoints.enqueue(r);
                        collinearPoints.enqueue(s);
                        last = s;

                        for (l = k + 2; l < pointsCopy.length; l++) {
                            s = pointsCopy[l];

                            // draw(s, StdDraw.GREEN, .01, 50, false);

                            if (slope == p.slopeTo(s)) {
                                last = s;
                                collinearPoints.enqueue(last);
                            } else
                                break;
                        }

                        if (!usedPointsMap.containsKey(slope)
                                || !usedPointsMap.get(slope).contains(last)) {
                            p.drawTo(last);
                            StdDraw.show(0);

                            while (!collinearPoints.isEmpty()) {
                                StdOut.print(collinearPoints.dequeue());
                                if (collinearPoints.size() > 0)
                                    StdOut.print(" -> ");
                            }
                            StdOut.println();

                            usedPoints = usedPointsMap.get(slope);
                            if (usedPoints != null)
                                usedPoints.add(last);
                            else {
                                usedPoints = new HashSet<>();
                                usedPoints.add(last);
                            }
                            usedPointsMap.put(slope, usedPoints);
                        } else {
                            while (!collinearPoints.isEmpty()) {
                                collinearPoints.dequeue();
                            }
                        }

                        found = true;

                        j = j + 2;
                    }
                    if (found)
                        break;
                }
            }
        }
    }

    private void draw(Point p, Color color, double penRadius, int msPause,
                      boolean printText) {
        StdDraw.setPenRadius(penRadius);
        StdDraw.setPenColor(color);
        p.draw();
        // if (printText)
        // StdDraw.text(p.x, p.y, p.toString());

        StdDraw.setPenRadius(.001);
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.show(msPause);
    }

}
