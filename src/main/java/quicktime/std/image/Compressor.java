package quicktime.std.image;

import quicktime.QTObject;
import quicktime.internal.toolbox.ImageCompressionManager;
import quicktime.internal.toolbox.SequenceGrabberManager;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.std.sg.SGVideoChannel;
import quicktime.std.sg.SGVideoCompressorInfo;


/**
 * Corresponds to the compressor component type
 * refer to CompressorComponent in QuickTime
 */
public final class Compressor extends CodecComponent implements QuickTimeLib {

    Compressor(int paramInt) {
        super(paramInt);
    }

    /**
     * Get a channelÕs current image-compression parameters.
     * @see "QuickTime::SGGetVideoCompressor"
     */
    public static SGVideoCompressorInfo fromSGVideoChannel(SGVideoChannel paramSGVideoChannel) throws StdQTException {
        short[] arrayOfShort = new short[1];
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int[] arrayOfInt3 = new int[1];
        int[] arrayOfInt4 = new int[1];
        int i = SGGetVideoCompressor(QTObject.ID(paramSGVideoChannel), arrayOfShort, arrayOfInt1, arrayOfInt2, arrayOfInt3, arrayOfInt4);
        StdQTException.checkError(i);
        return new SGVideoCompressorInfo(arrayOfShort[0], new Compressor(arrayOfInt1[0]), arrayOfInt2[0], arrayOfInt3[0], arrayOfInt4[0]);
    }

    /**
     * determines which of the installed compressors has been chosen to field requests
     * made using one of the special compressor identifiers.
     * @see "see QuickTime::FindCodec"
     * @param cType specifies the CodecComponent class.
     * @param specCodec contains a special identifier value.
     * @throws quicktime.std.StdQTException
     */
    public static Compressor findCodec(int cType, CodecComponent specCodec) throws StdQTException {
        int[] arrayOfInt = new int[1];
        int codec = FindCodec(cType, QTObject.ID(specCodec), arrayOfInt, 0);
        StdQTException.checkError(codec);
        Compressor localCompressor = new Compressor(arrayOfInt[0]);
        return localCompressor;
    }

    private static short FindCodec(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3) {
        return ImageCompressionManager.FindCodec(paramInt1, paramInt2, paramArrayOfInt, paramInt3);
    }

    private static int SGGetVideoCompressor(int paramInt, short[] paramArrayOfShort, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3, int[] paramArrayOfInt4) {
        return SequenceGrabberManager.SGGetVideoCompressor(paramInt, paramArrayOfShort, paramArrayOfInt1, paramArrayOfInt2, paramArrayOfInt3, paramArrayOfInt4);
    }
}
