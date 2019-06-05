
package quicktime.util;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.image.CodecName;


public class StringHandle extends QTHandleRef implements QuickTimeLib {
    private static Object linkage;

    public static final int kCStringFormat = 1;

    public static final int kPStringFormat = 2;

    private int strFormat;

    static {
        class PrivelegedAction {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(StringHandle.class);
                    }
                });
            }
        }
        new PrivelegedAction().establish();
    }

    public static StringHandle fromCodecName(CodecName paramCodecName) {
        int i = getIntFromPointer(QTObject.ID(paramCodecName), 40);
        return new StringHandle(i, paramCodecName, 2);
    }

    public StringHandle(String paramString, int paramInt) throws QTException {
        super(paramString.length() + 1, false);
        this.strFormat = paramInt;
        if (this.strFormat == 1) {
            setCStringAt(0, paramString.length(), paramString);
        } else if (this.strFormat == 2) {
            if (paramString.length() > 255)
                throw new QTException(-50);
            setPStringAt(0, paramString.length(), paramString);
        } else {
            throw new QTException(-50);
        }
    }

    protected StringHandle(int paramInt1, Object paramObject, int paramInt2) {
        super(paramInt1, paramObject, false);
        this.strFormat = paramInt2;
    }

    public int getStringLength() {
        return getSize() - 1;
    }

    public int getFormat() {
        return this.strFormat;
    }

    public String toJavaString() throws UtilException {
        return this.strFormat == 1 ? getCStringAt(0) : getPStringAt(0);
    }

    public void fromJavaString(String paramString) throws UtilException {
        if (this.strFormat == 1) {
            setCStringAt(0, getSize(), paramString);
        } else {
            setPStringAt(0, 255 > getSize() ? getSize() : 255, paramString);
        }
    }

    public String toString() {
        try {
            return getClass().getName() + "[" + toJavaString() + "]";
        } catch (UtilException localUtilException) {
        }
        return getClass().getName() + "[]";
    }

    private static native int getIntFromPointer(int paramInt1, int paramInt2);
}
