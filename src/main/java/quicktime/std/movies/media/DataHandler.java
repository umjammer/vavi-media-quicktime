
package quicktime.std.movies.media;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTObject;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.std.comp.Component;


public final class DataHandler extends Component implements QuickTimeLib {
    private static Object linkage;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(DataHandler.class);
                    }
                });
            }
        }.establish();
    }

    DataHandler(int paramInt, Object paramObject) {
        super(paramInt, paramObject);
    }

    public DataHandler(DataRef paramDataRef, int paramInt) throws StdQTException {
        this(allocate(paramDataRef, paramInt), null);
    }

    private static int allocate(DataRef paramDataRef, int paramInt) throws StdQTException {
        int i = GetDataHandler(QTObject.ID(paramDataRef), paramDataRef.getType(), paramInt);
        if (i == 0)
            throw new StdQTException(63524);
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(OpenAComponent(i, arrayOfInt));
        return arrayOfInt[0];
    }

    private static native int GetDataHandler(int paramInt1, int paramInt2, int paramInt3);

    private static native short OpenAComponent(int paramInt, int[] paramArrayOfInt);
}
