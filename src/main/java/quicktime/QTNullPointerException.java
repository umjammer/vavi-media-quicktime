package quicktime;

/**
 * A General catch all class that is used to signal errors that occur from QTML calls
 */
public class QTNullPointerException extends NullPointerException {

    /**
     * Creates an exception with the name of the class that caused the exception to be thrown.
     * The message string will also indicate whether the exception was thrown due to
     * a QuickTime native call being made without the QuickTime session being initialized.
     * @param str The name of the class that has a null pointer.
     */
    public QTNullPointerException(String paramString) {
        super(paramString);
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/
 * QTNullPointerException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
