
package quicktime.qd;

import quicktime.QTException;


public class QDException extends QTException {
    public QDException(String paramString) {
        super(paramString);
    }

    public QDException(int paramInt) {
        super(paramInt);
    }

    public static void checkError(int paramInt) throws QDException {
        if ((paramInt < 0) && (QTException.isDrawingError(paramInt) < 0))
            throw new QDException(paramInt);
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/qd/
 * QDException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
