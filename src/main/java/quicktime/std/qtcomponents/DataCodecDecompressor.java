package quicktime.std.qtcomponents;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.ImageCompressionManager;
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

    /**
     * Opens the specified data codec decompressor component, 'ddec', with the given subType.
     * @see "QuickTime::OpenADefaultComponent"
     * @param subType compressor type used to compress the data that will be decompressed
     */
    public DataCodecDecompressor(int subType) throws QTException {
        super(1684301155, subType);
    }

    /**
     * Given a component identifier it opens the specified component. The component type
     * must be 'ddec' or an exception will be thrown as it is not a DataCodecDecompressor.
     * @see "QuickTime::OpenAComponent"
     * @param comp the component identifier to open
     */
    public DataCodecDecompressor(ComponentIdentifier comp) throws QTException {
        super(comp, 1684301155);
    }

    /**
     * The DataCodecDecompress function decompresses data using the specified
     * compressor component. Before you decompress the data the size required to
     * store the data should be retrieved, which is normally stored at the beginning of the file.
     * @see "QuickTime::DataCodecDecompress"
     * @param srcData Contains a pointer to the data to be decompressed.
     * @param dstData Contains a pointer to the buffer in which to store the decompressed data.
     * @throws quicktime.std.StdQTException
     */
    public void decompress(QTPointerRef srcData, QTPointerRef dstData) throws StdQTException {
        StdQTException.checkError(DataCodecDecompress(_ID(), QTObject.ID(srcData), srcData.getSize(), QTObject.ID(dstData), dstData.getSize()));
    }

    private static int DataCodecDecompress(int dc, int srcData, int srcSize, int dstData, int dstBufferSize) {
        return ImageCompressionManager.DataCodecDecompress(dc, srcData, srcSize, dstData, dstBufferSize);
    }
}
