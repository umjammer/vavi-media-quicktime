
package quicktime.qd;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QTNative;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;


public class QDDimension extends QTByteObject implements PrimitivesLib, Cloneable {
    private static Object linkage;

    private static final int kNativeSize = 8;

    private static final long serialVersionUID = -3790051028201084503L;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(QDDimension.class);
                    }
                });
            }
        }.establish();
    }

    public static final EndianDescriptor getEndianDescriptor() {
        return EndianDescriptor.flipAll32;
    }

    public QDDimension() {
        this(0, 0);
    }

    public QDDimension(int paramInt1, int paramInt2) {
        super(8);
        setWidth(paramInt1);
        setHeight(paramInt2);
    }

    public QDDimension(float paramFloat1, float paramFloat2) {
        super(8);
        setWidth(paramFloat1);
        setHeight(paramFloat2);
    }

    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[8];

        byte[] arrayOfByte = new byte[12];
        paramObjectInputStream.read(arrayOfByte);

        int i = EndianOrder.flipBigEndianToNative32(getIntFromArray(arrayOfByte, 0));
        if (i == 1) {
            setIntInArray(getBytes(), 0, EndianOrder.flipBigEndianToNative32(getIntFromArray(arrayOfByte, 4)));

            setIntInArray(getBytes(), 4, EndianOrder.flipBigEndianToNative32(getIntFromArray(arrayOfByte, 8)));
        }
    }

    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        byte[] arrayOfByte = new byte[12];

        setIntInArray(arrayOfByte, 0, EndianOrder.flipNativeToBigEndian32(1));

        setIntInArray(arrayOfByte, 4, EndianOrder.flipNativeToBigEndian32(getIntFromArray(this.bytes, 0)));
        setIntInArray(arrayOfByte, 8, EndianOrder.flipNativeToBigEndian32(getIntFromArray(this.bytes, 4)));

        paramObjectOutputStream.write(arrayOfByte);
    }

    public int getWidth() {
        return (int) getFloatAt(0);
    }

    public float getWidthF() {
        return getFloatAt(0);
    }

    public int getHeight() {
        return (int) getFloatAt(4);
    }

    public float getHeightF() {
        return getFloatAt(4);
    }

    public void setWidth(int paramInt) {
        setFloatAt(0, paramInt);
    }

    public void setWidth(float paramFloat) {
        setFloatAt(0, paramFloat);
    }

    public void setHeight(int paramInt) {
        setFloatAt(4, paramInt);
    }

    public void setHeight(float paramFloat) {
        setFloatAt(4, paramFloat);
    }

    public Object clone() {
        return copy();
    }

    public QDDimension copy() {
        return new QDDimension(getWidth(), getHeight());
    }

    public boolean equals(Object paramObject) {
        if (paramObject == this)
            return true;
        if ((paramObject instanceof QDDimension)) {
            QDDimension localQDDimension = (QDDimension) paramObject;
            return (getWidth() == localQDDimension.getWidth()) && (getHeight() == localQDDimension.getHeight());
        }
        return false;
    }

    public String toString() {
        return getClass().getName() + "[width=" + getWidth() + ",height=" + getHeight() + "]";
    }

    private static native int getIntFromArray(byte[] paramArrayOfByte, int paramInt);

    private static native void setIntInArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/qd/
 * QDDimension.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
