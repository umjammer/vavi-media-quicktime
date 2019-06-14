package quicktime.util;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.QTNullPointerException;
import quicktime.QTObject;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.sound.SoundComponentData;
import quicktime.std.StdQTException;
import quicktime.std.image.GraphicsExporter;

/**
 * The QTPointer class implements a pointer that can be used in calls to QuickTime.
 */
public class QTPointer extends QTPointerRef implements QuickTimeLib, Cloneable {

    private static Object linkage;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(QTPointer.class);
                    }
                });
            }
        }.establish();
    }

    /**
     * The buffer that contains the sound data.
     * QuickTime::GraphicsExportGetInputPtr
     * @return the QTPointer buffer
     * @return {@link quicktime.std.StdQTException}
     * @see quicktime.std.image.GraphicsExporter.getInputPtr
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
     * @see {@link quicktime.sound.SoundComponentData#getBuffer()}
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
     * @return {@link quicktime.QTException}
     */
    public static QTPointer fromEncodedImage(EncodedImage paramEncodedImage) throws QTException {
        if ((paramEncodedImage instanceof ByteEncodedImage))
            return new QTPointer(((ByteEncodedImage) paramEncodedImage).getBytes());
        if ((paramEncodedImage instanceof IntEncodedImage)) {
            QTPointer localQTPointer = new QTPointer(paramEncodedImage.getSize(), false);
            int[] arrayOfInt = ((IntEncodedImage) paramEncodedImage).getInts();
            localQTPointer.copyFromArray(0, arrayOfInt, 0, arrayOfInt.length);
            return localQTPointer;
        }
        if ((paramEncodedImage instanceof RawEncodedImage)) {
            return (RawEncodedImage) paramEncodedImage;
        }
        return null;
    }

    /**
     * Allocate a pointer for general use.
     * @param size the size of the pointer to create.
     * @param clear clear the pointer upon creation.
     */
    public QTPointer(int paramInt, boolean paramBoolean) throws QTException {
        super(paramInt, paramBoolean);
    }

    /**
     * Construct a pointer from input data.
     * @param byteArray the input data.
     */
    public QTPointer(byte[] paramArrayOfByte) throws QTException {
        super(paramArrayOfByte.length, false);
        setBytesAt(0, paramArrayOfByte.length, paramArrayOfByte, 0);
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
    public QTPointer(QTHandleRef paramQTHandleRef, int paramInt1, int paramInt2) throws QTException {
        super(paramInt2 + paramInt1 > paramQTHandleRef.getSize() ? paramQTHandleRef.getSize() - paramInt1 : paramInt2, false);
        int i = paramQTHandleRef.lockAndDeref(paramInt1);
        BlockMove(i + 0, _ID() + 0, paramInt2);
        if (!paramQTHandleRef.isLocked()) {
            paramQTHandleRef.unlock();
        }
    }

    /**
     * Construct a pointer from the concatenation of the two supplied pointers.
     * The First pointer will appear first in the concatentated result.
     * @param firstPtr the first Pointer
     * @param secondPtr the second Pointer
     */
    public QTPointer(QTPointerRef paramQTPointerRef1, QTPointerRef paramQTPointerRef2) throws QTException {
        super(paramQTPointerRef1.getSize() + paramQTPointerRef2.getSize(), false);
        BlockMove(QTObject.ID(paramQTPointerRef1) + 0, _ID() + 0, paramQTPointerRef1.getSize());
        BlockMove(QTObject.ID(paramQTPointerRef2) + 0, _ID() + paramQTPointerRef1.getSize(), paramQTPointerRef2.getSize());
    }

    QTPointer(int paramInt1, int paramInt2, Object paramObject) {
        super(paramInt1, paramInt2, paramObject);
    }

    /**
     * Returns a copy of this object
     */
    public Object clone() {
        return new QTPointer(makeAndCopyPtr(0, getSize()), getSize(), null);
    }

    /**
     * This method will resize the memory that a pointer points to.
     * This operation can fail if memory cannot increase its size
     * QuickTime::SetPtrSize
     * @param newSize the new size
     * @param {@link quicktime.util.UtilException}
     */
    public void setSize(int paramInt) throws UtilException {
        SetPtrSize(_ID(), paramInt);
        UtilException.checkError(MemError());
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

    private static native int GraphicsExportGetInputPtr(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2);

    private static native void BlockMove(int paramInt1, int paramInt2, int paramInt3);

    private static native void SetPtrSize(int paramInt1, int paramInt2);

    private static native short MemError();

    private static native int getIntFromArray(byte[] paramArrayOfByte, int paramInt);

    private static native int getIntFromArray(int[] paramArrayOfInt, int paramInt);
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/util/
 * QTPointer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
