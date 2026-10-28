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
     * @see "QuickTime::FSpOpenDF()"
     * @param f the file to open
     * @return the opened file.
     * @throws quicktime.QTException
     */
    public static OpenFile asRead(QTFile f) throws QTException {
        return new OpenFile(f, 256);
    }

    /**
     * Open the specified file for writing.
     * @see "QuickTime::FSpOpenDF()"
     * @param f the file to open
     * @return the opened file.
     * @throws quicktime.QTException
     */
    public static OpenFile asWrite(QTFile f) throws QTException {
        return new OpenFile(f, 512);
    }

    private OpenFile(QTFile paramQTFile, int paramInt) throws QTException {
        super(paramQTFile, paramInt, 2);
    }
}

