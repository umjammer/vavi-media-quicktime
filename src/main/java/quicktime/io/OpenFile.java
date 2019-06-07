package quicktime.io;

import quicktime.QTException;

/**
 * An object of this class represents an opened file
 * that is opened for reading or writing. Only the file's
 * Data Fork is opened.
 */
public final class OpenFile extends OpenedFile {

    /**
     * Open the specified file for reading.
     * QuickTime::FSpOpenDF()
     * @param f the file to open
     * @return the opened file.
     * @return {@link quicktime.QTException}
     */
    public static OpenFile asRead(QTFile paramQTFile) throws QTException {
        return new OpenFile(paramQTFile, 256);
    }

    /**
     * Open the specified file for writing.
     * QuickTime::FSpOpenDF()
     * @param f the file to open
     * @return the opened file.
     * @return {@link quicktime.QTException}
     */
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
