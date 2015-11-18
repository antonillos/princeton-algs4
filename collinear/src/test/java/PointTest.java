import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class PointTest {

    @Test
    public void testCompareTo() {
        // Equal
        assertEquals(0, new Point(0, 1).compareTo(new Point(0, 1)));
        // Compare Y
        assertEquals(-1, new Point(1, 1).compareTo(new Point(1, 2)));
        assertEquals(1, new Point(1, 2).compareTo(new Point(1, 1)));
        // Compare X
        assertEquals(-1, new Point(1, 1).compareTo(new Point(2, 1)));
        assertEquals(1, new Point(2, 1).compareTo(new Point(1, 1)));

    }

    @Test
    public void testSlopeTo() {
        double delta = 0;
        // Vertical Line
        assertEquals(Double.POSITIVE_INFINITY,
                (new Point(8, 5)).slopeTo(new Point(8, 0)), delta);
        // Line Segment
        assertEquals(Double.NEGATIVE_INFINITY,
                (new Point(5, 5)).slopeTo(new Point(5, 5)), delta);
        // Horizontal Line
        assertEquals(0, (new Point(0, 5)).slopeTo(new Point(8, 5)), delta);
        assertEquals(0, (new Point(8, 5)).slopeTo(new Point(0, 5)), delta);
        // Negative
        assertEquals(-2, (new Point(1, 2)).slopeTo(new Point(3, -2)), delta);
        // Normal
        assertEquals(2.25, (new Point(1, 1)).slopeTo(new Point(5, 10)), delta);
        assertEquals(6.0, (new Point(1, 1)).slopeTo(new Point(5, 25)), delta);
    }

    @Test
    public void testSlopeOrderComparator() {
        Point originPoint = new Point(1, 1);

        // Same slope (straight line)
        assertEquals(0, originPoint.SLOPE_ORDER.compare(new Point(2, 2),
                new Point(5, 5)));

        // First slope is smaller
        assertEquals(-1, originPoint.SLOPE_ORDER.compare(new Point(5, 10),
                new Point(5, 25)));

        // Second slope is smaller
        assertEquals(+1, originPoint.SLOPE_ORDER.compare(new Point(5, 25),
                new Point(5, 10)));

    }

    @Test
    public void testSlopeOrderFromResultsEquals1() {
        assertEquals(1, new Point(395, 280).SLOPE_ORDER.compare(new Point(395,
                172), new Point(145, 188)));

        assertEquals(1, new Point(14018, 23647).SLOPE_ORDER.compare(new Point(
                14018, 7929), new Point(28872, 18319)));

        assertEquals(1, new Point(5, 0).SLOPE_ORDER.compare(new Point(1, 0),
                new Point(5, 0)));

        assertEquals(1, new Point(7, 8).SLOPE_ORDER.compare(new Point(7, 0),
                new Point(7, 8)));
    }

    @Test
    public void testSlopeOrderFromResultsEqualsMinus1() {
        assertEquals(-1, new Point(1, 0).SLOPE_ORDER.compare(new Point(1, 0),
                new Point(9, 2)));

        assertEquals(-1, new Point(3, 8).SLOPE_ORDER.compare(new Point(2, 5),
                new Point(3, 6)));

        assertEquals(-1, new Point(126, 37).SLOPE_ORDER.compare(new Point(387,
                114), new Point(126, 378)));

        assertEquals(-1, new Point(22314, 3472).SLOPE_ORDER.compare(new Point(
                32280, 30718), new Point(22314, 32712)));
    }

    @Test
    public void testSlopeOrderFromResultsHorizontalEquals0() {
        assertEquals(0, new Point(6, 6).SLOPE_ORDER.compare(new Point(1, 6),
                new Point(7, 6)));

        assertEquals(0, new Point(6, 1).SLOPE_ORDER.compare(new Point(9, 1),
                new Point(5, 1)));

    }

    @Test
    public void testSlopeOrderFromResultsVerticalEquals0() {
        assertEquals(0, new Point(8, 3).SLOPE_ORDER.compare(new Point(8, 8),
                new Point(8, 6)));

        assertEquals(0, new Point(228, 68).SLOPE_ORDER.compare(new Point(228,
                44), new Point(228, 22)));

        assertEquals(0, new Point(8, 9).SLOPE_ORDER.compare(new Point(8, 7),
                new Point(8, 8)));

    }

    @Test
    // @Ignore
    public void testSlopeOrderFromInput() {
        In[] files = new In[] { new In("src/main/resources/collinear/input1.txt"),
                new In("src/main/resources/collinear/input2.txt"), new In("src/main/resources/collinear/input3.txt"),
                new In("src/main/resources/collinear/input6.txt"), new In("src/main/resources/collinear/input8.txt"),
                new In("src/main/resources/collinear/input9.txt"),
                new In("src/main/resources/collinear/input10.txt"),
                new In("src/main/resources/collinear/equidistant.txt"),
                new In("src/main/resources/collinear/horizontal5.txt"),
                new In("src/main/resources/collinear/vertical5.txt"),
                new In("src/main/resources/collinear/input20.txt"),
                new In("src/main/resources/collinear/inarow.txt"),
                new In("src/main/resources/collinear/grid4x4.txt"),
                new In("src/main/resources/collinear/grid5x5.txt"),
                new In("src/main/resources/collinear/grid6x6.txt") };
        Point[] a;
        Point ref;
        int i;
        for (In in : files) {
            int N = in.readInt();
            a = new Point[N];
            for (i = 0; i < N; i++) {
                a[i] = new Point(in.readInt(), in.readInt());
            }
            in.close();

            ref = a[0]; // Reference point must be the first point in
            // the sorted array.
            Arrays.sort(a, ref.SLOPE_ORDER);
            double s1, s0;
            for (i = 1; i < N; i++) {
                s0 = ref.slopeTo(a[i - 1]);
                s1 = ref.slopeTo(a[i]);
                // StdOut.print(s1);
                // StdOut.print(":");
                // StdOut.print(a[i]);
                // StdOut.print("::");
                // StdOut.print(s0);
                // StdOut.print(":");
                // StdOut.print(a[i - 1]);
                // StdOut.print(":");
                assertTrue(s1 >= s0);
            }
        }
    }
}