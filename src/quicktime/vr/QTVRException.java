
package quicktime.vr;

import quicktime.QTException;


public class QTVRException extends QTException {
    public QTVRException(String paramString) {
        super(paramString);
    }

    public QTVRException(int paramInt) {
        super(paramInt);
    }

    public static void checkError(int paramInt) throws QTVRException {
        if ((paramInt < 0) && (QTException.isDrawingError(paramInt) < 0))
            throw new QTVRException(paramInt);
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/vr/
 * QTVRException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
