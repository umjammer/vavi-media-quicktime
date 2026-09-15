package quicktime.qd;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QTNative;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;
import quicktime.util.QTUtils;

/**
 * Provides information about a picture image relating to the source rectangle,
 * horizontal and vertical resolution of the image. The OpenCPicParams
 * implements methods for a corresponding QuickDraw structure.
 * Its methods offer access to the structure and its fields.
 */
public final class OpenCPicParams extends QTByteObject implements PrimitivesLib, Cloneable {

    private static Object linkage;

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 24;

    static final long serialVersionUID = -5429124066722306588L;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(OpenCPicParams.class);
                    }
                });
            }
        }.establish();
    }

    /**
     * The OpenCPicParams as a new object.
     * @param hRes the horizotal resolution e.g 72 dpi.
     * @param vRes the vertical resolution e.g 72 dpi
     */
    public OpenCPicParams(QDRect paramQDRect, float paramFloat1, float paramFloat2) {
        super(24);
        _setSrcRect(paramQDRect);
        _setHRes(paramFloat1);
        _setVRes(paramFloat2);
        setShortAt(16, (short) -2);
    }

    /**
     * The OpenCPicParams as a new object.
     */
    public OpenCPicParams(QDRect paramQDRect) {
        this(paramQDRect, 72.0F, 72.0F);
    }

    private OpenCPicParams(byte[] paramArrayOfByte) {
        super(paramArrayOfByte);
    }

    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[24];
        paramObjectInputStream.read(this.bytes);
        for (int i = 0; i < 8; i += 2) {
            setShortInArray(getBytes(), i, EndianOrder.flipBigEndianToNative16(getShortFromArray(this.bytes, i)));
        }
        for (int i = 8; i < 16; i += 4) {
            setIntInArray(getBytes(), i, EndianOrder.flipBigEndianToNative32(getIntFromArray(this.bytes, i + 8)));
        }
        setShortInArray(getBytes(), 16, EndianOrder.flipBigEndianToNative16(getShortFromArray(this.bytes, 16)));
    }

    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        byte[] arrayOfByte = new byte[24];
        for (int i = 0; i < 8; i += 2) {
            setShortInArray(arrayOfByte, i, EndianOrder.flipNativeToBigEndian16(getShortFromArray(getBytes(), i)));
        }
        for (int i = 8; i < 16; i += 4) {
            setIntInArray(arrayOfByte, i, EndianOrder.flipNativeToBigEndian32(getIntFromArray(getBytes(), i)));
        }
        setShortInArray(arrayOfByte, 16, EndianOrder.flipNativeToBigEndian16(getShortFromArray(getBytes(), 16)));
        paramObjectOutputStream.write(arrayOfByte);
    }

    /**
     * Gets the bounding rectangle of the picture.
     * @return The bounding rectangle.
     */
    public QDRect getSrcRect() {
        byte[] arrayOfByte = new byte[8];
        getBytesAt(0, 8, arrayOfByte, 0);
        return QDRect.fromArray(arrayOfByte, 8);
    }

    /**
     * Gets the horizontal resolution.
     * @return The horizontal resolution of the picture.
     */
    public float getHRes() {
        return QTUtils.Fix2X(getIntAt(8));
    }

    /**
     * Gets the vertical resolution.
     * @return The vertical resolution of the picture.
     */
    public float getVRes() {
        return QTUtils.Fix2X(getIntAt(12));
    }

    private void _setSrcRect(QDRect paramQDRect) {
        setBytesAt(0, 8, paramQDRect.getBytes(), 0);
    }

    private void _setHRes(float paramFloat) {
        setIntAt(8, QTUtils.X2Fix(paramFloat));
    }

    private void _setVRes(float paramFloat) {
        setIntAt(12, QTUtils.X2Fix(paramFloat));
    }

    /**
     * Return a copy of the OpenCPicParams.
     */
    public Object clone() {
        return new OpenCPicParams(this.bytes);
    }

    /**
     * Print information about this object.
     * @return a string representing this object
     */
    public String toString() {
        return getClass().getName() + "[Bounds=" + getSrcRect() + "]";
    }

    private static native int getIntFromArray(byte[] paramArrayOfByte, int paramInt);

    private static native void setIntInArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2);

    private static native short getShortFromArray(byte[] paramArrayOfByte, int paramInt);

    private static native void setShortInArray(byte[] paramArrayOfByte, int paramInt, short paramShort);
}

