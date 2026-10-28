package quicktime.qd;

import quicktime.QTObject;
import quicktime.internal.toolbox.QuickDrawManager;
import quicktime.jdirect.QuickTimeLib;


/**
 * @deprecated This class is used to make the incoming QDGraphics the current QDGraphics for drawing.
 * This class is deprecated as some internal issues can potentially create problems for
 * maintaining the set port. See QDGraphics.beginDrawing for the preferred way to do this.
 */
public final class SetGWorld implements QuickTimeLib {

    /**
     * QuickDraw uses a global "current graphics context". Calling this call
     * will save the previously current graphics context and then make the incoming
     * QDGraphics the current context. You should call reset after you have finished with
     * any drawing or setting of port operations. The incoming QDGraphics may NOT be null.
     * @see "QuickDraw::SetGWorld()"
     *
     * @see #reset
     * @deprecated
     */
    public SetGWorld(QDGraphics paramQDGraphics) throws QDException {
        this(paramQDGraphics, null);
    }

    /**
     * QuickDraw uses a global "current graphics context". Calling this call
     * will save the previously current graphics context and then make the incoming
     * QDGraphics the current context. You should call reset after you have finished with
     * any drawing or setting of port operations. The incoming QDGraphics may NOT be null.
     *
     * @see #reset
     * @deprecated
     */
    public SetGWorld(QDGraphics paramQDGraphics, GDevice paramGDevice) throws QDException {
        int i = QTObject.ID(paramQDGraphics);
        if (i == 0)
            throw new QDException(-50);
        if (i == -1)
            return;
        getGWorld(this.savedPort, this.savedDevice);
        setGWorld(i, QTObject.ID(paramGDevice));
    }

    private final int[] savedPort = {0};

    private final int[] savedDevice = {0};

    /**
     * Quickdraw uses a global "current graphics context". This call should be called
     * after you have set the context to a specific QDGraphics to reset it to the previously
     * current one.
     *
     * @see "set"
     * @deprecated
     */
    public void reset() {
        if (this.savedPort[0] != -1) {
            setGWorld(this.savedPort[0], this.savedDevice[0]);
            this.savedPort[0] = -1;
        }
    }

    private static void getGWorld(int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        QuickDrawManager.GetGWorld(paramArrayOfInt1, paramArrayOfInt2);
    }

    private static void setGWorld(int paramInt1, int paramInt2) {
        QuickDrawManager.SetGWorld(paramInt1, paramInt2);
    }
}
