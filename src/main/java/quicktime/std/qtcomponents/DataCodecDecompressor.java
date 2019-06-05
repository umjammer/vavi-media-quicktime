
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


public class DataCodecDecompressor extends Component implements QuickTimeLib {
    private static Object linkage;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(DataCodecDecompressor.class);
                    }
                });
            }
        }.establish();
    }

    public DataCodecDecompressor(int paramInt) throws QTException {
        super(1684301155, paramInt);
    }

    public DataCodecDecompressor(ComponentIdentifier paramComponentIdentifier) throws QTException {
        super(paramComponentIdentifier, 1684301155);
    }

    public void decompress(QTPointerRef paramQTPointerRef1, QTPointerRef paramQTPointerRef2) throws StdQTException {
        StdQTException.checkError(DataCodecDecompress(_ID(),
                                                      QTObject.ID(paramQTPointerRef1),
                                                      paramQTPointerRef1.getSize(),
                                                      QTObject.ID(paramQTPointerRef2),
                                                      paramQTPointerRef2.getSize()));
    }

    private static native int DataCodecDecompress(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);
}
