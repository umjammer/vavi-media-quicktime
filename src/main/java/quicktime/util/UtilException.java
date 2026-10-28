package quicktime.util;

import quicktime.QTException;

/**
 * A General catch all class that is used to signal errors that occur from QT calls
 */
public class UtilException extends QTException {

    /**
     * Creates an exception with a message that could contain information
     * that could be displayed to the user.
     * @param str The message that could be displayed to the user
     */
    public UtilException(String str) {
        super(str);
    }

    /**
     * Creates an exception with a specific error number
     * @param val the error number
     */
    public UtilException(int val) {
        super(val);
    }

    /**
     * This method will throw an exception if the incoming err argument is
     * a non-zero value.
     * @param err the result or error code if non-zero an exception is thrown
     * @throws quicktime.util.UtilException
     */
    public static void checkError(int err) throws UtilException {
        if (err < 0) {
            throw new UtilException(err);
        }
    }
}
