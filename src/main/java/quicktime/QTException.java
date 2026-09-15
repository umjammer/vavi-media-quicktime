package quicktime;

import java.lang.reflect.Field;
import quicktime.util.QTBuild;

/**
 * A General catch all class that is used to signal errors that occur from QTML calls
 */
public class QTException extends Exception {

    /**
     * Returns a string that represents the error code of the current QTException.
     * This will return the name of the error code as represented in the quicmtime.Errors interface.
     * If the error code is unknown then the string "Unknown Error Code" will be returned.
     * @return a string that represents the current error code of the QTException.
     */
    public static String errorCodeToString(int paramInt) {
        try {
            if (paramInt == -108) {
                return "memFullErr";
            }
            Class localClass = ClassLoader.getSystemClassLoader().loadClass("quicktime.Errors");
            Field[] arrayOfField = localClass.getFields();
            for (int i = 0; i < arrayOfField.length; i++) {
                if (arrayOfField[i].getInt(null) == paramInt)
                    return arrayOfField[i].getName();
            }
        } catch (Exception localException) {
            return "Exception generated: " + localException.toString();
        }
        return "Unknown Error Code";
    }

    /**
     * Creates an exception with a message that could contain information
     * that could be displayed to the user.
     * @param str The message that could be displayed to the user
     */
    public QTException(String paramString) {
        super(paramString);
    }

    /**
     * Creates an exception with a specific error number
     * @param val the error number
     */
    public QTException(int paramInt) {
        this(new Integer(paramInt).toString());
    }

    /**
     * This method will throw an exception if the incoming err argument is
     * a non-zero value.
     * @param err the result or error code if non-zero an exception is thrown
     * @param {@link quicktime.QTException}
     */
    public static void checkError(int paramInt) throws QTException {
        if ((paramInt < 0) && (isDrawingError(paramInt) < 0)) {
            throw new QTException(paramInt);
        }
    }

    /**
     * This routine checks the result of drawing operations that may report an error
     * code that is transitory and can be ignored.
     *
     * Error codes ignored are:
     *
     * codecNothingToBlitErr
     * codecOffscreenFailedErr
     */
    public static int isDrawingError(int paramInt) {
        switch(paramInt) {
            case -8988:
            case -8985:
            case -8984:
            case -8983:
            case -8982:
            case -8981:
            case -8980:
            case -8976:
                return 0;
        }
        return paramInt;
    }

    private int eCode = 0;

    /**
     * Return the error code that generated the exception. If there was no QT error code number known
     * this value will be 0 in which case the message contains information about the exception.
     * @return an int
     */
    public int errorCode() {
        return this.eCode;
    }

    /**
     * Returns a string that represents the error code of the current QTException.
     * This will return the name of the error code as represented in the quicmtime.Errors interface.
     * If the error code is unknown then the string "Unknown Error Code" will be returned.
     * @return a string that represents the current error code of the QTException.
     */
    public String errorCodeToString() {
        return errorCodeToString(this.eCode);
    }

    /**
     * Print current build information and current exception details.
     */
    public String toString() {
        String str = Integer.toString(this.eCode) + "=" + errorCodeToString();
        return getClass().getName() + QTBuild.info() + "," + str + ",QT.vers:" + Integer.toHexString(QTSession.getQTVersion());
    }
}

