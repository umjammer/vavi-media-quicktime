package quicktime;

import quicktime.util.QTBuild;

/**
 * QTRuntimeExceptions are thrown by the QTJava classes when an exceptional condition
 * is caused that cannot be recovered from at runtime. This will usually occur within
 * a java method (for eg. clone()) that has not API for throwing an exception. In the clone
 * method for instance this exception is thrown if there is an out of memory condition.
 */
public class QTRuntimeException extends RuntimeException {

    private static QTRuntimeHandler handler;

    /**
     * If there is a handler registered for the
     * exception it will be passed to that handler. The handler should either deal with the
     * exception or throw it - particularly if the exception is notified as
     * a potentially serious exception - eg. memFullErr
     * @param e the exception to either handle or throw
     */
    public static void handleOrThrow(QTRuntimeException paramQTRuntimeException, Object paramObject, String paramString) {
        if (handler == null)
            throw paramQTRuntimeException;
        boolean bool = paramQTRuntimeException.errorCode() == -108;
        handler.exceptionOccurred(paramQTRuntimeException, paramObject, paramString, bool);
    }

    /**
     * This registers an object that will handle the runtime Exceptions that may
     * be generated through the (ab)normal running of a QTJava application or applet.
     * @param h the QTRuntimeHandler that will handle or throw QTRuntimeExceptions or null to remove handler.
     */
    public static void registerHandler(QTRuntimeHandler paramQTRuntimeHandler) {
        handler = paramQTRuntimeHandler;
    }

    /**
     * Creates a QTRuntimeException
     * @param e the QTException that created the runtime error
     */
    public QTRuntimeException(QTException paramQTException) {
        this(paramQTException.errorCode());
        this.exception = paramQTException;
    }

    /**
     * Creates a QTRuntimeException
     * @param e the Exception that created the runtime error
     */
    public QTRuntimeException(Exception paramException) {
        this(paramException.toString());
        this.exception = paramException;
    }

    /**
     * Creates an QTRuntimeException with a message that could contain information
     * that could be displayed to the user.
     * @param val typically some error code
     */
    public QTRuntimeException(int paramInt) {
        this(new Integer(paramInt).toString());
    }

    /**
     * Creates an QTRuntimeException with a message that could contain information
     * that could be displayed to the user.
     * @param str The message that could be displayed to the user
     */
    public QTRuntimeException(String paramString) {
        super(paramString);
    }

    private int eCode = 0;

    private Exception exception = null;

    /**
     * Return the error code that generated the exception. If there was no QT error code number known
     * this value will be 0 in which case the message contains information about the exception.
     * @return an int
     */
    public final int errorCode() {
        return this.eCode;
    }

    /**
     * Return the Exception that created the runtime error.
     * @return an Exception that created the runtime error
     */
    public final Exception getException() {
        return this.exception;
    }

    /**
     * Returns a string that represents the error code of the current QTException.
     * This will return the name of the error code as represented in the quicmtime.Errors interface.
     * If the error code is unknown then the string "Unknown Error Code" will be returned.
     * @return a string that represents the current error code of the QTException.
     */
    public String errorCodeToString() {
        return QTException.errorCodeToString(this.eCode);
    }

    /**
     * Print current build information and current exception details.
     */
    public String toString() {
        String str = Integer.toString(this.eCode) + "=" + errorCodeToString();
        return getClass().getName() + QTBuild.info() + str + ",QT.vers:" + Integer.toHexString(QTSession.getQTVersion());
    }
}

