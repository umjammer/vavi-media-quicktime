package quicktime.util;

import quicktime.QTException;
import quicktime.QTNullPointerException;
import quicktime.QTObject;
import quicktime.internal.toolbox.GraphicsExportManager;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.QuickTimeLib;
import quicktime.sound.SoundComponentData;
import quicktime.std.StdQTException;
import quicktime.std.image.GraphicsExporter;


/**
 * The QTPointer class implements a pointer that can be used in calls to QuickTime.
 */
public class QTPointer extends QTPointerRef implements QuickTimeLib, Cloneable {

    /**
     * The buffer that contains the sound data.
     * @see "QuickTime::GraphicsExportGetInputPtr"
     * @return the QTPointer buffer
     * @throws quicktime.std.StdQTException
     * @see quicktime.std.image.GraphicsExporter#getInputPtr
     */
    public static QTPointerRef fromGraphicsExporter(GraphicsExporter paramGraphicsExporter) throws StdQTException {
        int[] arrayOfInt1 = { 0 };
        int[] arrayOfInt2 = new int[1];
        int i = GraphicsExportGetInputPtr(QTObject.ID(paramGraphicsExporter), arrayOfInt2, arrayOfInt1);
        StdQTException.checkError(i);
        return new QTPointerRef(arrayOfInt2[0], arrayOfInt1[0], paramGraphicsExporter);
    }

    /**
     * The buffer that contains the sound data.
     * @return the QTPointer buffer
     * @see quicktime.sound.SoundComponentData#getBuffer()
     */
    public static QTPointer fromSCData(SoundComponentData paramSoundComponentData) {
        if (getIntFromArray(paramSoundComponentData.getBytes(), 20) == 0)
            throw new QTNullPointerException("SCD buffer is not set");
        return new QTPointer(getIntFromArray(paramSoundComponentData.getBytes(), 20), paramSoundComponentData.getSampleSize() / 8 * paramSoundComponentData.getSampleCount(), paramSoundComponentData);
    }

    /**
     * Returns EncodedImage data as a QTPointer. This will copy the data if the source
     * is an int or byte encoded image, but will return the same object if it is a RawEncodedImage.
     * @param image the encoded image data
     * @return the encoded image data as a Pointer
     * @throws quicktime.QTException
     */
    public static QTPointer fromEncodedImage(EncodedImage image) throws QTException {
        if ((image instanceof ByteEncodedImage))
            return new QTPointer(((ByteEncodedImage) image).getBytes());
        if ((image instanceof IntEncodedImage)) {
            QTPointer localQTPointer = new QTPointer(image.getSize(), false);
            int[] arrayOfInt = ((IntEncodedImage) image).getInts();
            localQTPointer.copyFromArray(0, arrayOfInt, 0, arrayOfInt.length);
            return localQTPointer;
        }
        if ((image instanceof RawEncodedImage)) {
            return (RawEncodedImage) image;
        }
        return null;
    }

    /**
     * Allocate a pointer for general use.
     * @param size the size of the pointer to create.
     * @param clear clear the pointer upon creation.
     */
    public QTPointer(int size, boolean clear) throws QTException {
        super(size, clear);
    }

    /**
     * Construct a pointer from input data.
     * @param byteArray the input data.
     */
    public QTPointer(byte[] byteArray) throws QTException {
        super(byteArray.length, false);
        setBytesAt(0, byteArray.length, byteArray, 0);
    }

    /**
     * This constructor is used to create a QTPointer object from a Handle.
     * The contents of the handle will be copied into the pointer. This will copy
     * size bytes from the handle + offset. It will bounds check the size to be not
     * greater than the size of the handle.
     * @param hdl the handle to copy.
     * @param offset you can specify an offset within the handle from which to copy the data
     * @param size the amount of bytes to copy
     */
    public QTPointer(QTHandleRef hdl, int offset, int size) throws QTException {
        super(size + offset > hdl.getSize() ? hdl.getSize() - offset : size, false);
        int i = hdl.lockAndDeref(offset);
        BlockMove(i + 0, _ID() + 0, size);
        if (!hdl.isLocked()) {
            hdl.unlock();
        }
    }

    /**
     * Construct a pointer from the concatenation of the two supplied pointers.
     * The First pointer will appear first in the concatentated result.
     * @param firstPtr the first Pointer
     * @param secondPtr the second Pointer
     */
    public QTPointer(QTPointerRef firstPtr, QTPointerRef secondPtr) throws QTException {
        super(firstPtr.getSize() + secondPtr.getSize(), false);
        BlockMove(QTObject.ID(firstPtr) + 0, _ID() + 0, firstPtr.getSize());
        BlockMove(QTObject.ID(secondPtr) + 0, _ID() + firstPtr.getSize(), secondPtr.getSize());
    }

    QTPointer(int paramInt1, int paramInt2, Object paramObject) {
        super(paramInt1, paramInt2, paramObject);
    }

    /**
     * Returns a copy of this object
     */
    @Override
    public Object clone() {
        return new QTPointer(makeAndCopyPtr(0, getSize()), getSize(), null);
    }

    /**
     * This method will resize the memory that a pointer points to.
     * This operation can fail if memory cannot increase its size
     * @see "QuickTime::SetPtrSize"
     * @param newSize the new size
     * @throws quicktime.util.UtilException
     */
    public void setSize(int newSize) throws UtilException {
        SetPtrSize(_ID(), newSize);
        UtilException.checkError(MemError());
        setSizeField(newSize);
    }

    /**
     * Concatenates the supplied pointer to this pointer. This operation will fail
     * if unable to resize the pointer.
     */
    public void concatenate(QTPointerRef paramQTPointerRef) throws UtilException {
        int i = getSize();
        setSize(getSize() + paramQTPointerRef.getSize());
        BlockMove(QTObject.ID(paramQTPointerRef) + 0, _ID() + i, paramQTPointerRef.getSize());
    }

    private static int GraphicsExportGetInputPtr(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        return GraphicsExportManager.GraphicsExportGetInputPtr(paramInt, paramArrayOfInt1, paramArrayOfInt2);
    }

    private static void BlockMove(int paramInt1, int paramInt2, int paramInt3) {
        MemoryManager.BlockMove(paramInt1, paramInt2, paramInt3);
    }

    private static void SetPtrSize(int paramInt1, int paramInt2) {
        MemoryManager.SetPtrSize(paramInt1, paramInt2);
    }

    private static short MemError() {
        return MemoryManager.MemError();
    }

    private static int getIntFromArray(byte[] paramArrayOfByte, int paramInt) {
        return MemoryManager.getIntFromArray(paramArrayOfByte, paramInt);
    }

    private static int getIntFromArray(int[] paramArrayOfInt, int paramInt) {
        return MemoryManager.getIntFromArray(paramArrayOfInt, paramInt);
    }
}
