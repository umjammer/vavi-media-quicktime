package quicktime.app;

import quicktime.QTException;

/**
 * QTAppExceptions are thrown by quicktime.app classes when some condition
 * is not as expected. They are a general exception type with specific subclasses
 * for specific situations.
 */
public class QTAppException extends QTException {

    /**
     * Construct an Exception.
     * @param message A message
     */
    public QTAppException(String message) {
        super(message);
    }

    /**
     * Construct an Exception
     */
    public QTAppException() {
        super("");
    }
}
