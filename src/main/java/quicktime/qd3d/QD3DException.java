package quicktime.qd3d;

import quicktime.QTException;

/**
 * A General catch all class that is used to signal errors that occur from QT calls
 */
public class QD3DException extends QTException {

    /**
     * Creates an exception with a message that could contain information
     * that could be displayed to the user.
     * @param str The message that could be displayed to the user
     */
    public QD3DException(String str) {
        super(str);
    }

    /**
     * Creates an exception with a specific error number
     * @param val the error number
     */
    public QD3DException(int val) {
        super(val);
    }

    /**
     * This method will throw an exception if the incoming err argument is
     * a non-zero value.
     * @param err the result or error code if non-zero an exception is thrown
     * @throws quicktime.qd3d.QD3DException
     */
    public static void checkError(int err) throws QD3DException {
        if (err < 0) {
            throw new QD3DException(err);
        }
    }
}

