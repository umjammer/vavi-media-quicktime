
package quicktime.std.movies;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTObject;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.std.image.Curve;
import quicktime.std.image.VectorStream;
import quicktime.util.QTPointerRef;
import quicktime.util.UtilException;


public final class AtomData extends QTPointerRef implements QuickTimeLib {
    private static Object linkage;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(AtomData.class);
                    }
                });
            }
        }.establish();
    }

    public static AtomData fromVectorStream(Curve paramCurve,
                                            VectorStream paramVectorStream,
                                            int paramInt) throws StdQTException {
        int[] arrayOfInt1 = {
            0
        };
        int[] arrayOfInt2 = {
            0
        };

        paramVectorStream.lock();
        int i = CurveGetAtomDataFromVectorStream(QTObject.ID(paramCurve),
                                                 QTObject.ID(paramVectorStream),
                                                 paramInt,
                                                 arrayOfInt1,
                                                 arrayOfInt2);
        StdQTException.checkError(i);
        paramVectorStream.unlock();
        return new AtomData(arrayOfInt2[0], arrayOfInt1[0], paramCurve);
    }

    AtomData(int paramInt1, int paramInt2, Object paramObject) {
        super(paramInt1, paramInt2, paramObject);
    }

    public byte getByte(int paramInt) throws IndexOutOfBoundsException {
        checkOffset(paramInt);
        return getByteAt(paramInt);
    }

    public short getShort(int paramInt) throws IndexOutOfBoundsException {
        checkOffset(paramInt);
        return getShortAt(paramInt);
    }

    public int getInt(int paramInt) throws IndexOutOfBoundsException {
        checkOffset(paramInt);
        return getIntAt(paramInt);
    }

    public long getLong(int paramInt) throws IndexOutOfBoundsException {
        checkOffset(paramInt);
        return getLongAt(paramInt);
    }

    public float getFloat(int paramInt) throws IndexOutOfBoundsException {
        checkOffset(paramInt);
        return getFloatAt(paramInt);
    }

    public double getDouble(int paramInt) throws IndexOutOfBoundsException {
        checkOffset(paramInt);
        return getDoubleAt(paramInt);
    }

    public String getCString(int paramInt) throws UtilException, IndexOutOfBoundsException {
        checkOffset(paramInt);
        return getCStringAt(paramInt);
    }

    public String getPString(int paramInt) throws UtilException, IndexOutOfBoundsException {
        checkOffset(paramInt);
        return getPStringAt(paramInt);
    }

    public byte[] getBytes(int paramInt1, int paramInt2) throws IndexOutOfBoundsException {
        checkOffset(paramInt1);
        int i = getSize() - paramInt1;
        int j = i > paramInt2 ? paramInt2 : paramInt2 == 0 ? i : i;
        byte[] arrayOfByte = new byte[j];
        getBytesAt(paramInt1, j, arrayOfByte, 0);
        return arrayOfByte;
    }

    private final void checkOffset(int paramInt) throws IndexOutOfBoundsException {
        if (paramInt > getSize())
            throw new IndexOutOfBoundsException();
    }

    private static native int CurveGetAtomDataFromVectorStream(int paramInt1,
                                                               int paramInt2,
                                                               int paramInt3,
                                                               int[] paramArrayOfInt1,
                                                               int[] paramArrayOfInt2);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/AtomData.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
