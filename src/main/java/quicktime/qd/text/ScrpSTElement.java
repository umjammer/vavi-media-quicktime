
package quicktime.qd.text;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QTNative;
import quicktime.qd.QDColor;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianFlipSpec;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;
import quicktime.util.QTUtils;


public final class ScrpSTElement extends QTByteObject implements PrimitivesLib {
    static Object linkage;

    public static final int kNativeSize = 20;

    private static EndianDescriptor ed;

    private static final long serialVersionUID = 939259602280929910L;

    static {
        class PrivelegedAction {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(ScrpSTElement.class);
                    }
                });
            }
        }
        new PrivelegedAction().establish();
    }

    protected static EndianDescriptor makeED() {
        EndianDescriptor localEndianDescriptor = new EndianDescriptor(0);

        EndianFlipSpec localEndianFlipSpec1 = new EndianFlipSpec(0, 4, 1);
        localEndianDescriptor.addFlipSpec(localEndianFlipSpec1);

        EndianFlipSpec localEndianFlipSpec2 = new EndianFlipSpec(4, 2, 3);
        localEndianDescriptor.addFlipSpec(localEndianFlipSpec2);

        EndianFlipSpec localEndianFlipSpec3 = new EndianFlipSpec(12, 2, 1);
        localEndianDescriptor.addFlipSpec(localEndianFlipSpec3);

        EndianFlipSpec localEndianFlipSpec4 = new EndianFlipSpec(14, 2, 3);
        localEndianDescriptor.addFlipSpec(localEndianFlipSpec4);

        return localEndianDescriptor;
    }

    public static EndianDescriptor getEndianDescriptor() {
        if (ed == null)
            ed = makeED();
        return ed;
    }

    public ScrpSTElement() {
        super(20);
    }

    private ScrpSTElement(byte[] paramArrayOfByte) {
        super(paramArrayOfByte);
    }

    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[20];

        byte[] arrayOfByte = new byte[20];
        paramObjectInputStream.read(arrayOfByte);

        setIntInArray(this.bytes, 0, EndianOrder.flipBigEndianToNative32(getIntFromArray(arrayOfByte, 0)));
        setShortInArray(this.bytes, 4, EndianOrder.flipBigEndianToNative16(getShortFromArray(arrayOfByte, 4)));
        setShortInArray(this.bytes, 6, EndianOrder.flipBigEndianToNative16(getShortFromArray(arrayOfByte, 6)));
        setShortInArray(this.bytes, 8, EndianOrder.flipBigEndianToNative16(getShortFromArray(arrayOfByte, 8)));
        setShortInArray(this.bytes, 12, EndianOrder.flipBigEndianToNative16(getShortFromArray(arrayOfByte, 12)));
        setShortInArray(this.bytes, 14, EndianOrder.flipBigEndianToNative16(getShortFromArray(arrayOfByte, 14)));
        setShortInArray(this.bytes, 16, EndianOrder.flipBigEndianToNative16(getShortFromArray(arrayOfByte, 16)));
        setShortInArray(this.bytes, 18, EndianOrder.flipBigEndianToNative16(getShortFromArray(arrayOfByte, 18)));
    }

    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        byte[] arrayOfByte = new byte[20];

        setIntInArray(arrayOfByte, 0, EndianOrder.flipNativeToBigEndian32(getIntFromArray(this.bytes, 0)));
        setShortInArray(arrayOfByte, 4, EndianOrder.flipNativeToBigEndian16(getShortFromArray(this.bytes, 4)));
        setShortInArray(arrayOfByte, 6, EndianOrder.flipNativeToBigEndian16(getShortFromArray(this.bytes, 6)));
        setShortInArray(arrayOfByte, 8, EndianOrder.flipNativeToBigEndian16(getShortFromArray(this.bytes, 8)));
        setShortInArray(arrayOfByte, 12, EndianOrder.flipNativeToBigEndian16(getShortFromArray(this.bytes, 12)));
        setShortInArray(arrayOfByte, 14, EndianOrder.flipNativeToBigEndian16(getShortFromArray(this.bytes, 14)));
        setShortInArray(arrayOfByte, 16, EndianOrder.flipNativeToBigEndian16(getShortFromArray(this.bytes, 16)));
        setShortInArray(arrayOfByte, 18, EndianOrder.flipNativeToBigEndian16(getShortFromArray(this.bytes, 18)));

        paramObjectOutputStream.write(arrayOfByte);
    }

    public Object clone() {
        return new ScrpSTElement(getBytes());
    }

    public void setColor(QDColor paramQDColor) {
        if (paramQDColor == null) {
            setShortAt(14, (short) -1);
            setShortAt(16, (short) -1);
            setShortAt(18, (short) -1);
        } else {
            System.arraycopy(paramQDColor.getRGBColor(), 0, this.bytes, 14, 6);
        }
    }

    public QDColor getColor() {
        byte[] arrayOfByte = new byte[6];
        System.arraycopy(this.bytes, 14, arrayOfByte, 0, 6);
        return QDColor.fromArray(arrayOfByte, 6);
    }

    public void setScriptSize(int paramInt) {
        setShortAt(12, (short) paramInt);
    }

    public int getScriptSize() {
        return getShortAt(12);
    }

    public void setFace(int paramInt) {
        setByteAt(10, (byte) paramInt);
        setByteAt(11, (byte) 0);
    }

    public int getFace() {
        return QTUtils.UByte2Int(getByteAt(10));
    }

    public void setFont(int paramInt) {
        setShortAt(8, (short) paramInt);
    }

    public int getFont() {
        return getShortAt(8);
    }

    public void setAscent(int paramInt) {
        setShortAt(6, (short) paramInt);
    }

    public int getAscent() {
        return getShortAt(6);
    }

    public void setHeight(int paramInt) {
        setShortAt(4, (short) paramInt);
    }

    public int getHeight() {
        return getShortAt(4);
    }

    public void setStartChar(int paramInt) {
        setIntAt(0, paramInt);
    }

    public int getStartChar() {
        return getIntAt(0);
    }

    public String toString() {
        return getClass().getName() + "[" + ",startChar=" + getStartChar() + ",getHeight=" + getHeight() + ",getAscent="
               + getAscent() + ",getFont=" + getFont() + ",getFace=" + getFace() + ",getSize=" + getSize() + ",getColor="
               + getColor() + "]";
    }

    private static native int getIntFromArray(byte[] paramArrayOfByte, int paramInt);

    private static native void setIntInArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2);

    private static native short getShortFromArray(byte[] paramArrayOfByte, int paramInt);

    private static native void setShortInArray(byte[] paramArrayOfByte, int paramInt, short paramShort);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/qd/text/
 * ScrpSTElement.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
