
package quicktime.std.comp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;
import quicktime.util.QTUtils;


public class ComponentDescription extends QTByteObject implements QuickTimeLib {
    private static Object linkage;

    private static final int kNativeSize = 20;

    static final long serialVersionUID = 1680706231499437269L;

    String name;

    String info;

    static {
        class PrivelegedAction {
            void establish() {
                AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(ComponentDescription.class);
                    }
                });
            }
        }
        new PrivelegedAction().establish();
    }

    public ComponentDescription() {
        super(20);
    }

    public ComponentDescription(int paramInt) {
        this();
        setType(paramInt);
    }

    public ComponentDescription(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        this();
        setType(paramInt1);
        setSubType(paramInt2);
        setManufacturer(paramInt3);
        setFlags(paramInt4);
        setMask(paramInt5);
    }

    private ComponentDescription(byte[] paramArrayOfByte) {
        super(paramArrayOfByte);
    }

    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[20];

        byte[] arrayOfByte = new byte[20];

        paramObjectInputStream.read(arrayOfByte);

        for (int i = 0; i < 20; i += 4) {
            int j = getIntFromArray(arrayOfByte, i);
            int k = EndianOrder.flipBigEndianToNative32(j);
            setIntInArray(getBytes(), i, k);
        }
    }

    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        byte[] arrayOfByte = new byte[20];

        for (int i = 0; i < 20; i += 4) {
            int j = getIntFromArray(getBytes(), i);
            int k = EndianOrder.flipNativeToBigEndian32(j);
            setIntInArray(arrayOfByte, i, k);
        }

        paramObjectOutputStream.write(arrayOfByte);
    }

    public int getType() {
        return getIntAt(0);
    }

    public void setType(int paramInt) {
        setIntAt(0, paramInt);
    }

    public int getSubType() {
        return getIntAt(4);
    }

    public void setSubType(int paramInt) {
        setIntAt(4, paramInt);
    }

    public int getManufacturer() {
        return getIntAt(8);
    }

    public void setManufacturer(int paramInt) {
        setIntAt(8, paramInt);
    }

    public int getFlags() {
        return getIntAt(12);
    }

    public void setFlags(int paramInt) {
        setIntAt(12, paramInt);
    }

    public int getMask() {
        return getIntAt(16);
    }

    public void setMask(int paramInt) {
        setIntAt(16, paramInt);
    }

    public int count() {
        return CountComponents(getBytes());
    }

    public String getName() {
        return this.name;
    }

    public String getInformationString() {
        return this.info;
    }

    public String toString() {
        String str = "";
        if (this.name != null)
            str = str + ",name=" + this.name;
        if (this.info != null) {
            str = str + ",info=" + this.info;
        }
        return super.toString() + "[type=" + QTUtils.fromOSType(getType()) + ",subType=" + QTUtils.fromOSType(getSubType())
               + ",manufacturer=" + QTUtils.fromOSType(getManufacturer()) + ",flags=" + getFlags() + str + "]";
    }

    public Object clone() {
        return new ComponentDescription(getBytes());
    }

    private static native int CountComponents(byte[] paramArrayOfByte);

    private static native int getIntFromArray(byte[] paramArrayOfByte, int paramInt);

    private static native void setIntInArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/comp/
 * ComponentDescription.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
