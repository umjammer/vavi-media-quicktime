package quicktime.qd;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.internal.toolbox.QuickDrawManager;
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
                linkage = AccessController.doPrivileged((PrivilegedAction<Object>) () -> QTNative.linkNativeMethods(QDFont.class));
            }
        }.establish();
    }

    /**
     * The GetFontName procedure gets the name of a font family that has a specified family ID number.
     * @see "QuickDraw::GetFontName()"
     * @return the name of the font. If the font specified in the familyID parameter does not exist, theName contains an empty string.
     */
    public static String getFontName(int paramInt) {
        byte[] arrayOfByte = new byte[255];
        GetFontName((short) paramInt, arrayOfByte);
        return QTUtils.PString2String(arrayOfByte, 0);
    }

    /**
     * The GetFNum procedure gets the font family ID for a specified font family name.
     * @see "QuickDraw::GetFNum()"
     * @param theName The font family name
     * @return the familyID that corresponds to the supplied font family name. If the font specified in the parameter theName does not exist, familyID contains 0.
     */
    public static int getFNum(String paramString) {
        byte[] arrayOfByte = QTUtils.String2PString(paramString, 255);
        short[] arrayOfShort = { 0 };
        GetFNum(arrayOfByte, arrayOfShort);
        return arrayOfShort[0];
    }

    private static void GetFontName(short paramShort, byte[] paramArrayOfByte) {
        QuickDrawManager.GetFontName(paramShort, paramArrayOfByte);
    }

    private static void GetFNum(byte[] paramArrayOfByte, short[] paramArrayOfShort) {
        QuickDrawManager.GetFNum(paramArrayOfByte, paramArrayOfShort);
    }
}
