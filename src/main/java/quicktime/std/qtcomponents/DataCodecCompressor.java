
package quicktime.std.qtcomponents;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.std.comp.Component;
import quicktime.std.comp.ComponentIdentifier;
import quicktime.util.QTPointerRef;


public class DataCodecCompressor extends Component implements QuickTimeLib {
    private static Object linkage;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(DataCodecCompressor.class);
                    }
                });
            }
        }.establish();
    }

    public DataCodecCompressor(int paramInt) throws QTException {
        super(1684238189, paramInt);
    }

    public DataCodecCompressor(ComponentIdentifier paramComponentIdentifier) throws QTException {
        super(paramComponentIdentifier, 1684238189);
    }

    public DataCodecInfo compress(QTPointerRef paramQTPointerRef1, QTPointerRef paramQTPointerRef2) throws StdQTException {
        int[] arrayOfInt1 = {
            0
        };
        int[] arrayOfInt2 = {
            0
        };
        int i = DataCodecCompress(_ID(),
                                  QTObject.ID(paramQTPointerRef1),
                                  paramQTPointerRef1.getSize(),
                                  QTObject.ID(paramQTPointerRef2),
                                  paramQTPointerRef2.getSize(),
                                  arrayOfInt1,
                                  arrayOfInt2);

        StdQTException.checkError(i);
        return new DataCodecInfo(arrayOfInt1[0], arrayOfInt2[0]);
    }

    public int getCompressBufferSize(int paramInt) throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        StdQTException.checkError(DataCodecGetCompressBufferSize(_ID(), paramInt, arrayOfInt));
        return arrayOfInt[0];
    }

    private static native int DataCodecCompress(int paramInt1,
                                                int paramInt2,
                                                int paramInt3,
                                                int paramInt4,
                                                int paramInt5,
                                                int[] paramArrayOfInt1,
                                                int[] paramArrayOfInt2);

    private static native int DataCodecGetCompressBufferSize(int paramInt1, int paramInt2, int[] paramArrayOfInt);
}
