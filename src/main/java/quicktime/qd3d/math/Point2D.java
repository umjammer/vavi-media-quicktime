package quicktime.qd3d.math;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.internal.toolbox.QD3DManager;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickDraw3DLib;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.QDPoint;

/**
 * Point2D contains static calls - the implementation is found in the QDPoint class
 * @see <A href="../../../quicktime/qd/QDPoint.html">refer to Q3Point2D in QuickDraw3D</A>
 */
public final class Point2D implements QuickTimeLib, QuickDraw3DLib {

    private static Object linkage;

    static {
        if (QTSession.isCurrentOS(4)) {
            throw new QTRuntimeException("Unsupported OS");
        }
        class PrivelegedAction {

            void establish() {
                linkage = AccessController.doPrivileged((PrivilegedAction<Object>) () -> QTNative.linkNativeMethods(Point2D.class));
            }
        }
        new PrivelegedAction().establish();
    }

    /**
     * Returns a two-dimensional vector that is the result of subtracting the point p2 from p1.
     * @see "QuickDraw3D::Q3Point2D_Subtract"
     * @param p1 A two-dimensional point
     * @param p2 A two-dimensional point
     * @return a Vector2D object
     */
    public static Vector2D subtract(QDPoint p1, QDPoint p2) {
        Vector2D localVector2D = new Vector2D();
        int i = Q3Point2D_Subtract(p1.getQ3Point2D(), p2.getQ3Point2D(), localVector2D.getBytes());
        return localVector2D;
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
    public static QDPoint rRatio(QDPoint paramQDPoint1, QDPoint paramQDPoint2, float paramFloat1, float paramFloat2) {
        byte[] arrayOfByte = new byte[8];
        int i = Q3Point2D_RRatio(paramQDPoint1.getQ3Point2D(), paramQDPoint2.getQ3Point2D(), paramFloat1, paramFloat2, arrayOfByte);
        return QDPoint.fromArray(arrayOfByte, -8);
    }

    /**
     * Returns the result of adding a two-dimensional vector to a two-dimensional point.
     * @see "QuickDraw3D::Q3Point2D_Vector2D_Add"
     * @param point2D A two-dimensional point.
     * @param v a two-dimensional vector
     * @return a point that is the result of the add.
     */
    public static QDPoint add(QDPoint point2D, Vector2D v) {
        byte[] arrayOfByte = new byte[8];
        int i = Q3Point2D_Vector2D_Add(point2D.getQ3Point2D(), v.getBytes(), arrayOfByte);
        return QDPoint.fromArray(arrayOfByte, -8);
    }

    /**
     * Returns the result of subtracting a two-dimensional vector from a two-dimensional point.
     * @see "QuickDraw3D::Q3Point2D_Vector2D_Subtract"
     * @param point2D A two-dimensional point.
     * @param v a two-dimensional vector
     * @return a point that is the result of the add.
     */
    public static QDPoint subtract(QDPoint point2D, Vector2D v) {
        byte[] arrayOfByte = new byte[8];
        int i = Q3Point2D_Vector2D_Subtract(point2D.getQ3Point2D(), v.getBytes(), arrayOfByte);
        return QDPoint.fromArray(arrayOfByte, -8);
    }

    private static int Q3Point2D_Subtract(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3) {
        return QD3DManager.Q3Point2D_Subtract(paramArrayOfByte1, paramArrayOfByte2, paramArrayOfByte3);
    }

    private static float Q3Point2D_Distance(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2) {
        return QD3DManager.Q3Point2D_Distance(paramArrayOfByte1, paramArrayOfByte2);
    }

    private static float Q3Point2D_DistanceSquared(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2) {
        return QD3DManager.Q3Point2D_DistanceSquared(paramArrayOfByte1, paramArrayOfByte2);
    }

    private static int Q3Point2D_RRatio(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, float paramFloat1, float paramFloat2, byte[] paramArrayOfByte3) {
        return QD3DManager.Q3Point2D_RRatio(paramArrayOfByte1, paramArrayOfByte2, paramFloat1, paramFloat2, paramArrayOfByte3);
    }

    private static int Q3Point2D_Vector2D_Add(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3) {
        return QD3DManager.Q3Point2D_Vector2D_Add(paramArrayOfByte1, paramArrayOfByte2, paramArrayOfByte3);
    }

    private static int Q3Point2D_Vector2D_Subtract(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3) {
        return QD3DManager.Q3Point2D_Vector2D_Subtract(paramArrayOfByte1, paramArrayOfByte2, paramArrayOfByte3);
    }
}
