
package quicktime.qd3d;

import quicktime.QTException;


public class QD3DException extends QTException {
    public QD3DException(String paramString) {
        super(paramString);
    }

    public QD3DException(int paramInt) {
        super(paramInt);
    }

    public static void checkError(int paramInt) throws QD3DException {
        if (paramInt < 0) {
            throw new QD3DException(paramInt);
        }
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/qd3d/
 * QD3DException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
