package quicktime.std.image;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.util.ByteEncodedImage;
import quicktime.util.EncodedImage;
import quicktime.util.IntEncodedImage;
import quicktime.util.QTHandleRef;
import quicktime.util.RawEncodedImage;

/**
 * QuickTime 2.1 introduced support for an arbitrary number of sources of data
 * for an image sequence. This functionality forms the basis for dynamically
 * modifying parameters to a decompressor. It also allows for codecs to act as
 * special effects components, providing filtering and transition type effects. A
 * client can attach an arbitrary number of additional inputs to the codec. It is up
 * to the particular codec to determine whether to use each input and how to
 * interpret the input. For example, an 8-bit gray image could be interpreted as a
 * blend mask or as a replacement for one of the RGB data planes.
 */
public final class ImageSequenceDataSource extends QTObject implements QuickTimeLib {

    static Object linkage;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(ImageSequenceDataSource.class);
                    }
                });
            }
        }.establish();
    }

    private static int allocate(CDSequence paramCDSequence, int paramInt1, int paramInt2, QTHandleRef paramQTHandleRef) throws StdQTException {
        int[] arrayOfInt = { 0 };
        StdQTException.checkError(CDSequenceNewDataSource(QTObject.ID(paramCDSequence), arrayOfInt, paramInt1, paramInt2, QTObject.ID(paramQTHandleRef), 0, 0));
        return arrayOfInt[0];
    }

    private static int allocate(CDSequence paramCDSequence, int paramInt1, int paramInt2) throws StdQTException {
        int[] arrayOfInt = { 0 };
        StdQTException.checkError(CDSequenceGetDataSource(QTObject.ID(paramCDSequence), arrayOfInt, paramInt1, paramInt2));
        return arrayOfInt[0];
    }

    /**
     * Creates a new data source for an ImageSequence.
     * QuickTime::CDSequenceNewDataSource
     * @param sourceType A four-character code describing how the input will be used. This code is usually derived from the information returned by the codec. For example, if a mask plane was passed, this field might contain ÔmaskÕ.
     * @param sourceInputNumber More than one instance of a given source type may exist. The first occurrence should have a source input number of 1, and so on.
     * @param dataDescription A handle to a data structure describing the input data. For compressed image data, this is just an ImageDescriptionHandle.
     */
    public ImageSequenceDataSource(CDSequence paramCDSequence, int paramInt1, int paramInt2, QTHandleRef paramQTHandleRef) throws QTException {
        super(allocate(paramCDSequence, paramInt1, paramInt2, paramQTHandleRef));
    }

    /**
     * This will retrieve a previously created data source for an ImageSequence.
     * QuickTime::CDSequenceGetDataSource
     * @param sourceType A four-character code describing how the input will be used. This code is usually derived from the information returned by the codec. For example, if a mask plane was passed, this field might contain ÔmaskÕ.
     * @param sourceInputNumber More than one instance of a given source type may exist. The first occurrence should have a source input number of 1, and so on.
     */
    public ImageSequenceDataSource(CDSequence paramCDSequence, int paramInt1, int paramInt2) throws QTException {
        super(allocate(paramCDSequence, paramInt1, paramInt2));
    }

    private boolean isValid = true;

    /**
     * Notifies the compressor that the image source data has changed. Thus if the data is
     * a PixMap the data source itself is unchanged by when you draw into the PixMap the
     * contents of the PixMap have changed - so you use this method to notify the ImageSequence
     * that the data has changed.
     * QuickTime::CDSequenceChangedSourceData
     */
    public void changedSourceData() throws StdQTException {
        if (!this.isValid)
            throw new StdQTException(-50);
        StdQTException.checkError(CDSequenceChangedSourceData(_ID()));
    }

    /**
     * The CDSequenceSetSourceData function is called to set data in a new frame to a
     * specific source. For example, as a new frame of compressed data arrives at a
     * source, CDSequenceSetSourceData will be called.
     * QuickTime::CDSequenceSetSourceData
     * @param data the new encoded image data for the DataSource.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setSourceData(EncodedImage paramEncodedImage) throws StdQTException {
        if (!this.isValid)
            throw new StdQTException(-50);
        int i = -50;
        if ((paramEncodedImage instanceof ByteEncodedImage)) {
            i = CDSequenceSetSourceData(_ID(), ((ByteEncodedImage) paramEncodedImage).getBytes(), paramEncodedImage.getSize());
        } else if ((paramEncodedImage instanceof IntEncodedImage)) {
            i = CDSequenceSetSourceData(_ID(), ((IntEncodedImage) paramEncodedImage).getInts(), paramEncodedImage.getSize());
        } else if ((paramEncodedImage instanceof RawEncodedImage))
            i = CDSequenceSetSourceData(_ID(), QTObject.ID((RawEncodedImage) paramEncodedImage), paramEncodedImage.getSize());
        StdQTException.checkError(i);
    }

    /**
     * All data sources are diposed of with the sequence.
     * However if you want to remove a previously created data source for an image sequence
     * an application can use this call to dispose of this data source. Once called this object
     * is invalid and consequent calls on this object will return an error
     * QuickTime::CDSequenceDisposeDataSource
     */
    public void dispose() throws StdQTException {
        StdQTException.checkError(CDSequenceDisposeDataSource(_ID()));
        this.isValid = false;
    }

    private static native short CDSequenceNewDataSource(int paramInt1, int[] paramArrayOfInt, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6);

    private static native short CDSequenceGetDataSource(int paramInt1, int[] paramArrayOfInt, int paramInt2, int paramInt3);

    private static native short CDSequenceChangedSourceData(int paramInt);

    private static native short CDSequenceSetSourceData(int paramInt1, byte[] paramArrayOfByte, int paramInt2);

    private static native short CDSequenceSetSourceData(int paramInt1, int[] paramArrayOfInt, int paramInt2);

    private static native short CDSequenceSetSourceData(int paramInt1, int paramInt2, int paramInt3);

    private static native short CDSequenceDisposeDataSource(int paramInt);
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/image
 * /ImageSequenceDataSource.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
