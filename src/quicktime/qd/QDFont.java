
package quicktime.qd;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.util.QTUtils;


public final class QDFont implements QuickTimeLib {
    private static Object linkage;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(QDFont.class);
                    }
                });
            }
        }.establish();
    }

    public static final String getFontName(int paramInt) {
        byte[] arrayOfByte = new byte[255];
        GetFontName((short) paramInt, arrayOfByte);
        return QTUtils.PString2String(arrayOfByte, 0);
    }

    public static final int getFNum(String paramString) {
        byte[] arrayOfByte = QTUtils.String2PString(paramString, 255);
        short[] arrayOfShort = {
            0
        };
        GetFNum(arrayOfByte, arrayOfShort);
        return arrayOfShort[0];
    }

    private static native void GetFontName(short paramShort, byte[] paramArrayOfByte);

    private static native void GetFNum(byte[] paramArrayOfByte, short[] paramArrayOfShort);
}
