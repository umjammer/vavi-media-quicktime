
package quicktime.util;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QTNative;


public class IntEncodedImage implements PrimitivesLib, EncodedImage, Cloneable {
    static Object linkage;

    private int[] ints;

    private int rowBytes;

    static {
        class PrivelegedAction {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(IntEncodedImage.class);
                    }
                });
            }
        }
        new PrivelegedAction().establish();
    }

    public static IntEncodedImage fromIntArray(int[] paramArrayOfInt) {
        return new IntEncodedImage(paramArrayOfInt);
    }

    public static IntEncodedImage fromIntArray(int[] paramArrayOfInt, int paramInt) {
        return new IntEncodedImage(paramArrayOfInt, paramInt);
    }

    public IntEncodedImage(RawEncodedImage paramRawEncodedImage) {
        int i = paramRawEncodedImage.getSize();
        if (i % 4 != 0)
            i++;
        this.ints = new int[i];
        paramRawEncodedImage.copyToArray(0, this.ints, 0, paramRawEncodedImage.getSize());
        this.rowBytes = paramRawEncodedImage.getRowBytes();
    }

    public IntEncodedImage(int paramInt) {
        this(paramInt, -1);
    }

    public IntEncodedImage(int paramInt1, int paramInt2) {
        this.ints = new int[paramInt1];
        paramInt2 = paramInt2;
    }

    private IntEncodedImage(int[] paramArrayOfInt) {
        this(paramArrayOfInt, -1);
    }

    private IntEncodedImage(int[] paramArrayOfInt, int paramInt) {
        this.ints = paramArrayOfInt;
        this.rowBytes = paramInt;
    }

    public int[] getInts() {
        return this.ints;
    }

    public int getSize() {
        return this.ints.length * 4;
    }

    public byte getByte(int paramInt) {
        if ((paramInt > getSize()) || (paramInt < 0))
            throw new ArrayIndexOutOfBoundsException();
        return getByteFromArray(this.ints, paramInt);
    }

    public short getShort(int paramInt) {
        if ((paramInt > getSize() - 1) || (paramInt < 0))
            throw new ArrayIndexOutOfBoundsException();
        return getShortFromArray(this.ints, paramInt);
    }

    public int getInt(int paramInt) {
        if ((paramInt > getSize() - 3) || (paramInt < 0))
            throw new ArrayIndexOutOfBoundsException();
        return getIntFromArray(this.ints, paramInt);
    }

    public int getRowBytes() {
        return this.rowBytes;
    }

    public boolean equals(Object paramObject) {
        if (this == paramObject)
            return true;
        if ((paramObject != null) && ((paramObject instanceof IntEncodedImage))) {
            int[] arrayOfInt = ((IntEncodedImage) paramObject).ints;
            if (arrayOfInt.length != this.ints.length)
                return false;
            for (int i = 0; i < this.ints.length; i++)
                if (this.ints[i] != arrayOfInt[i])
                    return false;
            return true;
        }
        return false;
    }

    public Object clone() {
        int[] arrayOfInt = new int[this.ints.length];
        System.arraycopy(this.ints, 0, arrayOfInt, 0, this.ints.length);
        return new IntEncodedImage(arrayOfInt, this.rowBytes);
    }

    public String toString() {
        return getClass().getName();
    }

    private static native byte getByteFromArray(int[] paramArrayOfInt, int paramInt);

    private static native short getShortFromArray(int[] paramArrayOfInt, int paramInt);

    private static native int getIntFromArray(int[] paramArrayOfInt, int paramInt);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/util/
 * IntEncodedImage.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
