package quicktime.qd;

import quicktime.QTException;

/**
 * A General catch all class that is used to signal errors that occur from QT calls
 */
public class QDException extends QTException {

    /**
     * Creates an exception with a message that could contain information
     * that could be displayed to the user.
     * @param str The message that could be displayed to the user
     */
    public QDException(String paramString) {
        super(paramString);
    }

    /**
     * Creates an exception with a specific error number
     * @param val the error number
     */
    public QDException(int paramInt) {
        super(paramInt);
    }

    /**
     * This method will throw an exception if the incoming err argument is
     * a non-zero value.
     * @param err the result or error code if non-zero an exception is thrown
     * @throws quicktime.qd.QDException
     */
    public static void checkError(int paramInt) throws QDException {
        if ((paramInt < 0) && (QTException.isDrawingError(paramInt) < 0))
            throw new QDException(paramInt);
    }
}

