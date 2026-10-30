package quicktime.std.image;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.internal.toolbox.MatrixManager;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.QDPoint;
import quicktime.qd.QDRect;
import quicktime.qd.Region;
import quicktime.std.StdQTException;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;
import quicktime.util.QTUtils;


/**
 * A Matrix defines how to map points from one coordinate space into another coordinate space.
 */
public final class Matrix extends QTByteObject implements QuickTimeLib, Cloneable {

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 36;

    @Serial
    private static final long serialVersionUID = -3479751345142891528L;

    /**
     * Construct an instance of a Matrix, with default settings of the identity Matrix
     */
    public Matrix() {
        super(36);
        setIdentity();
    }

    private Matrix(byte[] bytes) {
        super(bytes);
    }

    public Matrix(float[][] values) {
        super(36);
        setSx(values[0][0]);
        setB(values[0][1]);
        setU(values[0][2]);
        setC(values[1][0]);
        setSy(values[1][1]);
        setV(values[1][2]);
        setTx(values[2][0]);
        setTy(values[2][1]);
        setW(values[2][2]);
    }

    /**
     * Construct a Matrix that is a delta matrix between
     * the two sets of 4 points. This call is available only
     * in QuickTime 4 or later. The resulting matrix can be concatenated
     * with the matrix from which the source points are derived
     * to transform the display object to the destination area.
     * @see "QuickTime::QuadToQuadMatrix"
     * @param source a set of 4 points t,l t,r b,r and b,l that describes the source area
     * @param dest a set of 4 points t,l t,r b,r and b,l that describes the destination area
     */
    public Matrix(QDPoint[] source, QDPoint[] dest) throws QTException {
        this();
        if ((source.length < 4) || (dest.length < 4) || (QTSession.getQTMajorVersion() < 4))
            throw new StdQTException(-50);
        int[] arrayOfInt1 = DoublePts2FixedPts(source);
        int[] arrayOfInt2 = DoublePts2FixedPts(dest);
        int i = QuadToQuadMatrix(arrayOfInt1, arrayOfInt2, getBytes());
        StdQTException.checkError(i);
    }

    private static int[] DoublePts2FixedPts(QDPoint[] qdPoints) throws QTException {
        if (qdPoints == null) {
            return null;
        }
        int[] arrayOfInt = new int[qdPoints.length * 2];
        for (int i = 0; i < qdPoints.length; i++) {
            arrayOfInt[(i * 2)] = QTUtils.X2Fix(qdPoints[i].getXF());
            arrayOfInt[(i * 2 + 1)] = QTUtils.X2Fix(qdPoints[i].getYF());
        }
        return arrayOfInt;
    }

    private static void FixedPts2DoublePts(int[] fixedPts, QDPoint[] qdPoints) throws QTException {
        if ((fixedPts == null) || (qdPoints == null)) {
            return;
        }
        for (int i = 0; i < qdPoints.length; i++) {
            qdPoints[i].setX(QTUtils.Fix2X(fixedPts[(i * 2)]));
            qdPoints[i].setY(QTUtils.Fix2X(fixedPts[(i * 2 + 1)]));
        }
    }

    private static int[] DoublePt2FixedPt(QDPoint point) throws QTException {
        if (point == null)
            return null;
        int[] arrayOfInt = new int[2];
        arrayOfInt[0] = QTUtils.X2Fix(point.getXF());
        arrayOfInt[1] = QTUtils.X2Fix(point.getYF());
        return arrayOfInt;
    }

