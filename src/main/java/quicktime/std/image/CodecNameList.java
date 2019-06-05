
package quicktime.std.image;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.util.QTPointerRef;


public final class CodecNameList extends QTPointerRef implements QuickTimeLib {
    private static Object linkage;

    public static final int kNativeSize = 6;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(CodecNameList.class);
                    }
                });
            }
        }.establish();
    }

    public CodecNameList(int paramInt) throws QTException {
        super(allocate((short) paramInt), kNativeSize, null);
    }

    private static int allocate(short paramShort) throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        int i = GetCodecNameList(arrayOfInt, paramShort);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    public CodecName getNth(int paramInt) throws StdQTException {
        if (paramInt > getCount()) {
            throw new StdQTException("CodecName.getNth");
        }
        return new CodecName(_ID() + 2 + 44 * paramInt, this);
    }

    public int getCount() {
        return getShortAt(0);
    }

    private static native short GetCodecNameList(int[] paramArrayOfInt, short paramShort);
}
