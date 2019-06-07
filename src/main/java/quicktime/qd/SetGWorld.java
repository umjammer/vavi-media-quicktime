package quicktime.qd;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTObject;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;

/**
 * Deprecated.
 * This class is used to make the incoming QDGraphics the current QDGraphics for drawing.
 * This class is deprecated as some internal issues can potentially create problems for
 * maintaining the set port. See QDGraphics.beginDrawing for the preferred way to do this.
 */
public final class SetGWorld implements QuickTimeLib {

    private static Object linkage;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(SetGWorld.class);
                    }
                });
            }
        }.establish();
    }

    /**
     * Deprecated.
     * QuickDraw uses a global "current graphics context". Calling this call
     * will save the previously current graphics context and then make the incoming
     * QDGraphics the current context. You should call reset after you have finished with
     * any drawing or setting of port operations. The incoming QDGraphics may NOT be null.
     * QuickDraw::SetGWorld()
     * @see reset
     */
    public SetGWorld(QDGraphics paramQDGraphics) throws QDException {
        this(paramQDGraphics, null);
    }

    /**
     * Deprecated.
     * QuickDraw uses a global "current graphics context". Calling this call
     * will save the previously current graphics context and then make the incoming
     * QDGraphics the current context. You should call reset after you have finished with
     * any drawing or setting of port operations. The incoming QDGraphics may NOT be null.
     * @see reset
     */
    public SetGWorld(QDGraphics paramQDGraphics, GDevice paramGDevice) throws QDException {
        int i = QTObject.ID(paramQDGraphics);
        if (i == 0)
            throw new QDException(-50);
        if (i == -1)
            return;
        GetGWorld(this.savedPort, this.savedDevice);
        SetGWorld(i, QTObject.ID(paramGDevice));
    }

    private int[] savedPort = { 0 };

    private int[] savedDevice = { 0 };

    /**
     * Deprecated.
     * Quickdraw uses a global "current graphics context". This call should be called
     * after you have set the context to a specific QDGraphics to reset it to the previously
     * current one.
     * @see set
     */
    public final void reset() {
        if (this.savedPort[0] != -1) {
            SetGWorld(this.savedPort[0], this.savedDevice[0]);
            this.savedPort[0] = -1;
        }
    }

    private static native void GetGWorld(int[] paramArrayOfInt1, int[] paramArrayOfInt2);

    private static native void SetGWorld(int paramInt1, int paramInt2);
}
