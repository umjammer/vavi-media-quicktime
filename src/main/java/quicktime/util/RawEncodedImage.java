
package quicktime.util;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.PixMap;
import quicktime.std.StdQTException;
import quicktime.std.anim.Sprite;


public final class RawEncodedImage extends QTPointer implements QuickTimeLib, EncodedImage {
    private static Object linkage;

    private int rowBytes;

    static {
        class PrivelegedAction {
            void establish() {
                AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(RawEncodedImage.class);
                    }
                });
            }
        }
        new PrivelegedAction().establish();
    }

    public static RawEncodedImage fromQTPointer(QTPointerRef paramQTPointerRef) {
        return new RawEncodedImage(QTObject.ID(paramQTPointerRef), paramQTPointerRef.getSize(), paramQTPointerRef, -1);
    }

    public static RawEncodedImage fromQTHandle(QTHandleRef paramQTHandleRef) {
        paramQTHandleRef.lock();
        return fromQTPointer(paramQTHandleRef.toQTPointer());
    }

    public static RawEncodedImage fromPixMap(PixMap paramPixMap) {
        int i = getShortFromHandle(QTObject.ID(paramPixMap), 10) - getShortFromHandle(QTObject.ID(paramPixMap), 6);
        int j = getShortFromHandle(QTObject.ID(paramPixMap), 4) & 0x3FFF;
        int k = i * j;
        int m = paramPixMap.isOffscreen() ? GetPixBaseAddr(QTObject.ID(paramPixMap))
                                          : getIntFromHandle(QTObject.ID(paramPixMap), 0);
        return new RawEncodedImage(m, k, paramPixMap, j);
    }

    public static RawEncodedImage fromSprite(Sprite paramSprite) throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        int i = GetSpriteProperty(QTObject.ID(paramSprite), 3, arrayOfInt);
        StdQTException.checkError(i);
        return new RawEncodedImage(arrayOfInt[0], paramSprite.getImageDataSize(), paramSprite, -1);
    }

    public RawEncodedImage(byte[] paramArrayOfByte) throws QTException {
        super(paramArrayOfByte);
        this.rowBytes = -1;
    }

    public RawEncodedImage(int[] paramArrayOfInt) throws QTException {
        this(paramArrayOfInt.length * 4, false);
        copyFromArray(0, paramArrayOfInt, 0, paramArrayOfInt.length);
    }

    public RawEncodedImage(int[] paramArrayOfInt, int paramInt) throws QTException {
        this(paramArrayOfInt.length * 4, false, paramInt);
        copyFromArray(0, paramArrayOfInt, 0, paramArrayOfInt.length);
    }

    public RawEncodedImage(int paramInt, boolean paramBoolean) throws QTException {
        super(paramInt, paramBoolean);
        this.rowBytes = -1;
    }

    public RawEncodedImage(int paramInt1, boolean paramBoolean, int paramInt2) throws QTException {
        super(paramInt1, paramBoolean);
        this.rowBytes = paramInt2;
    }

    private RawEncodedImage(int paramInt1, int paramInt2, Object paramObject, int paramInt3) {
        super(paramInt1, paramInt2, paramObject);
        this.rowBytes = paramInt3;
    }

    public Object clone() {
        return new RawEncodedImage(makeAndCopyPtr(0, getSize()), getSize(), null, this.rowBytes);
    }

    public byte getByte(int paramInt) {
        if ((paramInt > getSize()) || (paramInt < 0))
            throw new ArrayIndexOutOfBoundsException();
        return getByteAt(paramInt);
    }

    public void setByte(int paramInt, byte paramByte) {
        if ((paramInt > getSize()) || (paramInt < 0))
            throw new ArrayIndexOutOfBoundsException();
        setByteAt(paramInt, paramByte);
    }

    public short getShort(int paramInt) {
        if ((paramInt > getSize() - 1) || (paramInt < 0))
            throw new ArrayIndexOutOfBoundsException();
        return getShortAt(paramInt);
    }

    public void setShort(int paramInt, short paramShort) {
        if ((paramInt > getSize()) || (paramInt < 0))
            throw new ArrayIndexOutOfBoundsException();
        setShortAt(paramInt, paramShort);
    }

    public int getInt(int paramInt) {
        if ((paramInt > getSize() - 3) || (paramInt < 0))
            throw new ArrayIndexOutOfBoundsException();
        return getIntAt(paramInt);
    }

    public void setInt(int paramInt1, int paramInt2) {
        if ((paramInt1 > getSize()) || (paramInt1 < 0))
            throw new ArrayIndexOutOfBoundsException();
        setIntAt(paramInt1, paramInt2);
    }

    public void setInts(int paramInt, int[] paramArrayOfInt) {
        if ((paramInt + paramArrayOfInt.length * 4 > getSize()) || (paramInt < 0))
            throw new ArrayIndexOutOfBoundsException();
        copyArrayToPointer(paramArrayOfInt, 0, _ID(), paramInt, paramArrayOfInt.length * 4);
    }

    public int getRowBytes() {
        return this.rowBytes;
    }

    public void setRowBytes(int paramInt) {
        this.rowBytes = paramInt;
    }

    private static native int GetPixBaseAddr(int paramInt);

    private static native short GetSpriteProperty(int paramInt1, int paramInt2, int[] paramArrayOfInt);

    private static native short getShortFromHandle(int paramInt1, int paramInt2);

    private static native int getIntFromHandle(int paramInt1, int paramInt2);

    private static native void copyArrayToPointer(int[] paramArrayOfInt,
                                                  int paramInt1,
                                                  int paramInt2,
                                                  int paramInt3,
                                                  int paramInt4);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/util/
 * RawEncodedImage.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
