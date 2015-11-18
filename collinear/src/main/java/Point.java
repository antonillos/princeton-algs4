/*************************************************************************
 * Name:antonio saco
 * Email:
 *
 * Compilation:  javac Point.java
 * Execution:
 * Dependencies: StdDraw.java
 *
 * Description: An immutable data type for points in the plane.
 *
 *************************************************************************/

import java.util.Arrays;
import java.util.Comparator;

public class Point implements Comparable<Point> {

    // compare points by slope
    public final Comparator<Point> SLOPE_ORDER = new SlopeOrder();

    private final int x; // x coordinate
    private final int y; // y coordinate

    // the slope between the invoking point (x0, y0) and the argument point (x1,
    // y1)
    private class SlopeOrder implements Comparator<Point> {
        public int compare(Point p1, Point p2) {

            double slope1;

            if (!Point.this.isEqual(p1) && Point.this.x == p1.x)
                slope1 = Double.POSITIVE_INFINITY;
            else if (Point.this.isEqual(p1))
                slope1 = Double.NEGATIVE_INFINITY;
            else
                slope1 = (double) (p1.y - Point.this.y)
                        / (double) (p1.x - Point.this.x);
            if (slope1 == -0.0)
                slope1 = 0.0;

            double slope2;
            if (!Point.this.isEqual(p2) && Point.this.x == p2.x)
                slope2 = Double.POSITIVE_INFINITY;
            else if (Point.this.isEqual(p2))
                slope2 = Double.NEGATIVE_INFINITY;
            else
                slope2 = (double) (p2.y - Point.this.y)
                        / (double) (p2.x - Point.this.x);
            if (slope2 == -0.0)
                slope2 = 0.0;

            if (slope1 < slope2)
                return -1;
            else if (slope1 > slope2)
                return +1;
            else
                return 0;
        }
    }

    // create the point (x, y)
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    // plot this point to standard drawing
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    // draw line between this point and that point to standard drawing
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    // slope between this point and that point
    public double slopeTo(Point that) {
        if (!isEqual(that) && this.x == that.x) {
            return Double.POSITIVE_INFINITY;
        }
        if (isEqual(that)) {
            return Double.NEGATIVE_INFINITY;
        }

        double slope = (double) (that.y - this.y) / (double) (that.x - this.x);
        if (slope == -0.0) {
            return 0.0;
        } else {
            return slope;
        }
    }

    // is this point lexicographically smaller than that one?
    // comparing y-coordinates and breaking ties by x-coordinates
    public int compareTo(Point that) {
        int ret = 0;
        if (isEqual(that))
            ret = 0;
        else if (this.y < that.y)
            ret = -1;
        else if (this.y == that.y && this.x < that.x)
            ret = -1;
        else if (this.y > that.y)
            ret = +1;
        else if (this.y == that.y && this.x > that.x)
            ret = +1;
        return ret;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    private boolean isEqual(Point that) {
        return this.y == that.y && this.x == that.x;
    }

    // return string representation of this point
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    // unit test
    public static void main(String[] args) {
        testSlope();

        testCompare();

        printTestSlopCompare();

        testSlopeCompare();

    }

    private static void printTestSlopCompare() {
        Point origin = new Point(0, 0);
        Point[] pts = new Point[] { new Point(1, 1), new Point(1, 0),
                new Point(1, -1), new Point(0, 1), origin, new Point(0, -1),
                new Point(-1, 1), new Point(-1, 0), new Point(-1, -1) };
        Arrays.sort(pts, origin.SLOPE_ORDER);
        for (int i = 0; i < pts.length; ++i) {
            StdOut.println(pts[i] + ": " + origin.slopeTo(pts[i]));
        }
    }

    private static void testSlopeCompare() {
        Point p;
        Point q;
        Point r;

        p = new Point(14018, 23647);
        q = new Point(14018, 7929);
        r = new Point(28872, 18319);
        assert p.SLOPE_ORDER.compare(q, r) == 1;

        p = new Point(400, 148);
        q = new Point(177, 141);
        r = new Point(409, 309);
        assert p.SLOPE_ORDER.compare(q, r) == -1;

        p = new Point(1, 0);
        q = new Point(1, 0);
        r = new Point(9, 2);
        assert p.SLOPE_ORDER.compare(q, r) == -1;

    }

    private static void testCompare() {
        Point p = new Point(395, 280);
        Point q = new Point(395, 172);
        assert p.compareTo(q) == 1;
    }

    private static void testSlope() {
        Point p = new Point(497, 295);
        Point q = new Point(497, 77);
        assert p.slopeTo(q) == Double.POSITIVE_INFINITY;

        p = new Point(10056, 6836);
        q = new Point(10056, 6836);
        assert p.slopeTo(q) == Double.NEGATIVE_INFINITY;

        p = new Point(169, 10);
        q = new Point(169, 147);
        assert p.slopeTo(q) == Double.POSITIVE_INFINITY;

        p = new Point(3, 7);
        q = new Point(6, 7);
        assert p.slopeTo(q) == 0.0;

        p = new Point(395, 280);
        q = new Point(145, 188);
        assert p.slopeTo(q) == 0.368;

        p = new Point(6, 3);
        q = new Point(0, 4);
        assert p.slopeTo(q) == -0.16666666666666666;
    }
}
