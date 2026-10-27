/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;


/**
 * QuickDraw regions in their classic format, converted from and to {@link Area}s.
 * <pre>
 * rgnSize(2) rgnBBox(8: top left bottom right) [scan lines: y(2) x(2)... 0x7fff] 0x7fff
 * </pre>
 * A rectangular region is just the 10 byte header. The scan lines hold the inversion points:
 * the coordinates where the region changes compared to the scan line above.
 */
final class Regions {

    private Regions() {
    }

    private static final short END = 0x7fff;

    /** @return the bounds of the region handle, left top right bottom */
    static int[] bounds(int rgn) {
        byte[] b = Heap.handleBytes(rgn);
        if (b.length < 10) {
            return new int[4];
        }
        return new int[] {Bytes.getShort(b, 4), Bytes.getShort(b, 2), Bytes.getShort(b, 8), Bytes.getShort(b, 6)};
    }

    static boolean isRectangular(int rgn) {
        return Heap.handleSize(rgn) <= 10;
    }

    static boolean isEmpty(int[] bounds) {
        return bounds[2] <= bounds[0] || bounds[3] <= bounds[1];
    }

    /** @return the area of a region handle */
    static Area read(int rgn) {
        byte[] b = Heap.handleBytes(rgn);
        if (b.length < 10) {
            return new Area();
        }
        int top = Bytes.getShort(b, 2);
        int left = Bytes.getShort(b, 4);
        int bottom = Bytes.getShort(b, 6);
        int right = Bytes.getShort(b, 8);
        if (right <= left || bottom <= top) {
            return new Area();
        }
        if (b.length <= 10) {
            return new Area(new Rectangle(left, top, right - left, bottom - top));
        }
        // xor the inversion points down the scan lines
        Area area = new Area();
        List<Integer> current = new ArrayList<>();
        int p = 10;
        int lastY = Integer.MIN_VALUE;
        List<Integer> spanXs = new ArrayList<>();
        int spanY = 0;
        while (p + 2 <= b.length) {
            int y = Bytes.getShort(b, p);
            p += 2;
            if (y == END) {
                break;
            }
            if (lastY != Integer.MIN_VALUE && !spanXs.isEmpty()) {
                addSpans(area, spanXs, spanY, y);
            }
            List<Integer> inv = new ArrayList<>();
            while (p + 2 <= b.length) {
                int x = Bytes.getShort(b, p);
                p += 2;
                if (x == END) {
                    break;
                }
                inv.add(x);
            }
            current = xor(current, inv);
            spanXs = current;
            spanY = y;
            lastY = y;
        }
        return area;
    }

    private static void addSpans(Area area, List<Integer> xs, int y0, int y1) {
        for (int i = 0; i + 1 < xs.size(); i += 2) {
            area.add(new Area(new Rectangle(xs.get(i), y0, xs.get(i + 1) - xs.get(i), y1 - y0)));
        }
    }

    /** @return the sorted symmetric difference of two sorted lists */
    private static List<Integer> xor(List<Integer> a, List<Integer> b) {
        List<Integer> r = new ArrayList<>();
        int i = 0, j = 0;
        while (i < a.size() || j < b.size()) {
            if (j >= b.size() || (i < a.size() && a.get(i) < b.get(j))) {
                r.add(a.get(i++));
            } else if (i >= a.size() || b.get(j) < a.get(i)) {
                r.add(b.get(j++));
            } else {
                i++;
                j++;
            }
        }
        return r;
    }

    /** writes the area into the region handle */
    static void write(int rgn, Area area) {
        Rectangle r = area.getBounds();
        if (area.isEmpty() || r.width <= 0 || r.height <= 0) {
            Heap.setHandleBytes(rgn, new byte[10]);
            return;
        }
        if (area.isRectangular()) {
            byte[] b = new byte[10];
            Bytes.putShort(b, 0, 10);
            Bytes.putShort(b, 2, r.y);
            Bytes.putShort(b, 4, r.x);
            Bytes.putShort(b, 6, r.y + r.height);
            Bytes.putShort(b, 8, r.x + r.width);
            Heap.setHandleBytes(rgn, b);
            return;
        }
        // spans of each scan line, from the rectangles of the area
        List<int[]> rows = new ArrayList<>(); // y, then x pairs
        int[][] spans = spans(area, r);
        java.io.ByteArrayOutputStream os = new java.io.ByteArrayOutputStream();
        int[] prev = new int[0];
        for (int y = 0; y <= r.height; y++) {
            int[] cur = y < r.height ? spans[y] : new int[0];
            List<Integer> inv = xor(toList(prev), toList(cur));
            if (!inv.isEmpty()) {
                putShort(os, r.y + y);
                for (int x : inv) {
                    putShort(os, x);
                }
                putShort(os, END);
            }
            prev = cur;
        }
        putShort(os, END);
        byte[] data = os.toByteArray();
        byte[] b = new byte[10 + data.length];
        Bytes.putShort(b, 0, Math.min(0x7fff, b.length));
        Bytes.putShort(b, 2, r.y);
        Bytes.putShort(b, 4, r.x);
        Bytes.putShort(b, 6, r.y + r.height);
        Bytes.putShort(b, 8, r.x + r.width);
        System.arraycopy(data, 0, b, 10, data.length);
        Heap.setHandleBytes(rgn, b);
        rows.clear();
    }

