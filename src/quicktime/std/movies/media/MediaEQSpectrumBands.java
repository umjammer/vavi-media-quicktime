
package quicktime.std.movies.media;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.jdirect.InterfaceLib;
import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QTNative;
import quicktime.util.QTByteObject;
import quicktime.util.QTUtils;


public final class MediaEQSpectrumBands extends QTByteObject implements PrimitivesLib, InterfaceLib {
    private static Object linkage;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(MediaEQSpectrumBands.class);
                    }
                });
            }
        }.establish();
    }

    public MediaEQSpectrumBands(int paramInt) {
        super(6);
        setShortAt(0, (short) paramInt);
        setIntAt(2, NewPtrClear(paramInt * 4));
    }

    public short getCount() {
        return getShortAt(0);
    }

    public float getFrequency(int paramInt) {
        int i = getIntAt(2);
        return QTUtils.UFix2X(getIntFromPointer(i, paramInt * 4));
    }

    public void setFrequency(int paramInt, float paramFloat) {
        int i = getIntAt(2);
        setIntInPointer(i, paramInt * 4, QTUtils.X2UFix(paramFloat));
    }

    public String toString() {
        String str = getClass().getName() + "[count=" + getCount();
        if (getCount() > 0) {
            str = str + ",frequencies=[";
            for (int i = 0; i < getCount() - 1; i++)
                str = str + getFrequency(i) + ",";
            str = str + getFrequency(getCount() - 1);
            str = str + "]]";
        } else {
            str = str + "]";
        }
        return str;
    }

    protected void finalize() throws Throwable {
        DisposePtr(getIntAt(2));
        super.finalize();
    }

    private static native int getIntFromPointer(int paramInt1, int paramInt2);

    private static native void setIntInPointer(int paramInt1, int paramInt2, int paramInt3);

    private static native int NewPtrClear(int paramInt);

    private static native void DisposePtr(int paramInt);
}
