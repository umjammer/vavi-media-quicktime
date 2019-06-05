
package quicktime.std.image;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTObject;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.QDPoint;
import quicktime.std.StdQTException;
import quicktime.util.QTByteObject;
import quicktime.util.QTUtils;


public final class GXPaths extends QTByteObject implements QuickTimeLib {
    private static Object linkage;

    private Curve comp;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(GXPaths.class);
                    }
                });
            }
        }.establish();
    }

    private static int howManyBytes(GXPath[] paramArrayOfGXPath) {
        int i = 0;
        for (int j = 0; j < paramArrayOfGXPath.length; j++)
            i += paramArrayOfGXPath[j].getSize();
        return i;
    }

    public GXPaths(Curve paramCurve, GXPath[] paramArrayOfGXPath) {
        super(howManyBytes(paramArrayOfGXPath) + 4);
        setIntAt(0, paramArrayOfGXPath.length);
        int i = 4;
        for (int j = 0; j < paramArrayOfGXPath.length; j++) {
            setBytesAt(i, paramArrayOfGXPath[j].getSize(), paramArrayOfGXPath[j].getBytes(), 0);
            i += paramArrayOfGXPath[j].getSize();
        }
        this.comp = paramCurve;
    }

    public int getContours() {
        return getIntAt(0);
    }

    public int countPointsInPath(int paramInt) throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        int i = CurveCountPointsInPath(QTObject.ID(this.comp), getBytes(), paramInt, arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    public void setPathPoint(int paramInt1, int paramInt2, QDPoint paramQDPoint, boolean paramBoolean) throws StdQTException {
        int i = CurveSetPathPoint(QTObject.ID(this.comp),
                                  getBytes(),
                                  paramInt1,
                                  paramInt2,
                                  paramQDPoint.getGXPoint(),
                                  (byte) (paramBoolean ? 1 : 0));
        StdQTException.checkError(i);
    }

    public PathPointInfo getPathPoint(int paramInt1, int paramInt2) throws StdQTException {
        QDPoint localQDPoint = new QDPoint(0.0F, 0.0F);
        byte[] arrayOfByte = new byte[1];
        int i = CurveGetPathPoint(QTObject.ID(this.comp),
                                  getBytes(),
                                  paramInt1,
                                  paramInt2,
                                  localQDPoint.getGXPoint(),
                                  arrayOfByte);
        StdQTException.checkError(i);
        return new PathPointInfo(localQDPoint, arrayOfByte[0] != 0);
    }

    public QDPoint[] lengthToPoint(int paramInt1, int paramInt2) throws StdQTException {
        QDPoint localQDPoint1 = new QDPoint(0.0F, 0.0F);
        QDPoint localQDPoint2 = new QDPoint(0.0F, 0.0F);

        int i = CurveLengthToPoint(QTObject.ID(this.comp),
                                   getBytes(),
                                   paramInt1,
                                   paramInt2,
                                   localQDPoint1.getGXPoint(),
                                   localQDPoint2.getGXPoint());
        StdQTException.checkError(i);
        QDPoint[] arrayOfQDPoint = new QDPoint[2];
        arrayOfQDPoint[0] = localQDPoint1;
        arrayOfQDPoint[1] = localQDPoint2;
        return arrayOfQDPoint;
    }

    public long getLength(int paramInt) throws StdQTException {
        long[] arrayOfLong = {
            0L
        };
        int i = CurveGetLength(QTObject.ID(this.comp), getBytes(), paramInt, arrayOfLong);
        StdQTException.checkError(i);
        return arrayOfLong[0];
    }

    public float getPathPointToLength(int paramInt1, int paramInt2, QDPoint paramQDPoint) throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        int i = CurvePathPointToLength(QTObject.ID(this.comp),
                                       getBytes(),
                                       paramInt1,
                                       paramInt2,
                                       paramQDPoint.getGXPoint(),
                                       arrayOfInt);
        StdQTException.checkError(i);
        return QTUtils.Fix2X(arrayOfInt[0]);
    }

    public NearestPointInfo getNearestPathPoint(QDPoint paramQDPoint) throws StdQTException {
        int[] arrayOfInt1 = {
            0
        };
        int[] arrayOfInt2 = {
            0
        };
        int[] arrayOfInt3 = {
            0
        };

        int i = CurveGetNearestPathPoint(QTObject.ID(this.comp),
                                         getBytes(),
                                         paramQDPoint.getGXPoint(),
                                         arrayOfInt1,
                                         arrayOfInt2,
                                         arrayOfInt3);
        StdQTException.checkError(i);
        return new NearestPointInfo(arrayOfInt1[0], arrayOfInt2[0], QTUtils.Fix2X(arrayOfInt3[0]));
    }

    private static native int CurveCountPointsInPath(int paramInt1,
                                                     byte[] paramArrayOfByte,
                                                     int paramInt2,
                                                     int[] paramArrayOfInt);

    private static native int CurveSetPathPoint(int paramInt1,
                                                byte[] paramArrayOfByte1,
                                                int paramInt2,
                                                int paramInt3,
                                                byte[] paramArrayOfByte2,
                                                byte paramByte);

    private static native int CurveGetLength(int paramInt1, byte[] paramArrayOfByte, int paramInt2, long[] paramArrayOfLong);

    private static native int CurveGetPathPoint(int paramInt1,
                                                byte[] paramArrayOfByte1,
                                                int paramInt2,
                                                int paramInt3,
                                                byte[] paramArrayOfByte2,
                                                byte[] paramArrayOfByte3);

    private static native int CurveLengthToPoint(int paramInt1,
                                                 byte[] paramArrayOfByte1,
                                                 int paramInt2,
                                                 int paramInt3,
                                                 byte[] paramArrayOfByte2,
                                                 byte[] paramArrayOfByte3);

    private static native int CurveGetNearestPathPoint(int paramInt,
                                                       byte[] paramArrayOfByte1,
                                                       byte[] paramArrayOfByte2,
                                                       int[] paramArrayOfInt1,
                                                       int[] paramArrayOfInt2,
                                                       int[] paramArrayOfInt3);

    private static native int CurvePathPointToLength(int paramInt1,
                                                     byte[] paramArrayOfByte1,
                                                     int paramInt2,
                                                     int paramInt3,
                                                     byte[] paramArrayOfByte2,
                                                     int[] paramArrayOfInt);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/image
 * /GXPaths.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
