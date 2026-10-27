package quicktime.util;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTRuntimeException;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.InterfaceLib;
import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;

/**
 * A QTPointerRef is a "smart" pointer in that it can be either a Reference to a pointer
 * which is not disposable, or it can be a real "independent" pointer which is
 * automatically disposed on finalization.This class is used as a generic base class for pointer based objects. See QTHandle
 * for a user-level pointer object.
 *
 * The default setting is for the Ptr to be disposable - you must use the
 * QTPointerRef(int, int, Object) constructor to create a non-disposable reference
 * For non-disposable Pointers there are two situations to look out for:
 * (1) Upon finalization the pointer should NOT be disposed - it belongs to a parent struct
 * (2) The parent object from which the pointer is obtained should not be allowed to
 * go away whilst the reference object is alive.
 * In both situations we must maintain a reference to the parent or owner object.
 */
public class QTPointerRef extends QTObject implements QuickTimeLib, PrimitivesLib, InterfaceLib {

    private static Object linkage;

    private transient int size;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged((PrivilegedAction<Object>) () -> QTNative.linkNativeMethods(QTPointerRef.class));
            }
        }.establish();
    }

    protected QTPointerRef(int paramInt, boolean paramBoolean) throws QTException {
        this(allocate(paramInt, paramBoolean), paramInt, null);
        UtilException.checkError(MemError());
    }

    protected QTPointerRef(int paramInt1, int paramInt2, Object paramObject) {
        super(paramInt1, paramObject);
        this.size = paramInt2;
    }

    private static int allocate(int paramInt, boolean paramBoolean) throws UtilException {
        int i = paramBoolean ? NewPtrClear(paramInt) : NewPtr(paramInt);
        if (i == 0)
            throw new UtilException(-108);
        return i;
    }

    /**
     * Returns the size of the QTPionter
     */
    public int getSize() {
        return this.size;
    }

    /** updates the size after the memory block was resized */
    void setSizeField(int size) {
        this.size = size;
    }

    /**
     * Returns a copy of the data of the pointer as a byte array
     * @return a byte array - copy of pointer data
     */
    public byte[] getBytes() {
        byte[] arrayOfByte = new byte[this.size];
        getBytesAt(0, this.size, arrayOfByte, 0);
        return arrayOfByte;
    }

    protected void getBytesAt(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3) {
        copyPointerToArray(_ID(), paramInt1, paramArrayOfByte, paramInt3, paramInt2);
    }

    protected void setBytesAt(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3) {
        copyArrayToPointer(paramArrayOfByte, paramInt3, _ID(), paramInt1, paramInt2);
    }

    /**
     * An efficient byte[] copy that copies length bytes from the pointer to the
     * byte array.
     * @param ptrOffset the byte offset from the pointer to copy the bytes from
     * @param destArray the destination byte array
     * @param destOffset the offset within the byte array to start copying to
     * @param length the number of bytes to copy
     */
    public final void copyToArray(int ptrOffset, byte[] destArray, int destOffset, int length) {
        QTUtils.doBoundsChecks(ptrOffset, this.size, length, 1, destOffset, destArray.length);
        getBytesAt(ptrOffset, length, destArray, destOffset);
    }

    /**
     * An efficient short[] copy that copies length shorts from the pointer to the
     * short array.
     * @param ptrOffset the byte offset from the pointer to copy the shorts from
     * @param destArray the destination short array
     * @param destOffset the offset within the short array to start copying to
     * @param length the number of shorts to copy
     */
    public final void copyToArray(int ptrOffset, short[] destArray, int destOffset, int length) {
        QTUtils.doBoundsChecks(ptrOffset, this.size, length, 2, destOffset, destArray.length);
        copyPointerToArray(_ID(), ptrOffset, destArray, destOffset * 2, length * 2);
    }

    /**
     * An efficient char[] copy that copies length chars from the pointer to the
     * char array.
     * @param ptrOffset the byte offset from the pointer to copy the chars from
     * @param destArray the destination char array
     * @param destOffset the offset within the char array to start copying to
     * @param length the number of chars to copy
     */
    public final void copyToArray(int ptrOffset, char[] destArray, int destOffset, int length) {
        QTUtils.doBoundsChecks(ptrOffset, this.size, length, 2, destOffset, destArray.length);
        copyPointerToArray(_ID(), ptrOffset, destArray, destOffset * 2, length * 2);
    }

    /**
     * An efficient int[] copy that copies length ints from the pointer to the
     * int array.
     * @param ptrOffset the byte offset from the pointer to copy the ints from
     * @param destArray the destination int array
     * @param destOffset the offset within the int array to start copying to
     * @param length the number of ints to copy
     */
    public final void copyToArray(int ptrOffset, int[] destArray, int destOffset, int length) {
        QTUtils.doBoundsChecks(ptrOffset, this.size, length, 4, destOffset, destArray.length);
        copyPointerToArray(_ID(), ptrOffset, destArray, destOffset * 4, length * 4);
    }

    /**
     * An efficient float[] copy that copies length floats from the pointer to the
     * float array.
     * @param ptrOffset the byte offset from the pointer to copy the floats from
     * @param destArray the destination float array
     * @param destOffset the offset within the float array to start copying to
     * @param length the number of floats to copy
     */
    public final void copyToArray(int ptrOffset, float[] destArray, int destOffset, int length) {
        QTUtils.doBoundsChecks(ptrOffset, this.size, length, 4, destOffset, destArray.length);
        copyPointerToArray(_ID(), ptrOffset, destArray, destOffset * 4, length * 4);
    }

    /**
     * An efficient long[] copy that copies length longs from the pointer to the
     * long array.
     * @param ptrOffset the byte offset from the pointer to copy the longs from
     * @param destArray the destination long array
     * @param destOffset the offset within the long array to start copying to
     * @param length the number of longs to copy
     */
    public final void copyToArray(int ptrOffset, long[] destArray, int destOffset, int length) {
        QTUtils.doBoundsChecks(ptrOffset, this.size, length, 8, destOffset, destArray.length);
        copyPointerToArray(_ID(), ptrOffset, destArray, destOffset * 8, length * 8);
    }

    /**
     * An efficient double[] copy that copies length doubles from the pointer to the
     * double array.
     * @param ptrOffset the byte offset from the pointer to copy the doubles from
     * @param destArray the destination double array
     * @param destOffset the offset within the double array to start copying to
     * @param length the number of doubles to copy
     */
    public final void copyToArray(int ptrOffset, double[] destArray, int destOffset, int length) {
        QTUtils.doBoundsChecks(ptrOffset, this.size, length, 8, destOffset, destArray.length);
        copyPointerToArray(_ID(), ptrOffset, destArray, destOffset * 8, length * 8);
    }

    /**
     * An efficient byte[] copy that copies length bytes from the byte array to
     * the pointer.
     * @param ptrOffset how many bytes offset from the base pointer to copy to.
     * @param srcArray the byte[] to copy
     * @param srcOffset the nth index within the byte [] to start copying from
     * @param length how many elements of the byte array to copy
     */
    public final void copyFromArray(int ptrOffset, byte[] srcArray, int srcOffset, int length) {
        QTUtils.doBoundsChecks(ptrOffset, this.size, length, 1, srcOffset, srcArray.length);
        setBytesAt(ptrOffset, length, srcArray, srcOffset);
    }

    /**
     * An efficient short[] copy that copies length shorts from the short array to
     * the pointer.
     * @param ptrOffset how many bytes offset from the base pointer to copy to.
     * @param srcArray the short[] to copy
     * @param srcOffset the nth index within the short [] to start copying from
     * @param length how many elements of the short array to copy
     */
    public final void copyFromArray(int ptrOffset, short[] srcArray, int srcOffset, int length) {
        QTUtils.doBoundsChecks(ptrOffset, this.size, length, 2, srcOffset, srcArray.length);
        copyArrayToPointer(srcArray, srcOffset * 2, _ID(), ptrOffset, length * 2);
    }

    /**
     * An efficient char[] copy that copies length chars from the char array to
     * the pointer.
     * @param ptrOffset how many bytes offset from the base pointer to copy to.
     * @param srcArray the char[] to copy
     * @param srcOffset the nth index within the char [] to start copying from
     * @param length how many elements of the char array to copy
     */
    public final void copyFromArray(int ptrOffset, char[] srcArray, int srcOffset, int length) {
        QTUtils.doBoundsChecks(ptrOffset, this.size, length, 2, srcOffset, srcArray.length);
        copyArrayToPointer(srcArray, srcOffset * 2, _ID(), ptrOffset, length * 2);
    }

    /**
     * An efficient int[] copy that copies length ints from the int array to
     * the pointer.
     * @param ptrOffset how many bytes offset from the base pointer to copy to.
     * @param srcArray the int[] to copy
     * @param srcOffset the nth index within the int [] to start copying from
     * @param length how many elements of the int array to copy
     */
    public final void copyFromArray(int ptrOffset, int[] srcArray, int srcOffset, int length) {
        QTUtils.doBoundsChecks(ptrOffset, this.size, length, 4, srcOffset, srcArray.length);
        copyArrayToPointer(srcArray, srcOffset * 4, _ID(), ptrOffset, length * 4);
    }

    /**
     * An efficient float[] copy that copies length floats from the float array to
     * the pointer.
     * @param ptrOffset how many bytes offset from the base pointer to copy to.
     * @param srcArray the float[] to copy
     * @param srcOffset the nth index within the float [] to start copying from
     * @param length how many elements of the float array to copy
     */
    public final void copyFromArray(int ptrOffset, float[] srcArray, int srcOffset, int length) {
        QTUtils.doBoundsChecks(ptrOffset, this.size, length, 4, srcOffset, srcArray.length);
        copyArrayToPointer(srcArray, srcOffset * 4, _ID(), ptrOffset, length * 4);
    }

    /**
     * An efficient long[] copy that copies length longs from the long array to
     * the pointer.
     * @param ptrOffset how many bytes offset from the base pointer to copy to.
     * @param srcArray the long[] to copy
     * @param srcOffset the nth index within the long [] to start copying from
     * @param length how many elements of the long array to copy
     */
    public final void copyFromArray(int ptrOffset, long[] srcArray, int srcOffset, int length) {
        QTUtils.doBoundsChecks(ptrOffset, this.size, length, 8, srcOffset, srcArray.length);
        copyArrayToPointer(srcArray, srcOffset * 8, _ID(), ptrOffset, length * 8);
    }

    /**
     * An efficient double[] copy that copies length doubles from the double array to
     * the pointer.
     * @param ptrOffset how many bytes offset from the base pointer to copy to.
     * @param srcArray the double[] to copy
     * @param srcOffset the nth index within the double [] to start copying from
     * @param length how many elements of the double array to copy
     */
    public final void copyFromArray(int ptrOffset, double[] srcArray, int srcOffset, int length) {
        QTUtils.doBoundsChecks(ptrOffset, this.size, length, 8, srcOffset, srcArray.length);
        copyArrayToPointer(srcArray, srcOffset * 8, _ID(), ptrOffset, length * 8);
    }

    /**
     * A String representation of the class.
     */
    public String toString() {
        return super.toString() + "[size=" + getSize() + "]";
    }

    protected int makeAndCopyPtr(int paramInt1, int paramInt2) {
        int i = 0;
        try {
            i = allocate(paramInt2, false);
        } catch (UtilException localUtilException) {
            throw new QTRuntimeException(localUtilException);
        }
        BlockMove(_ID() + paramInt1, i, this.size);
        return i;
    }

    protected byte getByteAt(int paramInt) {
        return getByteFromPointer(_ID(), paramInt);
    }

    protected short getShortAt(int paramInt) {
        return getShortFromPointer(_ID(), paramInt);
    }

    protected int getIntAt(int paramInt) {
        return getIntFromPointer(_ID(), paramInt);
    }

    protected long getLongAt(int paramInt) {
        return getLongFromPointer(_ID(), paramInt);
    }

    protected float getFloatAt(int paramInt) {
        return getFloatFromPointer(_ID(), paramInt);
    }

    protected double getDoubleAt(int paramInt) {
        return getDoubleFromPointer(_ID(), paramInt);
    }

    protected void setByteAt(int paramInt, byte paramByte) {
        setByteInPointer(_ID(), paramInt, paramByte);
    }

    protected void setShortAt(int paramInt, short paramShort) {
        setShortInPointer(_ID(), paramInt, paramShort);
    }

    protected void setIntAt(int paramInt1, int paramInt2) {
        setIntInPointer(_ID(), paramInt1, paramInt2);
    }

    protected void setLongAt(int paramInt, long paramLong) {
        setLongInPointer(_ID(), paramInt, paramLong);
    }

    protected void setFloatAt(int paramInt, float paramFloat) {
        setFloatInPointer(_ID(), paramInt, paramFloat);
    }

    protected void setDoubleAt(int paramInt, double paramDouble) {
        setDoubleInPointer(_ID(), paramInt, paramDouble);
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

    private static short MemError() {
        return MemoryManager.MemError();
    }

    private static int NewPtrClear(int paramInt) {
        return MemoryManager.NewPtrClear(paramInt);
    }

    private static int NewPtr(int paramInt) {
        return MemoryManager.NewPtr(paramInt);
    }

    private static void BlockMove(int paramInt1, int paramInt2, int paramInt3) {
        MemoryManager.BlockMove(paramInt1, paramInt2, paramInt3);
    }

    private static void copyPointerToArray(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3, int paramInt4) {
        MemoryManager.copyPointerToArray(paramInt1, paramInt2, paramArrayOfByte, paramInt3, paramInt4);
    }

    private static void copyPointerToArray(int paramInt1, int paramInt2, short[] paramArrayOfShort, int paramInt3, int paramInt4) {
        MemoryManager.copyPointerToArray(paramInt1, paramInt2, paramArrayOfShort, paramInt3, paramInt4);
    }

    private static void copyPointerToArray(int paramInt1, int paramInt2, char[] paramArrayOfChar, int paramInt3, int paramInt4) {
        MemoryManager.copyPointerToArray(paramInt1, paramInt2, paramArrayOfChar, paramInt3, paramInt4);
    }

    private static void copyPointerToArray(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3, int paramInt4) {
        MemoryManager.copyPointerToArray(paramInt1, paramInt2, paramArrayOfInt, paramInt3, paramInt4);
    }

    private static void copyPointerToArray(int paramInt1, int paramInt2, float[] paramArrayOfFloat, int paramInt3, int paramInt4) {
        MemoryManager.copyPointerToArray(paramInt1, paramInt2, paramArrayOfFloat, paramInt3, paramInt4);
    }

    private static void copyPointerToArray(int paramInt1, int paramInt2, long[] paramArrayOfLong, int paramInt3, int paramInt4) {
        MemoryManager.copyPointerToArray(paramInt1, paramInt2, paramArrayOfLong, paramInt3, paramInt4);
    }

    private static void copyPointerToArray(int paramInt1, int paramInt2, double[] paramArrayOfDouble, int paramInt3, int paramInt4) {
        MemoryManager.copyPointerToArray(paramInt1, paramInt2, paramArrayOfDouble, paramInt3, paramInt4);
    }

    private static void copyArrayToPointer(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        MemoryManager.copyArrayToPointer(paramArrayOfByte, paramInt1, paramInt2, paramInt3, paramInt4);
    }

    private static void copyArrayToPointer(short[] paramArrayOfShort, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        MemoryManager.copyArrayToPointer(paramArrayOfShort, paramInt1, paramInt2, paramInt3, paramInt4);
    }

    private static void copyArrayToPointer(char[] paramArrayOfChar, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        MemoryManager.copyArrayToPointer(paramArrayOfChar, paramInt1, paramInt2, paramInt3, paramInt4);
    }

    private static void copyArrayToPointer(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        MemoryManager.copyArrayToPointer(paramArrayOfInt, paramInt1, paramInt2, paramInt3, paramInt4);
    }

    private static void copyArrayToPointer(float[] paramArrayOfFloat, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        MemoryManager.copyArrayToPointer(paramArrayOfFloat, paramInt1, paramInt2, paramInt3, paramInt4);
    }

    private static void copyArrayToPointer(long[] paramArrayOfLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        MemoryManager.copyArrayToPointer(paramArrayOfLong, paramInt1, paramInt2, paramInt3, paramInt4);
    }

    private static void copyArrayToPointer(double[] paramArrayOfDouble, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        MemoryManager.copyArrayToPointer(paramArrayOfDouble, paramInt1, paramInt2, paramInt3, paramInt4);
    }

    private static byte getByteFromPointer(int paramInt1, int paramInt2) {
        return MemoryManager.getByteFromPointer(paramInt1, paramInt2);
    }

    private static short getShortFromPointer(int paramInt1, int paramInt2) {
        return MemoryManager.getShortFromPointer(paramInt1, paramInt2);
    }

    private static int getIntFromPointer(int paramInt1, int paramInt2) {
        return MemoryManager.getIntFromPointer(paramInt1, paramInt2);
    }

    private static long getLongFromPointer(int paramInt1, int paramInt2) {
        return MemoryManager.getLongFromPointer(paramInt1, paramInt2);
    }

    private static double getDoubleFromPointer(int paramInt1, int paramInt2) {
        return MemoryManager.getDoubleFromPointer(paramInt1, paramInt2);
    }

    private static float getFloatFromPointer(int paramInt1, int paramInt2) {
        return MemoryManager.getFloatFromPointer(paramInt1, paramInt2);
    }

    private static void setByteInPointer(int paramInt1, int paramInt2, byte paramByte) {
        MemoryManager.setByteInPointer(paramInt1, paramInt2, paramByte);
    }

    private static void setShortInPointer(int paramInt1, int paramInt2, short paramShort) {
        MemoryManager.setShortInPointer(paramInt1, paramInt2, paramShort);
    }

    private static void setIntInPointer(int paramInt1, int paramInt2, int paramInt3) {
        MemoryManager.setIntInPointer(paramInt1, paramInt2, paramInt3);
    }

    private static void setLongInPointer(int paramInt1, int paramInt2, long paramLong) {
        MemoryManager.setLongInPointer(paramInt1, paramInt2, paramLong);
    }

    private static void setFloatInPointer(int paramInt1, int paramInt2, float paramFloat) {
        MemoryManager.setFloatInPointer(paramInt1, paramInt2, paramFloat);
    }

    private static void setDoubleInPointer(int paramInt1, int paramInt2, double paramDouble) {
        MemoryManager.setDoubleInPointer(paramInt1, paramInt2, paramDouble);
    }
}

