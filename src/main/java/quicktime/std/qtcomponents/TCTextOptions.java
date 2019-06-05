
package quicktime.std.qtcomponents;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QTNative;
import quicktime.qd.QDColor;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;


public final class TCTextOptions extends QTByteObject implements PrimitivesLib {
    private static Object linkage;

    private static final int kNativeSize = 20;

    static final long serialVersionUID = -7109590438601877043L;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(TCTextOptions.class);
                    }
                });
            }
        }.establish();
    }

    public TCTextOptions() {
        super(20);
    }

    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[20];

        paramObjectInputStream.read(getBytes());

        for (int i = 0; i < 20; i += 2) {
            short s1 = getShortFromArray(getBytes(), i);
            short s2 = EndianOrder.flipBigEndianToNative16(s1);
            setShortInArray(getBytes(), i, s2);
        }
    }

    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        byte[] arrayOfByte = new byte[20];

        for (int i = 0; i < 20; i += 2) {
            short s1 = getShortFromArray(arrayOfByte, i);
            short s2 = EndianOrder.flipNativeToBigEndian16(s1);
            setShortInArray(arrayOfByte, i, s2);
        }

        paramObjectOutputStream.write(arrayOfByte);
    }

    public int getTXFont() {
        return getShortAt(0);
    }

    public void setTXFont(int paramInt) {
        setShortAt(0, (short) paramInt);
    }

    public int getTXFace() {
        return getShortAt(2);
    }

    public void setTXFace(int paramInt) {
        setShortAt(2, (short) paramInt);
    }

    public int getTXSize() {
        return getShortAt(4);
    }

    public void setTXSize(int paramInt) {
        setShortAt(4, (short) paramInt);
    }

    public QDColor getForeColor() {
        byte[] arrayOfByte = new byte[6];
        System.arraycopy(getBytes(), 8, arrayOfByte, 0, 6);
        return QDColor.fromArray(arrayOfByte, 6);
    }

    public void setForeColor(QDColor paramQDColor) {
        System.arraycopy(paramQDColor.getRGBColor(), 0, getBytes(), 8, 6);
    }

    public QDColor getBackColor() {
        byte[] arrayOfByte = new byte[6];
        System.arraycopy(getBytes(), 14, arrayOfByte, 0, 6);
        return QDColor.fromArray(arrayOfByte, 6);
    }

    public void setBackColor(QDColor paramQDColor) {
        System.arraycopy(paramQDColor.getRGBColor(), 0, getBytes(), 14, 6);
    }

    private static native short getShortFromArray(byte[] paramArrayOfByte, int paramInt);

    private static native void setShortInArray(byte[] paramArrayOfByte, int paramInt, short paramShort);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * qtcomponents/TCTextOptions.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
