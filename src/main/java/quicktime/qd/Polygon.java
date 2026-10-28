package quicktime.qd;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.internal.toolbox.QuickDrawManager;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.util.QTHandleRef;
import quicktime.util.UtilException;


/**
 * The Polygon represents an arbitary area or set of areas on the drawing coordinate plane.
 * Methods are implemented for a corresponding QuickDraw structure used by QuickTime.
 * refer to Polygon in QuickDraw
 */
public final class Polygon extends QTHandleRef implements QuickTimeLib, Cloneable {

    private static final int[] savedPort = {0};

    private static final int[] savedDevice = {0};

    private static int allocate(QDGraphics paramQDGraphics) throws QTException {
        if (paramQDGraphics == null) {
            throw new StdQTException(-50);
        }
        int i = 0;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(paramQDGraphics), 0);
            i = OpenPoly();
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        if (i == 0)
            throw new UtilException(-108);
        return i;
    }

    /**
     * Allocates a new empty Polygon.
     *
     * @see "QuickDraw::OpenPoly"
     */
    public Polygon(QDGraphics paramQDGraphics) throws QTException {
        super(allocate(paramQDGraphics), null, false);
        if (QTObject.ID(this) == 0)
            QDException.checkError(-108);
        this.gw = paramQDGraphics;
    }

    private Polygon(int paramInt) throws QTException {
        super(paramInt, null, false);
        if (QTObject.ID(this) == 0) {
            QDException.checkError(-108);
        }
    }

    private transient QDGraphics gw = null;

    /**
     * Completes the collection of lines that define the polygon.
     *
     * @see "QuickDraw::ClosePoly"
     */
    public void close() throws QDException {
        if (this.gw != null) {
            synchronized (QTNative.globalsLock) {
                if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                    GetGWorld(savedPort, savedDevice);
                SetGWorld(QTObject.ID(this.gw), 0);
                ClosePoly();
                if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                    SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
        this.gw = null;
    }

    /**
     * Returns size of the Polygon.
     *
     * @return the size of the handle
     */
    @Override
    public int getSize() {
        return getShortAt(0);
    }

    /**
     * Returns a copy of the bounding rectangle of the Polygon.
     *
     * @return a rect
     */
    public QDRect getPolyBBox() {
        byte[] arrayOfByte = new byte[8];
        getBytesAt(2, 8, arrayOfByte, 0);
        return QDRect.fromArray(arrayOfByte, 8);
    }

    /**
     * Returns a copy of the bounding rectangle of the Polygon.
     *
     * @return a rect
     */
    public QDRect getBounds() {
        return getPolyBBox();
    }

    /**
     * Copies the mathematical structure of the implicit Polygon into a newly created Polygon.
     *
     * @return A new Polygon copy.
     * @throws quicktime.QTException
     */
    public Polygon copy() throws QTException {
        return new Polygon(makeAndCopyHandle());
    }

    /**
     * Copies the mathematical structure of the implicit Polygon into a newly created Polygon.
     *
     * @return A new Polygon copy.
     * @see "QuickDraw::Copy"
     */
    @Override
    public Object clone() {
        try {
            return copy();
        } catch (QTException localQTException) {
            throw new QTRuntimeException(localQTException);
        }
    }

    /**
     * Print information about this object.
     *
     * @return a string representing this Polygon object
     */
    public String toString() {
        return super.toString() + "[polyBBox=" + getPolyBBox() + "]";
    }

    /**
     * offset moves the Polygon by adding
     * the value you specify in the dh parameter to the horizontal coordinates of its points,
     * and by adding the value you specify in the dv parameter to the vertical coordinates of
     * all points of its boundary. If the values of dh and dv are positive, the movement
     * is to the right and down; if either is negative, the corresponding movement is in the opposite
     * direction. The Polygon retains its size and shape. This doesnÕt affect the screen unless
     * you subsequently redraw the polygon.
     *
     * @param dh The horizontal distance to move the polygon.
     * @param dv The vertical distance to move the polygon.
     * @throws quicktime.qd.QDException
     * @see "QuickDraw::OffsetPoly()"
     */
    public void offset(int dh, int dv) throws QDException {
        OffsetPoly(_ID(), (short) dh, (short) dv);
    }

    private static int OpenPoly() {
        return QuickDrawManager.OpenPoly();
    }

    private static void ClosePoly() {
        QuickDrawManager.ClosePoly();
    }

    private static void OffsetPoly(int paramInt, short paramShort1, short paramShort2) {
        QuickDrawManager.OffsetPoly(paramInt, paramShort1, paramShort2);
    }

    private static void SetGWorld(int paramInt1, int paramInt2) {
        QuickDrawManager.SetGWorld(paramInt1, paramInt2);
    }

    private static void GetGWorld(int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        QuickDrawManager.GetGWorld(paramArrayOfInt1, paramArrayOfInt2);
    }
}
