package quicktime;

public class QTUnknownOSException extends QTException {

    public QTUnknownOSException() {
        super(System.getProperty("os.name"));
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/
 * QTUnknownOSException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
