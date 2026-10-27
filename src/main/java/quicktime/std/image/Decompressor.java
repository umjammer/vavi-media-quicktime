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

    Decompressor(int paramInt) {
        super(paramInt);
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
        int[] arrayOfInt = new int[1];
        int i = FindCodec(cType, QTObject.ID(specCodec), 0, arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0] == 0 ? null : new Decompressor(arrayOfInt[0]);
    }

    private static short FindCodec(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt) {
        return ImageCompressionManager.FindCodec(paramInt1, paramInt2, paramInt3, paramArrayOfInt);
    }
}
