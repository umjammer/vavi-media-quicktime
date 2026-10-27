package quicktime.std.image;

import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.comp.ComponentIdentifier;
import quicktime.std.qtcomponents.SpatialSettings;


/**
 * Corresponds to the Codec Component type.
 * refer to Image Compression Manager in QuickTime
 */
public class CodecComponent extends ComponentIdentifier implements QuickTimeLib {

    /**
     * choose the first compressor or decompressor of the specified type.
     */
    public static final CodecComponent anyCodec = new CodecComponent(0);

    /**
     * choose the fastest compressor or decompressor of the specified type.
     */
    public static final CodecComponent bestSpeedCodec = new CodecComponent(-1);

    /**
     * choose the most accurate compressor or decompressor of the specified type.
     */
    public static final CodecComponent bestFidelityCodec = new CodecComponent(-2);

    /**
     * choose the compressor that produces the smallest resulting data.
     */
    public static final CodecComponent bestCompressionCodec = new CodecComponent(-3);

    /**
     * Get the default Codec in the dialog box.
     * @return the Codec
     * @see quicktime.std.qtcomponents.SpatialSettings#getCodec()
     */
    public static CodecComponent fromSpatialSettings(SpatialSettings paramSpatialSettings) {
        return new CodecComponent(getIntFromArray(paramSpatialSettings.getBytes(), 4));
    }

    CodecComponent(int paramInt) {
        super(paramInt, new Object());
    }

    private static int getIntFromArray(byte[] paramArrayOfByte, int paramInt) {
        return MemoryManager.getIntFromArray(paramArrayOfByte, paramInt);
    }
}
