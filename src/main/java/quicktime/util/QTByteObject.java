package quicktime.util;

import java.io.Serializable;
import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QTNative;

/**
 * Provides a representation of the native data structure as a byte array.
 * The class implements the Serializable interface and can thus be serialized.
 * However the byte[] member is marked as transient because typically a QTByteObject
 * is used to contain objects that have fields, such as shorts, ints, that are larger
 * than bytes. Moving these objects from big to little to big endian run-time architectures
 * can cause problems if the endian issues of these members are not taken care of by
 * the particular class of the object that is being serialized. Thus, any subclass of QTByteObject
 * that is to be serialized defines custome read and write object methods that will
 * ensure that the serialized data that is written out is in big endian format, and if required
 * will be flipped back to native format when read in.
 */
public class QTByteObject implements PrimitivesLib, Serializable {

    private static Object linkage;

    protected transient byte[] bytes;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged((PrivilegedAction<Object>) () -> QTNative.linkNativeMethods(QTByteObject.class));
            }
        }.establish();
    }

    /**
     * This call will return a QTByteObject which becomes a proxy for the
     * specified byte array. It does not copy the byte array - but references it.
     * @param ar the byte array to represent
     * @return a QTByteObject with the specified array as its target bytes
     */
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

    /**
     * Returns the byte array containing the packed native object.
     * @return the actual byte array NOT a copy
     */
    public final byte[] getBytes() {
        return this.bytes;
    }

    /**
     * Returns the size of the represented native object.
     * @return size
     */
    public int getSize() {
        return this.bytes.length;
    }

    /**
     * Returns true if the two objects have the same value.
     */
    public boolean equals(Object paramObject) {
        if (this == paramObject)
            return true;
        if ((paramObject != null) && ((paramObject instanceof QTByteObject))) {
            byte[] arrayOfByte = ((QTByteObject) paramObject).bytes;
            if (arrayOfByte.length != this.bytes.length)
                return false;
            for (int i = 0; i < this.bytes.length; i++) if (this.bytes[i] != arrayOfByte[i])
                return false;
            return true;
        }
        return false;
    }

    /**
     * Returns a string representation of this object
     */
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
        while (getByteAt(paramInt++) != 0) i++;
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

    private static short getShortFromArray(byte[] paramArrayOfByte, int paramInt) {
        return MemoryManager.getShortFromArray(paramArrayOfByte, paramInt);
    }

    private static int getIntFromArray(byte[] paramArrayOfByte, int paramInt) {
        return MemoryManager.getIntFromArray(paramArrayOfByte, paramInt);
    }

    private static long getLongFromArray(byte[] paramArrayOfByte, int paramInt) {
        return MemoryManager.getLongFromArray(paramArrayOfByte, paramInt);
    }

    private static float getFloatFromArray(byte[] paramArrayOfByte, int paramInt) {
        return MemoryManager.getFloatFromArray(paramArrayOfByte, paramInt);
    }

    private static double getDoubleFromArray(byte[] paramArrayOfByte, int paramInt) {
        return MemoryManager.getDoubleFromArray(paramArrayOfByte, paramInt);
    }

    private static void setShortInArray(byte[] paramArrayOfByte, int paramInt, short paramShort) {
        MemoryManager.setShortInArray(paramArrayOfByte, paramInt, paramShort);
    }

    private static void setIntInArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
        MemoryManager.setIntInArray(paramArrayOfByte, paramInt1, paramInt2);
    }

    private static void setLongInArray(byte[] paramArrayOfByte, int paramInt, long paramLong) {
        MemoryManager.setLongInArray(paramArrayOfByte, paramInt, paramLong);
    }

    private static void setFloatInArray(byte[] paramArrayOfByte, int paramInt, float paramFloat) {
        MemoryManager.setFloatInArray(paramArrayOfByte, paramInt, paramFloat);
    }

    private static void setDoubleInArray(byte[] paramArrayOfByte, int paramInt, double paramDouble) {
        MemoryManager.setDoubleInArray(paramArrayOfByte, paramInt, paramDouble);
    }
}
