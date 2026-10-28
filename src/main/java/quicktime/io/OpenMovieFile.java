package quicktime.io;

import quicktime.QTException;

/**
 * An object of this class represents an opened movie file
 * that is opened for reading or writing.
 */
public final class OpenMovieFile extends OpenedFile {

    /**
     * Open the specified movie file for reading.
     * @see "QuickTime::OpenMovieFile()"
     * @param f the movie file to open
     * @return the opened movie file.
     * @throws quicktime.QTException
     */
    public static OpenMovieFile asRead(QTFile f) throws QTException {
        return new OpenMovieFile(f, 256);
    }

    /**
     * Open the specified movie file for writing.
     * @see "QuickTime::OpenMovieFile()"
     * @param f the movie file to open
     * @return the opened movie file.
     * @throws quicktime.QTException
     */
    public static OpenMovieFile asWrite(QTFile f) throws QTException {
        return new OpenMovieFile(f, 512);
    }

    private OpenMovieFile(QTFile paramQTFile, int paramInt) throws QTException {
        super(paramQTFile, paramInt, 1);
    }
}

