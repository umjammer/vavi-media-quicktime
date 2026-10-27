package quicktime.util;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.image.CodecName;


/**
 * This class is used to store 7bit ASCII value strings
 */
public class StringHandle extends QTHandleRef implements QuickTimeLib {

    private static Object linkage;

    public static final int kCStringFormat = 1;

    public static final int kPStringFormat = 2;

    private final int strFormat;

    /**
     * Get the name of the compressor component.
     * @return the name.
     * @see quicktime.std.image.CodecName#getName()
     */
    public static StringHandle fromCodecName(CodecName paramCodecName) {
        int i = getIntFromPointer(QTObject.ID(paramCodecName), 40);
        return new StringHandle(i, paramCodecName, 2);
    }

    /**
     * Construct a handle from a java String. The string will only be stored as 7bit ASCII values
     * - the full unicode specification of the Java String is lost in this conversion.
     * You must supply information about how this String is to be formatted.
     * @param str the Java string to store in the handle
     * @param format if kCStringFormat the string is formatted as a C string with a '\0' termination
     *  if kPStringFormat then the string is formatted as a PString with a count byte inserted at the start.
     */
    public StringHandle(String str, int format) throws QTException {
        super(str.length() + 1, false);
        this.strFormat = format;
        if (this.strFormat == 1) {
            setCStringAt(0, str.length(), str);
        } else if (this.strFormat == 2) {
            if (str.length() > 255)
                throw new QTException(-50);
            setPStringAt(0, str.length(), str);
        } else {
            throw new QTException(-50);
        }
    }

    protected StringHandle(int paramInt1, Object paramObject, int paramInt2) {
        super(paramInt1, paramObject, false);
        this.strFormat = paramInt2;
    }

    /**
     * Returns the length of the String that is stored in the StringHandle.
     * @return the number of characters in this String
     */
    public int getStringLength() {
        return getSize() - 1;
    }

    /**
     * Returns true if the string stored in the handle is a C String, or false
     * if it is a PString
     */
    public int getFormat() {
        return this.strFormat;
    }

    /**
     * Returns the String found in the Handle as a java.lang.String
     * @return java.lang.String representation
     * @throws quicktime.util.UtilException
     */
    public String toJavaString() throws UtilException {
        return this.strFormat == 1 ? getCStringAt(0) : getPStringAt(0);
    }

    /**
     * Sets the String found in the handle to the java.lang.String characters.
     * This will NOT grow the handle so that handle must be big enough to hold the new string
     * @param str the characters that the StringHandle will be set to
     * @throws quicktime.util.UtilException
     */
    public void fromJavaString(String str) throws UtilException {
        if (this.strFormat == 1) {
            setCStringAt(0, getSize(), str);
        } else {
            setPStringAt(0, Math.min(255, getSize()), str);
        }
    }

    /**
     * Returns the String found in the Handle as a java.lang.String
     * @return java.lang.String representation
     */
    public String toString() {
        try {
            return getClass().getName() + "[" + toJavaString() + "]";
        } catch (UtilException localUtilException) {
        }
        return getClass().getName() + "[]";
    }

    private static int getIntFromPointer(int paramInt1, int paramInt2) {
        return MemoryManager.getIntFromPointer(paramInt1, paramInt2);
    }
}
