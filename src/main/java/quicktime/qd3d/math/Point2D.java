
package quicktime.qd3d.math;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickDraw3DLib;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.QDPoint;


public final class Point2D implements QuickTimeLib, QuickDraw3DLib {
    private static Object linkage;

    static {
        if (QTSession.isCurrentOS(4)) {
            throw new QTRuntimeException("Unsupported OS");
        }

        class PrivelegedAction {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(Point2D.class);
                    }
                });
            }
        }
        new PrivelegedAction().establish();
    }

    public static Vector2D subtract(QDPoint paramQDPoint1, QDPoint paramQDPoint2) {
        Vector2D localVector2D = new Vector2D();
        int i = Q3Point2D_Subtract(paramQDPoint1.getQ3Point2D(), paramQDPoint2.getQ3Point2D(), localVector2D.getBytes());
        return localVector2D;
    }

    public static float distance(QDPoint paramQDPoint1, QDPoint paramQDPoint2) {
        return Q3Point2D_Distance(paramQDPoint1.getQ3Point2D(), paramQDPoint2.getQ3Point2D());
    }

    public static float distanceSquared(QDPoint paramQDPoint1, QDPoint paramQDPoint2) {
        return Q3Point2D_DistanceSquared(paramQDPoint1.getQ3Point2D(), paramQDPoint2.getQ3Point2D());
    }

    public static QDPoint rRatio(QDPoint paramQDPoint1, QDPoint paramQDPoint2, float paramFloat1, float paramFloat2) {
        byte[] arrayOfByte = new byte[8];
        int i = Q3Point2D_RRatio(paramQDPoint1.getQ3Point2D(),
                                 paramQDPoint2.getQ3Point2D(),
                                 paramFloat1,
                                 paramFloat2,
                                 arrayOfByte);
        return QDPoint.fromArray(arrayOfByte, -8);
    }

    public static QDPoint add(QDPoint paramQDPoint, Vector2D paramVector2D) {
        byte[] arrayOfByte = new byte[8];
        int i = Q3Point2D_Vector2D_Add(paramQDPoint.getQ3Point2D(), paramVector2D.getBytes(), arrayOfByte);
        return QDPoint.fromArray(arrayOfByte, -8);
    }

    public static QDPoint subtract(QDPoint paramQDPoint, Vector2D paramVector2D) {
        byte[] arrayOfByte = new byte[8];
        int i = Q3Point2D_Vector2D_Subtract(paramQDPoint.getQ3Point2D(), paramVector2D.getBytes(), arrayOfByte);
        return QDPoint.fromArray(arrayOfByte, -8);
    }

    private static native int Q3Point2D_Subtract(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3);

    private static native float Q3Point2D_Distance(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);

    private static native float Q3Point2D_DistanceSquared(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);

    private static native int Q3Point2D_RRatio(byte[] paramArrayOfByte1,
                                               byte[] paramArrayOfByte2,
                                               float paramFloat1,
                                               float paramFloat2,
                                               byte[] paramArrayOfByte3);

    private static native int Q3Point2D_Vector2D_Add(byte[] paramArrayOfByte1,
                                                     byte[] paramArrayOfByte2,
                                                     byte[] paramArrayOfByte3);

    private static native int Q3Point2D_Vector2D_Subtract(byte[] paramArrayOfByte1,
                                                          byte[] paramArrayOfByte2,
                                                          byte[] paramArrayOfByte3);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/qd3d/math
 * /Point2D.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
