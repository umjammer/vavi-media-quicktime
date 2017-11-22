
package quicktime.qd;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.std.image.GraphicsExporter;
import quicktime.std.movies.Track;
import quicktime.util.QTHandleRef;
import quicktime.util.RawEncodedImage;


public final class PixMap extends QTHandleRef implements QuickTimeLib {
    private static Object linkage;

    public static final int kNativeSize = 50;

    private boolean isOff;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(PixMap.class);
                    }
                });
            }
        }.establish();
    }

    public static PixMap fromGraphicsExporter(GraphicsExporter paramGraphicsExporter) {
        int[] arrayOfInt = {
            0
        };
        int i = GraphicsExportGetInputPixmap(QTObject.ID(paramGraphicsExporter), arrayOfInt);
        return new PixMap(arrayOfInt[0], paramGraphicsExporter, false);
    }

    public static PixMap fromQDGraphics(QDGraphics paramQDGraphics) {
        return new PixMap(GetGWorldPixMap(QTObject.ID(paramQDGraphics)), paramQDGraphics, paramQDGraphics.isOffscreen());
    }

    public static PixMap fromGDevice(GDevice paramGDevice) {
        return new PixMap(getIntFromHandle(QTObject.ID(paramGDevice), 22), paramGDevice, !paramGDevice.testAttribute(13));
    }

    public static PixMap fromTrack(Track paramTrack) throws StdQTException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = GetTrackMatte(QTObject.ID(paramTrack));
            StdQTException.checkError(GetMoviesError());
        }
        if (i == 0)
            return null;
        return new PixMap(i, null, true);
    }

    private PixMap(int paramInt, Object paramObject, boolean paramBoolean) {
        super(paramInt, paramObject, true);
        doLock();
        this.isOff = paramBoolean;
    }

    public boolean isOffscreen() {
        return this.isOff;
    }

    public int getRowBytes() {
        return getShortAt(4) & 0x3FFF;
    }

    public int getPixelSize() {
        return getShortAt(32);
    }

    public int getPixelFormat() {
        int i = getIntAt(38);
        return i != 0 ? i : getPixelSize();
    }

    public RawEncodedImage getPixelData() {
        return RawEncodedImage.fromPixMap(this);
    }

    public QDRect getBounds() {
        if (QTSession.isCurrentOS(4)) {
            Object localObject = new QDRect();
            GetPixBounds(_ID(), ((QDRect) localObject).getBytes());
            return (QDRect) localObject;
        }
        Object localObject = new byte[8];
        getBytesAt(6, 8, (byte[]) localObject, 0);
        return QDRect.fromArray((byte[]) localObject, 8);
    }

    public ColorTable getColorTable() {
        return ColorTable.fromPixMap(this);
    }

    protected void doLock() {
        LockPixels(_ID());
    }

    protected void doUnlock() {
        UnlockPixels(_ID());
    }

    public String toString() {
        return super.toString() + "[Bounds=" + getBounds() + "]";
    }

    private static native int GraphicsExportGetInputPixmap(int paramInt, int[] paramArrayOfInt);

    private static native int GetGWorldPixMap(int paramInt);

    private static native byte LockPixels(int paramInt);

    private static native void UnlockPixels(int paramInt);

    private static native int GetTrackMatte(int paramInt);

    private static native short GetMoviesError();

    private static native int getIntFromHandle(int paramInt1, int paramInt2);

    private static native int GetPixBounds(int paramInt, byte[] paramArrayOfByte);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/qd/PixMap
 * .class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
