/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * QuickDraw GX paths of the curve utilities and vector streams.
 * <p>
 * gxPaths: contours(4) {vectors(4) controlBits((vectors + 31) / 32 longs, most significant bit first) points(vectors x Fixed x, y)}...
 * A set control bit marks an off curve point, contours are closed quadratic B-splines.
 */
final class Curves {

    private Curves() {
    }

    /** a contour */
    static final class Contour {
        final List<double[]> points = new ArrayList<>();
        final List<Boolean> onCurve = new ArrayList<>();
    }

    /** @return offsets of the atoms of a vector stream: offset, size, type */
    static List<int[]> atoms(byte[] s) {
        List<int[]> l = new ArrayList<>();
        int p = 0;
        while (p + 8 <= s.length) {
            int size = Bytes.getInt(s, p);
            if (size < 8 || p + size > s.length) {
                break;
            }
            l.add(new int[] {p, size, Bytes.getInt(s, p + 4)});
            p += size;
        }
        return l;
    }

    /** @return the offset of the contour, -1 if there is none */
    private static int offset(byte[] paths, int index) {
        if (paths.length < 4) {
            return -1;
        }
        int contours = Bytes.getInt(paths, 0);
        if (index < 0 || index >= contours) {
            return -1;
        }
        int p = 4;
        for (int i = 0; i < index; i++) {
            if (p + 4 > paths.length) {
                return -1;
            }
            int n = Bytes.getInt(paths, p);
            p += 4 + ((n + 31) / 32) * 4 + n * 8;
        }
        return p + 4 <= paths.length ? p : -1;
    }

    private static int size(Contour c) {
        int n = c.points.size();
        return 4 + ((n + 31) / 32) * 4 + n * 8;
    }

    static Contour contour(byte[] paths, int index) {
        int p = offset(paths, index);
        if (p < 0) {
            return null;
        }
        int n = Bytes.getInt(paths, p);
        int bits = p + 4;
        int pts = bits + ((n + 31) / 32) * 4;
        if (n < 0 || pts + n * 8 > paths.length) {
            return null;
        }
        Contour c = new Contour();
        for (int i = 0; i < n; i++) {
            int word = Bytes.getInt(paths, bits + (i / 32) * 4);
            c.onCurve.add((word & (0x8000_0000 >>> (i % 32))) == 0);
            c.points.add(new double[] {Bytes.fixedToDouble(Bytes.getInt(paths, pts + i * 8)), Bytes.fixedToDouble(Bytes.getInt(paths, pts + i * 8 + 4))});
        }
        return c;
    }

    private static byte[] bytes(Contour c) {
        int n = c.points.size();
        byte[] b = new byte[size(c)];
        Bytes.putInt(b, 0, n);
        int pts = 4 + ((n + 31) / 32) * 4;
        for (int i = 0; i < n; i++) {
            if (!c.onCurve.get(i)) {
                int o = 4 + (i / 32) * 4;
                Bytes.putInt(b, o, Bytes.getInt(b, o) | (0x8000_0000 >>> (i % 32)));
            }
            Bytes.putInt(b, pts + i * 8, Bytes.doubleToFixed(c.points.get(i)[0]));
            Bytes.putInt(b, pts + i * 8 + 4, Bytes.doubleToFixed(c.points.get(i)[1]));
        }
        return b;
    }

    /** @return the paths with the contour replaced */
    static byte[] replace(byte[] paths, int index, Contour c) {
        int p = offset(paths, index);
        int n = Bytes.getInt(paths, p);
        int old = 4 + ((n + 31) / 32) * 4 + n * 8;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write(paths, 0, p);
        os.writeBytes(bytes(c));
        os.write(paths, p + old, paths.length - p - old);
        return os.toByteArray();
    }

