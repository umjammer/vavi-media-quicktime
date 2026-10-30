package quicktime.qd3d.math;

import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.internal.toolbox.QD3DManager;
import quicktime.jdirect.QuickDraw3DLib;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.QDPoint;


/**
 * Point2D contains static calls - the implementation is found in the QDPoint class
 * @see <A href="../../../quicktime/qd/QDPoint.html">refer to Q3Point2D in QuickDraw3D</A>
 */
public final class Point2D implements QuickTimeLib, QuickDraw3DLib {

    static {
        if (QTSession.isCurrentOS(4)) {
            throw new QTRuntimeException("Unsupported OS");
        }
    }

    /**
     * Returns a two-dimensional vector that is the result of subtracting the point p2 from p1.
     * @see "QuickDraw3D::Q3Point2D_Subtract"
     * @param p1 A two-dimensional point
     * @param p2 A two-dimensional point
     * @return a Vector2D object
     */
    public static Vector2D subtract(QDPoint p1, QDPoint p2) {
        Vector2D vector2D = new Vector2D();
        int i = Q3Point2D_Subtract(p1.getQ3Point2D(), p2.getQ3Point2D(), vector2D.getBytes());
        return vector2D;
    }

    /**
     * Returns the absolute value of the distance between points p1 and p2.
     * @see "QuickDraw3D::Q3Point2D_Distance"
     * @param p1 A two-dimensional point
     * @param p2 A two-dimensional point
     * @return the distance
     */
    public static float distance(QDPoint p1, QDPoint p2) {
        return Q3Point2D_Distance(p1.getQ3Point2D(), p2.getQ3Point2D());
    }

    /**
     * Returns the square value of the distance between points p1 and p2.
     * @see "QuickDraw3D::Q3Point2D_DistanceSquared"
     * @param p1 A two-dimensional point
     * @param p2 A two-dimensional point
     * @return the distance
     */
    public static float distanceSquared(QDPoint p1, QDPoint p2) {
        return Q3Point2D_DistanceSquared(p1.getQ3Point2D(), p2.getQ3Point2D());
    }

    /**
     * Returns the two-dimensional point that lies on the line segment between
     * the points p1 and p2 and that is at a distance from the first point determined by
     * the ratio r1/(r1 + r2).
     * @see "QuickDraw3D::Q3Point2D_RRatio"
     * @param p2 A two-dimensional point.
     * @param r1 A floating-point number.
     * @param r2 A floating-point number.
     * @return a QDPoint
     */
    public static QDPoint rRatio(QDPoint point1, QDPoint p2, float r1, float r2) {
        byte[] result = new byte[8];
        int i = Q3Point2D_RRatio(point1.getQ3Point2D(), p2.getQ3Point2D(), r1, r2, result);
        return QDPoint.fromArray(result, -8);
    }

    /**
     * Returns the result of adding a two-dimensional vector to a two-dimensional point.
     * @see "QuickDraw3D::Q3Point2D_Vector2D_Add"
     * @param point2D A two-dimensional point.
     * @param v a two-dimensional vector
     * @return a point that is the result of the add.
     */
    public static QDPoint add(QDPoint point2D, Vector2D v) {
        byte[] result = new byte[8];
        int i = Q3Point2D_Vector2D_Add(point2D.getQ3Point2D(), v.getBytes(), result);
        return QDPoint.fromArray(result, -8);
    }

    /**
     * Returns the result of subtracting a two-dimensional vector from a two-dimensional point.
     * @see "QuickDraw3D::Q3Point2D_Vector2D_Subtract"
     * @param point2D A two-dimensional point.
     * @param v a two-dimensional vector
     * @return a point that is the result of the add.
     */
    public static QDPoint subtract(QDPoint point2D, Vector2D v) {
        byte[] result = new byte[8];
        int i = Q3Point2D_Vector2D_Subtract(point2D.getQ3Point2D(), v.getBytes(), result);
        return QDPoint.fromArray(result, -8);
    }

    private static int Q3Point2D_Subtract(byte[] p1, byte[] p2, byte[] result) {
        return QD3DManager.Q3Point2D_Subtract(p1, p2, result);
    }

    private static float Q3Point2D_Distance(byte[] p1, byte[] p2) {
        return QD3DManager.Q3Point2D_Distance(p1, p2);
    }

    private static float Q3Point2D_DistanceSquared(byte[] p1, byte[] p2) {
        return QD3DManager.Q3Point2D_DistanceSquared(p1, p2);
    }

    private static int Q3Point2D_RRatio(byte[] p1, byte[] p2, float r1, float r2, byte[] result) {
        return QD3DManager.Q3Point2D_RRatio(p1, p2, r1, r2, result);
    }

    private static int Q3Point2D_Vector2D_Add(byte[] point2D, byte[] vector2D, byte[] result) {
        return QD3DManager.Q3Point2D_Vector2D_Add(point2D, vector2D, result);
    }

    private static int Q3Point2D_Vector2D_Subtract(byte[] point2D, byte[] vector2D, byte[] result) {
        return QD3DManager.Q3Point2D_Vector2D_Subtract(point2D, vector2D, result);
    }
}
