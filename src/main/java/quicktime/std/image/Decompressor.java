
package quicktime.std.image;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTObject;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;


public final class Decompressor extends CodecComponent implements QuickTimeLib {
    private static Object linkage;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(Decompressor.class);
                    }
                });
            }
        }.establish();
    }

    Decompressor(int paramInt) {
        super(paramInt);
    }

    public static Decompressor findCodec(int paramInt, CodecComponent paramCodecComponent) throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = FindCodec(paramInt, QTObject.ID(paramCodecComponent), 0, arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0] == 0 ? null : new Decompressor(arrayOfInt[0]);
    }

    private static native short FindCodec(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt);
}
