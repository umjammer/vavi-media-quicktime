package quicktime.std.image;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTObject;
import quicktime.internal.toolbox.ImageCompressionManager;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.QDPoint;
import quicktime.std.StdQTException;
import quicktime.util.QTHandle;

/**
 * You use the CurvePath to specify a single contour composed of
 * straight lines and curves.
 */
public final class CurvePath extends QTHandle implements QuickTimeLib {

    private static Object linkage;

    private Curve c;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(CurvePath.class);
                    }
                });
            }
        }.establish();
    }

    private static int allocate(Curve paramCurve) throws StdQTException {
        int[] arrayOfInt = { 0 };
        int i = CurveNewPath(QTObject.ID(paramCurve), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * This method is used to create a new path.
     * QuickTime::CurveNewPath
     */
    public CurvePath(Curve paramCurve) throws StdQTException {
        super(allocate(paramCurve), null, false);
        this.c = paramCurve;
    }

    /**
     * This method is best for adding a single point to a path rather than large numbers of points to a path.
     * QuickTime::CurveInsertPointIntoPath
     * @param aPoint the point to add to the path.
     * @param contourIndex Specifies the index of the path contour to which to add the point.
     * @param pointIndex Specifies the index of the point to add.
     * @param ptIsOnPath If TRUE , specifies that the new point is to be on the path.
     * @param {@link quicktime.std.StdQTException}
     */
    public void insertPoint(QDPoint paramQDPoint, int paramInt1, int paramInt2, boolean paramBoolean) throws StdQTException {
        int i = CurveInsertPointIntoPath(QTObject.ID(this.c), paramQDPoint.getGXPoint(), _ID(), paramInt1, paramInt2, (byte) (paramBoolean == true ? 1 : 0));
        StdQTException.checkError(i);
    }

    private static int CurveNewPath(int paramInt, int[] paramArrayOfInt) {
        return ImageCompressionManager.CurveNewPath(paramInt, paramArrayOfInt);
    }

    private static int CurveInsertPointIntoPath(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3, int paramInt4, byte paramByte) {
        return ImageCompressionManager.CurveInsertPointIntoPath(paramInt1, paramArrayOfByte, paramInt2, paramInt3, paramInt4, paramByte);
    }
}
