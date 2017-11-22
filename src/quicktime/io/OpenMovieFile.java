
package quicktime.io;

import quicktime.QTException;


public final class OpenMovieFile extends OpenedFile {
    public static OpenMovieFile asRead(QTFile paramQTFile) throws QTException {
        return new OpenMovieFile(paramQTFile, 256);
    }

    public static OpenMovieFile asWrite(QTFile paramQTFile) throws QTException {
        return new OpenMovieFile(paramQTFile, 512);
    }

    private OpenMovieFile(QTFile paramQTFile, int paramInt) throws QTException {
        super(paramQTFile, paramInt, 1);
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/io/
 * OpenMovieFile.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