    private static void FixedPt2DoublePt(int[] fixedPt, QDPoint point) throws QTException {
        if ((fixedPt == null) || (point == null)) {
            return;
        }
        point.setX(QTUtils.Fix2X(fixedPt[0]));
        point.setY(QTUtils.Fix2X(fixedPt[1]));
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException {
        this.bytes = new byte[36];
        in.read(this.bytes);
        for (int i = 0; i < 36; i += 4) {
            int j = getIntFromArray(this.bytes, i);
            int k = EndianOrder.flipBigEndianToNative32(j);
            setIntInArray(this.bytes, i, k);
        }
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        byte[] movie = new byte[36];
        for (int i = 0; i < getBytes().length; i += 4) {
            int j = getIntFromArray(getBytes(), i);
            int k = EndianOrder.flipNativeToBigEndian32(j);
            setIntInArray(movie, i, k);
        }
        out.write(movie);
    }

    /**
     * Set the X axis translation by specified amount.
     * @param tx amount to set x-axis translation
     */
    public void setTx(float tx) {
        setIntAt(24, QTUtils.X2Fix(tx));
    }

    /**
     * Gets the X axis translation amount.
     * @return x-axis translation
     */
    public float getTx() {
        return QTUtils.Fix2X(getIntAt(24));
    }

    /**
     * Set the Y axis translation by specified amount.
     * @param ty amount to set y-axis translation
     */
    public void setTy(float ty) {
        setIntAt(28, QTUtils.X2Fix(ty));
    }

    /**
     * Get the Y axis translation amount.
     * @return y-axis translation
     */
    public float getTy() {
        return QTUtils.Fix2X(getIntAt(28));
    }

    /**
     * Set the X axis Scale by specified amount.
     * @param sx amount to set x-axis scale.
     */
    public void setSx(float sx) {
        setIntAt(0, QTUtils.X2Fix(sx));
    }

    /**
     * Gets the X axis scale amount.
     * @return x-axis scale.
     */
    public float getSx() {
        return QTUtils.Fix2X(getIntAt(0));
    }

    /**
     * Set the Y axis Scale by specified amount.
     * @param sy amount to set x-axis scale.
     */
    public void setSy(float sy) {
        setIntAt(16, QTUtils.X2Fix(sy));
    }

    /**
     * Gets the Y axis scale amount.
     * @return Y-axis scale.
     */
    public float getSy() {
        return QTUtils.Fix2X(getIntAt(16));
    }

    /**
     * Set b by specified amount.
     * @param b amoutn specified.
     */
    public void setB(float b) {
        setIntAt(4, QTUtils.X2Fix(b));
    }

    /**
     * Gets the b value.
     * @return b value.
     */
    public float getB() {
        return QTUtils.Fix2X(getIntAt(4));
    }

    /**
     * Set u by specified amount.
     * @param u amount specified.
     */
    public void setU(float u) {
        setIntAt(8, QTUtils.X2Fract(u));
    }

    /**
     * Gets the u value.
     * @return u value.
     */
    public float getU() {
        return QTUtils.Fract2X(getIntAt(8));
    }

    /**
     * Set c by specified amount.
     * @param c amount specified.
     */
    public void setC(float c) {
        setIntAt(12, QTUtils.X2Fix(c));
    }

    /**
     * Gets the c value.
     * @return c value.
     */
    public float getC() {
        return QTUtils.Fix2X(getIntAt(12));
    }

    /**
     * Set v by specified amount.
     * @param v amount specified.
     */
    public void setV(float v) {
        setIntAt(20, QTUtils.X2Fract(v));
    }

    /**
     * Gets the v value.
     * @return v value.
     */
    public float getV() {
        return QTUtils.Fract2X(getIntAt(20));
    }

    /**
     * Set w by specified amount.
     * @param w amount specified.
     */
    public void setW(float w) {
        setIntAt(32, QTUtils.X2Fract(w));
    }

    /**
     * Gets the w value.
     * @return w value.
     */
    public float getW() {
        return QTUtils.Fract2X(getIntAt(32));
    }

    /**
     * Obtains information about the matrix.
     * @see "QuickTime::GetMatrixType"
     * @return indicates the nature of the transformation defined by Matrix.
     */
    public short getType() {
        return GetMatrixType(getBytes());
    }

    /**
     * Returns true if the matrix is an identity matrix otherwise false
     */
    public boolean isIdentity() {
        return getType() == 0;
    }

    /**
     * Set the contents of a matrix so that it performs no transformation.
     * @see "QuickTime::SetIdentityMatrix"
     */
    public void setIdentity() {
        SetIdentityMatrix(getBytes());
    }

    /**
     * add a translation value to a specified matrix.
     * @see "QuickTime::TranslateMatrix"
     * @param deltaV specifies the value to be added to the x coordinate translation value.
     */
    public void translate(float deltaH, float deltaV) {
        TranslateMatrix(getBytes(), QTUtils.X2Fix(deltaH), QTUtils.X2Fix(deltaV));
    }

    /**
     * modifies the contents of a matrix so that is defines a rotation operation.
     * @see "QuickTime::RotateMatrix"
     * @param degrees specifies the number of degrees of rotation.
     * @param aboutX specifies the x coordinate of the anchor point of rotation.
     * @param aboutY specifies the y coordinate of the anchor point of rotation.
     */
    public void rotate(float degrees, float aboutX, float aboutY) {
        RotateMatrix(getBytes(), QTUtils.X2Fix(degrees), QTUtils.X2Fix(aboutX), QTUtils.X2Fix(aboutY));
    }

    /**
     * modifies the contents of a matrix so that it defines a scaling operation.
     * @see "QuickTime::ScaleMatrix"
     * @param scaleX specifies the scaling factor applied to x coordinates.
     * @param scaleY specifies the scaling factor applied to y coordinates.
     * @param aboutX specifies the x coordinate of the anchor point.
     * @param aboutY specifies the y coordinate of the anchor point.
     */
    public void scale(float scaleX, float scaleY, float aboutX, float aboutY) {
        ScaleMatrix(getBytes(), QTUtils.X2Fix(scaleX), QTUtils.X2Fix(scaleY), QTUtils.X2Fix(aboutX), QTUtils.X2Fix(aboutY));
    }

    /**
     * modifies the contents of a matrix so that it defines a skew transformation.
     * @see "QuickTime::SkewMatrix"
     * @param skewX specifies the skew value to be applied to x coordinate.
     * @param skewY specifies the skew value to be applied to y coordinate.
     * @param aboutX specifies the x coordinate of the anchor point.
     * @param aboutY specifies the y coordinate of the anchor point.
     */
    public void skew(float skewX, float skewY, float aboutX, float aboutY) {
        SkewMatrix(getBytes(), QTUtils.X2Fix(skewX), QTUtils.X2Fix(skewY), QTUtils.X2Fix(aboutX), QTUtils.X2Fix(aboutY));
    }

    /**
     * creates a new matrix that is the inverse of a specified matrix.
     * @see "QuickTime::InverseMatrix"
     * @param mr specifies the new inverse matrix.
     * @return true if inverse matrix was created, else return false.
     */
    public boolean inverse(Matrix mr) {
        return InverseMatrix(getBytes(), mr.getBytes()) != 0;
    }

    /**
     * Concatenates two matrices, combining the transformations described by both
     * matrices into a single matrix.
     * @see "QuickTime::ConcatMatrix"
     */
    public void concat(Matrix matrix) {
        ConcatMatrix(getBytes(), matrix.getBytes());
    }

    /**
     * Transforms a set of fixed points through a specified matrix.
     * @see "QuickTime::TransformFixedPoints"
     * @param dpts a set of float points.
     * @throws quicktime.QTException
     */
    public void transformDPoints(QDPoint dpts) throws QTException {
        int[] points = DoublePt2FixedPt(dpts);
        int i = TransformFixedPoints(getBytes(), points, 1);
        StdQTException.checkError(i);
        FixedPt2DoublePt(points, dpts);
    }

    public void transformDPoints(QDPoint[] qdPoints) throws QTException {
        int[] points = DoublePts2FixedPts(qdPoints);
        int i = TransformFixedPoints(getBytes(), points, qdPoints.length);
        StdQTException.checkError(i);
        FixedPts2DoublePts(points, qdPoints);
    }

    /**
     * transforms a set of QuickDraw points through a specified matrix.
     * @see "QuickTime::TransformPoints"
     * @param pts an array of Points.
     * @throws quicktime.std.StdQTException
     */
    public void transformPoints(QDPoint pts) throws StdQTException {
        int[] points = new int[1];
        points[0] = ((pts.getY() << 16) + pts.getX());
        StdQTException.checkError(TransformPoints(getBytes(), points, 1));
        pts.setY(points[0] >> 16);
        pts.setX(points[0] & 0xFFFF);
    }

    public void transformPoints(QDPoint[] qdPoints) throws StdQTException {
        int[] points = new int[qdPoints.length];
        for (int i = 0; i < qdPoints.length; i++) {
            points[i] = ((qdPoints[i].getY() << 16) + qdPoints[i].getX());
        }
        StdQTException.checkError(TransformPoints(getBytes(), points, qdPoints.length));
        for (int i = 0; i < qdPoints.length; i++) {
            qdPoints[i].setY(points[i] >> 16);
            qdPoints[i].setX(points[i] & 0xFFFF);
        }
    }

    /**
     * transforms the upper left and lower right points of a rectangle through a specified
     * matrix.
     * @see "QuickTime::TransformFixedRect"
     * @param r the QDRect to be transformed.
     * @return false if the transformation is other than scaling and translation.
     * @throws quicktime.QTException
     */
    public boolean transformDRect(QDRect r) throws QTException {
        if (r.getFixedRect() == null)
            throw new QTException(-50);
        return TransformFixedRect(getBytes(), r.getFixedRect(), 0) != 0;
    }

    /**
     * transforms the upper left and lower right points of a rectangle through a specified
     * matrix.
     * @see "QuickTime::TransformFixedRect"
     * @param r the QDRect to be transformed.
     * @return false if the transformation is other than scaling and translation.
     * @throws quicktime.QTException
     */
    public boolean transformDRect(QDRect r, QDPoint[] qdPoints) throws QTException {
        int[] points = DoublePts2FixedPts(qdPoints);
        if (r.getFixedRect() == null)
            throw new QTException(-50);
        int i = TransformFixedRect(getBytes(), r.getFixedRect(), points);
        if (i == 0) {
            FixedPts2DoublePts(points, qdPoints);
        }
        return i != 0;
    }

    /**
     * transforms the upper left and lower right points of a rectangle through a specified matrix.
     * @see "QuickTime::TransformRect"
     * @param r the rectangle to be transformed.
     * @return false if the transformation is other than scaling and translation.
     * @throws quicktime.QTException
     */
    public boolean transformRect(QDRect r) throws QTException {
        return TransformRect(getBytes(), r.getRect(), 0) != 0;
    }

    /**
     * transforms the upper left and lower right points of a rectangle through a specified matrix.
     * @see "QuickTime::TransformRect"
     * @param r the rectangle to be transformed.
     * @return false if the transformation is other than scaling and translation.
     * @throws quicktime.QTException
     */
    public boolean transformRect(QDRect r, QDPoint[] qdPoints) throws QTException {
        int[] points = DoublePts2FixedPts(qdPoints);
        int i = TransformRect(getBytes(), r.getRect(), points);
        if (i == 0) {
            FixedPts2DoublePts(points, qdPoints);
        }
        return i != 0;
    }

    /**
     * applies a specified matrix to a region.
     * @see "QuickTime::TransformRgn"
     * @param r the region to be transformed.
     * @throws quicktime.std.StdQTException
     */
    public void transformRgn(Region r) throws StdQTException {
        StdQTException.checkError(TransformRgn(getBytes(), QTObject.ID(r)));
    }

    /**
     * creates a matrix that performs a translate and scale operation as described by the relationship
     * between two rectangles.
     * @see "QuickTime::RectMatrix"
     * @param srcRect the source rectangle.
     * @param dstRect the destination rectangle.
     */
    public void rect(QDRect srcRect, QDRect dstRect) {
        RectMatrix(getBytes(), srcRect.getRect(), dstRect.getRect());
    }

    /**
     * alters an existing matrix so that it defines a transformation from one rectangle to
     * another.
     * @see "QuickTime::MapMatrix"
     * @param fromRect the source rectangle.
     * @param toRect the destination rectangle.
     */
    public void map(QDRect fromRect, QDRect toRect) {
        MapMatrix(getBytes(), fromRect.getRect(), toRect.getRect());
    }

    /**
     * Prints the matrix.
     */
    public String toString() {
        return getClass().getName() + "[(" + Double.toString(getSx()) + ", " + Double.toString(getB()) + ", " + Double.toString(getU()) + "), (" + Double.toString(getC()) + ", " + Double.toString(getSy()) + ", " + Double.toString(getV()) + "), (" + Double.toString(getTx()) + ", " + Double.toString(getTy()) + ", " + Double.toString(getW()) + ")]";
    }

    /**
     * copies the contents of the one matrix into another matrix.
     * @see "QuickTime::CopyMatrix"
     * @return a clone Matrix.
     */
    public Matrix copy() {
        return new Matrix(getBytes());
    }

    /**
     * copies the contents of the one matrix into another matrix.
     * @see "QuickTime::CopyMatrix"
     * @return a clone Matrix.
     */
    @Override
    public Object clone() {
        return copy();
    }

    /**
     * Compares the contents of a matrix for Equality.
     * @see "QuickTime::EqualMatrix"
     * @param m the Matrix you are testing for equality.
     * @return a boolean of whether the matrices are equal.
     */
    public boolean equals(Matrix m) {
        return EqualMatrix(getBytes(), m.getBytes()) != 0;
    }

    private static short QuadToQuadMatrix(int[] source, int[] dest, byte[] m) {
        return MatrixManager.QuadToQuadMatrix(source, dest, m);
    }

    private static short GetMatrixType(byte[] m) {
        return MatrixManager.GetMatrixType(m);
    }

    private static void SetIdentityMatrix(byte[] m) {
        MatrixManager.SetIdentityMatrix(m);
    }

    private static void TranslateMatrix(byte[] m, int dx, int dy) {
        MatrixManager.TranslateMatrix(m, dx, dy);
    }

    private static void RotateMatrix(byte[] m, int degrees, int aboutX, int aboutY) {
        MatrixManager.RotateMatrix(m, degrees, aboutX, aboutY);
    }

    private static void ScaleMatrix(byte[] m, int scaleX, int scaleY, int aboutX, int aboutY) {
        MatrixManager.ScaleMatrix(m, scaleX, scaleY, aboutX, aboutY);
    }

    private static void SkewMatrix(byte[] m, int skewX, int skewY, int aboutX, int aboutY) {
        MatrixManager.SkewMatrix(m, skewX, skewY, aboutX, aboutY);
    }

    private static byte InverseMatrix(byte[] m, byte[] im) {
        return MatrixManager.InverseMatrix(m, im);
    }

    private static void ConcatMatrix(byte[] a, byte[] b) {
        MatrixManager.ConcatMatrix(a, b);
    }

    private static short TransformFixedPoints(byte[] m, int[] points, int count) {
        return MatrixManager.TransformFixedPoints(m, points, count);
    }

    private static short TransformPoints(byte[] m, int[] points, int count) {
        return MatrixManager.TransformPoints(m, points, count);
    }

    private static byte TransformFixedRect(byte[] m, byte[] rect, int[] points) {
        return MatrixManager.TransformFixedRect(m, rect, points);
    }

    private static byte TransformFixedRect(byte[] m, byte[] rect, int points) {
        return MatrixManager.TransformFixedRect(m, rect, points);
    }

    private static byte TransformRect(byte[] m, byte[] rect, int[] points) {
        return MatrixManager.TransformRect(m, rect, points);
    }

    private static byte TransformRect(byte[] m, byte[] rect, int points) {
        return MatrixManager.TransformRect(m, rect, points);
    }

    private static short TransformRgn(byte[] m, int rgn) {
        return MatrixManager.TransformRgn(m, rgn);
    }

    private static void RectMatrix(byte[] m, byte[] src, byte[] dst) {
        MatrixManager.RectMatrix(m, src, dst);
    }

    private static void MapMatrix(byte[] m, byte[] from, byte[] to) {
        MatrixManager.MapMatrix(m, from, to);
    }

    private static void CopyMatrix(byte[] src, byte[] dst) {
        MatrixManager.CopyMatrix(src, dst);
    }

    private static byte EqualMatrix(byte[] a, byte[] b) {
        return MatrixManager.EqualMatrix(a, b);
    }

    private static int getIntFromArray(byte[] array, int offset) {
        return MemoryManager.getIntFromArray(array, offset);
    }

    private static void setIntInArray(byte[] array, int offset, int value) {
        MemoryManager.setIntInArray(array, offset, value);
    }
}
