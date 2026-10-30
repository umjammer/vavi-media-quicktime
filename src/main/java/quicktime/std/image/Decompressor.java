package quicktime.std.image;

import quicktime.QTObject;
import quicktime.internal.toolbox.ImageCompressionManager;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;


/**
 * Corresponds to the Decompressor component type
 * Refer to DecompressorComponent in QuickTime
 */
public final class Decompressor extends CodecComponent implements QuickTimeLib {

    Decompressor(int nativeRep) {
        super(nativeRep);
    }

    /**
     * determines which of the installed decompressors has been chosen to field requests
     * made using one of the special compressor identifiers.
     * @see "see QuickTime::FindCodec"
     * @param cType specifies the CodecComponent class.
     * @param specCodec contains a special identifier value.
     * @throws quicktime.std.StdQTException
     */
    public static Decompressor findCodec(int cType, CodecComponent specCodec) throws StdQTException {
        int[] decompressor = new int[1];
        int i = FindCodec(cType, QTObject.ID(specCodec), 0, decompressor);
        StdQTException.checkError(i);
        return decompressor[0] == 0 ? null : new Decompressor(decompressor[0]);
    }

    private static short FindCodec(int cType, int specCodec, int compressor, int[] decompressor) {
        return ImageCompressionManager.FindCodec(cType, specCodec, compressor, decompressor);
    }
}
