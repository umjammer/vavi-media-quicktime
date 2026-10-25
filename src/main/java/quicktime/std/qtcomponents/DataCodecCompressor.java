package quicktime.std.qtcomponents;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.ImageCompressionManager;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.std.comp.Component;
import quicktime.std.comp.ComponentIdentifier;
import quicktime.util.QTPointerRef;

/**
 * Compresses data using the specified compressor
 */
public class DataCodecCompressor extends Component implements QuickTimeLib {

    private static Object linkage;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(DataCodecCompressor.class);
                    }
                });
            }
        }.establish();
    }

    /**
     * Opens the specified data codec compressor component, 'dcom', with the given subType.
     * QuickTime::OpenADefaultComponent
     * @param subType the compressor type that will be used to compress the data
     */
    public DataCodecCompressor(int paramInt) throws QTException {
        super(1684238189, paramInt);
    }

    /**
     * Given a component identifier it opens the specified component. The component type
     * must be 'dcom' or an exception will be thrown as it is not a DataCodecCompressor.
     * QuickTime::OpenAComponent
     * @param comp the component identifier to open
     */
    public DataCodecCompressor(ComponentIdentifier paramComponentIdentifier) throws QTException {
        super(paramComponentIdentifier, 1684238189);
    }

    /**
     * Compress the specified data.
     * Before calling the DataCodecCompress function, you should call the
     * DataCodecGetCompressBufferSize function to obtain the maximum possible size
     * of the compressed data that will be returned. You can then use this value as the
     * value of the dstBufferSize parameter. Note that a buffer for compressed data
     * that is the same size as the uncompressed data may not be large enough: in
     * some cases, the size of the compressed data can be larger than the size of the
     * decompressed data.
     * QuickTime::DataCodecCompress
     * @param srcData Contains a pointer to the data to be compressed.
     * @param dstData Contains a pointer to the buffer in which to store the compressed data.
     * @return a DataCodecInfo that contains information about the compression.
     * @return {@link quicktime.std.StdQTException}
     */
    public DataCodecInfo compress(QTPointerRef paramQTPointerRef1, QTPointerRef paramQTPointerRef2) throws StdQTException {
        int[] arrayOfInt1 = { 0 };
        int[] arrayOfInt2 = { 0 };
        int i = DataCodecCompress(_ID(), QTObject.ID(paramQTPointerRef1), paramQTPointerRef1.getSize(), QTObject.ID(paramQTPointerRef2), paramQTPointerRef2.getSize(), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(i);
        return new DataCodecInfo(arrayOfInt1[0], arrayOfInt2[0]);
    }

    /**
     * Returns the estimated maximum size of compressing the specified size bytes
     * using this compressor.
     * QuickTime::DataCodecGetCompressBufferSize
     * @param srcSize the size of the buffer that will be compressed.
     * @return the size of the buffer you should allocate to contain the specified srcSize data
     * @return {@link quicktime.std.StdQTException}
     */
    public int getCompressBufferSize(int paramInt) throws StdQTException {
        int[] arrayOfInt = { 0 };
        StdQTException.checkError(DataCodecGetCompressBufferSize(_ID(), paramInt, arrayOfInt));
        return arrayOfInt[0];
    }

    private static int DataCodecCompress(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        return ImageCompressionManager.DataCodecCompress(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramArrayOfInt1, paramArrayOfInt2);
    }

    private static int DataCodecGetCompressBufferSize(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        return ImageCompressionManager.DataCodecGetCompressBufferSize(paramInt1, paramInt2, paramArrayOfInt);
    }
}