    private static List<Integer> toList(int[] a) {
        List<Integer> l = new ArrayList<>(a.length);
        for (int v : a) {
            l.add(v);
        }
        return l;
    }

    private static void putShort(java.io.ByteArrayOutputStream os, int v) {
        os.write(v >> 8);
        os.write(v);
    }

    /** @return the x spans (start, end pairs) of each scan line of the area */
    private static int[][] spans(Area area, Rectangle r) {
        List<List<int[]>> rows = new ArrayList<>();
        for (int y = 0; y < r.height; y++) {
            rows.add(new ArrayList<>());
        }
        // the area is decomposed into horizontal strips of rectangles by the path of a flattened area
        for (Rectangle rect : rectangles(area)) {
            for (int y = Math.max(rect.y, r.y); y < Math.min(rect.y + rect.height, r.y + r.height); y++) {
                rows.get(y - r.y).add(new int[] {rect.x, rect.x + rect.width});
            }
        }
        int[][] result = new int[r.height][];
        for (int y = 0; y < r.height; y++) {
            List<int[]> row = rows.get(y);
            row.sort(Comparator.comparingInt(a -> a[0]));
            List<Integer> xs = new ArrayList<>();
            for (int[] s : row) {
                if (!xs.isEmpty() && s[0] <= xs.getLast()) {
                    xs.set(xs.size() - 1, Math.max(xs.getLast(), s[1]));
                } else {
                    xs.add(s[0]);
                    xs.add(s[1]);
                }
            }
            result[y] = xs.stream().mapToInt(Integer::intValue).toArray();
        }
        return result;
    }

    /** @return integer rectangles covering the area, by rasterizing each scan line through the area */
    static List<Rectangle> rectangles(Area area) {
        List<Rectangle> list = new ArrayList<>();
        Rectangle b = area.getBounds();
        for (int y = b.y; y < b.y + b.height; y++) {
            Area line = new Area(new Rectangle(b.x, y, b.width, 1));
            line.intersect(area);
            if (line.isEmpty()) {
                continue;
            }
            for (double[] seg : segments(line, y + 0.5)) {
                int x0 = (int) Math.round(seg[0]);
                int x1 = (int) Math.round(seg[1]);
                if (x1 > x0) {
                    list.add(new Rectangle(x0, y, x1 - x0, 1));
                }
            }
        }
        return list;
    }

    /** @return the x ranges a horizontal line crosses inside the shape */
    private static List<double[]> segments(Shape s, double y) {
        List<Double> xs = new ArrayList<>();
        PathIterator it = s.getPathIterator(null, 0.25);
        double[] c = new double[6];
        double lx = 0, ly = 0, mx = 0, my = 0;
        while (!it.isDone()) {
            int t = it.currentSegment(c);
            if (t == PathIterator.SEG_MOVETO) {
                lx = mx = c[0];
                ly = my = c[1];
            } else {
                double x = t == PathIterator.SEG_CLOSE ? mx : c[0];
                double yy = t == PathIterator.SEG_CLOSE ? my : c[1];
                if ((ly <= y && yy > y) || (yy <= y && ly > y)) {
                    xs.add(lx + (y - ly) * (x - lx) / (yy - ly));
                }
                lx = x;
                ly = yy;
            }
            it.next();
        }
        double[] a = xs.stream().mapToDouble(Double::doubleValue).toArray();
        Arrays.sort(a);
        List<double[]> r = new ArrayList<>();
        for (int i = 0; i + 1 < a.length; i += 2) {
            r.add(new double[] {a[i], a[i + 1]});
        }
        return r;
    }
}
