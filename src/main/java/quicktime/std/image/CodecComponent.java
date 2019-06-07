package quicktime.std.image;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.comp.ComponentIdentifier;
import quicktime.std.qtcomponents.SpatialSettings;

/**
 * Corresponds to the Codec Component type.
 * refer to Image Compression Manager in QuickTime
 */
public class CodecComponent extends ComponentIdentifier implements QuickTimeLib {

    private static Object linkage;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(CodecComponent.class);
                    }
                });
            }
        }.establish();
    }

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
     * @see {@link quicktime.std.qtcomponents.SpatialSettings#getCodec()}
     */
    public static CodecComponent fromSpatialSettings(SpatialSettings paramSpatialSettings) {
        return new CodecComponent(getIntFromArray(paramSpatialSettings.getBytes(), 4));
    }

    CodecComponent(int paramInt) {
        super(paramInt, new Object());
    }

    private static native int getIntFromArray(byte[] paramArrayOfByte, int paramInt);
}
