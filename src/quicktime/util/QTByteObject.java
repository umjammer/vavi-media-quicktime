
package quicktime.util;

import java.io.Serializable;
import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QTNative;


public class QTByteObject implements PrimitivesLib, Serializable {
    private static Object linkage;

    protected transient byte[] bytes;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(QTByteObject.class);
                    }
                });
            }
        }.establish();
    }

    public static QTByteObject fromArray(byte[] paramArrayOfByte) {
        QTByteObject localQTByteObject = new QTByteObject();
        localQTByteObject.bytes = paramArrayOfByte;
        return localQTByteObject;
    }

    protected QTByteObject(int paramInt) {
        if (paramInt > 0) {
            this.bytes = new byte[paramInt];
        }
    }

    protected QTByteObject(byte[] paramArrayOfByte) {
        this(paramArrayOfByte.length);
        System.arraycopy(paramArrayOfByte, 0, this.bytes, 0, paramArrayOfByte.length);
    }

    protected final void setBytesAt(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3) {
        System.arraycopy(paramArrayOfByte, paramInt3, this.bytes, paramInt1, paramInt2);
    }

    protected final void getBytesAt(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3) {
        System.arraycopy(this.bytes, paramInt1, paramArrayOfByte, paramInt3, paramInt2);
    }

    public final byte[] getBytes() {
        return this.bytes;
    }

    public int getSize() {
        return this.bytes.length;
    }

    public boolean equals(Object paramObject) {
        if (this == paramObject)
            return true;
        if ((paramObject != null) && ((paramObject instanceof QTByteObject))) {
            byte[] arrayOfByte = ((QTByteObject) paramObject).bytes;
            if (arrayOfByte.length != this.bytes.length)
                return false;
            for (int i = 0; i < this.bytes.length; i++)
                if (this.bytes[i] != arrayOfByte[i])
                    return false;
            return true;
        }
        return false;
    }

    public String toString() {
        return getClass().getName();
    }

    protected final byte getByteAt(int paramInt) {
        return this.bytes[paramInt];
    }

    protected final short getShortAt(int paramInt) {
        return getShortFromArray(this.bytes, paramInt);
    }

    protected final int getIntAt(int paramInt) {
        return getIntFromArray(this.bytes, paramInt);
    }

    protected final long getLongAt(int paramInt) {
        return getLongFromArray(this.bytes, paramInt);
    }

    protected final float getFloatAt(int paramInt) {
        return getFloatFromArray(this.bytes, paramInt);
    }

    protected final double getDoubleAt(int paramInt) {
        return getDoubleFromArray(this.bytes, paramInt);
    }

    protected final void setByteAt(int paramInt, byte paramByte) {
        this.bytes[paramInt] = paramByte;
    }

    protected final void setShortAt(int paramInt, short paramShort) {
        setShortInArray(this.bytes, paramInt, paramShort);
    }

    protected final void setIntAt(int paramInt1, int paramInt2) {
        setIntInArray(this.bytes, paramInt1, paramInt2);
    }

    protected final void setLongAt(int paramInt, long paramLong) {
        setLongInArray(this.bytes, paramInt, paramLong);
    }

    protected final void setFloatAt(int paramInt, float paramFloat) {
        setFloatInArray(this.bytes, paramInt, paramFloat);
    }

    protected final void setDoubleAt(int paramInt, double paramDouble) {
        setDoubleInArray(this.bytes, paramInt, paramDouble);
    }

    protected int strlen(int paramInt) {
        int i = 0;
        while (getByteAt(paramInt++) != 0)
            i++;
        return i;
    }

    protected String getCStringAt(int paramInt) throws UtilException {
        int i = strlen(paramInt);
        if (i > getSize()) {
            throw new UtilException(-50);
        }
        byte[] arrayOfByte = new byte[i];
        getBytesAt(paramInt, i, arrayOfByte, 0);

        return new String(arrayOfByte);
    }

    protected void setCStringAt(int paramInt1, int paramInt2, String paramString) throws UtilException {
        paramInt2 = Math.min(paramInt2, paramString.length());
        if (paramInt2 + paramInt1 > getSize()) {
            throw new UtilException(-50);
        }
        byte[] arrayOfByte = paramString.getBytes();
        setBytesAt(paramInt1, paramInt2, arrayOfByte, 0);
        setByteAt(paramInt1 + paramInt2, (byte) 0);
    }

    protected String getPStringAt(int paramInt) throws UtilException {
        int i = getByteAt(paramInt++);
        if (i > getSize()) {
            throw new UtilException(-50);
        }
        byte[] arrayOfByte = new byte[i];
        getBytesAt(paramInt, i, arrayOfByte, 0);

        return new String(arrayOfByte);
    }

    protected void setPStringAt(int paramInt1, int paramInt2, String paramString) throws UtilException {
        paramInt2 = Math.min(Math.min(paramInt2, paramString.length()), 255);
        if (paramInt2 + paramInt1 > getSize()) {
            throw new UtilException(-50);
        }
        setByteAt(paramInt1++, (byte) paramInt2);

        byte[] arrayOfByte = paramString.getBytes();
        setBytesAt(paramInt1, paramInt2, arrayOfByte, 0);
    }

    private QTByteObject() {
    }

    private static native short getShortFromArray(byte[] paramArrayOfByte, int paramInt);

    private static native int getIntFromArray(byte[] paramArrayOfByte, int paramInt);

    private static native long getLongFromArray(byte[] paramArrayOfByte, int paramInt);

    private static native float getFloatFromArray(byte[] paramArrayOfByte, int paramInt);

    private static native double getDoubleFromArray(byte[] paramArrayOfByte, int paramInt);

    private static native void setShortInArray(byte[] paramArrayOfByte, int paramInt, short paramShort);

    private static native void setIntInArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2);

    private static native void setLongInArray(byte[] paramArrayOfByte, int paramInt, long paramLong);

    private static native void setFloatInArray(byte[] paramArrayOfByte, int paramInt, float paramFloat);

    private static native void setDoubleInArray(byte[] paramArrayOfByte, int paramInt, double paramDouble);
}
