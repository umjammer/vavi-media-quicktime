package quicktime.util;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTRuntimeException;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.InterfaceLib;
import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QuickTimeLib;


/**
 * A QTPointerRef is a "smart" pointer in that it can be either a Reference to a pointer
 * which is not disposable, or it can be a real "independent" pointer which is
 * automatically disposed on finalization.This class is used as a generic base class for pointer based objects. See QTHandle
 * for a user-level pointer object.
 * <p>
 * The default setting is for the Ptr to be disposable - you must use the
 * QTPointerRef(int, int, Object) constructor to create a non-disposable reference
 * For non-disposable Pointers there are two situations to look out for:
 * (1) Upon finalization the pointer should NOT be disposed - it belongs to a parent struct
 * (2) The parent object from which the pointer is obtained should not be allowed to
 * go away whilst the reference object is alive.
 * In both situations we must maintain a reference to the parent or owner object.
 */
public class QTPointerRef extends QTObject implements QuickTimeLib, PrimitivesLib, InterfaceLib {

    private transient int size;

    protected QTPointerRef(int size, boolean clear) throws QTException {
        this(allocate(size, clear), size, null);
        UtilException.checkError(MemError());
    }

    protected QTPointerRef(int nativeRep, int size, Object ref) {
        super(nativeRep, ref);
        this.size = size;
    }