    /** @return the contour as line segments, curves flattened */
    static List<double[]> polyline(Contour c) {
        List<double[]> out = new ArrayList<>();
        int n = c.points.size();
        if (n == 0) {
            return out;
        }
        // start at an on curve point, or the midpoint of the first two off curve points
        int start = c.onCurve.indexOf(true);
        double[] first;
        if (start < 0) {
            double[] a = c.points.get(0), b = c.points.get(n > 1 ? 1 : 0);
            first = new double[] {(a[0] + b[0]) / 2, (a[1] + b[1]) / 2};
            start = 0;
        } else {
            first = c.points.get(start);
        }
        out.add(first);
        double[] cur = first;
        double[] control = null;
        for (int k = 1; k <= n; k++) {
            int i = (start + k) % n;
            double[] p = c.points.get(i);
            boolean on = c.onCurve.get(i) || k == n && control == null;
            if (k == n) {
                p = first;
                on = true;
            }
            if (on) {
                if (control == null) {
                    out.add(p);
                } else {
                    quad(out, cur, control, p);
                    control = null;
                }
                cur = p;
            } else {
                if (control != null) {
                    double[] mid = {(control[0] + p[0]) / 2, (control[1] + p[1]) / 2};
                    quad(out, cur, control, mid);
                    cur = mid;
                }
                control = p;
            }
        }
        return out;
    }

    private static void quad(List<double[]> out, double[] a, double[] c, double[] b) {
        int steps = 16;
        for (int i = 1; i <= steps; i++) {
            double t = i / (double) steps, u = 1 - t;
            out.add(new double[] {u * u * a[0] + 2 * u * t * c[0] + t * t * b[0], u * u * a[1] + 2 * u * t * c[1] + t * t * b[1]});
        }
    }

    static double length(Contour c) {
        List<double[]> l = polyline(c);
        double len = 0;
        for (int i = 1; i < l.size(); i++) {
            len += Math.hypot(l.get(i)[0] - l.get(i - 1)[0], l.get(i)[1] - l.get(i - 1)[1]);
        }
        return len;
    }

    /** @return the point and the unit tangent at the distance along the contour */
    static double[][] pointAt(Contour c, double distance) {
        List<double[]> l = polyline(c);
        if (l.isEmpty()) {
            return new double[][] {{0, 0}, {1, 0}};
        }
        double total = length(c);
        double d = total > 0 ? ((distance % total) + total) % total : 0;
        for (int i = 1; i < l.size(); i++) {
            double[] a = l.get(i - 1), b = l.get(i);
            double seg = Math.hypot(b[0] - a[0], b[1] - a[1]);
            if (d <= seg || i == l.size() - 1) {
                double t = seg == 0 ? 0 : Math.min(1, d / seg);
                double[] tangent = seg == 0 ? new double[] {1, 0} : new double[] {(b[0] - a[0]) / seg, (b[1] - a[1]) / seg};
                return new double[][] {{a[0] + (b[0] - a[0]) * t, a[1] + (b[1] - a[1]) * t}, tangent};
            }
            d -= seg;
        }
        return new double[][] {l.get(0), {1, 0}};
    }

    /** @return the distance along the contour, between start and end, of the point nearest to x, y */
    static double nearestLength(Contour c, double x, double y, double start, double end) {
        List<double[]> l = polyline(c);
        double best = Double.MAX_VALUE, bestLen = start;
        double len = 0;
        for (int i = 1; i < l.size(); i++) {
            double[] a = l.get(i - 1), b = l.get(i);
            double seg = Math.hypot(b[0] - a[0], b[1] - a[1]);
            double t = seg == 0 ? 0 : Math.max(0, Math.min(1, ((x - a[0]) * (b[0] - a[0]) + (y - a[1]) * (b[1] - a[1])) / (seg * seg)));
            double px = a[0] + (b[0] - a[0]) * t, py = a[1] + (b[1] - a[1]) * t;
            double at = len + seg * t;
            double dist = Math.hypot(px - x, py - y);
            if (at >= start && at <= end && dist < best) {
                best = dist;
                bestLen = at;
            }
            len += seg;
        }
        return bestLen;
    }
}
