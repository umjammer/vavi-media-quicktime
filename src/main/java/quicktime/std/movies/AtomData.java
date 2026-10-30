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
    public static AtomData fromVectorStream(Curve effect, VectorStream vectorStream, int atomType) throws StdQTException {
        int[] dataSize = { 0 };
        int[] dataPtr = { 0 };
        vectorStream.lock();
        int i = CurveGetAtomDataFromVectorStream(QTObject.ID(effect), QTObject.ID(vectorStream), atomType, dataSize, dataPtr);
        StdQTException.checkError(i);
        vectorStream.unlock();
        return new AtomData(dataPtr[0], dataSize[0], effect);
    }

    AtomData(int nativeRep, int size, Object ref) {
        super(nativeRep, size, ref);
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
        byte[] bytes = new byte[j];
        getBytesAt(offset, j, bytes, 0);
        return bytes;
    }

    private void checkOffset(int offset) throws IndexOutOfBoundsException {
        if (offset > getSize())
            throw new IndexOutOfBoundsException();
    }

    private static int CurveGetAtomDataFromVectorStream(int effect, int stream, int atomType, int[] dataSize, int[] dataPtr) {
        return ImageCompressionManager.CurveGetAtomDataFromVectorStream(effect, stream, atomType, dataSize, dataPtr);
    }
}
