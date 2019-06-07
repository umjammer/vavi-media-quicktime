package quicktime.std.image;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.QDColor;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;

/**
 * The GraphicsMode class implements details that describe transfer operations and
 * transperancy operations.
 * refer to ModifierTrackGraphicsMode in QuickTime
 */
public final class GraphicsMode extends QTByteObject implements QuickTimeLib, Cloneable {

    private static Object linkage;

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 10;

    private static final long serialVersionUID = -7245842945216323992L;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(GraphicsMode.class);
                    }
                });
            }
        }.establish();
    }

    /**
     * This constructor creates an empty GraphicsMode object.
     */
    public GraphicsMode() {
        super(10);
    }

    /**
     * This constructor creates a GraphicsMode object for specified mode and operand color.
     * @param gMode Specifies the graphics mode.
     * @param opColor Specifies the operand color.
     */
    public GraphicsMode(int paramInt, QDColor paramQDColor) {
        this();
        setGraphicsMode(paramInt);
        setColor(paramQDColor);
    }

    private GraphicsMode(byte[] paramArrayOfByte) {
        super(paramArrayOfByte);
    }

    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[10];
        paramObjectInputStream.read(this.bytes);
        int i = getIntFromArray(this.bytes, 0);
        setGraphicsMode(EndianOrder.flipBigEndianToNative32(i));
        for (int j = 4; j < 10; j += 2) {
            short s1 = getShortFromArray(this.bytes, j);
            short s2 = EndianOrder.flipBigEndianToNative16(s1);
            setShortInArray(getBytes(), j, s2);
        }
    }

    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        int i = getGraphicsMode();
        byte[] arrayOfByte = new byte[10];
        setIntInArray(arrayOfByte, 0, EndianOrder.flipNativeToBigEndian32(i));
        for (int j = 4; j < 10; j += 2) {
            short s1 = getShortFromArray(getBytes(), j);
            short s2 = EndianOrder.flipNativeToBigEndian16(s1);
            setShortInArray(arrayOfByte, j, s2);
        }
        paramObjectOutputStream.write(arrayOfByte);
    }

    /**
     * This method sets the graphics mode (such as srcCopy, srcOr, etc)
     * @param mode Specifies the new graphics mode.
     */
    public void setGraphicsMode(int paramInt) {
        setIntAt(0, paramInt);
    }

    /**
     * This method returns the graphics mode
     * @return the graphics mode.
     */
    public int getGraphicsMode() {
        return getIntAt(0);
    }

    /**
     * Returns true if the current mode is opaque, otherwise false.
     */
    public boolean isGraphicsModeOpaque() {
        int i = getIntAt(0);
        return (i == 0) || (i == 64) || (i == 259);
    }

    /**
     * This method sets the operand color.
     * @param newColor Specifies the new color.
     */
    public void setColor(QDColor paramQDColor) {
        if (paramQDColor == null) {
            System.arraycopy(new byte[6], 0, getBytes(), 4, 6);
        } else {
            System.arraycopy(paramQDColor.getRGBColor(), 0, getBytes(), 4, 6);
        }
    }

    /**
     * This method returns the operand mode
     * @return the color.
     */
    public QDColor getColor() {
        byte[] arrayOfByte = new byte[6];
        System.arraycopy(getBytes(), 4, arrayOfByte, 0, 6);
        return QDColor.fromArray(arrayOfByte, 6);
    }

    /**
     * Returns the String representation of this Rectangle's values.
     */
    public String toString() {
        return super.toString() + "[graphicsMode=" + getGraphicsMode() + ",color=" + getColor() + "]";
    }

    /**
     * Return a copy of the GraphicsMode object.
     */
    public Object clone() {
        return new GraphicsMode(getBytes());
    }

    private static native void setShortInArray(byte[] paramArrayOfByte, int paramInt, short paramShort);

    private static native short getShortFromArray(byte[] paramArrayOfByte, int paramInt);

    private static native void setIntInArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2);

    private static native int getIntFromArray(byte[] paramArrayOfByte, int paramInt);
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/image
 * /GraphicsMode.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
