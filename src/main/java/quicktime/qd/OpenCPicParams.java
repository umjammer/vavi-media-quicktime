package quicktime.qd;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;

import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.PrimitivesLib;
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

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 24;

    @Serial
    private static final long serialVersionUID = -5429124066722306588L;

    /**
     * The OpenCPicParams as a new object.
     * @param hRes the horizotal resolution e.g 72 dpi.
     * @param vRes the vertical resolution e.g 72 dpi
     */
    public OpenCPicParams(QDRect rect, float hRes, float vRes) {
        super(kNativeSize);
        _setSrcRect(rect);
        _setHRes(hRes);
        _setVRes(vRes);
        setShortAt(16, (short) -2);
    }

    /**
     * The OpenCPicParams as a new object.
     */
    public OpenCPicParams(QDRect rect) {
        this(rect, 72.0F, 72.0F);
    }

    private OpenCPicParams(byte[] bytes) {
        super(bytes);
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException {
        this.bytes = new byte[kNativeSize];
        in.read(this.bytes);
        for (int i = 0; i < 8; i += 2) {
            setShortInArray(getBytes(), i, EndianOrder.flipBigEndianToNative16(getShortFromArray(this.bytes, i)));
        }
        for (int i = 8; i < 16; i += 4) {
            setIntInArray(getBytes(), i, EndianOrder.flipBigEndianToNative32(getIntFromArray(this.bytes, i + 8)));
        }
        setShortInArray(getBytes(), 16, EndianOrder.flipBigEndianToNative16(getShortFromArray(this.bytes, 16)));
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        byte[] movie = new byte[kNativeSize];
        for (int i = 0; i < 8; i += 2) {
            setShortInArray(movie, i, EndianOrder.flipNativeToBigEndian16(getShortFromArray(getBytes(), i)));
        }
        for (int i = 8; i < 16; i += 4) {
            setIntInArray(movie, i, EndianOrder.flipNativeToBigEndian32(getIntFromArray(getBytes(), i)));
        }
        setShortInArray(movie, 16, EndianOrder.flipNativeToBigEndian16(getShortFromArray(getBytes(), 16)));
        out.write(movie);
    }

    /**
     * Gets the bounding rectangle of the picture.
     * @return The bounding rectangle.
     */
    public QDRect getSrcRect() {
        byte[] destArray = new byte[8];
        getBytesAt(0, 8, destArray, 0);
        return QDRect.fromArray(destArray, 8);
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

    private void _setSrcRect(QDRect rect) {
        setBytesAt(0, 8, rect.getBytes(), 0);
    }

    private void _setHRes(float hRes) {
        setIntAt(8, QTUtils.X2Fix(hRes));
    }

    private void _setVRes(float vRes) {
        setIntAt(12, QTUtils.X2Fix(vRes));
    }

    /**
     * Return a copy of the OpenCPicParams.
     */
    @Override
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

    private static int getIntFromArray(byte[] array, int offset) {
        return MemoryManager.getIntFromArray(array, offset);
    }

    private static void setIntInArray(byte[] array, int offset, int value) {
        MemoryManager.setIntInArray(array, offset, value);
    }

    private static short getShortFromArray(byte[] array, int offset) {
        return MemoryManager.getShortFromArray(array, offset);
    }

    private static void setShortInArray(byte[] array, int offset, short value) {
        MemoryManager.setShortInArray(array, offset, value);
    }
}
