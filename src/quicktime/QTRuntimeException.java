
package quicktime;

import quicktime.util.QTBuild;


public class QTRuntimeException extends RuntimeException {
    private static QTRuntimeHandler handler;

    public static void handleOrThrow(QTRuntimeException paramQTRuntimeException, Object paramObject, String paramString) {
        if (handler == null)
            throw paramQTRuntimeException;
        boolean bool = paramQTRuntimeException.errorCode() == -108;
        handler.exceptionOccurred(paramQTRuntimeException, paramObject, paramString, bool);
    }

    public static void registerHandler(QTRuntimeHandler paramQTRuntimeHandler) {
        handler = paramQTRuntimeHandler;
    }

    public QTRuntimeException(QTException paramQTException) {
        this(paramQTException.errorCode());
        this.exception = paramQTException;
    }

    public QTRuntimeException(Exception paramException) {
        this(paramException.toString());
        this.exception = paramException;
    }

    public QTRuntimeException(int paramInt) {
        this(new Integer(paramInt).toString());
    }

    public QTRuntimeException(String paramString) {
        super(paramString);
    }

    private int eCode = 0;

    private Exception exception = null;

    public final int errorCode() {
        return this.eCode;
    }

    public final Exception getException() {
        return this.exception;
    }

    public String errorCodeToString() {
        return QTException.errorCodeToString(this.eCode);
    }

    public String toString() {
        String str = Integer.toString(this.eCode) + "=" + errorCodeToString();
        return getClass().getName() + QTBuild.info() + str + ",QT.vers:" + Integer.toHexString(QTSession.getQTVersion());
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/
 * QTRuntimeException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
