
package quicktime.io;

import quicktime.QTException;


public class QTIOException extends QTException {
    private String path;

    public QTIOException(String paramString) {
        super(paramString);
        this.path = "";
    }

    public QTIOException(int paramInt, String paramString) {
        super(paramInt);
        this.path = paramString;
    }

    public static void checkError(int paramInt, String paramString) throws QTIOException {
        if (paramInt < 0) {
            throw new QTIOException(paramInt, paramString);
        }
    }

    public String getFilePath() {
        return this.path;
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/io/
 * QTIOException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
