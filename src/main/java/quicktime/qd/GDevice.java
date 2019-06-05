
package quicktime.qd;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTObject;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.std.sg.SequenceGrabber;
import quicktime.util.QTHandleRef;
import quicktime.util.QTUtils;


public final class GDevice extends QTHandleRef implements QuickTimeLib {
    private static Object linkage;

    private static final int singleDevicesBit = 0;

    private static final int dontMatchSeedsBit = 1;

    private static final int allDevicesBit = 2;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(GDevice.class);
                    }
                });
            }
        }.establish();
    }

    public static GDevice fromSequenceGrabber(SequenceGrabber paramSequenceGrabber) throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int i = SGGetGWorld(QTObject.ID(paramSequenceGrabber), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(i);
        return new GDevice(arrayOfInt2[0]);
    }

    private GDevice(int paramInt) {
        super(paramInt, new Object(), false);
    }

    public static GDevice get() {
        int i = GetGDevice();
        return new GDevice(i);
    }

    public static GDevice getList() {
        int i = GetDeviceList();
        return new GDevice(i);
    }

    public static GDevice getMain() {
        int i = GetMainDevice();
        return new GDevice(i);
    }

    public static GDevice getMax(QDRect paramQDRect) {
        int i = GetMaxDevice(paramQDRect.getRect());
        return new GDevice(i);
    }

    public void init(int paramInt1, int paramInt2) {
        InitGDevice((short) paramInt1, paramInt2, _ID());
    }

    public void setAttribute(int paramInt, boolean paramBoolean) {
        SetDeviceAttribute(_ID(), (short) paramInt, (byte) (paramBoolean ? 1 : 0));
    }

    public void set() {
        SetGDevice(_ID());
    }

    public GDevice getNext() {
        int i = GetNextDevice(_ID());
        return i == 0 ? null : new GDevice(i);
    }

    public boolean testAttribute(int paramInt) {
        int i = TestDeviceAttribute(_ID(), (short) paramInt);
        return i != 0;
    }

    public PixMap getPixMap() {
        return PixMap.fromGDevice(this);
    }

    public QDRect getBounds() {
        byte[] arrayOfByte = new byte[8];
        getBytesAt(34, 8, arrayOfByte, 0);
        return QDRect.fromArray(arrayOfByte, 8);
    }

    public float hasScale(int paramInt, float paramFloat) throws StdQTException {
        int[] arrayOfInt = {
            QTUtils.X2Fix(paramFloat)
        };
        StdQTException.checkError(GDHasScale(_ID(), (short) paramInt, arrayOfInt));
        return QTUtils.Fix2X(arrayOfInt[0]);
    }

    public float getScale() throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        short[] arrayOfShort = {
            0
        };
        StdQTException.checkError(GDGetScale(_ID(), arrayOfInt, arrayOfShort));
        return QTUtils.Fix2X(arrayOfInt[0]);
    }

    public int getFlags() throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        short[] arrayOfShort = {
            0
        };
        StdQTException.checkError(GDGetScale(_ID(), arrayOfInt, arrayOfShort));
        return arrayOfShort[0];
    }

    public void setScale(int paramInt1, int paramInt2) throws StdQTException {
        StdQTException.checkError(GDSetScale(_ID(), QTUtils.X2Fix(paramInt1), (short) paramInt2));
    }

    private static native int SGGetGWorld(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2);

    private static native int GetGDevice();

    private static native int GetDeviceList();

    private static native int GetMainDevice();

    private static native int GetMaxDevice(byte[] paramArrayOfByte);

    private static native void InitGDevice(short paramShort, int paramInt1, int paramInt2);

    private static native void SetDeviceAttribute(int paramInt, short paramShort, byte paramByte);

    private static native void SetGDevice(int paramInt);

    private static native int GetNextDevice(int paramInt);

    private static native int TestDeviceAttribute(int paramInt, short paramShort);

    private static native short GDHasScale(int paramInt, short paramShort, int[] paramArrayOfInt);

    private static native short GDGetScale(int paramInt, int[] paramArrayOfInt, short[] paramArrayOfShort);

    private static native short GDSetScale(int paramInt1, int paramInt2, short paramShort);
}
