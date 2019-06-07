package quicktime.std.movies;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTObject;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.std.image.Curve;
import quicktime.std.image.VectorStream;
import quicktime.util.QTPointerRef;
import quicktime.util.UtilException;

/**
 * This is a Read Only structure that is used to read the values that are stored in an atom of an
 * AtomContainer. To store or alter these values you should use the appropriate methods in the
 * AtomContainer class.
 */
public final class AtomData extends QTPointerRef implements QuickTimeLib {

    private static Object linkage;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(AtomData.class);
                    }
                });
            }
        }.establish();
    }

    /**
     * This method is used to find the first atom of a specified type within a vector data stream and get its data.
     * QuickTime::CurveGetAtomDataFromVectorStream
     * @param vectorStream Contains a handle to the vector data stream from which to get the atom.
     * @param atomType Specifies the type of atom to find.
     * @return the atom's data.
     * @return {@link quicktime.std.StdQTException}
     */
    public static AtomData fromVectorStream(Curve paramCurve, VectorStream paramVectorStream, int paramInt) throws StdQTException {
        int[] arrayOfInt1 = { 0 };
        int[] arrayOfInt2 = { 0 };
        paramVectorStream.lock();
        int i = CurveGetAtomDataFromVectorStream(QTObject.ID(paramCurve), QTObject.ID(paramVectorStream), paramInt, arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(i);
        paramVectorStream.unlock();
        return new AtomData(arrayOfInt2[0], arrayOfInt1[0], paramCurve);
    }

    AtomData(int paramInt1, int paramInt2, Object paramObject) {
        super(paramInt1, paramInt2, paramObject);
    }

    /**
     * Get the byte value at the given position(offset) within the native object.
     * @param offset the given number of bytes to offset
     * @return the byte value at the offset.
     * @throws java.lang.IndexOutOfBoundsException is thrown if the offset is larger than the size of the data
     */
    public byte getByte(int paramInt) throws IndexOutOfBoundsException {
        checkOffset(paramInt);
        return getByteAt(paramInt);
    }

    /**
     * Get the short value at the given position(offset) within the native object.
     * @param offset the given number of bytes to offset
     * @return the short value at the offset.
     * @throws java.lang.IndexOutOfBoundsException is thrown if the offset is larger than the size of the data
     */
    public short getShort(int paramInt) throws IndexOutOfBoundsException {
        checkOffset(paramInt);
        return getShortAt(paramInt);
    }

    /**
     * Get the int value at the given position(offset) within the native object.
     * @param offset the given number of bytes to offset
     * @return the int value at the offset.
     * @throws java.lang.IndexOutOfBoundsException is thrown if the offset is larger than the size of the data
     */
    public int getInt(int paramInt) throws IndexOutOfBoundsException {
        checkOffset(paramInt);
        return getIntAt(paramInt);
    }

    /**
     * Get the long value at the given position(offset) within the native object.
     * @param offset the given number of bytes to offset
     * @return the long value at the offset.
     * @throws java.lang.IndexOutOfBoundsException is thrown if the offset is larger than the size of the data
     */
    public long getLong(int paramInt) throws IndexOutOfBoundsException {
        checkOffset(paramInt);
        return getLongAt(paramInt);
    }

    /**
     * Get the float value at the given position(offset) within the native object.
     * @param offset the given number of bytes to offset
     * @return the float value at the offset.
     * @throws java.lang.IndexOutOfBoundsException is thrown if the offset is larger than the size of the data
     */
    public float getFloat(int paramInt) throws IndexOutOfBoundsException {
        checkOffset(paramInt);
        return getFloatAt(paramInt);
    }

    /**
     * Get the double value at the given position(offset) within the native object.
     * @param offset the given number of bytes to offset
     * @return the double value at the offset.
     * @throws java.lang.IndexOutOfBoundsException is thrown if the offset is larger than the size of the data
     */
    public double getDouble(int paramInt) throws IndexOutOfBoundsException {
        checkOffset(paramInt);
        return getDoubleAt(paramInt);
    }

    /**
     * Returns the C string found at the offset as a java.lang.String
     * @param offset the given number of bytes to offset
     * @return java.lang.String representation
     * @throws java.lang.IndexOutOfBoundsException is thrown if the offset is larger than the size of the data
     * @throws quicktime.util.UtilException
     */
    public String getCString(int paramInt) throws UtilException, IndexOutOfBoundsException {
        checkOffset(paramInt);
        return getCStringAt(paramInt);
    }

    /**
     * Returns the Pascal string found at the offset as a java.lang.String
     * @param offset the given number of bytes to offset
     * @return java.lang.String representation
     * @throws java.lang.IndexOutOfBoundsException is thrown if the offset is larger than the size of the data
     * @throws quicktime.util.UtilException
     */
    public String getPString(int paramInt) throws UtilException, IndexOutOfBoundsException {
        checkOffset(paramInt);
        return getPStringAt(paramInt);
    }

    public byte[] getBytes(int paramInt1, int paramInt2) throws IndexOutOfBoundsException {
        checkOffset(paramInt1);
        int i = getSize() - paramInt1;
        int j = i > paramInt2 ? paramInt2 : paramInt2 == 0 ? i : i;
        byte[] arrayOfByte = new byte[j];
        getBytesAt(paramInt1, j, arrayOfByte, 0);
        return arrayOfByte;
    }

    private final void checkOffset(int paramInt) throws IndexOutOfBoundsException {
        if (paramInt > getSize())
            throw new IndexOutOfBoundsException();
    }

    private static native int CurveGetAtomDataFromVectorStream(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt1, int[] paramArrayOfInt2);
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/AtomData.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
