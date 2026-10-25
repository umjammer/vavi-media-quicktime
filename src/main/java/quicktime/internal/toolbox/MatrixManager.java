/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;


/**
 * MatrixManager.
 * <p>
 * QuickTime transformation matrices. A MatrixRecord is 3x3 big endian 32 bit values row by row,
 * {@code {a, b, u}, {c, d, v}, {tx, ty, w}}, where u, v and w are 2.30 fractions and the rest 16.16 fixed.
 * Points are row vectors: {@code x' = (a x + c y + tx) / (u x + v y + w)}, {@code y' = (b x + d y + ty) / (u x + v y + w)}.
 */
public final class MatrixManager {

    private MatrixManager() {
    }

    static final int identityMatrixType = 0x00;
    static final int translateMatrixType = 0x01;
    static final int scaleMatrixType = 0x02;
    static final int scaleTranslateMatrixType = 0x03;
    static final int linearMatrixType = 0x04;
    static final int linearTranslateMatrixType = 0x05;
    static final int perspectiveMatrixType = 0x06;

    /** @return the identity matrix as 9 raw values */
    static int[] identity() {
        return new int[] {0x10000, 0, 0, 0, 0x10000, 0, 0, 0, 0x4000_0000};
    }

    static double[][] toDoubles(int[] m) {
        double[][] d = new double[3][3];
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                d[r][c] = c == 2 ? m[r * 3 + c] / (double) (1 << 30) : m[r * 3 + c] / 65536.0;
            }
        }
        return d;
    }

    static int[] fromDoubles(double[][] d) {
        int[] m = new int[9];
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                double v = c == 2 ? d[r][c] * (1 << 30) : d[r][c] * 65536.0;
                m[r * 3 + c] = (int) Math.max(Integer.MIN_VALUE, Math.min(Integer.MAX_VALUE, Math.round(v)));
            }
        }
        return m;
    }

    static int[] read(byte[] b) {
        int[] m = new int[9];
        for (int i = 0; i < 9; i++) {
            m[i] = Bytes.getInt(b, i * 4);
        }
        return m;
    }

    static void write(int[] m, byte[] b) {
        for (int i = 0; i < 9; i++) {
            Bytes.putInt(b, i * 4, m[i]);
        }
    }

    static double[][] get(byte[] b) {
        return toDoubles(read(b));
    }

    static void set(byte[] b, double[][] d) {
        write(fromDoubles(d), b);
    }

    static double[][] multiply(double[][] a, double[][] b) {
        double[][] r = new double[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                r[i][j] = a[i][0] * b[0][j] + a[i][1] * b[1][j] + a[i][2] * b[2][j];
            }
        }
        return r;
    }

    private static double[][] translation(double dx, double dy) {
        return new double[][] {{1, 0, 0}, {0, 1, 0}, {dx, dy, 1}};
    }

    /** m := m * T(-about) * op * T(about) */
    private static void apply(byte[] m, double[][] op, double aboutX, double aboutY) {
        double[][] t = multiply(multiply(translation(-aboutX, -aboutY), op), translation(aboutX, aboutY));
        set(m, multiply(get(m), t));
    }

    /** @return the transformed point */
    static double[] transform(double[][] m, double x, double y) {
        double w = m[0][2] * x + m[1][2] * y + m[2][2];
        if (w == 0) {
            w = 1;
        }
        return new double[] {(m[0][0] * x + m[1][0] * y + m[2][0]) / w, (m[0][1] * x + m[1][1] * y + m[2][1]) / w};
    }

    static double[][] inverse(double[][] m) {
        double det = m[0][0] * (m[1][1] * m[2][2] - m[1][2] * m[2][1]) - m[0][1] * (m[1][0] * m[2][2] - m[1][2] * m[2][0]) + m[0][2] * (m[1][0] * m[2][1] - m[1][1] * m[2][0]);
        if (Math.abs(det) < 1e-12) {
            return null;
        }
        double[][] r = new double[3][3];
        r[0][0] = (m[1][1] * m[2][2] - m[1][2] * m[2][1]) / det;
        r[0][1] = (m[0][2] * m[2][1] - m[0][1] * m[2][2]) / det;
        r[0][2] = (m[0][1] * m[1][2] - m[0][2] * m[1][1]) / det;
        r[1][0] = (m[1][2] * m[2][0] - m[1][0] * m[2][2]) / det;
        r[1][1] = (m[0][0] * m[2][2] - m[0][2] * m[2][0]) / det;
        r[1][2] = (m[0][2] * m[1][0] - m[0][0] * m[1][2]) / det;
        r[2][0] = (m[1][0] * m[2][1] - m[1][1] * m[2][0]) / det;
        r[2][1] = (m[0][1] * m[2][0] - m[0][0] * m[2][1]) / det;
        r[2][2] = (m[0][0] * m[1][1] - m[0][1] * m[1][0]) / det;
        return r;
    }

    static int type(int[] m) {
        if (m[2] != 0 || m[5] != 0 || m[8] != 0x4000_0000) {
            return perspectiveMatrixType;
        }
        boolean translate = m[6] != 0 || m[7] != 0;
        if (m[1] != 0 || m[3] != 0) {
            return translate ? linearTranslateMatrixType : linearMatrixType;
        }
        boolean scale = m[0] != 0x10000 || m[4] != 0x10000;
        return (scale ? scaleMatrixType : 0) | (translate ? translateMatrixType : 0);
    }

    /** @return the rect mapping matrix, rects are top, left, bottom, right */
    private static double[][] rectMatrix(byte[] src, byte[] dst) {
        double st = Bytes.getShort(src, 0), sl = Bytes.getShort(src, 2), sb = Bytes.getShort(src, 4), sr = Bytes.getShort(src, 6);
        double dt = Bytes.getShort(dst, 0), dl = Bytes.getShort(dst, 2), db = Bytes.getShort(dst, 4), dr = Bytes.getShort(dst, 6);
        double sx = sr - sl == 0 ? 1 : (dr - dl) / (sr - sl);
        double sy = sb - st == 0 ? 1 : (db - dt) / (sb - st);
        return new double[][] {{sx, 0, 0}, {0, sy, 0}, {dl - sl * sx, dt - st * sy, 1}};
    }

    public static void SetIdentityMatrix(byte[] m) {
        write(identity(), m);
    }

    public static short GetMatrixType(byte[] m) {
        return (short) type(read(m));
    }

    public static void CopyMatrix(byte[] src, byte[] dst) {
        System.arraycopy(src, 0, dst, 0, 36);
    }

    public static byte EqualMatrix(byte[] a, byte[] b) {
        return (byte) (java.util.Arrays.equals(read(a), read(b)) ? 1 : 0);
    }

    /** b := a * b */
    public static void ConcatMatrix(byte[] a, byte[] b) {
        set(b, multiply(get(a), get(b)));
    }

    public static void TranslateMatrix(byte[] m, int dx, int dy) {
        set(m, multiply(get(m), translation(dx / 65536.0, dy / 65536.0)));
    }

    public static void RotateMatrix(byte[] m, int degrees, int aboutX, int aboutY) {
        double r = Math.toRadians(degrees / 65536.0);
        double c = Math.cos(r);
        double s = Math.sin(r);
        apply(m, new double[][] {{c, s, 0}, {-s, c, 0}, {0, 0, 1}}, aboutX / 65536.0, aboutY / 65536.0);
    }

    public static void ScaleMatrix(byte[] m, int scaleX, int scaleY, int aboutX, int aboutY) {
        apply(m, new double[][] {{scaleX / 65536.0, 0, 0}, {0, scaleY / 65536.0, 0}, {0, 0, 1}}, aboutX / 65536.0, aboutY / 65536.0);
    }

    public static void SkewMatrix(byte[] m, int skewX, int skewY, int aboutX, int aboutY) {
        apply(m, new double[][] {{1, skewY / 65536.0, 0}, {skewX / 65536.0, 1, 0}, {0, 0, 1}}, aboutX / 65536.0, aboutY / 65536.0);
    }

    public static byte InverseMatrix(byte[] m, byte[] im) {
        double[][] r = inverse(get(m));
        if (r == null) {
            return 0;
        }
        // normalize w to 1
        double w = r[2][2];
        if (w != 0 && r[0][2] == 0 && r[1][2] == 0) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    r[i][j] /= w;
                }
            }
        }
        set(im, r);
        return 1;
    }

    public static void RectMatrix(byte[] m, byte[] src, byte[] dst) {
        set(m, rectMatrix(src, dst));
    }

    public static void MapMatrix(byte[] m, byte[] from, byte[] to) {
        set(m, multiply(get(m), rectMatrix(from, to)));
    }

    /** points are packed v(16) h(16) */
    public static short TransformPoints(byte[] m, int[] points, int count) {
        double[][] d = get(m);
        for (int i = 0; i < count; i++) {
            double[] p = transform(d, (short) points[i], (short) (points[i] >> 16));
            int h = (int) Math.round(p[0]);
            int v = (int) Math.round(p[1]);
            points[i] = (v << 16) | (h & 0xffff);
        }
        return 0;
    }

    /** fixed points are x, y pairs */
    public static short TransformFixedPoints(byte[] m, int[] points, int count) {
        double[][] d = get(m);
        for (int i = 0; i < count; i++) {
            double[] p = transform(d, points[i * 2] / 65536.0, points[i * 2 + 1] / 65536.0);
            points[i * 2] = Bytes.doubleToFixed(p[0]);
            points[i * 2 + 1] = Bytes.doubleToFixed(p[1]);
        }
        return 0;
    }

    /** @return the 4 corners of the transformed rect: top left, top right, bottom right, bottom left */
    private static double[][] corners(double[][] d, double l, double t, double r, double b) {
        return new double[][] {transform(d, l, t), transform(d, r, t), transform(d, r, b), transform(d, l, b)};
    }

    /** @return 1 if the result is still a rectangle */
    private static boolean bounds(double[][] c, double[] out) {
        double l = Double.MAX_VALUE, t = Double.MAX_VALUE, r = -Double.MAX_VALUE, b = -Double.MAX_VALUE;
        for (double[] p : c) {
            l = Math.min(l, p[0]);
            r = Math.max(r, p[0]);
            t = Math.min(t, p[1]);
            b = Math.max(b, p[1]);
        }
        out[0] = l;
        out[1] = t;
        out[2] = r;
        out[3] = b;
        return Math.abs(c[0][1] - c[1][1]) < 1e-9 && Math.abs(c[0][0] - c[3][0]) < 1e-9;
    }

    public static byte TransformRect(byte[] m, byte[] rect, int points) {
        return TransformRect(m, rect, (int[]) null);
    }

    public static byte TransformRect(byte[] m, byte[] rect, int[] points) {
        double[][] c = corners(get(m), Bytes.getShort(rect, 2), Bytes.getShort(rect, 0), Bytes.getShort(rect, 6), Bytes.getShort(rect, 4));
        double[] o = new double[4];
        boolean isRect = bounds(c, o);
        Bytes.putShort(rect, 0, (int) Math.round(o[1]));
        Bytes.putShort(rect, 2, (int) Math.round(o[0]));
        Bytes.putShort(rect, 4, (int) Math.round(o[3]));
        Bytes.putShort(rect, 6, (int) Math.round(o[2]));
        if (points != null) {
            for (int i = 0; i < 4 && i * 2 + 1 < points.length; i++) {
                points[i * 2] = Bytes.doubleToFixed(c[i][0]);
                points[i * 2 + 1] = Bytes.doubleToFixed(c[i][1]);
            }
        }
        return (byte) (isRect ? 1 : 0);
    }

    public static byte TransformFixedRect(byte[] m, byte[] rect, int points) {
        return TransformFixedRect(m, rect, (int[]) null);
    }

    /** fixed rects are left, top, right, bottom */
    public static byte TransformFixedRect(byte[] m, byte[] rect, int[] points) {
        double[][] c = corners(get(m), Bytes.getInt(rect, 0) / 65536.0, Bytes.getInt(rect, 4) / 65536.0, Bytes.getInt(rect, 8) / 65536.0, Bytes.getInt(rect, 12) / 65536.0);
        double[] o = new double[4];
        boolean isRect = bounds(c, o);
        for (int i = 0; i < 4; i++) {
            Bytes.putInt(rect, i * 4, Bytes.doubleToFixed(o[i]));
        }
        if (points != null) {
            for (int i = 0; i < 4 && i * 2 + 1 < points.length; i++) {
                points[i * 2] = Bytes.doubleToFixed(c[i][0]);
                points[i * 2 + 1] = Bytes.doubleToFixed(c[i][1]);
            }
        }
        return (byte) (isRect ? 1 : 0);
    }

    public static short TransformRgn(byte[] m, int rgn) {
        return QuickDrawManager.transformRegion(read(m), rgn);
    }

    /** solves the perspective matrix mapping the source quadrilateral to the destination one */
    public static short QuadToQuadMatrix(int[] source, int[] dest, byte[] m) {
        double[][] a = squareToQuad(dest);
        double[][] b = squareToQuad(source);
        double[][] bi = b == null ? null : inverse(b);
        if (a == null || bi == null) {
            return -50;
        }
        double[][] r = multiply(bi, a);
        double w = r[2][2];
        if (w != 0) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    r[i][j] /= w;
                }
            }
        }
        set(m, r);
        return 0;
    }

    /** @return the matrix mapping the unit square to the quadrilateral (4 fixed x, y pairs) */
    private static double[][] squareToQuad(int[] q) {
        double x0 = q[0] / 65536.0, y0 = q[1] / 65536.0, x1 = q[2] / 65536.0, y1 = q[3] / 65536.0;
        double x2 = q[4] / 65536.0, y2 = q[5] / 65536.0, x3 = q[6] / 65536.0, y3 = q[7] / 65536.0;
        double dx1 = x1 - x2, dx2 = x3 - x2, dx3 = x0 - x1 + x2 - x3;
        double dy1 = y1 - y2, dy2 = y3 - y2, dy3 = y0 - y1 + y2 - y3;
        double g, h;
        if (dx3 == 0 && dy3 == 0) {
            g = 0;
            h = 0;
        } else {
            double den = dx1 * dy2 - dx2 * dy1;
            if (den == 0) {
                return null;
            }
            g = (dx3 * dy2 - dx2 * dy3) / den;
            h = (dx1 * dy3 - dx3 * dy1) / den;
        }
        double a = x1 - x0 + g * x1, b = x3 - x0 + h * x3, c = x0;
        double d = y1 - y0 + g * y1, e = y3 - y0 + h * y3, f = y0;
        // row vector form
        return new double[][] {{a, d, g}, {b, e, h}, {c, f, 1}};
    }

    /** used by DSequence */
    public static short GetDSequenceMatrix(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("GetDSequenceMatrix");
    }

    /** used by GraphicsImporter */
    public static int GraphicsImportGetDefaultMatrix(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("GraphicsImportGetDefaultMatrix");
    }

    /** used by GraphicsImporter */
    public static int GraphicsImportGetMatrix(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("GraphicsImportGetMatrix");
    }

    /** used by GraphicsImporter */
    public static int GraphicsImportSetMatrix(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("GraphicsImportSetMatrix");
    }

    /** used by Quaternion */
    public static int Q3Quaternion_SetMatrix(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2) {
        throw new UnsupportedToolboxException("Q3Quaternion_SetMatrix");
    }

    /** used by Presentation */
    public static short QTSPresGetMatrix(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("QTSPresGetMatrix");
    }

    /** used by Presentation */
    public static short QTSPresSetMatrix(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("QTSPresSetMatrix");
    }

    /** used by VisualChannel */
    public static int SGGetChannelMatrix(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("SGGetChannelMatrix");
    }

    /** used by VisualChannel */
    public static int SGSetChannelMatrix(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("SGSetChannelMatrix");
    }

    /** used by DSequence */
    public static short SetDSequenceMatrix(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("SetDSequenceMatrix");
    }

    /** used by SpriteWorld */
    public static short SetSpriteWorldMatrix(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("SetSpriteWorldMatrix");
    }

}
