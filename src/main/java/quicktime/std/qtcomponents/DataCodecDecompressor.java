package quicktime.std.qtcomponents;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.std.comp.Component;
import quicktime.std.comp.ComponentIdentifier;
import quicktime.util.QTPointerRef;

/**
 * This class is used to decompress data given an appropriate compressor type
 * which is the compressor used to compress the data.
 */
public class DataCodecDecompressor extends Component implements QuickTimeLib {

    private static Object linkage;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(DataCodecDecompressor.class);
                    }
                });
            }
        }.establish();
    }

    /**
     * Opens the specified data codec decompressor component, 'ddec', with the given subType.
     * QuickTime::OpenADefaultComponent
     * @param subType compressor type used to compress the data that will be decompressed
     */
    public DataCodecDecompressor(int paramInt) throws QTException {
        super(1684301155, paramInt);
    }

    /**
     * Given a component identifier it opens the specified component. The component type
     * must be 'ddec' or an exception will be thrown as it is not a DataCodecDecompressor.
     * QuickTime::OpenAComponent
     * @param comp the component identifier to open
     */
    public DataCodecDecompressor(ComponentIdentifier paramComponentIdentifier) throws QTException {
        super(paramComponentIdentifier, 1684301155);
    }

    /**
     * The DataCodecDecompress function decompresses data using the specified
     * compressor component. Before you decompress the data the size required to
     * store the data should be retrieved, which is normally stored at the beginning of the file.
     * QuickTime::DataCodecDecompress
     * @param srcData Contains a pointer to the data to be decompressed.
     * @param dstData Contains a pointer to the buffer in which to store the decompressed data.
     * @param {@link quicktime.std.StdQTException}
     */
    public void decompress(QTPointerRef paramQTPointerRef1, QTPointerRef paramQTPointerRef2) throws StdQTException {
        StdQTException.checkError(DataCodecDecompress(_ID(), QTObject.ID(paramQTPointerRef1), paramQTPointerRef1.getSize(), QTObject.ID(paramQTPointerRef2), paramQTPointerRef2.getSize()));
    }

    private static native int DataCodecDecompress(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);
}
