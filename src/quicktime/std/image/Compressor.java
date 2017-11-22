
package quicktime.std.image;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTObject;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.std.sg.SGVideoChannel;
import quicktime.std.sg.SGVideoCompressorInfo;


public final class Compressor extends CodecComponent implements QuickTimeLib {
    private static Object linkage;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(Compressor.class);
                    }
                });
            }
        }.establish();
    }

    Compressor(int paramInt) {
        super(paramInt);
    }

    public static SGVideoCompressorInfo fromSGVideoChannel(SGVideoChannel paramSGVideoChannel) throws StdQTException {
        short[] arrayOfShort = new short[1];
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int[] arrayOfInt3 = new int[1];
        int[] arrayOfInt4 = new int[1];

        int i = SGGetVideoCompressor(QTObject.ID(paramSGVideoChannel),
                                     arrayOfShort,
                                     arrayOfInt1,
                                     arrayOfInt2,
                                     arrayOfInt3,
                                     arrayOfInt4);

        StdQTException.checkError(i);

        return new SGVideoCompressorInfo(arrayOfShort[0],
                                         new Compressor(arrayOfInt1[0]),
                                         arrayOfInt2[0],
                                         arrayOfInt3[0],
                                         arrayOfInt4[0]);
    }

    public static Compressor findCodec(int paramInt, CodecComponent paramCodecComponent) throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = FindCodec(paramInt, QTObject.ID(paramCodecComponent), arrayOfInt, 0);
        StdQTException.checkError(i);
        Compressor localCompressor = new Compressor(arrayOfInt[0]);
        return localCompressor;
    }

    private static native short FindCodec(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3);

    private static native int SGGetVideoCompressor(int paramInt,
                                                   short[] paramArrayOfShort,
                                                   int[] paramArrayOfInt1,
                                                   int[] paramArrayOfInt2,
                                                   int[] paramArrayOfInt3,
                                                   int[] paramArrayOfInt4);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/image
 * /Compressor.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
