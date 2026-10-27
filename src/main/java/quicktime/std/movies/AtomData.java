package quicktime.std.movies;

import quicktime.QTObject;
import quicktime.internal.toolbox.ImageCompressionManager;
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

    /**
     * This method is used to find the first atom of a specified type within a vector data stream and get its data.
     * @see "QuickTime::CurveGetAtomDataFromVectorStream"
     * @param vectorStream Contains a handle to the vector data stream from which to get the atom.
     * @param atomType Specifies the type of atom to find.
     * @return the atom's data.
     * @throws quicktime.std.StdQTException
     */
    public static AtomData fromVectorStream(Curve paramCurve, VectorStream vectorStream, int atomType) throws StdQTException {
        int[] arrayOfInt1 = { 0 };
        int[] arrayOfInt2 = { 0 };
        vectorStream.lock();
        int i = CurveGetAtomDataFromVectorStream(QTObject.ID(paramCurve), QTObject.ID(vectorStream), atomType, arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(i);
        vectorStream.unlock();
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
    public byte getByte(int offset) throws IndexOutOfBoundsException {
        checkOffset(offset);
        return getByteAt(offset);
    }

    /**
     * Get the short value at the given position(offset) within the native object.
     * @param offset the given number of bytes to offset
     * @return the short value at the offset.
     * @throws java.lang.IndexOutOfBoundsException is thrown if the offset is larger than the size of the data
     */
    public short getShort(int offset) throws IndexOutOfBoundsException {
        checkOffset(offset);
        return getShortAt(offset);
    }

    /**
     * Get the int value at the given position(offset) within the native object.
     * @param offset the given number of bytes to offset
     * @return the int value at the offset.
     * @throws java.lang.IndexOutOfBoundsException is thrown if the offset is larger than the size of the data
     */
    public int getInt(int offset) throws IndexOutOfBoundsException {
        checkOffset(offset);
        return getIntAt(offset);
    }

    /**
     * Get the long value at the given position(offset) within the native object.
     * @param offset the given number of bytes to offset
     * @return the long value at the offset.
     * @throws java.lang.IndexOutOfBoundsException is thrown if the offset is larger than the size of the data
     */
    public long getLong(int offset) throws IndexOutOfBoundsException {
        checkOffset(offset);
        return getLongAt(offset);
    }

    /**
     * Get the float value at the given position(offset) within the native object.
     * @param offset the given number of bytes to offset
     * @return the float value at the offset.
     * @throws java.lang.IndexOutOfBoundsException is thrown if the offset is larger than the size of the data
     */
    public float getFloat(int offset) throws IndexOutOfBoundsException {
        checkOffset(offset);
        return getFloatAt(offset);
    }

    /**
     * Get the double value at the given position(offset) within the native object.
     * @param offset the given number of bytes to offset
     * @return the double value at the offset.
     * @throws java.lang.IndexOutOfBoundsException is thrown if the offset is larger than the size of the data
     */
    public double getDouble(int offset) throws IndexOutOfBoundsException {
        checkOffset(offset);
        return getDoubleAt(offset);
    }

    /**
     * Returns the C string found at the offset as a java.lang.String
     * @param offset the given number of bytes to offset
     * @return java.lang.String representation
     * @throws java.lang.IndexOutOfBoundsException is thrown if the offset is larger than the size of the data
     * @throws quicktime.util.UtilException
     */
    public String getCString(int offset) throws UtilException, IndexOutOfBoundsException {
        checkOffset(offset);
        return getCStringAt(offset);
    }

    /**
     * Returns the Pascal string found at the offset as a java.lang.String
     * @param offset the given number of bytes to offset
     * @return java.lang.String representation
     * @throws java.lang.IndexOutOfBoundsException is thrown if the offset is larger than the size of the data
     * @throws quicktime.util.UtilException
     */
    public String getPString(int offset) throws UtilException, IndexOutOfBoundsException {
        checkOffset(offset);
        return getPStringAt(offset);
    }

    /**
     * Returns the byte values at the given position(offset) within the object's memory area.
     * @param offset the given number of bytes to locate the struct.
     * @param length the given number of bytes of memory. The maximum length that will be copied is
     *  (size - offset) bytes. If zero then this will return all of the available bytes from the offset.
     * @return the byte array
     * @throws java.lang.IndexOutOfBoundsException is thrown if the offset is larger than the size of the data
     */
    public byte[] getBytes(int offset, int length) throws IndexOutOfBoundsException {
        checkOffset(offset);
        int i = getSize() - offset;
        int j = i > length ? length : length == 0 ? i : i;
        byte[] arrayOfByte = new byte[j];
        getBytesAt(offset, j, arrayOfByte, 0);
        return arrayOfByte;
    }

    private final void checkOffset(int paramInt) throws IndexOutOfBoundsException {
        if (paramInt > getSize())
            throw new IndexOutOfBoundsException();
    }

    private static int CurveGetAtomDataFromVectorStream(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        return ImageCompressionManager.CurveGetAtomDataFromVectorStream(paramInt1, paramInt2, paramInt3, paramArrayOfInt1, paramArrayOfInt2);
    }
}

