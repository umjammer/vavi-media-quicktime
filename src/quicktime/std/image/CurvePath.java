
package quicktime.std.image;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTObject;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.QDPoint;
import quicktime.std.StdQTException;
import quicktime.util.QTHandle;


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
        int[] arrayOfInt = {
            0
        };
        int i = CurveNewPath(QTObject.ID(paramCurve), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    public CurvePath(Curve paramCurve) throws StdQTException {
        super(allocate(paramCurve), null, false);
        this.c = paramCurve;
    }

    public void insertPoint(QDPoint paramQDPoint, int paramInt1, int paramInt2, boolean paramBoolean) throws StdQTException {
        int i = CurveInsertPointIntoPath(QTObject.ID(this.c),
                                         paramQDPoint.getGXPoint(),
                                         _ID(),
                                         paramInt1,
                                         paramInt2,
                                         (byte) (paramBoolean == true ? 1 : 0));
        StdQTException.checkError(i);
    }

    private static native int CurveNewPath(int paramInt, int[] paramArrayOfInt);

    private static native int CurveInsertPointIntoPath(int paramInt1,
                                                       byte[] paramArrayOfByte,
                                                       int paramInt2,
                                                       int paramInt3,
                                                       int paramInt4,
                                                       byte paramByte);
}
