
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

    private static int allocate(CDSequence paramCDSequence,
                                int paramInt1,
                                int paramInt2,
                                QTHandleRef paramQTHandleRef) throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        StdQTException
                .checkError(CDSequenceNewDataSource(QTObject.ID(paramCDSequence),
                                                    arrayOfInt,
                                                    paramInt1,
                                                    paramInt2,
                                                    QTObject.ID(paramQTHandleRef),
                                                    0,
                                                    0));

        return arrayOfInt[0];
    }

    private static int allocate(CDSequence paramCDSequence, int paramInt1, int paramInt2) throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        StdQTException.checkError(CDSequenceGetDataSource(QTObject.ID(paramCDSequence), arrayOfInt, paramInt1, paramInt2));

        return arrayOfInt[0];
    }

    public ImageSequenceDataSource(CDSequence paramCDSequence,
            int paramInt1,
            int paramInt2,
            QTHandleRef paramQTHandleRef) throws QTException {
        super(allocate(paramCDSequence, paramInt1, paramInt2, paramQTHandleRef));
    }

    public ImageSequenceDataSource(CDSequence paramCDSequence, int paramInt1, int paramInt2) throws QTException {
        super(allocate(paramCDSequence, paramInt1, paramInt2));
    }

    private boolean isValid = true;

    public void changedSourceData() throws StdQTException {
        if (!this.isValid)
            throw new StdQTException(-50);
        StdQTException.checkError(CDSequenceChangedSourceData(_ID()));
    }

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

    public void dispose() throws StdQTException {
        StdQTException.checkError(CDSequenceDisposeDataSource(_ID()));
        this.isValid = false;
    }

    private static native short CDSequenceNewDataSource(int paramInt1,
                                                        int[] paramArrayOfInt,
                                                        int paramInt2,
                                                        int paramInt3,
                                                        int paramInt4,
                                                        int paramInt5,
                                                        int paramInt6);

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
