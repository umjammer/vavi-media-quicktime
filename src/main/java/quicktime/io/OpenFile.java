
package quicktime.io;

import quicktime.QTException;


public final class OpenFile extends OpenedFile {
    public static OpenFile asRead(QTFile paramQTFile) throws QTException {
        return new OpenFile(paramQTFile, 256);
    }

    public static OpenFile asWrite(QTFile paramQTFile) throws QTException {
        return new OpenFile(paramQTFile, 512);
    }

    private OpenFile(QTFile paramQTFile, int paramInt) throws QTException {
        super(paramQTFile, paramInt, 2);
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/io/
 * OpenFile.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