    private static int allocate(int size, boolean clear) throws UtilException {
        int i = clear ? NewPtrClear(size) : NewPtr(size);
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
     *
     * @return a byte array - copy of pointer data
     */
    public byte[] getBytes() {
        byte[] bytes = new byte[this.size];
        getBytesAt(0, this.size, bytes, 0);
        return bytes;
    }

    protected void getBytesAt(int ptrOffset, int size, byte[] array, int arrayOffset) {
        copyPointerToArray(_ID(), ptrOffset, array, arrayOffset, size);
    }

    protected void setBytesAt(int ptrOffset, int size, byte[] array, int arrayOffset) {
        copyArrayToPointer(array, arrayOffset, _ID(), ptrOffset, size);
    }

    /**
     * An efficient byte[] copy that copies length bytes from the pointer to the
     * byte array.
     *
     * @param ptrOffset  the byte offset from the pointer to copy the bytes from
     * @param destArray  the destination byte array
     * @param destOffset the offset within the byte array to start copying to
     * @param length     the number of bytes to copy
     */
    public final void copyToArray(int ptrOffset, byte[] destArray, int destOffset, int length) {
        QTUtils.doBoundsChecks(ptrOffset, this.size, length, 1, destOffset, destArray.length);
        getBytesAt(ptrOffset, length, destArray, destOffset);
    }

    /**
     * An efficient short[] copy that copies length shorts from the pointer to the
     * short array.
     *
     * @param ptrOffset  the byte offset from the pointer to copy the shorts from
     * @param destArray  the destination short array
     * @param destOffset the offset within the short array to start copying to
     * @param length     the number of shorts to copy
     */
    public final void copyToArray(int ptrOffset, short[] destArray, int destOffset, int length) {
        QTUtils.doBoundsChecks(ptrOffset, this.size, length, 2, destOffset, destArray.length);
        copyPointerToArray(_ID(), ptrOffset, destArray, destOffset * 2, length * 2);
    }

    /**
     * An efficient char[] copy that copies length chars from the pointer to the
     * char array.
     *
     * @param ptrOffset  the byte offset from the pointer to copy the chars from
     * @param destArray  the destination char array
     * @param destOffset the offset within the char array to start copying to
     * @param length     the number of chars to copy
     */
    public final void copyToArray(int ptrOffset, char[] destArray, int destOffset, int length) {
        QTUtils.doBoundsChecks(ptrOffset, this.size, length, 2, destOffset, destArray.length);
        copyPointerToArray(_ID(), ptrOffset, destArray, destOffset * 2, length * 2);
    }

    /**
     * An efficient int[] copy that copies length ints from the pointer to the
     * int array.
     *
     * @param ptrOffset  the byte offset from the pointer to copy the ints from
     * @param destArray  the destination int array
     * @param destOffset the offset within the int array to start copying to
     * @param length     the number of ints to copy
     */
    public final void copyToArray(int ptrOffset, int[] destArray, int destOffset, int length) {
        QTUtils.doBoundsChecks(ptrOffset, this.size, length, 4, destOffset, destArray.length);
        copyPointerToArray(_ID(), ptrOffset, destArray, destOffset * 4, length * 4);
    }

    /**
     * An efficient float[] copy that copies length floats from the pointer to the
     * float array.
     *
     * @param ptrOffset  the byte offset from the pointer to copy the floats from
     * @param destArray  the destination float array
     * @param destOffset the offset within the float array to start copying to
     * @param length     the number of floats to copy
     */
    public final void copyToArray(int ptrOffset, float[] destArray, int destOffset, int length) {
        QTUtils.doBoundsChecks(ptrOffset, this.size, length, 4, destOffset, destArray.length);
        copyPointerToArray(_ID(), ptrOffset, destArray, destOffset * 4, length * 4);
    }

    /**
     * An efficient long[] copy that copies length longs from the pointer to the
     * long array.
     *
     * @param ptrOffset  the byte offset from the pointer to copy the longs from
     * @param destArray  the destination long array
     * @param destOffset the offset within the long array to start copying to
     * @param length     the number of longs to copy
     */
    public final void copyToArray(int ptrOffset, long[] destArray, int destOffset, int length) {
        QTUtils.doBoundsChecks(ptrOffset, this.size, length, 8, destOffset, destArray.length);
        copyPointerToArray(_ID(), ptrOffset, destArray, destOffset * 8, length * 8);
    }

    /**
     * An efficient double[] copy that copies length doubles from the pointer to the
     * double array.
     *
     * @param ptrOffset  the byte offset from the pointer to copy the doubles from
     * @param destArray  the destination double array
     * @param destOffset the offset within the double array to start copying to
     * @param length     the number of doubles to copy
     */
    public final void copyToArray(int ptrOffset, double[] destArray, int destOffset, int length) {
        QTUtils.doBoundsChecks(ptrOffset, this.size, length, 8, destOffset, destArray.length);
        copyPointerToArray(_ID(), ptrOffset, destArray, destOffset * 8, length * 8);
    }

    /**
     * An efficient byte[] copy that copies length bytes from the byte array to
     * the pointer.
     *
     * @param ptrOffset how many bytes offset from the base pointer to copy to.
     * @param srcArray  the byte[] to copy
     * @param srcOffset the nth index within the byte [] to start copying from
     * @param length    how many elements of the byte array to copy
     */
    public final void copyFromArray(int ptrOffset, byte[] srcArray, int srcOffset, int length) {
        QTUtils.doBoundsChecks(ptrOffset, this.size, length, 1, srcOffset, srcArray.length);
        setBytesAt(ptrOffset, length, srcArray, srcOffset);
    }

    /**
     * An efficient short[] copy that copies length shorts from the short array to
     * the pointer.
     *
     * @param ptrOffset how many bytes offset from the base pointer to copy to.
     * @param srcArray  the short[] to copy
     * @param srcOffset the nth index within the short [] to start copying from
     * @param length    how many elements of the short array to copy
     */
    public final void copyFromArray(int ptrOffset, short[] srcArray, int srcOffset, int length) {
        QTUtils.doBoundsChecks(ptrOffset, this.size, length, 2, srcOffset, srcArray.length);
        copyArrayToPointer(srcArray, srcOffset * 2, _ID(), ptrOffset, length * 2);
    }

    /**
     * An efficient char[] copy that copies length chars from the char array to
     * the pointer.
     *
     * @param ptrOffset how many bytes offset from the base pointer to copy to.
     * @param srcArray  the char[] to copy
     * @param srcOffset the nth index within the char [] to start copying from
     * @param length    how many elements of the char array to copy
     */
    public final void copyFromArray(int ptrOffset, char[] srcArray, int srcOffset, int length) {
        QTUtils.doBoundsChecks(ptrOffset, this.size, length, 2, srcOffset, srcArray.length);
        copyArrayToPointer(srcArray, srcOffset * 2, _ID(), ptrOffset, length * 2);
    }

    /**
     * An efficient int[] copy that copies length ints from the int array to
     * the pointer.
     *
     * @param ptrOffset how many bytes offset from the base pointer to copy to.
     * @param srcArray  the int[] to copy
     * @param srcOffset the nth index within the int [] to start copying from
     * @param length    how many elements of the int array to copy
     */
    public final void copyFromArray(int ptrOffset, int[] srcArray, int srcOffset, int length) {
        QTUtils.doBoundsChecks(ptrOffset, this.size, length, 4, srcOffset, srcArray.length);
        copyArrayToPointer(srcArray, srcOffset * 4, _ID(), ptrOffset, length * 4);
    }

    /**
     * An efficient float[] copy that copies length floats from the float array to
     * the pointer.
     *
     * @param ptrOffset how many bytes offset from the base pointer to copy to.
     * @param srcArray  the float[] to copy
     * @param srcOffset the nth index within the float [] to start copying from
     * @param length    how many elements of the float array to copy
     */
    public final void copyFromArray(int ptrOffset, float[] srcArray, int srcOffset, int length) {
        QTUtils.doBoundsChecks(ptrOffset, this.size, length, 4, srcOffset, srcArray.length);
        copyArrayToPointer(srcArray, srcOffset * 4, _ID(), ptrOffset, length * 4);
    }

    /**
     * An efficient long[] copy that copies length longs from the long array to
     * the pointer.
     *
     * @param ptrOffset how many bytes offset from the base pointer to copy to.
     * @param srcArray  the long[] to copy
     * @param srcOffset the nth index within the long [] to start copying from
     * @param length    how many elements of the long array to copy
     */
    public final void copyFromArray(int ptrOffset, long[] srcArray, int srcOffset, int length) {
        QTUtils.doBoundsChecks(ptrOffset, this.size, length, 8, srcOffset, srcArray.length);
        copyArrayToPointer(srcArray, srcOffset * 8, _ID(), ptrOffset, length * 8);
    }

    /**
     * An efficient double[] copy that copies length doubles from the double array to
     * the pointer.
     *
     * @param ptrOffset how many bytes offset from the base pointer to copy to.
     * @param srcArray  the double[] to copy
     * @param srcOffset the nth index within the double [] to start copying from
     * @param length    how many elements of the double array to copy
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

    protected int makeAndCopyPtr(int offset, int size) {
        int i = 0;
        try {
            i = allocate(size, false);
        } catch (UtilException e) {
            throw new QTRuntimeException(e);
        }
        BlockMove(_ID() + offset, i, this.size);
        return i;
    }

    protected byte getByteAt(int offset) {
        return getByteFromPointer(_ID(), offset);
    }

    protected short getShortAt(int offset) {
        return getShortFromPointer(_ID(), offset);
    }

    protected int getIntAt(int offset) {
        return getIntFromPointer(_ID(), offset);
    }

    protected long getLongAt(int offset) {
        return getLongFromPointer(_ID(), offset);
    }

    protected float getFloatAt(int offset) {
        return getFloatFromPointer(_ID(), offset);
    }

    protected double getDoubleAt(int offset) {
        return getDoubleFromPointer(_ID(), offset);
    }

    protected void setByteAt(int offset, byte value) {
        setByteInPointer(_ID(), offset, value);
    }

    protected void setShortAt(int offset, short value) {
        setShortInPointer(_ID(), offset, value);
    }

    protected void setIntAt(int offset, int value) {
        setIntInPointer(_ID(), offset, value);
    }

    protected void setLongAt(int offset, long value) {
        setLongInPointer(_ID(), offset, value);
    }

    protected void setFloatAt(int offset, float value) {
        setFloatInPointer(_ID(), offset, value);
    }

    protected void setDoubleAt(int offset, double value) {
        setDoubleInPointer(_ID(), offset, value);
    }

    protected int strlen(int offset) {
        int i = 0;
        while (getByteAt(offset++) != 0) i++;
        return i;
    }

    protected String getCStringAt(int offset) throws UtilException {
        int i = strlen(offset);
        if (i > getSize()) {
            throw new UtilException(-50);
        }
        byte[] arrayOfByte = new byte[i];
        getBytesAt(offset, i, arrayOfByte, 0);
        return new String(arrayOfByte);
    }

    protected void setCStringAt(int offset, int length, String str) throws UtilException {
        length = Math.min(length, str.length());
        if (length + offset > getSize()) {
            throw new UtilException(-50);
        }
        byte[] bytes = str.getBytes();
        setBytesAt(offset, length, bytes, 0);
        setByteAt(offset + length, (byte) 0);
    }

    protected String getPStringAt(int offset) throws UtilException {
        int i = getByteAt(offset++);
        if (i > getSize()) {
            throw new UtilException(-50);
        }
        byte[] arrayOfByte = new byte[i];
        getBytesAt(offset, i, arrayOfByte, 0);
        return new String(arrayOfByte);
    }

    protected void setPStringAt(int offset, int length, String str) throws UtilException {
        length = Math.min(Math.min(length, str.length()), 255);
        if (length + offset > getSize()) {
            throw new UtilException(-50);
        }
        setByteAt(offset++, (byte) length);
        byte[] bytes = str.getBytes();
        setBytesAt(offset, length, bytes, 0);
    }

    private static short MemError() {
        return MemoryManager.MemError();
    }

    private static int NewPtrClear(int size) {
        return MemoryManager.NewPtrClear(size);
    }

    private static int NewPtr(int size) {
        return MemoryManager.NewPtr(size);
    }

    private static void BlockMove(int src, int dst, int size) {
        MemoryManager.BlockMove(src, dst, size);
    }

    private static void copyPointerToArray(int ptr, int ptrOffset, byte[] array, int arrayOffset, int size) {
        MemoryManager.copyPointerToArray(ptr, ptrOffset, array, arrayOffset, size);
    }

    private static void copyPointerToArray(int ptr, int ptrOffset, short[] array, int arrayOffset, int size) {
        MemoryManager.copyPointerToArray(ptr, ptrOffset, array, arrayOffset, size);
    }

    private static void copyPointerToArray(int ptr, int ptrOffset, char[] array, int arrayOffset, int size) {
        MemoryManager.copyPointerToArray(ptr, ptrOffset, array, arrayOffset, size);
    }

    private static void copyPointerToArray(int ptr, int ptrOffset, int[] array, int arrayOffset, int size) {
        MemoryManager.copyPointerToArray(ptr, ptrOffset, array, arrayOffset, size);
    }

    private static void copyPointerToArray(int ptr, int ptrOffset, float[] array, int arrayOffset, int size) {
        MemoryManager.copyPointerToArray(ptr, ptrOffset, array, arrayOffset, size);
    }

    private static void copyPointerToArray(int ptr, int ptrOffset, long[] array, int arrayOffset, int size) {
        MemoryManager.copyPointerToArray(ptr, ptrOffset, array, arrayOffset, size);
    }

    private static void copyPointerToArray(int ptr, int ptrOffset, double[] array, int arrayOffset, int size) {
        MemoryManager.copyPointerToArray(ptr, ptrOffset, array, arrayOffset, size);
    }

    private static void copyArrayToPointer(byte[] array, int arrayOffset, int ptr, int ptrOffset, int size) {
        MemoryManager.copyArrayToPointer(array, arrayOffset, ptr, ptrOffset, size);
    }

    private static void copyArrayToPointer(short[] array, int arrayOffset, int ptr, int ptrOffset, int size) {
        MemoryManager.copyArrayToPointer(array, arrayOffset, ptr, ptrOffset, size);
    }

    private static void copyArrayToPointer(char[] array, int arrayOffset, int ptr, int ptrOffset, int size) {
        MemoryManager.copyArrayToPointer(array, arrayOffset, ptr, ptrOffset, size);
    }

    private static void copyArrayToPointer(int[] array, int arrayOffset, int ptr, int ptrOffset, int size) {
        MemoryManager.copyArrayToPointer(array, arrayOffset, ptr, ptrOffset, size);
    }

    private static void copyArrayToPointer(float[] array, int arrayOffset, int ptr, int ptrOffset, int size) {
        MemoryManager.copyArrayToPointer(array, arrayOffset, ptr, ptrOffset, size);
    }

    private static void copyArrayToPointer(long[] array, int arrayOffset, int ptr, int ptrOffset, int size) {
        MemoryManager.copyArrayToPointer(array, arrayOffset, ptr, ptrOffset, size);
    }

    private static void copyArrayToPointer(double[] array, int arrayOffset, int ptr, int ptrOffset, int size) {
        MemoryManager.copyArrayToPointer(array, arrayOffset, ptr, ptrOffset, size);
    }

    private static byte getByteFromPointer(int ptr, int offset) {
        return MemoryManager.getByteFromPointer(ptr, offset);
    }

    private static short getShortFromPointer(int ptr, int offset) {
        return MemoryManager.getShortFromPointer(ptr, offset);
    }

    private static int getIntFromPointer(int ptr, int offset) {
        return MemoryManager.getIntFromPointer(ptr, offset);
    }

    private static long getLongFromPointer(int ptr, int offset) {
        return MemoryManager.getLongFromPointer(ptr, offset);
    }

    private static double getDoubleFromPointer(int ptr, int offset) {
        return MemoryManager.getDoubleFromPointer(ptr, offset);
    }

    private static float getFloatFromPointer(int ptr, int offset) {
        return MemoryManager.getFloatFromPointer(ptr, offset);
    }

    private static void setByteInPointer(int ptr, int offset, byte value) {
        MemoryManager.setByteInPointer(ptr, offset, value);
    }

    private static void setShortInPointer(int ptr, int offset, short value) {
        MemoryManager.setShortInPointer(ptr, offset, value);
    }

    private static void setIntInPointer(int ptr, int offset, int value) {
        MemoryManager.setIntInPointer(ptr, offset, value);
    }

    private static void setLongInPointer(int ptr, int offset, long value) {
        MemoryManager.setLongInPointer(ptr, offset, value);
    }

    private static void setFloatInPointer(int ptr, int offset, float value) {
        MemoryManager.setFloatInPointer(ptr, offset, value);
    }

    private static void setDoubleInPointer(int ptr, int offset, double value) {
        MemoryManager.setDoubleInPointer(ptr, offset, value);
    }
}
