
package quicktime.std.image;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.comp.ComponentIdentifier;
import quicktime.std.qtcomponents.SpatialSettings;


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

    public static final CodecComponent anyCodec = new CodecComponent(0);

    public static final CodecComponent bestSpeedCodec = new CodecComponent(-1);

    public static final CodecComponent bestFidelityCodec = new CodecComponent(-2);

    public static final CodecComponent bestCompressionCodec = new CodecComponent(-3);

    public static CodecComponent fromSpatialSettings(SpatialSettings paramSpatialSettings) {
        return new CodecComponent(getIntFromArray(paramSpatialSettings.getBytes(), 4));
    }

    CodecComponent(int paramInt) {
        super(paramInt, new Object());
    }

    private static native int getIntFromArray(byte[] paramArrayOfByte, int paramInt);
}
