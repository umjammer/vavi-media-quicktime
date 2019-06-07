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

/**
 * The GXPaths class allows you to group multiple path contours together.
 * You use this class when specifying the geometry of a path shape.
 */
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
        for (int j = 0; j < paramArrayOfGXPath.length; j++) i += paramArrayOfGXPath[j].getSize();
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

    /**
     * Returns the no. of contours in the Paths object
     * @return the total number of coutours
     */
    public int getContours() {
        return getIntAt(0);
    }

    /**
     * This method is used to count the points along either one of a path's contours
     * or all of its contours.
     * QuickTime::CurveCountPointsInPath
     * @param contourIndex the index of the contour to be counted.
     * @return the number of points in the contour or path.
     * @return {@link quicktime.std.StdQTException}
     */
    public int countPointsInPath(int paramInt) throws StdQTException {
        int[] arrayOfInt = { 0 };
        int i = CurveCountPointsInPath(QTObject.ID(this.comp), getBytes(), paramInt, arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * This method is best for adding a single point to a path rather than large numbers of points to a path.
     * QuickTime::CurveSetPathPoint
     * @param contourIndex Specifies the index of the path contour to which to add the point.
     * @param pointIndex Specifies the index of the point to add.
     * @param ptIsOnPath If TRUE , specifies that the new point is to be on the path.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setPathPoint(int paramInt1, int paramInt2, QDPoint paramQDPoint, boolean paramBoolean) throws StdQTException {
        int i = CurveSetPathPoint(QTObject.ID(this.comp), getBytes(), paramInt1, paramInt2, paramQDPoint.getGXPoint(), (byte) (paramBoolean ? 1 : 0));
        StdQTException.checkError(i);
    }

    /**
     * This method is used to get a point from a path and to find out if the point is on the curve.
     * QuickTime::CurveGetPathPoint
     * @param contourIndex Specifies the index of the contour from which to get the point.
     * @param pointIndex Specifies the index of the point to get.
     * @return thePoint field that is to receive the point.
     * @return {@link quicktime.std.StdQTException}
     */
    public PathPointInfo getPathPoint(int paramInt1, int paramInt2) throws StdQTException {
        QDPoint localQDPoint = new QDPoint(0.0F, 0.0F);
        byte[] arrayOfByte = new byte[1];
        int i = CurveGetPathPoint(QTObject.ID(this.comp), getBytes(), paramInt1, paramInt2, localQDPoint.getGXPoint(), arrayOfByte);
        StdQTException.checkError(i);
        return new PathPointInfo(localQDPoint, arrayOfByte[0] != 0);
    }

    public QDPoint[] lengthToPoint(int paramInt1, int paramInt2) throws StdQTException {
        QDPoint localQDPoint1 = new QDPoint(0.0F, 0.0F);
        QDPoint localQDPoint2 = new QDPoint(0.0F, 0.0F);
        int i = CurveLengthToPoint(QTObject.ID(this.comp), getBytes(), paramInt1, paramInt2, localQDPoint1.getGXPoint(), localQDPoint2.getGXPoint());
        StdQTException.checkError(i);
        QDPoint[] arrayOfQDPoint = new QDPoint[2];
        arrayOfQDPoint[0] = localQDPoint1;
        arrayOfQDPoint[1] = localQDPoint2;
        return arrayOfQDPoint;
    }

    /**
     * This method is used to calculate the length of one of a path's contours or the
     * sum of the lengths of all of its contours.
     * QuickTime::CurveGetLength
     * @param index Contains the index of the contour whose length is to be calculated or, if the value is 0 ,
     *  specifies to calculate the lengths of all of the path's contours and return the sum of the lengths.
     * @return wideLength Contains the length.
     * @return {@link quicktime.std.StdQTException}
     */
    public long getLength(int paramInt) throws StdQTException {
        long[] arrayOfLong = { 0L };
        int i = CurveGetLength(QTObject.ID(this.comp), getBytes(), paramInt, arrayOfLong);
        StdQTException.checkError(i);
        return arrayOfLong[0];
    }

    /**
     * This method is used to to get the length of a path between specified starting
     * and ending distances that is nearest a point.
     * QuickTime::CurvePathPointToLength
     * @param startDist Specifies the distance along the path at which to start measuring the path's length.
     * @param endDist Specifies the distance along the path at which to stop measuring the path's length.
     * @param thePoint Contains a pointer to a point; the function measures the path closest to this point.
     * @return the length of the specified part of the path.
     * @return {@link quicktime.std.StdQTException}
     */
    public float getPathPointToLength(int paramInt1, int paramInt2, QDPoint paramQDPoint) throws StdQTException {
        int[] arrayOfInt = { 0 };
        int i = CurvePathPointToLength(QTObject.ID(this.comp), getBytes(), paramInt1, paramInt2, paramQDPoint.getGXPoint(), arrayOfInt);
        StdQTException.checkError(i);
        return QTUtils.Fix2X(arrayOfInt[0]);
    }

    /**
     * This method is used to to find the closest point on a path to a specified point.
     * In programs where users directly manipulate curves, you can use this function
     * to determine the closest control point to a given point.
     * QuickTime::CurveGetNearestPathPoint
     * @param thePoint Contains a point for which to find the closest point on the path.
     * @return information about the specified point
     * @return {@link quicktime.std.StdQTException}
     */
    public NearestPointInfo getNearestPathPoint(QDPoint paramQDPoint) throws StdQTException {
        int[] arrayOfInt1 = { 0 };
        int[] arrayOfInt2 = { 0 };
        int[] arrayOfInt3 = { 0 };
        int i = CurveGetNearestPathPoint(QTObject.ID(this.comp), getBytes(), paramQDPoint.getGXPoint(), arrayOfInt1, arrayOfInt2, arrayOfInt3);
        StdQTException.checkError(i);
        return new NearestPointInfo(arrayOfInt1[0], arrayOfInt2[0], QTUtils.Fix2X(arrayOfInt3[0]));
    }

    private static native int CurveCountPointsInPath(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int[] paramArrayOfInt);

    private static native int CurveSetPathPoint(int paramInt1, byte[] paramArrayOfByte1, int paramInt2, int paramInt3, byte[] paramArrayOfByte2, byte paramByte);

    private static native int CurveGetLength(int paramInt1, byte[] paramArrayOfByte, int paramInt2, long[] paramArrayOfLong);

    private static native int CurveGetPathPoint(int paramInt1, byte[] paramArrayOfByte1, int paramInt2, int paramInt3, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3);

    private static native int CurveLengthToPoint(int paramInt1, byte[] paramArrayOfByte1, int paramInt2, int paramInt3, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3);

    private static native int CurveGetNearestPathPoint(int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3);

    private static native int CurvePathPointToLength(int paramInt1, byte[] paramArrayOfByte1, int paramInt2, int paramInt3, byte[] paramArrayOfByte2, int[] paramArrayOfInt);
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/image
 * /GXPaths.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
