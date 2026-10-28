package quicktime.io;

import quicktime.QTException;


/**
 * A General catch all class that is used to signal errors that occur from QT calls
 */
public class QTIOException extends QTException {

    private final String path;

    /**
     * Creates an exception with a string representation.
     *
     * @param errorString a description of the exception
     */
    public QTIOException(String errorString) {
        super(errorString);
        this.path = "";
    }

    /**
     * Creates an exception with a specific error number
     *
     * @param val the error number
     */
    public QTIOException(int val, String paramString) {
        super(val);
        this.path = paramString;
    }

    /**
     * This method will throw an exception if the incoming err argument is
     * a non-zero value.
     *
     * @param err the result or error code if non-zero an exception is thrown
     * @throws quicktime.io.QTIOException
     */
    public static void checkError(int err, String paramString) throws QTIOException {
        if (err < 0) {
            throw new QTIOException(err, paramString);
        }
    }

    /**
     * Returns the path of the file that caused the exception to be thrown
     *
     * @return a file path
     */
    public String getFilePath() {
        return this.path;
    }
}
