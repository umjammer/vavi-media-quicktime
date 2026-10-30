package quicktime.qd;

import quicktime.internal.toolbox.QuickDrawManager;
import quicktime.jdirect.QuickTimeLib;
import quicktime.util.QTUtils;


/**
 * This class provides basic services for translating font information from
 * Java fonts to the font information that QuickTime requires.
 */
public final class QDFont implements QuickTimeLib {

    /**
     * The GetFontName procedure gets the name of a font family that has a specified family ID number.
     *
     * @return the name of the font. If the font specified in the familyID parameter does not exist, theName contains an empty string.
     * @see "QuickDraw::GetFontName()"
     */
    public static String getFontName(int familyID) {
        byte[] name = new byte[255];
        GetFontName((short) familyID, name);
        return QTUtils.PString2String(name, 0);
    }

    /**
     * The GetFNum procedure gets the font family ID for a specified font family name.
     *
     * @param theName The font family name
     * @return the familyID that corresponds to the supplied font family name. If the font specified in the parameter theName does not exist, familyID contains 0.
     * @see "QuickDraw::GetFNum()"
     */
    public static int getFNum(String theName) {
        byte[] name = QTUtils.String2PString(theName, 255);
        short[] familyID = {0};
        GetFNum(name, familyID);
        return familyID[0];
    }

    private static void GetFontName(short familyID, byte[] name) {
        QuickDrawManager.GetFontName(familyID, name);
    }

    private static void GetFNum(byte[] name, short[] familyID) {
        QuickDrawManager.GetFNum(name, familyID);
    }
}
