package quicktime.qd;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.util.QTUtils;

/**
 * This class provides basic services for translating font information from
 * Java fonts to the font information that QuickTime requires.
 */
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

    /**
     * The GetFontName procedure gets the name of a font family that has a specified family ID number.
     * QuickDraw::GetFontName()
     * @return the name of the font. If the font specified in the familyID parameter does not exist, theName contains an empty string.
     */
    public static final String getFontName(int paramInt) {
        byte[] arrayOfByte = new byte[255];
        GetFontName((short) paramInt, arrayOfByte);
        return QTUtils.PString2String(arrayOfByte, 0);
    }

    /**
     * The GetFNum procedure gets the font family ID for a specified font family name.
     * QuickDraw::GetFNum()
     * @param theName The font family name
     * @return the familyID that corresponds to the supplied font family name. If the font specified in the parameter theName does not exist, familyID contains 0.
     */
    public static final int getFNum(String paramString) {
        byte[] arrayOfByte = QTUtils.String2PString(paramString, 255);
        short[] arrayOfShort = { 0 };
        GetFNum(arrayOfByte, arrayOfShort);
        return arrayOfShort[0];
    }

    private static native void GetFontName(short paramShort, byte[] paramArrayOfByte);

    private static native void GetFNum(byte[] paramArrayOfByte, short[] paramArrayOfShort);
}
