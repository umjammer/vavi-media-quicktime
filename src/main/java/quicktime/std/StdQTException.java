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
    public StdQTException(String paramString) {
        super(paramString);
    }

    /**
     * Creates an exception with a specific error number
     * @param val the error number
     */
    public StdQTException(int paramInt) {
        super(paramInt);
    }

    /**
     * This method will throw an exception if the incoming err argument is
     * a non-zero value.
     * @param err the result or error code if non-zero an exception is thrown
     * @param {@link quicktime.std.StdQTException}
     */
    public static void checkError(int paramInt) throws StdQTException {
        if ((paramInt < 0) && (QTException.isDrawingError(paramInt) < 0))
            throw new StdQTException(paramInt);
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * StdQTException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
