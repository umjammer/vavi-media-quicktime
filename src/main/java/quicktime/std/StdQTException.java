package quicktime.std;

import quicktime.QTException;

/**
 * A General catch all class that is used to signal errors that occur from QT calls
 */
public class StdQTException extends QTException {

    /**
     * Creates an exception with a message that could contain information
     * that could be displayed to the user.
     * @param str The message that could be displayed to the user
     */
    public StdQTException(String str) {
        super(str);
    }

    /**
     * Creates an exception with a specific error number
     * @param val the error number
     */
    public StdQTException(int val) {
        super(val);
    }

    /**
     * This method will throw an exception if the incoming err argument is
     * a non-zero value.
     * @param err the result or error code if non-zero an exception is thrown
     * @throws quicktime.std.StdQTException
     */
    public static void checkError(int err) throws StdQTException {
        if ((err < 0) && (QTException.isDrawingError(err) < 0))
            throw new StdQTException(err);
    }
}
