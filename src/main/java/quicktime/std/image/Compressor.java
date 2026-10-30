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

    Compressor(int nativeRep) {
        super(nativeRep);
    }

    /**
     * Get a channelÕs current image-compression parameters.
     * @see "QuickTime::SGGetVideoCompressor"
     */
    public static SGVideoCompressorInfo fromSGVideoChannel(SGVideoChannel ref) throws StdQTException {
        short[] depth = new short[1];
        int[] compressor = new int[1];
        int[] spatialQuality = new int[1];
        int[] temporalQuality = new int[1];
        int[] keyFrameRate = new int[1];
        int i = SGGetVideoCompressor(QTObject.ID(ref), depth, compressor, spatialQuality, temporalQuality, keyFrameRate);
        StdQTException.checkError(i);
        return new SGVideoCompressorInfo(depth[0], new Compressor(compressor[0]), spatialQuality[0], temporalQuality[0], keyFrameRate[0]);
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
        int[] compressor = new int[1];
        int codec = FindCodec(cType, QTObject.ID(specCodec), compressor, 0);
        StdQTException.checkError(codec);
        Compressor compressor2 = new Compressor(compressor[0]);
        return compressor2;
    }

    private static short FindCodec(int cType, int specCodec, int[] compressor, int decompressor) {
        return ImageCompressionManager.FindCodec(cType, specCodec, compressor, decompressor);
    }

    private static int SGGetVideoCompressor(int ref, short[] depth, int[] compressor, int[] spatialQuality, int[] temporalQuality, int[] keyFrameRate) {
        return SequenceGrabberManager.SGGetVideoCompressor(ref, depth, compressor, spatialQuality, temporalQuality, keyFrameRate);
    }
}
