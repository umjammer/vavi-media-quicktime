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
    public QTAppException(String paramString) {
        super(paramString);
    }

    /**
     * Construct an Exception
     */
    public QTAppException() {
        super("");
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/
 * QTAppException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
