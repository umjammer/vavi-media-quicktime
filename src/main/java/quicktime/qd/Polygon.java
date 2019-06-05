
package quicktime.qd;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.util.QTHandleRef;
import quicktime.util.UtilException;


public final class Polygon extends QTHandleRef implements QuickTimeLib, Cloneable {
    private static Object linkage;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(Polygon.class);
                    }
                });
            }
        }.establish();
    }

    private static final int[] savedPort = {
        0
    };

    private static final int[] savedDevice = {
        0
    };

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

    public int getSize() {
        return getShortAt(0);
    }

    public QDRect getPolyBBox() {
        byte[] arrayOfByte = new byte[8];
        getBytesAt(2, 8, arrayOfByte, 0);
        return QDRect.fromArray(arrayOfByte, 8);
    }

    public QDRect getBounds() {
        return getPolyBBox();
    }

    public Polygon copy() throws QTException {
        return new Polygon(makeAndCopyHandle());
    }

    public Object clone() {
        try {
            return copy();
        } catch (QTException localQTException) {
            throw new QTRuntimeException(localQTException);
        }
    }

    public String toString() {
        return super.toString() + "[polyBBox=" + getPolyBBox() + "]";
    }

    public void offset(int paramInt1, int paramInt2) throws QDException {
        OffsetPoly(_ID(), (short) paramInt1, (short) paramInt2);
    }

    private static native int OpenPoly();

    private static native void ClosePoly();

    private static native void OffsetPoly(int paramInt, short paramShort1, short paramShort2);

    private static native void SetGWorld(int paramInt1, int paramInt2);

    private static native void GetGWorld(int[] paramArrayOfInt1, int[] paramArrayOfInt2);
}
